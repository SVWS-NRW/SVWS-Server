package de.svws_nrw.base.shell;


/**
 * Diese Klasse repräsentiert eine Kommandozeilenoption
 */
public class CommandLineOption {

	/// Die kurze Bezeichnung für die Option (wird mit einem einfachen Bindestrich verwendet), z.B. -o
	private final String shortTag;

	/// Die lange Bezeichnung für die Option (wird mit zwei einfachen Bindestrichen verwendet), z.B. --option
	private final String longTag;

	/// Gibt an, ob nach der Bezeichnung für die Option ein Argument folgen muss oder nicht
	private final boolean hasArgument;

	/// Eine textuelle Beschreibung dieser Option
	private final String description;


	/**
	 * Erzeugt eine neue Kommandozeilenoption.
	 *
	 * @param shortTag      die kurze Bezeichnung für die Option, wird mit einem einfachen Bindestrich verwendet
	 * @param longTag       die lange Bezeichnung für die Option, wird mit zwei einfachen Bindestrichen verwendet
	 * @param hasArgument   gibt an, ob nach der Bezeichnung für die Option ein Argument folgen muss oder nicht
	 * @param description   eine textuelle Beschreibung dieser Option
	 *
	 * @throws CommandLineException falls die kurze oder die lange Bezeichnung null sind.
	 */
	public CommandLineOption(final String shortTag, final String longTag, final boolean hasArgument, final String description) throws CommandLineException {
		if (shortTag == null)
			throw new CommandLineException(CommandLineExceptionType.SHORT_TAG_NOT_DEFINED);
		if (longTag == null)
			throw new CommandLineException(CommandLineExceptionType.LONG_TAG_NOT_DEFINED);
		this.shortTag = shortTag;
		this.longTag = longTag;
		this.hasArgument = hasArgument;
		this.description = description;
	}


	/**
	 * Gibt die kurze Bezeichnung für die Option zurück.
	 *
	 * @return die kurze Bezeichnung für die Option
	 */
	public String getShortTag() {
		return shortTag;
	}


	/**
	 * Gibt die lange Bezeichnung für die Option zurück.
	 *
	 * @return die lange Bezeichnung für die Option
	 */
	public String getLongTag() {
		return longTag;
	}


	/**
	 * Gibt zurück, ob die Kommandozeilen-Option einen Parameter hat oder nicht.
	 *
	 * @return true, falls die Kommandozeilen-Option einen Parameter hat, ansonsten false
	 */
	public boolean hasArgument() {
		return hasArgument;
	}


	/**
	 * Gibt die textuelle Beschreibung dieser Option zurück.
	 *
	 * @return die textuelle Beschreibung dieser Option
	 */
	public String getDescription() {
		return description;
	}

}
