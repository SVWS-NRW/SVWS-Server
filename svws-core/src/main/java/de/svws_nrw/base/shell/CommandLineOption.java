package de.svws_nrw.base.shell;


/**
 * Diese Klasse repräsentiert eine Kommandozeilenoption 
 */
public class CommandLineOption {
	
	/// Die kurze Bezeichnung für die Option (wird mit einem einfachen Bindestrich verwendet), z.B. -o
	private String shortTag;
	
	/// Die lange Bezeichnung für die Option (wird mit zwei einfachen Bindestrichen verwendet), z.B. --option
	private String longTag;
	
	/// Gibt an, ob nach der Bezeichnung für die Option ein Argument folgen muss oder nicht
	private boolean hasArgument;
	
	/// Eine textuelle Beschreibung dieser Option
	private String description;
	

	/**
	 * Erzeugt eine neue Kommandozeilenoption.
	 * 
	 * @param shortTag      die kurze Bezeichnung für die Option, wird mit einem einfachen Bindestrich verwendet 
	 * @param longTag       die lange Bezeichnung für die Option, wird mit zwei einfachen Bindestrichen verwendet
	 * @param hasArgument   gibt an, ob nach der Bezeichnung für die Option ein Argument folgen muss oder nicht
	 * @param description   eine textuelle Beschreibung dieser Option
	 */
	public CommandLineOption(String shortTag, String longTag, boolean hasArgument, String description) {
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
