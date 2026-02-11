package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse markiert zusätzliche Kurse, um die Defizitregeln im Abitur
 * zu erfüllen.
 */
public class BKGymAbiturMarkierungsregelDefizite extends BKGymAbiturMarkierungsregel {

	/** die erforderliche Anzahl der Kurse */
	final int anzahlKurse;

	/** die erlaubte Anzahl an Defiziten */
	final int erlaubteDefizite;

	/**
	 * erstellt eine Regel zur Markierung weiterer Kurse um die Defizitregeln zu erfüllen
	 *
	 * @param anzahlKurse        die Art des Kurses
	 * @param erlaubteDefizite   die geforderte Anzahl an Kursen
	 * @param regelkuerzel       das eindeutige Kürzel dieser Regel
	 * @param hinweis            Hinweis für das log
	 * @param bezugAPOBK         Referenz in den APO-BK
	 */
	public BKGymAbiturMarkierungsregelDefizite(final int anzahlKurse, final int erlaubteDefizite,
			final @NotNull String regelkuerzel, final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.anzahlKurse = anzahlKurse;
		this.erlaubteDefizite = erlaubteDefizite;
	}


	/**
	 * Markiert weitere Kurse wenn zu viele Defizite vorhanden sind
	 */
	@Override
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (variante.sindDefizitregelnAbgeschlossen()) {
			variante.addLogEintrag(1, "Die Defizitregel wurde bereits erfüllt.");
			return;
		}
		if (variante.getDefizite() < erlaubteDefizite) {
			variante.addLogEintrag(1, "Die erlaubte Anzahl von " + erlaubteDefizite + " Defiziten wurde nicht überschritten.");
			return;
		}
		if (variante.getDefizite() == erlaubteDefizite) {
			variante.setDefizitregelnAbgeschlossen(true);
			if (variante.anzahlEingebrachteKurse() >= anzahlKurse) {
				variante.addLogEintrag(1, "Die erlaubte Anzahl von " + erlaubteDefizite + " Defiziten wurde nicht überschritten.");
				return;
			}
			final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung = markierung -> true;
			variante.markiereKursanzahl(anzahlKurse - variante.anzahlEingebrachteKurse(), bedingung);
		}
		if (variante.getDefizite() > erlaubteDefizite) {
			variante.addLogEintrag(1, "Fehler: Die Defizitregel konnte auch nicht durch Markieren weiterer Kurse erfüllt werden.");
			variante.setHatZulassung(false);
		} else {
			variante.addLogEintrag(1, "Die Defizitregel konnte durch Markieren weiterer Kurse erfüllt werden.");
		}
	}
}
