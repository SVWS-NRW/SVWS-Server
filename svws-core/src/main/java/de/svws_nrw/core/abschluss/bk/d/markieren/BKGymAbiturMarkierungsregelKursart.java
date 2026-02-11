package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl von Kursen entsprechend der Kursart.
 */
public class BKGymAbiturMarkierungsregelKursart extends BKGymAbiturMarkierungsregel {

	/** die Kursart: LK1, LK2, AB3, AB4, AB5 */
	final @NotNull String kursart;

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse einer Kursart
	 *
	 * @param kursart        die Art des Kurses
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelKursart(final @NotNull String kursart, final int anzahl,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.kursart = kursart;
		this.anzahl = anzahl;
	}


	/**
	 * Führt die Markierung entsprechend der Kursart durch.
	 *
	 * @param variante   die zu bearbeitende Variante
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final GostAbiturFach abifach = GostAbiturFach.fromKuerzel(kursart);
		if (abifach == null)
			throw new DeveloperNotificationException("Die Prüfbedingung " + kuerzel + " enthält die unzulässige Kursart '" + kursart + "'.");
		final Long abiFachID = variante.varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(abifach);
		if (abiFachID == null) {
			variante.addLogEintrag(1, "Fehler: Eine entsprechende Belegung konnte nicht gefunden werden.");
			variante.setHatZulassung(false);
			return;
		}

		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> ((markierung != null) && (markierung.fachID == abiFachID)
						&& (markierung.punkte != null) && (markierung.punkte > 0));
		final int verbleibend = variante.markiereKursanzahl(anzahl, bedingung);
		variante.addLogAnzahlMarkierungen(verbleibend, anzahl, 1);
	}
}
