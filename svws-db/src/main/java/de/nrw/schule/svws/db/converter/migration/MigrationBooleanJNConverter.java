package de.nrw.schule.svws.db.converter.migration;

import de.nrw.schule.svws.db.converter.DBAttributeConverter;
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
public class MigrationBooleanJNConverter extends DBAttributeConverter<Boolean, String> {

	/** Die Instanz des Konverters */
	public final static MigrationBooleanJNConverter instance = new MigrationBooleanJNConverter();
	
	@Override
	public String convertToDatabaseColumn(Boolean value) {
		if (value == null)
			return null;
		return value ? "J" : "N";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;
		return "J".equals(dbData);
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
