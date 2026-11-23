package de.svws_nrw.module.reporting.types.ankreuzkompetenz;

import java.util.Arrays;

import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für die Konfiguration der Ankreuzkompetenzen der Schule (Tabelle K_Ankreuzdaten): die Texte der
 * Stufen 1 bis 5 und die Bezeichnung der frei definierbaren Zeugnisrubrik.
 * <p>
 * Die Anzahl der Stufen ergibt sich aus den Texten: Gezählt wird ab Stufe 1 bis zum ersten fehlenden Text. Ein Text fehlt, wenn er
 * {@code null}, leer oder nur aus Leerzeichen ist, damit auf dem Zeugnis keine Spalte ohne Überschrift entsteht. Ein „#“ im Text steht für
 * einen Zeilenumbruch im Spaltenkopf und wird im Fließtext zu einem Leerzeichen.
 */
public class ReportingAnkreuzkompetenzKonfiguration extends ReportingBaseType {

	/** Die höchste mögliche Anzahl an Stufen. */
	private static final int MAX_STUFEN = 5;

	/** Die Texte der Stufen 1 bis 5 (Index 0 bis 4); einzelne Einträge können {@code null} sein. */
	protected String[] textStufen;

	/** Die Bezeichnung der frei definierbaren Zeugnisrubrik „Sonstiges“. */
	protected String textSonstiges;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param textStufen    Die Texte der Stufen 1 bis 5; {@code null} oder ein kürzeres Array sind zulässig, fehlende Einträge gelten als fehlende Texte.
	 * @param textSonstiges Die Bezeichnung der frei definierbaren Zeugnisrubrik; {@code null} wird zu einem leeren String.
	 */
	public ReportingAnkreuzkompetenzKonfiguration(final String[] textStufen, final String textSonstiges) {
		this.textStufen = new String[MAX_STUFEN];
		if (textStufen != null) {
			System.arraycopy(textStufen, 0, this.textStufen, 0, Math.min(textStufen.length, MAX_STUFEN));
		}
		this.textSonstiges = ersetzeNullBlankTrim(textSonstiges);
	}



	// ##### Berechnete Methoden #####

	/**
	 * Die Anzahl der Stufen: die Zahl der Texte ab Stufe 1 bis zum ersten fehlenden Text ({@code null}, leer oder nur Leerzeichen).
	 *
	 * @return Die Anzahl der Stufen, 0 bis 5.
	 */
	public int anzahlStufen() {
		int anzahl = 0;
		while ((anzahl < MAX_STUFEN) && !einzeilig(textStufen[anzahl]).isEmpty()) {
			anzahl++;
		}
		return anzahl;
	}

	/**
	 * Der Text zur angegebenen Stufe in einer Zeile: getrimmt, ein „#“ wird zu einem Leerzeichen.
	 *
	 * @param stufe Die Stufe, beginnend bei 1.
	 *
	 * @return Der Text der Stufe; nie {@code null}, ein leerer String, wenn die Stufe kleiner als 1 oder größer als {@link #anzahlStufen()} ist.
	 */
	public String textStufe(final int stufe) {
		if ((stufe < 1) || (stufe > anzahlStufen())) {
			return "";
		}
		return einzeilig(textStufen[stufe - 1]);
	}

	private static String einzeilig(final String text) {
		if (text == null) {
			return "";
		}
		return String.join(" ", Arrays.stream(text.split("#")).map(String::trim).filter(teil -> !teil.isEmpty()).toList());
	}



	// ##### Getter #####

	/**
	 * Die Bezeichnung der frei definierbaren Zeugnisrubrik „Sonstiges“.
	 *
	 * @return Inhalt des Feldes textSonstiges; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String textSonstiges() {
		return textSonstiges;
	}

}
