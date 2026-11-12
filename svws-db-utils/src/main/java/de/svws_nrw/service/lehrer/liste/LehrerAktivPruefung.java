package de.svws_nrw.service.lehrer.liste;

import java.time.LocalDate;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Termin;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;

/**
 * Prüft anhand des Zugangs- und des Abgangsdatums, ob ein Lehrer in einem Schuljahresabschnitt aktiv ist.
 * <p>
 * Ein Lehrer ist nicht aktiv, wenn er erst nach dem Schuljahresabschnitt an die Schule kommt oder wenn
 * er die Schule vor Beginn des Schuljahresabschnitts verlassen hat. Das Ende des ersten Halbjahres wird dabei
 * durch den letzten Unterrichtstag im ersten Halbjahr bestimmt.
 * </p>
 */
public final class LehrerAktivPruefung {

	/** Der erste Monat eines Schuljahres. */
	private static final int MONAT_SCHULJAHRESBEGINN = 8;

	/** Der letzte Monat vor dem Beginn eines Schuljahres. */
	private static final int MONAT_VOR_SCHULJAHRESBEGINN = MONAT_SCHULJAHRESBEGINN - 1;

	/** Die Nummer des ersten Halbjahresabschnitts. */
	private static final int ERSTER_ABSCHNITT = 1;

	/** Die Nummer des zweiten Halbjahresabschnitts. */
	private static final int ZWEITER_ABSCHNITT = 2;

	/**
	 * Privater Konstruktor, da die Klasse nur statische Methoden bereitstellt.
	 */
	private LehrerAktivPruefung() {
		// leer
	}

	/**
	 * Prüft, ob der Lehrer in dem Schuljahresabschnitt aktiv ist.
	 *
	 * @param lehrer     das DTO des Lehrers
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return {@code true}, wenn der Lehrer in dem Schuljahresabschnitt aktiv ist, und ansonsten {@code false}
	 */
	public static boolean isAktiv(final DTOLehrer lehrer, final Schuljahresabschnitt abschnitt) {
		return !isNochNichtZugegangen(lehrer.DatumZugang, abschnitt) && !isBereitsAbgegangen(lehrer.DatumAbgang, abschnitt);
	}

	/**
	 * Prüft, ob der Lehrer erst nach dem Schuljahresabschnitt an die Schule kommt.
	 *
	 * @param datumZugang   das Zugangsdatum des Lehrers oder null
	 * @param abschnitt     der Schuljahresabschnitt
	 *
	 * @return {@code true}, wenn der Zugang nach dem Schuljahresabschnitt liegt, sonst {@code false}
	 */
	private static boolean isNochNichtZugegangen(final String datumZugang, final Schuljahresabschnitt abschnitt) {
		if (datumZugang == null) {
			return false;
		}

		final var zugang = LocalDate.parse(datumZugang);
		final var folgejahr = abschnitt.schuljahr + 1;
		if (zugang.getYear() > folgejahr) {
			return true;
		}

		final var isZugangImFolgejahr = zugang.getYear() == folgejahr;

		return isZugangImFolgejahr && (isAbSchuljahresbeginn(zugang) || isNachErstemHalbjahr(zugang, abschnitt));
	}

	/**
	 * Prüft, ob der Lehrer die Schule vor Beginn des Schuljahresabschnitts verlassen hat.
	 *
	 * @param datumAbgang   das Abgangsdatum des Lehrers oder null
	 * @param abschnitt     der Schuljahresabschnitt
	 *
	 * @return {@code true}, wenn der Abgang vor dem Schuljahresabschnitt liegt, sonst {@code false}
	 */
	private static boolean isBereitsAbgegangen(final String datumAbgang, final Schuljahresabschnitt abschnitt) {
		if (datumAbgang == null) {
			return false;
		}

		final var abgang = LocalDate.parse(datumAbgang);

		return isAbgangVorSchuljahr(abgang, abschnitt)
				|| isAbgangImSchuljahrBeiZweitemAbschnitt(abgang, abschnitt)
				|| isAbgangVorBeginnDesSchuljahres(abgang, abschnitt)
				|| isAbgangBisEndeErstesHalbjahr(abgang, abschnitt);
	}

	private static boolean isAbSchuljahresbeginn(final LocalDate datum) {
		return datum.getMonthValue() >= MONAT_SCHULJAHRESBEGINN;
	}

	/**
	 * Prüft für den ersten Abschnitt, ob das Datum nach dem letzten Unterrichtstag im ersten Halbjahr liegt.
	 *
	 * @param datum      das Datum
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return {@code true}, wenn es sich um den ersten Abschnitt handelt und das Datum nach dem letzten Unterrichtstag liegt
	 */
	private static boolean isNachErstemHalbjahr(final LocalDate datum, final Schuljahresabschnitt abschnitt) {
		if (abschnitt.abschnitt != ERSTER_ABSCHNITT) {
			return false;
		}
		final var letzterUnterrichtstag = Termin.getLetzterUnterrichtstagImErstenHalbjahr(abschnitt.schuljahr);

		return (letzterUnterrichtstag != null) && isNach(datum, letzterUnterrichtstag);
	}

	private static boolean isAbgangVorSchuljahr(final LocalDate abgang, final Schuljahresabschnitt abschnitt) {
		return abgang.getYear() < abschnitt.schuljahr;
	}

	private static boolean isAbgangImSchuljahrBeiZweitemAbschnitt(final LocalDate abgang, final Schuljahresabschnitt abschnitt) {
		return (abschnitt.abschnitt == ZWEITER_ABSCHNITT) && (abgang.getYear() == abschnitt.schuljahr);
	}

	private static boolean isAbgangVorBeginnDesSchuljahres(final LocalDate abgang, final Schuljahresabschnitt abschnitt) {
		return (abschnitt.abschnitt == ERSTER_ABSCHNITT) && (abgang.getYear() == abschnitt.schuljahr)
				&& (abgang.getMonthValue() <= MONAT_VOR_SCHULJAHRESBEGINN);
	}

	/**
	 * Prüft für den zweiten Abschnitt, ob der Abgang im Folgejahr bis zum letzten Unterrichtstag im ersten Halbjahr liegt.
	 *
	 * @param abgang     das Abgangsdatum
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return {@code true}, wenn es sich um den zweiten Abschnitt handelt und der Abgang nicht nach dem letzten Unterrichtstag liegt
	 */
	private static boolean isAbgangBisEndeErstesHalbjahr(final LocalDate abgang, final Schuljahresabschnitt abschnitt) {
		if ((abschnitt.abschnitt != ZWEITER_ABSCHNITT) || (abgang.getYear() != (abschnitt.schuljahr + 1))) {
			return false;
		}
		final var letzterUnterrichtstag = Termin.getLetzterUnterrichtstagImErstenHalbjahr(abschnitt.schuljahr);

		return (letzterUnterrichtstag != null) && !isNach(abgang, letzterUnterrichtstag);
	}

	private static boolean isNach(final LocalDate datum, final DateManager stichtag) {
		return (datum.getMonthValue() > stichtag.getMonat())
				|| ((datum.getMonthValue() == stichtag.getMonat()) && (datum.getDayOfMonth() > stichtag.getTag()));
	}

}
