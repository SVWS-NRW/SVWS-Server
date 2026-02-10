package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert weitere Kurse bis die geforderte Anzahl an Punkten in Block I
 * erreicht ist.
 */
public class BKGymAbiturMarkierungsregelMinAnzahlpunkte extends BKGymAbiturMarkierungsregel {
	/** die erforderliche Punktzahl */
	final int minPunkte;

	/** die maximale Anzahl von Kursen */
	final int maxKurse;

	/**
	 * erstellt eine Regel zur Markierung weitere Kurse zur Erreichung der geforderten
	 * Anzahl an Punkten in Block I.
	 *
	 * @param minPunkte      die geforderte Anzahl an Kursen
	 * @param maxKurse       die maximale Anzahl von Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelMinAnzahlpunkte(final int minPunkte, final int maxKurse,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.minPunkte = minPunkte;
		this.maxKurse = maxKurse;
	}


	/**
	 * Markiert möglichst so viele Kurse, dass die Mindestpunktzahl für Block I erreicht wird.
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (variante.getPunktzahlBlockI() >= minPunkte) {
			variante.addLogEintrag(1, "Die Mindestpunktzahl ist bereits erreicht.");
			return;
		}
		// weitere Kurse markieren
		final int vorherMarkiert = variante.anzahlEingebrachteKurse();
		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> (markierung != null) && (minPunkte < variante.getPunktzahlBlockI());
		variante.markiereKursanzahl(maxKurse - vorherMarkiert, bedingung);
		if (variante.getPunktzahlBlockI() < minPunkte) {
			variante.addLogEintrag(1, "Fehler: Die Mindestpunktzahl konnte auch nicht durch Markieren weiterer Kurse erreicht werden.");
			variante.setHatZulassung(false);
			return;
		}
		variante.addLogEintrag(1, "Durch das Markieren von " + (variante.anzahlEingebrachteKurse() - vorherMarkiert)
					+ "weiteren Kursen konnte die Mindestpunktzahl erreicht werden.");
	}
}
