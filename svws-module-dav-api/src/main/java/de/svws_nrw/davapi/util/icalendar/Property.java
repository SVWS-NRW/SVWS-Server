package de.svws_nrw.davapi.util.icalendar;

/**
 * Diese Klasse repräsentiert ein Property an einem VCalendar
 *
 */
public final class Property implements IProperty {

	/** der Key des Properties */
	private final String value;
	/** der Wert des Properties */
	private final String key;

	/**
	 * Konstruktor für ein Property aus Key und Value
	 *
	 * @param propertyKey   der Key des Property
	 * @param propertyValue der Value des Property
	 */
	public Property(final String propertyKey, final String propertyValue) {
		this.key = propertyKey;
		this.value = propertyValue;
	}

	/**
	 * Konstruktor für Property aus Schlüssel und Wert
	 * @param key der Key
	 * @param propertyValue der Wert
	 */
	public Property(final PropertyKeys key, final String propertyValue) {
		this.key = key.name();
		this.value = propertyValue;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.key + ":" + this.value;
	}

	@Override
	public void serialize(final StringBuffer sb) {
		sb.append(key);
		sb.append(COLON_CHAR);
		sb.append(value);
		sb.append(VCalendar.LINEBREAK);
	}
}
