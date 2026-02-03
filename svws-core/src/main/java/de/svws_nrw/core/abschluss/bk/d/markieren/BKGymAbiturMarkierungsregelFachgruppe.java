package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungManager;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.bk.BKGymAufgabenfeld;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl Kurse einer Fachgruppe wie zum Beispiel der Fachgruppe NW.
 */
public class BKGymAbiturMarkierungsregelFachgruppe extends BKGymAbiturMarkierungsregel {

	/** die Bezeichnung der Fachgruppe */
	final @NotNull String fachgruppe;

	/** die erforderliche Anzahl */
	final int anzahl;

	/** belegt seit */
	final String belegtSeit;

	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl von Kursen einer Fachgruppe
	 *
	 * @param fachgruppe     die Art des Kurses
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param belegtSeit     seit welchem GostHalbjahr der Kurs belegt sein muss
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelFachgruppe(final @NotNull String fachgruppe, final int anzahl, final String belegtSeit,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.fachgruppe = fachgruppe;
		this.anzahl = anzahl;
		this.belegtSeit = belegtSeit;
	}


	/**
	 * Führt die Markierung entsprechend der Fachgruppe durch.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final BKGymAufgabenfeld gruppe = BKGymAufgabenfeld.getAufgabenfeldFromKuerzel(fachgruppe);
		if (gruppe == null)
			throw new DeveloperNotificationException("Die Prüfbedingung spezifiziert eine nicht vorhandene Fachgruppe.");

		final @NotNull ArrayList<@NotNull Long> faecher = moeglicheFaecherAusFachgruppe(gruppe, belegtSeit, variante);
		if (pruefeBereitsMarkiert(faecher, variante)) {
			variante.addLogEintrag(1, "Die erforderliche Anzahl an Kursen ist bereits markiert.");
			return;
		}

		final Long bestesFachID = ermittleBesteFachID(faecher, variante);
		if (bestesFachID == null) {
			variante.addLogAnzahlMarkierungen(anzahl, anzahl, 1);
			return;
		}
		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> (markierung != null) && (bestesFachID == markierung.fachID);
		variante.markiereKursanzahl(anzahl, bedingung);
		variante.addLogAnzahlMarkierungen((bestesFachID == -1 ? anzahl : 0), anzahl, 1);
	}


	/**
	 * Ermittelt aus der Liste der Fächer das Fach, das die meisten Punkte liefert und
	 * gibt dessen ID zurück.
	 *
	 * @param faecher    die Fachbezeichnungen der zu untersuchenden Fächer
	 * @param variante   die Variante
	 *
	 * @return die ID des besten Fachs oder null wenn es keins gibt.
	 */
	private Long ermittleBesteFachID(final @NotNull ArrayList<@NotNull Long> faecher,
		final @NotNull BKGymAbiturMarkierungsVariante variante) {
		Long beste = null;
		int maxPunktanzahl = 0;
		for (final @NotNull Long fachID : faecher) {
			final int punkte = variante.punktsummeFuerFach(fachID, anzahl);
			if (punkte > maxPunktanzahl) {
				beste = fachID;
				maxPunktanzahl = punkte;
			}
		}
		return beste;
	}


	/**
	 * Prueft ob die geforderte Anzahl von Markierungen schon vorhanden sind
	 *
	 * @param faecher    die FachIDs, der in Frage kommenden Fächer
	 * @param variante   die Variante
	 *
	 * @return true, wenn Markierungen vorhanden sind, sonst false
	 */
	private boolean pruefeBereitsMarkiert(final @NotNull ArrayList<@NotNull Long> faecher,
			final @NotNull BKGymAbiturMarkierungsVariante variante) {
		//prüfe ob bereits markiert
		for (final long fachID : faecher) {
			final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
					markierung -> (markierung != null) && (fachID == markierung.fachID);
			if (variante.zaehleMarkierte(bedingung) >= anzahl)
				return true;
		}
		return false;
	}


	/**
	 * ermittelt die Fachbezeichnungen des Aufgabenfeldes/der Fachgruppe, die ab dem gegebenen Halbjahr in der
	 * Einführungsphase belegt sind oder alle Fächer des Aufgabenfeldes/der Fachgruppe, wenn seitHalbjahr leer ist.
	 *
	 * @param gruppe         das Aufgabenfeld/die Fachgruppe
	 * @param seitHalbjahr   ggfs. das Halbjahr, ab dem belegt sein muss in EF
	 * @param variante       eine Markierungsvariante, auf der gearbeitet wird
	 *
	 * @return die Liste der Fachbezeichnungen
	 */
	private static @NotNull ArrayList<@NotNull Long> moeglicheFaecherAusFachgruppe(final @NotNull BKGymAufgabenfeld gruppe, final String seitHalbjahr,
			final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final @NotNull BKGymFachbelegungManager fachbelegungManager = variante.varianten.abiturdatenManager.getFachbelegungManager();
		// prüfe gegebenenfalls die Belegung in der Einführungsphase
		final @NotNull ArrayList<@NotNull Long> result = new ArrayList<>();
		if ((seitHalbjahr == null) || seitHalbjahr.isEmpty()) {
			for (final @NotNull String fach : gruppe.getFaecher()) {
				result.add(fachbelegungManager.getFachIDByBezeichnung(fach));
			}
			return result;
		}
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(seitHalbjahr);
		if (hj == null)
			throw new DeveloperNotificationException("Die Prüfbedingung enthält ein ungültiges GostHalbjahr '" + seitHalbjahr + "'.");

		final @NotNull GostHalbjahr @NotNull [] hje = GostHalbjahr.getHalbjahreAbHalbjahr(hj);
		final @NotNull ArrayList<GostHalbjahr> leereHje = new ArrayList<>();
		for (final @NotNull GostHalbjahr h : hje)
			if (!variante.varianten.abiturdatenManager.istBewertet(h))
				leereHje.add(h);
		if (!leereHje.isEmpty())
			variante.addLogEintrag(1, "HINWEIS: Nicht alle Halbjahre bewertet. Bitte die erforderliche Belegung der Fachgruppe '" + gruppe.name() + "' prüfen!");
		for (final @NotNull String fach : gruppe.getFaecher())
			if (fachbelegungManager.pruefeBelegung(fachbelegungManager.getFachbelegungByBezeichnung(fach), leereHje, hje))
				result.add(fachbelegungManager.getFachIDByBezeichnung(fach));

		return result;
	}
}
