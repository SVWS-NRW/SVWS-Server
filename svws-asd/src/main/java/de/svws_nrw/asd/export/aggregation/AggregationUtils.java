/**
 *
 */
package de.svws_nrw.asd.export.aggregation;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;

/*
 * AggregationUtils.java
 *
 * Copyright (c) 2026 Projekt ZeBrAS - Zentrale Bearbeitung der amtlichen Schuldaten
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 29.04.2026 - Vorname Nachname (knitt01) - erste Version
 */

/**
 * Die Klasse AggregationUtils ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts ZeBrAS.
 *
 * @since 2026
 * @version 1.00 - 29.04.2026
 * @author Vorname Nachname (knitt01)
 *
 */
public final class AggregationUtils {


	private AggregationUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Auffüllen eines Feldes auf eine bestimmte Anzahl an Stellen mit Leerzeichen.
	 *
	 * @param feld
	 * @param anzahlStellen
	 * @return mit Leerzeichen aufgefülltes Feld
	 */
	public static String auffuellenStellengerecht(final String feld, final int anzahlStellen) {
		String feldFormatiert = feld;

		while (feldFormatiert.toCharArray().length < anzahlStellen) {
			feldFormatiert = feldFormatiert.concat(AggregationStatistikExport.EIN_LEERZEICHEN);
		}

		return feldFormatiert;
	}

	/**
	 * Ermittelt einen zugehörigen Schüler-Lernabschnitt anhand der IDs der Schuljahresabschnitte. <br>
	 * Die Schuljahresabschnitte werden nach der <i>Nummer des Abschnitts im Schuljahr</i> absteigend sortiert durchlaufen.
	 * D.h. es wird z.B. mit dem zweiten Schulhalbjahr, wenn vorhanden, begonnen. <br>
	 * Sobald ein zugehöriger Lernabschnitt gefunden wurde, wird die Suche abgebrochen und dieser zurück gegeben. <br>
	 * Kann kein zugehöriger gefunden werden wird ein leerer Lernabschnitt mit der ID = 0 zurück gegeben.
	 * Dies kann z.B. bei der Suche nach Vorjahres-Abgängern für die V51 bei Schülern auftreten, die im ersten oder
	 * zweiten Halbjahr des aktuellen Schuljahres Abgegangen sind.
	 *
	 * @param schueler - die Daten eines Schülers
	 * @param schuljahresabschnitte - die Schuljahresabscnitte aus denen die ID bezogen wird
	 * @return der zu einer ID passende Lernabschnitt des Schülers
	 */
	public static SchuelerLernabschnittStatistikGesamt ermittelnLernabschnitt(final SchuelerStatistikGesamt schueler,
			final List<Schuljahresabschnitt> schuljahresabschnitte) {
		SchuelerLernabschnittStatistikGesamt schuelerLernabschnittStatistikGesamt = new SchuelerLernabschnittStatistikGesamt();
		final List<Schuljahresabschnitt> schuljahresabschnitteSortiert =
				schuljahresabschnitte.stream().sorted(Comparator.comparingInt(e -> e.abschnitt)).toList().reversed();

		for (final Schuljahresabschnitt schuljahresabschnitt : schuljahresabschnitteSortiert) {
			schuelerLernabschnittStatistikGesamt = ermittelnLernabschnitt(schueler, schuljahresabschnitt.id);

			// Leere Schülerlernabschnitte sind an der ID=0 zu erkennen
			if (schuelerLernabschnittStatistikGesamt.id != 0) {
				return schuelerLernabschnittStatistikGesamt;
			}
		}

		return schuelerLernabschnittStatistikGesamt;
	}

