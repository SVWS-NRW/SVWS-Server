package de.svws_nrw.davapi.util.vcard;

/**
 * Dieses Interface beschreibt Methoden für VCard-Properties und konstanten zur
 * Serialiserung
 *
 */
public interface VCardProperty {

	/**
	 * Zeichen für Zeilenumbruch innerhalb eines Properties beim überschreiten der
	 * maximalen Zeilenlänge
	 */
	String INNER_PROPERTY_LINEBREAK = "\n ";

	/**
	 * Trennzeichen innerhalb eines Properties welches aus Sequenzen besteht (bspw.
	 * Name-Property oder Adressproperty)
	 */
	String SEQUENCE_ELEMENT_SEPARATOR = ";";

	/**
	 * Trennzeichen für Propertyelemente, welche als Liste angegeben werden können
	 * (bspw. mehrere Vornamen, Titel, etc.)
	 */
	String PROPERTY_LIST_ENTRY_SEPARATOR = ",";

	/**
	 * Maximale Zeilenlänge von VCard-Properties. Dient der Lesbarkeit, der
	 * serialisierten VCard.
	 */
	int LINE_LENGTH = 75;

	/**
	 * Gibt den Typ des Properties zurück
	 *
	 * @return der Typ des Properties
	 */
	String getType();

	/**
	 * fügt einem {@link StringBuilder} den Typ dieses Properties hinzu.
	 *
	 * @param sb der StringBuilder
	 */
	void serializeType(StringBuilder sb);

	/**
	 * fügt einem {@link StringBuilder} das Property hinzu.
	 *
	 * @param sb der StringBuilder
	 */
	void serializeProperty(StringBuilder sb);

}
