package de.svws_nrw.db.converter.migration;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (Boolean)
 * zu einer Stringdarstellung in der Datenbank, bei der die - false repräsentiert
 * und die + true.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class MigrationBooleanPlusMinusConverter extends DBAttributeConverter<Boolean, String> {

	/** Die Instanz des Konverters */
	public static final MigrationBooleanPlusMinusConverter instance = new MigrationBooleanPlusMinusConverter();

	@Override
	public String convertToDatabaseColumn(final Boolean value) {
		if (value == null)
			return null;
		return value ? "+" : "-";
	}

	@Override
	public Boolean convertToEntityAttribute(final String dbData) {
		if (dbData == null)
			return null;
		return "+".equals(dbData);
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
