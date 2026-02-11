package de.svws_nrw.core.abschluss.bk.d.markieren;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse erzeugt eine Kopie eine Variante und fügt sie der Liste der
 * Varianten hinzu. Diese Regel dient nicht zur Markierung von weiteren Kursen,
 * sondern erzeugt eine weitere Variante, in der eine andere Markierungsoption
 * umgesetzt wird, wie es im Regelsatz des Markierungsalgorithmus definiert ist.
 */
public class BKGymAbiturMarkierungsregelKopie extends BKGymAbiturMarkierungsregel {

	/** die Kennung der Variante */
	final @NotNull String kennung;

	/** ob für Facharbeit Kopie erstellt wird */
	final boolean facharbeit;

	/**
	 * erstellt eine Regel zur Erzeugung einer neuen Variante
	 *
	 * @param kennung        die Art des Kurses
	 * @param facharbeit     ob Kopie für Variante mit Facharbeit
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelKopie(final @NotNull String kennung, final boolean facharbeit,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.kennung = kennung;
		this.facharbeit = facharbeit;
	}


	/**
	 * Führt die Markierung entsprechend der Kursart durch.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (facharbeit && !variante.varianten.abiturdatenManager.istFacharbeitVorhanden())
			return;
		variante.varianten.addVariante(new BKGymAbiturMarkierungsVariante(variante, kennung, facharbeit));
	}
}
