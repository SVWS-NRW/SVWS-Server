package de.svws_nrw.core.logger;

import java.util.ArrayList;
import java.util.function.Consumer;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Loggen von Informationen im SVWS-Server. Dabei
 * können mehrere Ziele (Consumer) für die Log-Informationen definiert werden,
 * welche diese weiterverarbeiten.
 */
public class Logger {

	/** Die globale Instanz des Loggers */
	private static Logger _instance = null;

	/// Ein interner Vektor zum Speichern der Consumer von Log-Informationen.
	private final @NotNull ArrayList<Consumer<LogData>> consumer = new ArrayList<>();

	/// Das Standard-Log-Level, welches für neue Log-Informationen genutzt wird.
	private @NotNull LogLevel defaultLevel = LogLevel.INFO;

	/// Die Einrückung für die Ausgabe, die bei neuen Log-Informationen genutzt wird.
	private int indent = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Logger() {
		// leer
	}

	/**
	 * Gibt die Instanz des Loggers zurück.
	 *
	 * @return die globale Logger-Instanz
	 */
	public static Logger global() {
		if (_instance == null) {
			_instance = new Logger();
			_instance.addConsumer(new LogConsumerConsole(true, true));
		}
		return _instance;
	}


	/**
	 * Fügt einen Consumer für Log-Informationen zum Logger hinzu.
	 *
	 * @param c   der hinzuzufügende Consumer von Log-Informationen
	 */
	public void addConsumer(final @NotNull Consumer<LogData> c) {
		consumer.add(c);
	}


	/**
	 * Fügt alle Consumer des anderen Loggers zu diesem hinzu.
	 *
	 * @param other   der andere Logger
	 */
	public void copyConsumer(final @NotNull Logger other) {
		consumer.addAll(other.consumer);
	}


	/**
	 * Entfernt den angegeben Consumer für Log-Informationen aus dem Logger.
	 *
	 * @param c   der zu entfernende Consumer von Log-Informationen
	 */
	public void removeConsumer(final @NotNull Consumer<LogData> c) {
		consumer.remove(c);
	}


	/**
	 * Gibt das aktuelle Default-Log-Level für neue Log-Informationen zurück.
	 *
	 * @return das aktuelle Default-Log-Level für neue Log-Informationen
	 */
	public @NotNull LogLevel getDefaultLevel() {
		return defaultLevel;
	}


	/**
	 * Setzt das Default-Log-Level für neue Log-Informationen.
	 *
	 * @param defaultLevel   das neue Default-Log-Level für neue Log-Informationen
	 */
	public void setDefaultLevel(final @NotNull LogLevel defaultLevel) {
		this.defaultLevel = defaultLevel;
	}


	/**
	 * Gibt die Anzahl der Leerzeichen zurück, die für die Einrückung bei einer Ausgabe von
	 * neuen Log-Informationen genutzt wird.
	 *
	 * @return die Anzahl der Leerzeichen
	 */
	public int getIndent() {
		return indent;
	}


	/**
	 * Setzt die Anzahl der Leerzeichen, die für die Einrückung bei einer Ausgabe von
	 * neuen Log-Informationen genutzt wird.
	 *
	 * @param indent   die Anzahl der Leerzeichen
	 */
	public void setIndent(final int indent) {
		this.indent = (indent < 0) ? 0 : indent;
	}


	/**
	 * Verändert die Anzahl der Leerzeichen, die für die Einrückung bei einer Ausgabe von
	 * neuen Log-Informationen genutzt wird.
	 *
	 * @param indent   die Veränderung bei der Anzahl der Leerzeichen
	 */
	public void modifyIndent(final int indent) {
		this.indent = ((this.indent + indent) < 0) ? 0 : (this.indent + indent);
	}



	/**
	 * Loggt die übergebenen Log-Informationen bei diesem Debugger.
	 *
	 * @param data   die Log-Informationen
	 */
	private void log(final @NotNull LogData data) {
		for (final @NotNull Consumer<LogData> c : consumer)
			c.accept(data);
	}



	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der angegebenen Einrückung.
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public void log(final @NotNull LogLevel level, final int indent, final @NotNull String text) {
		log(new LogData(level, indent, false, text));
	}


	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der angegebenen Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public void logLn(final @NotNull LogLevel level, final int indent, final @NotNull String text) {
		log(new LogData(level, indent, true, text));
	}


	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der Standard-Einrückung.
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param text     der Text
	 */
	public void log(final @NotNull LogLevel level, final @NotNull String text) {
		log(level, this.indent, text);
	}


	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der Standard-Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param text     der Text
	 */
	public void logLn(final @NotNull LogLevel level, final @NotNull String text) {
		logLn(level, this.indent, text);
	}


	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der angegebenen Einrückung.
	 *
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public void log(final int indent, final @NotNull String text) {
		log(defaultLevel, indent, text);
	}


	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der angegebenen Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public void logLn(final int indent, final @NotNull String text) {
		logLn(defaultLevel, indent, text);
	}


	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der Standard-Einrückung.
	 *
	 * @param text     der Text
	 */
	public void log(final @NotNull String text) {
		log(defaultLevel, text);
	}


	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der Standard-Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 * @param text     der Text
	 */
	public void logLn(final @NotNull String text) {
		logLn(defaultLevel, text);
	}

}
