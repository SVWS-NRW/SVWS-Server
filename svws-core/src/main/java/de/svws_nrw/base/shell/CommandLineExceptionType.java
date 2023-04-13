package de.svws_nrw.base.shell;

/**
 * Dies Ausfzählung spezifiziert den genauen Typ der CommandLineEcxeption
 */
public enum CommandLineExceptionType {

	/** Gibt an, dass die Option in der Kommandozeile unbekannt ist. */
	UNKNOWN_OPTION("The command line option is unknown."),

	/** Gibt an, dass keine Optionen für die Kommandozeile definiert wurden. */
	NO_OPTIONS("No command line option defined."),

	/** Gibt an, dass die kurze Bezeichnung für die Kommandozeilen-Option bereits verwendet wurde. */
	SHORT_TAG_ALREADY_DEFINED("The short tag of the command line option was already defined."),

	/** Gibt an, dass die lange Bezeichnung für die Kommandozeilen-Option bereits verwendet wurde. */
	LONG_TAG_ALREADY_DEFINED("The long tag of the command line option was already defined."),

	/** Gibt an, dass die kurze Bezeichnung für die Kommandozeilen-Option nicht angegeben wurde. */
	SHORT_TAG_NOT_DEFINED("The short tag of the command line option is not defined."),

	/** Gibt an, dass die lange Bezeichnung für die Kommandozeilen-Option nicht angegeben wurde. */
	LONG_TAG_NOT_DEFINED("The long tag of the command line option is not defined.");


	/// Die Nachricht, die bei der Exception angezeigt wird
	private String message;


	/**
	 * Erzeugt einen neuen CommandLineEcxeption-Typ mit der angegebenen Nachricht.
	 *
	 * @param message   die Nachricht, die bei diesem Typ angezeigt werden soll
	 */
	CommandLineExceptionType(final String message) {
		this.message = message;
	}


	/**
	 * Gibt die anzuzeigende Nachricht zurpck.
	 *
	 * @return die anzuzeigende Nachricht
	 */
	public String getMessage() {
		return message;
	}

}
