package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import jakarta.validation.constraints.NotNull;

/*
 * Diese Klasse prüft die geforderte Belegung der neu einsetzenden Fremdsprache in der
 * Qualifikationsphase
 */
public class BKGymAbiturMarkierungsregelFremdsprachePruefeNeue extends BKGymAbiturMarkierungsregel {

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Prüfung der Belegung der zweiten Fremdsprache in der Qualifikationsphase
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelFremdsprachePruefeNeue(final int anzahl,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}


	/**
	 * Prüft, dass alle 4 neuen Fremdsprachenkurse mehr als 0 Punkte haben, wenn keine ausreichende Fremdsprachenbelegung in SEK-I.
	 * Gegebenenfalls wird der Erfolg der Variante auf false gesetzt.
	 * § 15 Abs. 2 Nr. 4 : Schülerinnen und Schüler, die in der Sekundarstufe I keinen durchgängigen Unterricht in einer zweiten Fremdsprache im Umfang
	 * von mindestens vier Jahren erhalten haben, dürfen zum Erwerb der allgemeinen Hochschulreife in keinem der vier in der Qualifikationsphase
	 * belegten Kurse der in der Jahrgangsstufe 11 neu einsetzenden Fremdsprache mit null Punkten bewertet worden sein.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (variante.varianten.abiturdatenManager.getZweiteFremdspracheInSekIErfuellt()) {
			variante.addLogEintrag(1, "Ausreichende Belegung einer zweiten Fremdsprache in der SekI liegt vor.");
			return;
		}
		final Long zweiteFremdspracheID = variante.varianten.abiturdatenManager.getFachbelegungManager().getZweiteFremdspracheID();
		if (zweiteFremdspracheID == null) {
			variante.addLogEintrag(1, "Fehler: Es fehlt die Belegung der zweiten Fremdsprache, da nicht in der SekI abgedeckt.");
			variante.setHatZulassung(false);
			return;
		}
		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> (markierung != null) && (markierung.fachID == zweiteFremdspracheID)
				&& (markierung.punkte != null) && (markierung.punkte > 0);
		final int verbleibend = variante.pruefeKursanzahl(anzahl, bedingung);
		if (verbleibend == 0)
			variante.addLogEintrag(1, "Alle Kurshalbjahre in der Qualifikationsphase mit mindestens einem Punkt abgeschlossen.");
		else {
			variante.addLogEintrag(1, "Fehler: Nur " + (anzahl - verbleibend) + " von " + anzahl + " Kursen haben mehr als 0 Punkte.");
			variante.setHatZulassung(false);
		}
	}
}
