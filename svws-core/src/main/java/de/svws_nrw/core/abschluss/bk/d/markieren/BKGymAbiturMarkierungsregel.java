package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.function.Predicate;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse hält die generellen Daten einer Markierungsregel für das
 * Berufliche Gymnasium.
 * Jede Markierungsregel muss die Methode markiere implementieren, die die
 * entsprechenden Markierungen vornimmt.
 */
public abstract class BKGymAbiturMarkierungsregel {

	/** Das eindeutige Kürzel der Regel */
	protected final @NotNull String kuerzel;

	/** Die Kennung der zu bearbeitenden Variante, null für alle */
	protected final String kennungVariante;

	/** Der Text des Fehlers, der ausgegeben wird */
	protected final @NotNull String hinweis;

	/** Der Text des Fehlers, der ausgegeben wird */
	protected final @NotNull String bezugAPOBK;

	/**
	 * Erzeugt eine Markierungsregel-Objekt.
	 * Kindklassen enthalten weitere regelspezifische Attribute
	 *
	 * @param kuerzel     das eindeutige Kürzel dieser Regel
	 * @param varKennung  Kennung der zu bearbeitenden Variante oder null für alle
	 * @param hinweis     Hinweis für das log
	 * @param bezugAPOBK  Referenz in den APO-BK
	 */
	protected BKGymAbiturMarkierungsregel(final @NotNull String kuerzel, final String varKennung,
			final @NotNull String hinweis, final @NotNull String bezugAPOBK) {
		this.kuerzel = kuerzel;
		this.kennungVariante = varKennung;
		this.hinweis = hinweis;
		this.bezugAPOBK = bezugAPOBK;
	}


	/**
	 * Die Methode, die die Markierung durchführt
	 *
	 * @param variante   die zu bearbeitende Variante
	 */
	public void markiere(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		// nichts zu tun
	}


	/**
	 * Prüft ob die Regel für die Variante angewendet werden soll und gibt dann auch
	 * die Regel ins Log aus.
	 *
	 * @param variante   die zu bearbeitende Variante
	 */
	public void exec(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		if (variante.istGestoppt())
			return;
		if ((kennungVariante != null) && !kennungVariante.equals(variante.getKennung()))
			return;
		variante.addLogEintrag(0, "Regel " + kuerzel + ": " + hinweis + " entsprechend " + bezugAPOBK);
		markiere(variante);
	}


	/**
	 * Markiert Kurse entsprechend der Fachbezeichnung.
	 * Ist hier implementiert, da von mehreren Regeln benötigt.
	 *
	 * @param fachbezeichnung   die Bezeichnung des Fachs
	 * @param anzahl            die Anzahl zu markierender Kurse
	 * @param variante          die Variante auf der markiert wird
	 */
	public static void markiereFach(final @NotNull String fachbezeichnung, final int anzahl, final @NotNull BKGymAbiturMarkierungsVariante variante) {
		final Long fachID = variante.varianten.abiturdatenManager.getFachbelegungManager().getFachIDByBezeichnung(fachbezeichnung);
		if (fachID == null) {
			variante.addLogEintrag(1, "Fehler: Eine entsprechende Belegung konnte nicht gefunden werden.");
			variante.setHatZulassung(false);
			return;
		}
		// sind schon Kurse markiert?
		final @NotNull Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung =
				markierung -> (markierung != null) && (fachID == markierung.fachID)
				&& (markierung.punkte != null) && (markierung.punkte > 0);
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

		// weitere Kurse markieren
		verbleibend = variante.markiereKursanzahl(verbleibend, bedingung);
		variante.addLogAnzahlMarkierungen(verbleibend, anzahl, 1);
	}
}