	/**
	 * Ermittelt einen zugehörigen Schüler-Lernabschnitt anhand der ID des Schuljahresabschnitts. <br>
	 * Kann kein zugehöriger gefunden werden wird ein leerer Lernabschnitt mit der ID = 0 zurück gegeben.
	 *
	 * @param schueler - die Daten eines Schülers
	 * @param idSchuljahresabschnitt - die ID des Schuljahresabscnitts
	 * @return der zur ID passende Lernabschnitt des Schülers
	 */
	public static SchuelerLernabschnittStatistikGesamt ermittelnLernabschnitt(final SchuelerStatistikGesamt schueler, final long idSchuljahresabschnitt) {
		SchuelerLernabschnittStatistikGesamt lernabschnitt = new SchuelerLernabschnittStatistikGesamt();
		final Optional<SchuelerLernabschnittStatistikGesamt> optional =
				schueler.lernabschnitte.stream().filter(e -> e.idSchuljahresabschnitt == idSchuljahresabschnitt).findFirst();

		if (optional.isPresent()) {
			lernabschnitt = optional.get();
		}

		return lernabschnitt;
	}

	/**
	 * Wenn die 2. Staatsangehörigkeit Deutsch ist, ist der Schüler als Deutscher zu werten. <br>
	 * In diesem Fall wird die idStaatsangehoerigkeit2 zurück gegeben und ansonsten die idStaatsangehoerigkeit.
	 *
	 * @param idStaatsangehoerigkeit
	 * @param idStaatsangehoerigkeit2
	 * @param schuljahr
	 * @return die gültige ID zu den übergebenen Staatsangehörigkeiten
	 */
	public static Long ermittleStaatsangehoerigkeit(final Long idStaatsangehoerigkeit, final Long idStaatsangehoerigkeit2, final int schuljahr) {

		if ((idStaatsangehoerigkeit2 != null) && Nationalitaeten.getDEU().id(schuljahr).equals(idStaatsangehoerigkeit2)) {
			return idStaatsangehoerigkeit2;
		}

		return idStaatsangehoerigkeit;
	}

	/**
	 * Ermittelt den gültigen Schlüssel zur übergebenen Staatsangehörigkeit-ID. <br>
	 * Ist die Staatsangehörigkeit Deutsch, dann wird als Schlüssel ein Leerstring zurück gegeben. <br>
	 * Kann die Staatsangehörigkeit nicht ermittelt werden, wird auch ein Leerstring zurück gegeben und die
	 * Staatsangehörigkeit als Deutsch gewertet. <br>
	 *
	 * @param idStaatsangehoerigkeit
	 * @return der gültige Schlüssel zu der übergebenen Staatsangehörigkeit-ID
	 */
	public static String ermittleStaatsangehoerigkeitSchluessel(final Long idStaatsangehoerigkeit) {

		if (idStaatsangehoerigkeit == null) {
			return "";
		}
		final String schluessel = Nationalitaeten.data().getSchluesselByIDOrNull(idStaatsangehoerigkeit);
		if (schluessel == null) {
			return "";
		}
		if (Nationalitaeten.getDEU().equals(Nationalitaeten.data().getWertByIDOrNull(idStaatsangehoerigkeit))) {
			return "";
		}

		return schluessel;
	}

	/**
	 * Wenn die 2. Staatsangehörigkeit Deutsch ist, ist der Schüler als Deutscher zu werten. <br>
	 * In diesem Fall wird als Schlüssel ein Leerstring zurück gegeben. <br>
	 * Wird die erste Staatsangehörigkeit herangezogen und diese ist Deutsch, dann ebenfalls. <br>
	 * Kann die Staatsangehörigkeit nicht ermittelt werden, wird auch ein Leerstring zurück gegeben und die
	 * Staatsangehörigkeit als Deutsch gewertet.
	 *
	 * @param idStaatsangehoerigkeit
	 * @param idStaatsangehoerigkeit2
	 * @param schuljahr
	 * @return der gültige Schlüssel zu den übergebenen Staatsangehörigkeiten
	 */
	public static String ermittleStaatsangehoerigkeitSchluessel(final Long idStaatsangehoerigkeit, final Long idStaatsangehoerigkeit2, final int schuljahr) {

		return ermittleStaatsangehoerigkeitSchluessel(ermittleStaatsangehoerigkeit(idStaatsangehoerigkeit, idStaatsangehoerigkeit2, schuljahr));
	}

	/**
	 * Prüft den übergebenen Wert auf null und gibt gegebenenfalls einen Leerstring zurück.
	 *
	 * @param wert - Ein String-Wert
	 * @return den Wert oder einen Leerstring
	 */
	public static String ersetzeNullDurchLeer(final String wert) {
		if (wert == null) {
			return "";
		}
		return wert;
	}

