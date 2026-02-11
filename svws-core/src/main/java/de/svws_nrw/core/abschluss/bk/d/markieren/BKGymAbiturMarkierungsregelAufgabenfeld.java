package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.bk.BKGymAufgabenfeld;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert die geforderte Anzahl von Kursen entsprechend des
 * Aufgabenfeldes.
 */
public class BKGymAbiturMarkierungsregelAufgabenfeld extends BKGymAbiturMarkierungsregel {

	/** das Aufgabenfeld I, II, III*/
	final @NotNull String aufgabenfeld;

	/** die erforderliche Anzahl */
	final int anzahl;

	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse eines Aufgabenfeldes
	 *
	 * @param aufgabenfeld   die Art des Kurses
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelAufgabenfeld(final @NotNull String aufgabenfeld, final int anzahl,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.aufgabenfeld = aufgabenfeld;
		this.anzahl = anzahl;
	}


	/**
	 * Markiert Kurse entsprechend der geforderten Anzahl Kurse für ein Aufgabenfeld
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final BKGymAufgabenfeld feld = BKGymAufgabenfeld.getAufgabenfeldFromKuerzel(aufgabenfeld);
		if (feld == null)
			throw new DeveloperNotificationException("Die Prüfbedingung " + kuerzel + "spezifiziert ein nicht vorhandenes Aufgabenfeld.");

		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> (markierung != null)
				&& feld.hatFachbezeichnung(variante.varianten.abiturdatenManager.getFaecherManager().getBezeichnungByFachID(markierung.fachID));
		final int anzBereitsMarkiert = variante.zaehleMarkierte(bedingung);
		int verbleibend = anzahl;
		if (anzBereitsMarkiert > 0) {
			if (anzBereitsMarkiert >= verbleibend) {
				variante.addLogEintrag(1, "Die erforderliche Anzahl an Kursen ist bereits markiert.");
				return;
			}
			variante.addLogEintrag(1, "" + anzBereitsMarkiert + " Kurse sind bereits markiert.");
			verbleibend -= anzBereitsMarkiert;
		}

		verbleibend = variante.markiereKursanzahl(verbleibend, bedingung);
		variante.addLogAnzahlMarkierungen(verbleibend, anzahl, 1);
	}
}
