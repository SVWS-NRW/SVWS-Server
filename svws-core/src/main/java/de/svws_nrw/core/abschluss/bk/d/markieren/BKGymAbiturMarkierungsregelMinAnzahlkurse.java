package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert weitere Kurse bis die geforderte Anzahl von Kursen markiert ist.
 */
public class BKGymAbiturMarkierungsregelMinAnzahlkurse extends BKGymAbiturMarkierungsregel {

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung weiterer Kurse bis die geforderte Anzahl Kurse insgesamt markiert ist.
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelMinAnzahlkurse(final int anzahl,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}


	/**
	 * Es werden zusätzlich soviel Kurse markiert, so dass die angegeben Zahl an Kursen erreicht wird.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final int anzMarkiert = variante.anzahlEingebrachteKurse();
		if (anzMarkiert >= anzahl) {
			variante.addLogEintrag(1, "Es sind bereits " + anzMarkiert + " Kurse durch die vorherigen Bedingungen markiert.");
			return;
		}
		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung = markierung -> true;
		variante.markiereKursanzahl(anzahl - anzMarkiert, bedingung);
		variante.addLogAnzahlMarkierungen(anzahl - variante.anzahlEingebrachteKurse(), anzahl, 1);
	}
}
