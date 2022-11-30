package de.nrw.schule.svws.davapi.util.icalendar;

/**
 * Diese Klasse repräsentiert ein Property an einem VCalendar
 *
 */
public class Property implements IProperty {

	/** der Key des Properties */
	private String value;
	/** der Wert des Properties */
	private String key;

	/**
	 * Konstruktor für ein Property aus Key und Value
	 * 
	 * @param propertyKey   der Key des Property
	 * @param propertyValue der Value des Property
	 */
	public Property(String propertyKey, String propertyValue) {
		this.key = propertyKey;
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
	public void serialize(StringBuffer sb) {
		sb.append(key);
		sb.append(COLON_CHAR);
		sb.append(value);
		sb.append(VCalendar.LINEBREAK);
	}
}
