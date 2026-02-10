package de.svws_nrw.core.abschluss.bk.d.markieren;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl von Kursen der zweiten Fremdsprache,
 * für die Variante, in der die erste Fremdsprache zunächst nicht markiert wird.
 */
public class BKGymAbiturMarkierungsregelFremdspracheZweiteNur extends BKGymAbiturMarkierungsregel {

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
	public BKGymAbiturMarkierungsregelFremdspracheZweiteNur(final int anzahl, final String varKennung,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}


	/**
	 * Führt die Markierung der zweiten Fremdsprache durch mit der angegebenen Anzahl.
	 *
	 * Entweder als zwei Pflichtkurse zusätzlich zur ersten Fremdsprache
	 * oder nur die 4 Kurse der zweiten Fremdsprache. Englisch wird erstmal nicht eingebracht.
	 *
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final String zweiteFremdsprache = variante.varianten.abiturdatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (zweiteFremdsprache == null) {
			variante.addLogEintrag(1, "Fehler: Eine zweite Fremdsprache wurde nicht belegt.");
			variante.setHatZulassung(false);
			return;
		}
		BKGymAbiturMarkierungsregel.markiereFach(zweiteFremdsprache, anzahl, variante);
	}
}
