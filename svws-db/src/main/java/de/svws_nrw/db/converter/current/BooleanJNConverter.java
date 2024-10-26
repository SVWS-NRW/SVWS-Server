package de.svws_nrw.db.converter.current;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (Boolean)
 * zu einer Stringdarstellung in der Datenbank, bei der "N" false repräsentiert
 * und "J" true.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class BooleanJNConverter extends DBAttributeConverter<Boolean, String> {

	/** Die Instanz des Konverters */
	public static final BooleanJNConverter instance = new BooleanJNConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BooleanJNConverter() {
		// leer
	}

	@Override
	public String convertToDatabaseColumn(final Boolean value) {
		if (value == null)
			return null;
		return value ? "J" : "N";
	}

	@Override
	public Boolean convertToEntityAttribute(final String dbData) {
		return (dbData != null) && "J".equals(dbData);
	}

	@Override
	public Class<Boolean> getResultType() {
		return Boolean.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