	/**
	 * Holt den Schlüsselwert aus dem zugehörigen Katalog oder gibt einen Leerstring zurück, falls zu der ID kein Eintrag gefunden wird.
	 *
	 * @param idFachklasse - ID der Fachklasse
	 * @return Schlüsselwert
	 */
	public static String getFachklasseById(final Long idFachklasse) {
		return ersetzeNullDurchLeer(Fachklasse.data().getSchluesselByIDOrNull(idFachklasse));
	}

	/**
	 * Holt den Schlüsselwert aus dem zugehörigen Katalog oder gibt einen Leerstring zurück, falls zu der ID kein Eintrag gefunden wird.
	 *
	 * @param idFoerderschwerpunkt - ID des Foerderschwerpunkt
	 * @param foerderschwerpunktIdMap
	 * @return Schlüsselwert
	 */
	public static String getFoerderschwerpunktById(final Long idFoerderschwerpunkt, final Map<Long, Long> foerderschwerpunktIdMap) {
		return ersetzeNullDurchLeer(Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIdMap.get(idFoerderschwerpunkt)));
	}

	/**
	 * Holt den Schlüsselwert aus dem zugehörigen Katalog oder gibt einen Leerstring zurück, falls zu der ID kein Eintrag gefunden wird.
	 *
	 * @param idKlassenart - ID der Klassenart
	 * @return Schlüsselwert
	 */
	public static String getKlassenartById(final Long idKlassenart) {
		return ersetzeNullDurchLeer(Klassenart.data().getSchluesselByIDOrNull(idKlassenart));
	}

	/**
	 * @param idStaatsangehoerigkeit
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @return der ISO3-Wert zur übergebenen ID
	 */
	public static String getNationalitaetIso3(final Long idStaatsangehoerigkeit, final int schuljahr) {
		return Nationalitaeten.data().getWertByIDOrNull(idStaatsangehoerigkeit) == null ? ""
				: Nationalitaeten.data().getWertByID(idStaatsangehoerigkeit).daten(schuljahr).iso3;
	}

	/**
	 * Holt den Schlüsselwert aus dem zugehörigen Katalog oder gibt einen Leerstring zurück, falls zu der ID kein Eintrag gefunden wird.
	 *
	 * @param idSchulgliederung - ID der Schulgliederung
	 * @return Schlüsselwert
	 */
	public static String getSchulgliederungById(final Long idSchulgliederung) {
		return ersetzeNullDurchLeer(Schulgliederung.data().getSchluesselByIDOrNull(idSchulgliederung));
	}

	/**
	 * Prüft, ob der Schüler Ausländer ist. <br>
	 * Hierzu werden sowohl die erste als auch die zweite Staatsangehörigkeit zur Prüfung herangezogen. <br>
	 * Kann keine Staatsangehörigkeit ermittelt werden, wird diese als Deutsch angenommen.
	 *
	 * @param schueler   SchuelerStatistikGesamt
	 * @param schuljahr  das zu prüfende Schuljahr
	 * @return true, Schüler Ausländer ist
	 */
	public static boolean istAuslaender(final SchuelerStatistikGesamt schueler, final int schuljahr) {
		final Long gueltigeIdStaatsangehoerigkeit = ermittleStaatsangehoerigkeit(schueler.idStaatsangehoerigkeit, schueler.idStaatsangehoerigkeit2, schuljahr);
		// Kann die Staatsangehörigkeit nicht ermittelt werden, wird diese als Deutsch angenommen.
		if (gueltigeIdStaatsangehoerigkeit == null) {
			return false;
		}

		return !Nationalitaeten.getDEU().id(schuljahr).equals(gueltigeIdStaatsangehoerigkeit);
	}

	/**
	 * Prüft, ob die Schulform 30 (BK) oder 88 (SB) entspricht.
	 *
	 * @param schulform   - Die zu prüfende Schulform
	 * @return ist berufsbildend laut Definition der ASD
	 */
	public static boolean istBK(final Schulform schulform) {
		return ((Schulform.BK == schulform) || (Schulform.SB == schulform));
	}

}
