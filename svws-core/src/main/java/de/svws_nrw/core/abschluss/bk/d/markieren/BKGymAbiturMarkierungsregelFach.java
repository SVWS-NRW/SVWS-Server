package de.svws_nrw.core.abschluss.bk.d.markieren;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl Kurse für ein Fach
 */
public class BKGymAbiturMarkierungsregelFach extends BKGymAbiturMarkierungsregel {

	/** die Bezeichnung des Fachs */
	final @NotNull String fachbezeichnung;

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse eines Fachs
	 *
	 * @param fachbezeichnung   die Art des Kurses
	 * @param anzahl            die geforderte Anzahl an Kursen
	 * @param regelkuerzel      das eindeutige Kürzel dieser Regel
	 * @param hinweis           Hinweis für das log
	 * @param bezugAPOBK        Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelFach(final @NotNull String fachbezeichnung, final int anzahl,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.fachbezeichnung = fachbezeichnung;
		this.anzahl = anzahl;
	}


	/**
	 * Führt die Markierung entsprechend des Fachs durch.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		BKGymAbiturMarkierungsregel.markiereFach(fachbezeichnung, anzahl, variante);
	}
}
