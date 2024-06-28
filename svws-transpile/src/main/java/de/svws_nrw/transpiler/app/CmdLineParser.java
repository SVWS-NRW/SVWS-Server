package de.svws_nrw.transpiler.app;

import java.util.HashMap;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Umgang mit Parametern beim Aufruf von Java-Programmen aus der
 * Kommandozeile.
 */
public class CmdLineParser {

	/// Gibt an, ob die Kommandozeile bereits in Bezug auf die angegebenen Optionen untersucht wurde
	private boolean parsed = false;

	/// Die Kommandozeilen-Parameter als String-Array
	private final String[] args;

	/// Der Logger für die Ausgabe von Rückmeldungen des Parsers
	private final @NotNull Logger logger;


	/// Eine Hashmap mit allen Kommandozeilenoptionen, welche ihren Short-Tags zugeordnet sind
	private final HashMap<String, CmdLineOption> options = new HashMap<>();

	/// Eine Hashmap mit allen Kommandozeilenoptionen, welche ihren Long-Tags zugeordnet sind
	private final HashMap<String, CmdLineOption> optionsLong = new HashMap<>();

	/// Eine Hashmap, welche dem short tag einer Option den Wert zuordnet, der über den Parser eingelesen wurde
	private final HashMap<String, String> values = new HashMap<>();


	/**
	 * Dieser Konstruktor erzeugt einen neuen Parser für die
	 * Kommandozeile.
	 *
	 * @param args   die Kommandozeilenparameter
	 */
	public CmdLineParser(final String[] args) {
		this.args = args;
		this.logger = Logger.getLogger("Transpiler");
		this.logger.getParent().getHandlers()[0].setFormatter(new Formatter() {
			@Override
			public String format(final LogRecord record) {
				return record.getMessage() + System.lineSeparator();
			}
		});
	}


	/**
	 * Prüft die Kommandozeile auf die einzelnen Optionen.
	 *
	 * @throws CmdLineException    tritt auf, falls eine ungültige Option in der Kommandozeile entdeckt wird
	 */
	private void parse() throws CmdLineException {
		if (options.isEmpty())
			throw new CmdLineException(CmdLineExceptionType.NO_OPTIONS);
		// Setze die aktuelle option auf die erste definierte Option, diese wird nicht benötigt, sollte aber nicht null sein
		CmdLineOption current = options.entrySet().iterator().next().getValue();
		boolean isArgument = false;
		for (final String arg : args) {
			// Prüfe, zunächst, ob es sich um ein Argument der zuvor gelesenen Option handelt und schreibe ggf. den Wert
			if (isArgument) {
				values.put(current.getShortTag(), arg);
				isArgument = false;
			} else {
				if (arg.startsWith("--")) {
					current = optionsLong.get(arg.substring(2));
				} else if (arg.startsWith("-")) {
					current = options.get(arg.substring(1));
				} else {
					throw new CmdLineException(CmdLineExceptionType.UNKNOWN_OPTION);
				}
				if (current == null)
					throw new CmdLineException(CmdLineExceptionType.UNKNOWN_OPTION);
				if (current.hasArgument())
					isArgument = true;
				else
					values.put(current.getShortTag(), "");
			}
		}
		parsed = true;
	}


	/**
	 * Fügt eine neue Option zum Parser hinzu
	 *
	 * @param option   die hinzuzufügende Option
	 *
	 * @throws CmdLineException   tritt auf, falls der short oder long tag der Option bereits zuvor verwendte wurde
	 */
	public void addOption(final CmdLineOption option) throws CmdLineException {
		if (options.get(option.getShortTag()) != null)
			throw new CmdLineException(CmdLineExceptionType.SHORT_TAG_ALREADY_DEFINED);
		if (optionsLong.get(option.getLongTag()) != null)
			throw new CmdLineException(CmdLineExceptionType.LONG_TAG_ALREADY_DEFINED);
		options.put(option.getShortTag(), option);
		optionsLong.put(option.getLongTag(), option);
	}



	/**
	 * Liefert der Wert der Option mt dem angegebenen short tag.
	 *
	 * @param tag   das short tag der Option
	 *
	 * @return der Wert der Option
	 *
	 * @throws CmdLineException tritt auf, wenn die option nicht bekannt ist oder ein Fehler beim Parsen der Kommandozeile auftritt
	 */
	public String getValue(final String tag) throws CmdLineException {
		if (!options.containsKey(tag))
			throw new CmdLineException(CmdLineExceptionType.UNKNOWN_OPTION);
		if (!parsed)
			parse();
		return values.get(tag);
	}


	/**
	 * Liefert der Wert der Option mt dem angegebenen short tag oder im Fehlerfall
	 * den angegebenen Default-Wert
	 *
	 * @param tag   das short tag der Option
	 * @param def   der Default-Wert der Option
	 *
	 * @return der Wert der Option
	 */
	public String getValue(final String tag, final String def) {
		try {
			final String result = getValue(tag);
			if (result != null)
				return result;
			return def;
		} catch (@SuppressWarnings("unused") final CmdLineException e) {
			return def;
		}
	}


	/**
	 * Gibt zurück, ob die Option gesetzt wurde oder nicht.
	 *
	 * @param tag   das short tag der Option
	 *
	 * @return true, falls die Option gesetzt wurde, sonst false
	 */
	public boolean isSet(final String tag) {
		if (!options.containsKey(tag))
			return false;
		if (!parsed) {
			try {
				parse();
			} catch (@SuppressWarnings("unused") final CmdLineException e) {
				return false;
			}
		}
		return values.containsKey(tag);
	}



	/**
	 * Gibt die übergebene Fehlermeldung und die gültigen Kommandozeilen-Optionen
	 * über den zugeordneten Logger aus.
	 *
	 * @param error   die Fehlermeldung, die ausgegeben werden soll
	 */
	public void printOptions(final String error) {
		if (error != null) {
			logger.severe(error);
		}
		logger.info("Gültige Kommandozeilen-Optionen sind:");
		for (final CmdLineOption option : options.values()) {
			logger.info("  -" + option.getShortTag());
			logger.info("  --" + option.getLongTag());
			logger.info("        " + option.getDescription());
		}
	}


	/**
	 * Gibt die übergebene Fehlermeldung und die gültigen Kommandozeilen-Optionen
	 * auf der Konsole (System.out) aus und beendet anschließend das Programm
	 * mit dem angebeben Exit-Code.
	 *
	 * @param code    die Fehlermeldung, die ausgegeben werden soll
	 * @param error   der Exit-Code, mit dem das Programm beendet wird
	 */
	public void printOptionsAndExit(final int code, final String error) {
		printOptions(error);
		System.exit(code);
	}

}
