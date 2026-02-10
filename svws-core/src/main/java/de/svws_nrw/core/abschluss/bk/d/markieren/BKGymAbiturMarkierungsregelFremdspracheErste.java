package de.svws_nrw.core.abschluss.bk.d.markieren;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl Kurse der ersten Fremdsprache
 */
public class BKGymAbiturMarkierungsregelFremdspracheErste extends BKGymAbiturMarkierungsregel {

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse der ersten Fremdsprache
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param varKennung     Kennung der zu bearbeitenden Variante oder null für alle
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelFremdspracheErste(final int anzahl, final String varKennung,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}


	/**
	 * Markiert 4 Kurse der ersten Fremdsprache (immer Englisch)
	 * § 15 Abs. 3 Nr. 2 b) : vier Kurse der aus der Sekundarstufe I fortgeführten (oder der in der Jahrgangsstufe 11 neu einsetzende Fremdsprache)
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		BKGymAbiturMarkierungsregel.markiereFach("Englisch", anzahl, variante);
	}
}
