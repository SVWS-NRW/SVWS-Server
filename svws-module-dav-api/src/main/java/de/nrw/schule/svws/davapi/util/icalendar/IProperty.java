package de.nrw.schule.svws.davapi.util.icalendar;

import jakarta.validation.constraints.NotNull;

/**
 * Interface für Properties von VCalendars bestehend aus Key und Value
 */
public interface IProperty {
	/** colon character */
	static final char COLON_CHAR = ':';

	/**
	 * getter für den Key des Properties
	 * 
	 * @return der Key des Properties
	 */
	String getKey();

	/**
	 * getter für den Value des Properties
	 * 
	 * @return der Value des Properties
	 */
	String getValue();

	/**
	 * Statische Methode zum Parsen eines Properties aus einem gegebenen String
	 * 
	 * @param string der gegebene String des Properties
	 * @return das geparste Property
	 */
	static IProperty fromString(String string) {
		int splitIndex = 0;
		char[] charArray = string.toCharArray();
		int i = 0;
		while (i < charArray.length) {
			char c = charArray[i];
			if (c == '"') {
				// skip split character for inlined strings
				do {
					i++;
				} while (charArray[i] != '"');
			}
			if (c == COLON_CHAR) {
				splitIndex = i;
				break;
			}
			i++;
		}
		String propertyKey = string.substring(0, splitIndex);
		String propertyValue = string.substring(splitIndex + 1);
		return new Property(propertyKey, propertyValue);
	}

	/**
	 * Gibt zurück, ob ein gegebenes IProperty den gegebenen Key und den gegebenen
	 * Value enthält
	 * 
	 * @param property das Property, welches geprüft werden soll
	 * @param key      der Key gegen den das Property geprüft werden soll
	 * @param value    der Value gegen den das Property geprüft werden soll
	 * @return ob key und value in dem property enthalten sind
	 */
	static boolean isProperty(@NotNull IProperty property, @NotNull String key, @NotNull String value) {
		return key.equals(property.getKey()) && value.equals(property.getValue());
	}

	/**
	 * serialisiert dieses Property am gegebenen Stringbuffer
	 * 
	 * @param sb der Stringbuffer
	 */
	void serialize(StringBuffer sb);
}
