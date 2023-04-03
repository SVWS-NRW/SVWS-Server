package de.svws_nrw.db.converter.migration;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (String)
 * zu einer Stringdarstellung in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class MigrationBoolean01StringConverter extends DBAttributeConverter<Boolean, String> {

	/** Die Instanz des Konverters */
	public static final MigrationBoolean01StringConverter instance = new MigrationBoolean01StringConverter();

	@Override
	public String convertToDatabaseColumn(final Boolean value) {
		return ((value != null) && value) ? "1" : "0";
	}

	@Override
	public Boolean convertToEntityAttribute(final String dbData) {
		return (dbData != null) && (!"0".equals(dbData));
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
