package de.svws_nrw.core.abschluss.bk.d.markieren;

import jakarta.validation.constraints.NotNull;

/*
 * Diese Klasse prüft, ob die Voraussetzung zum Markieren gegeben sind.
 * Sie prüft ob die Halbjahre EF.1 bis Q2.2 gewertet sind.
 */
public class BKGymAbiturMarkierungsregelPruefeVorraussetzung extends BKGymAbiturMarkierungsregel {

	/**
	 * erstellt eine Regel zur Prüfung der Belegung der zweiten Fremdsprache in der Qualifikationsphase
	 *
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelPruefeVorraussetzung(final @NotNull String regelkuerzel, final @NotNull String hinweis,
			final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
	}


	/**
	 * Prüft, dass alle sechs Halbjahre gewertet sind.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (variante.varianten.abiturdatenManager.istBewertetQualifikationsPhase()) {
			variante.addLogEintrag(1, "Alle Halbjahre sind gewertet.");
			return;
		}

		variante.addLogEintrag(1, "Nicht alle Halbjahre sind gewertet, Markierung der Kurse nicht möglich.");
		variante.setHatZulassung(false);
		variante.setGestoppt(true);
	}
}
