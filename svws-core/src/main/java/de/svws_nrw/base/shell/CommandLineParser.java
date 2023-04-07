package de.svws_nrw.base.shell;

import java.util.HashMap;


/**
 * Diese Klasse dient dem Umgang mit Parametern beim Aufruf von Java-Programmen aus der
 * Kommandozeile.
 */
public class CommandLineParser {

	/// Gibt an, ob die Kommandozeile bereits in Bezug auf die angegebenen Optionen untersucht wurde
	private boolean parsed = false;

	/// Die Kommandozeilen-Parameter als String-Array
	private final String[] args;


	/// Eine Hashmap mit allen Kommandozeilenoptionen, welche ihren Short-Tags zugeordnet sind
	private final HashMap<String, CommandLineOption> options = new HashMap<>();

	/// Eine Hashmap mit allen Kommandozeilenoptionen, welche ihren Long-Tags zugeordnet sind
	private final HashMap<String, CommandLineOption> optionsLong = new HashMap<>();

	/// Eine Hashmap, welche dem short tag einer Option den Wert zuordnet, der über den Parser eingelesen wurde
	private final HashMap<String, String> values = new HashMap<>();


	/**
	 * Dieser Konstruktor erzeugt einen neuen Parser für die
	 *
	 * @param args   die Kommandozeilenparameter
	 */
	public CommandLineParser(final String[] args) {
		this.args = args;
	}



	/**
	 * Prüft die Kommandozeile auf die einzelnen Optionen.
	 *
	 * @throws CommandLineException    tritt auf, falls eine ungültige Option in der Kommandozeile entdeckt wird
	 */
	private void parse() throws CommandLineException {
		CommandLineOption current = null;
		boolean isArgument = false;
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];
			// Prüfe, zunächst, ob es sich um ein Argument der zuvor gelesenen Option handelt und schreibe ggf. den Wert
			if (isArgument && (current != null)) {
				values.put(current.getShortTag(), arg);
				isArgument = false;
			} else {
				if (arg.startsWith("--")) {
					current = optionsLong.get(arg.substring(2));
				} else if (arg.startsWith("-")) {
					current = options.get(arg.substring(1));
				} else {
					throw new CommandLineException(CommandLineExceptionType.UNKNOWN_OPTION);
				}
				if (current == null)
					throw new CommandLineException(CommandLineExceptionType.UNKNOWN_OPTION);
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
	 * @throws CommandLineException   tritt auf, falls der short oder long tag der Option bereits zuvor verwendte wurde
	 */
	public void addOption(final CommandLineOption option) throws CommandLineException {
		if (options.get(option.getShortTag()) != null)
			throw new CommandLineException(CommandLineExceptionType.SHORT_TAG_ALREADY_DEFINED);
		if (optionsLong.get(option.getLongTag()) != null)
			throw new CommandLineException(CommandLineExceptionType.LONG_TAG_ALREADY_DEFINED);
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
	 * @throws CommandLineException tritt auf, wenn die option nicht bekannt ist oder ein Fehler beim Parsen der Kommandozeile auftritt
	 */
	public String getValue(final String tag) throws CommandLineException {
		if (!options.containsKey(tag))
			throw new CommandLineException(CommandLineExceptionType.UNKNOWN_OPTION);
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
		} catch (@SuppressWarnings("unused") final CommandLineException e) {
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
			} catch (@SuppressWarnings("unused") final CommandLineException e) {
				return false;
			}
		}
		return values.containsKey(tag);
	}



	/**
	 * Gibt die übergebene Fehlermeldung und die gültigen Kommandozeilen-Optionen
	 * auf der Konsole (System.out) aus.
	 *
	 * @param error   die Fehlermeldung, die ausgegeben werden soll
	 */
	public void printOptions(final String error) {
		if (error != null) {
			System.out.println(error);
			System.out.println();
		}
		System.out.println("Gültige Kommandozeilen-Optionen sind: ");
		for (final CommandLineOption option : options.values()) {
			System.out.println("  -" + option.getShortTag());
			System.out.println("  --" + option.getLongTag());
			System.out.println("        " + option.getDescription());
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
