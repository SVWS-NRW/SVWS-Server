package de.svws_nrw.core.logger;

import java.util.Arrays;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse enthält eine Log-Information. Sie unterstützt das Comparable-Interface,
 * um Log-Daten anhand des Zeitstempels zu vergleichen und damit einen Sortierung zu
 * erlauben.
 */
public final class LogData implements Comparable<LogData> {

	/// der Zeitstempel der Log-Information
	private final long time;

	/// das zugehörige Log-Level
	private final @NotNull LogLevel level;

	/// der Text der Log-Information
	private final @NotNull String text;

	/// gibt an, ob die Log-Informationen beim Ausgeben mit einer neuen Zeile beendet werden sollen oder nicht
	private final boolean newLine;

	/// die Anzahl der Leerzeichen, die bei der Ausgabe zur Einrückung genutzt werden sollen.
	private int indent;


	/**
	 * Erzeugt eine neue Log-Information.
	 *
	 * @param level     das zugehörige Log-Level
	 * @param indent    die Anzahl der Leerzeichen, die bei der Ausgabe zur Einrückung genutzt werden sollen
	 * @param newLine   gibt an, ob die Log-Informationen beim Ausgeben mit einer neuen Zeile beendet werden sollen oder nicht
	 * @param text      der Text der Log-Information
	 */
	public LogData(final @NotNull LogLevel level, final int indent, final boolean newLine, final @NotNull String text) {
		this.time = System.currentTimeMillis();
		this.level = level;
		this.indent = (indent < 0) ? 0 : indent;
		this.newLine = newLine;
		this.text = (text == null) ? "" : text;
	}


	/**
	 * Vergleicht zwei Log-Informationen anhand der Zeit.
	 *
	 * @param other   die zu vergleichenden Log-Informationen
	 *
	 * @return ein negativer Wert, falls diese Log-Information früher geloggt wurde, 0, falls
	 *         sie zur gleichen Zeit geloggt wurden oder ein positiver Wert, falls diese
	 *         Log-Information später geloggt wurde
	 */
	@Override
	public int compareTo(final @NotNull LogData other) {
		// TODO compare by time (Long.compare(this.time, other.time)), then by level, then by newLine an then by text
		if (this.time < other.time)
			return -1;
		if (this.time > other.time)
			return 1;
		return 0;
	}


	/**
	 * Gibt diese Log-Information als JSON-String mit Zeit und Log-Level zurück.
	 *
	 * @return die Log-Informationen als JSON-String
	 */
	@Override
	public @NotNull String toString() {
		return "{ \"time\":" + time + ", \"level\":" + level.toInteger() + ", \"text\":\"" + getText() + "\"}";
	}


	/**
	 * Erhöht die Einrückung bei der Ausgabe, um den angegebenen Wert.
	 *
	 * @param indent   der Wert, um den die Einrückung erhöht wird.
	 */
	public void addIndent(final int indent) {
		this.indent += indent;
	}


	/**
	 * Gibt den Zeitstempel der Log-Information zurück.
	 *
	 * @return der Zeitstempel der Log-Information
	 */
	public long getTime() {
		return time;
	}


	/**
	 * Gibt das Log-Level der Log-Information zurück.
	 *
	 * @return das Log-Level der Log-Information
	 */
	public @NotNull LogLevel getLevel() {
		return level;
	}


	/**
	 * Gibt an, ob bei der Ausgabe mit einer neue Zeile beendet werden soll oder nicht.
	 *
	 * @return true, falls die Ausgabe mit einer neuen Zeile beendet werden soll, ansonsten false
	 */
	public boolean isNewLine() {
		return newLine;
	}


	/**
	 * Gibt den Text dieser Log-Information mit Einrückung, aber ohne Zeitangabe und
	 * Log-Level als String zurück.
	 *
	 * @return der Text dieser Log-Information
	 */
	public @NotNull String getText() {
		if (indent <= 0)
			return text;
		final char[] indentChars = new char[indent];
		Arrays.fill(indentChars, ' ');
		return new String(indentChars) + text;
	}


	@Override
	public int hashCode() {
		return super.hashCode();
	}


	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof LogData)
			return compareTo((LogData) obj) == 0;
		return super.equals(obj);
	}

}


