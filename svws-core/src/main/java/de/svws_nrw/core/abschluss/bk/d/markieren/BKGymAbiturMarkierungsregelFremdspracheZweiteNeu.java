package de.svws_nrw.core.abschluss.bk.d.markieren;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl von Kursen der zweiten Fremdsprache
 */
public class BKGymAbiturMarkierungsregelFremdspracheZweiteNeu extends BKGymAbiturMarkierungsregel {

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse der zweiten Fremdsprache
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param varKennung     Kennung der zu bearbeitenden Variante oder null für alle
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelFremdspracheZweiteNeu(final int anzahl, final String varKennung,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}


	/**
	 * Markiert zusätzlich zur ersten Fremdsprache zwei Kurse der zweiten Fremdsprache, wenn die Belegung in der SekI nicht reicht.
	 * § 15 Abs. 3 Nr. 2 f) : zum Erwerb der allgemeinen Hochschulreife ergänzend zwei Kurse der in der Jahrgangsstufe 11 neu einsetzenden Fremdsprache,
	 * wenn Schülerinnen und Schüler in der Sekundarstufe I keinen durchgängigen Unterricht in einer zweiten Fremdsprache im Umfang
	 * von mindestens vier Jahren erhalten haben.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (variante.varianten.abiturdatenManager.getZweiteFremdspracheInSekIErfuellt()) {
			variante.addLogEintrag(1, "Die Belegung der zweiten Fremdsprache in der SekI sind ausreichend.");
			return;
		}
		final String zweiteFremdsprache = variante.varianten.abiturdatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (zweiteFremdsprache == null) {
			variante.addLogEintrag(1, "Fehler: Eine zweite Fremdsprache wurde nicht belegt.");
			variante.setHatZulassung(false);
			return;
		}
		BKGymAbiturMarkierungsregel.markiereFach(zweiteFremdsprache, anzahl, variante);
	}
}
