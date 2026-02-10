package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert weitere Kurse bis zur angegeben maximalen Anzahl solange
 * sich die Punktzahl in Block I verbessert.die geforderte Anzahl von Kursen entsprechend des
 */
public class BKGymAbiturMarkierungsregelMaxAnzahlkurse extends BKGymAbiturMarkierungsregel {

	/** die maximal erlaubte Anzahl an Kursen */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung weiterer Kurse, wenn sich das Ergebnis in Block I verbessert.
	 *
	 * @param anzahl         die maximale Anzahl an Kursen
	 * @param varKennung     Kennung der zu bearbeitenden Variante oder null für alle
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelMaxAnzahlkurse(final int anzahl, final String varKennung,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}


	/**
	 * Markiert bis zur angegebenen Anzahl Kurse, solange sich das Ergebnis verbessert.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final int vorherMarkiert = variante.anzahlEingebrachteKurse();
		if (vorherMarkiert > anzahl)
			throw new DeveloperNotificationException("Es wurden mehr Kurse markiert als maximal erlaubt ist.");
		if (vorherMarkiert == anzahl) {
			variante.addLogEintrag(1, "Es sind bereits " + vorherMarkiert + " Kurse durch die vorherigen Bedingungen markiert.");
			return;
		}
		// weitere Kurse markieren
		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> (markierung != null)
				&& (markierung.punkte != null) && (variante.getDurchschnitt() < markierung.punkte);
		variante.markiereKursanzahl(anzahl - vorherMarkiert, bedingung);
		final int jetztMarkiert = variante.anzahlEingebrachteKurse();
		if (vorherMarkiert < jetztMarkiert)
			variante.addLogEintrag(1, "Es konnten " + (jetztMarkiert - vorherMarkiert) + " weitere Kurse zur Verbesserung markiert werden.");
		else
			variante.addLogEintrag(1, "Weitere Kurse wurden nicht markiert, da dadurch keine Verbesserung erreicht wird.");
	}
}
