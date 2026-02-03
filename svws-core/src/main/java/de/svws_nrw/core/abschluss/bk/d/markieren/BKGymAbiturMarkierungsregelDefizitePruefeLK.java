package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import jakarta.validation.constraints.NotNull;

/*
 * Diese Klasse prüft auf zulässige Zahl von Defiziten im LK-Bereich
 */
public class BKGymAbiturMarkierungsregelDefizitePruefeLK extends BKGymAbiturMarkierungsregel {

	/** die erforderliche Anzahl */
	final int erlaubteDefizite;

	/**
	 * erstellt eine Regel zur Prüfung der Belegung der zweiten Fremdsprache in der Qualifikationsphase
	 *
	 * @param anzahl         die erlaubte Anzahl an Defiziten
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelDefizitePruefeLK(final int anzahl,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.erlaubteDefizite = anzahl;
	}


	/**
	 * Prüft, dass nicht mehr als die erlaubte Anzahl an Defiziten im LK-Bereich vorliegen
	 *
	 * Gegebenenfalls wird der Erfolg der Variante auf false gesetzt.
	 * § 15 Abs. 2 Nr. 4 : Schülerinnen und Schüler, die in der Sekundarstufe I keinen durchgängigen Unterricht in einer zweiten Fremdsprache im Umfang
	 * von mindestens vier Jahren erhalten haben, dürfen zum Erwerb der allgemeinen Hochschulreife in keinem der vier in der Qualifikationsphase
	 * belegten Kurse der in der Jahrgangsstufe 11 neu einsetzenden Fremdsprache mit null Punkten bewertet worden sein.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final int anzahlLKDefizite = anzahlDefizite("LK1", variante) + anzahlDefizite("LK2", variante);
		if (anzahlLKDefizite <= erlaubteDefizite)
			variante.addLogEintrag(1, "Defizite im LK-Bereich: Erlaubt=" + erlaubteDefizite + " Ist=" + anzahlLKDefizite + ".");
		else {
			variante.addLogEintrag(1, "Fehler: Es sind mehr als die " + erlaubteDefizite + " erlaubten Defizite im LK-Bereich vorhanden.");
			variante.setHatZulassung(false);
		}
	}


	/**
	 * Diese Methode zählt die Anzahl der Defizite für die angegebene Kursart von markierten Kursen
	 *
	 * @param kursart    die Kursart z.B. LK1
	 * @param variante   die Variante
	 *
	 * @return die Anzahl der Defizite
	 */
	private int anzahlDefizite(final @NotNull String kursart, final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final GostAbiturFach abifach = GostAbiturFach.fromKuerzel(kursart);
		if (abifach == null)
			throw new DeveloperNotificationException("Die Prüfbedingung " + kuerzel + " enthält die unzulässige Kursart '" + kursart + "'.");
		final Long abiFachID = variante.varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(abifach);
		if (abiFachID == null)
			return 0;

		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> ((markierung != null) && (markierung.fachID == abiFachID)
						&& (markierung.punkte != null) && (markierung.punkte < 5));
		return variante.zaehleMarkierte(bedingung);
	}
}
