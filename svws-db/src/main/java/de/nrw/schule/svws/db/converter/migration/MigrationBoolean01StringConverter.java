package de.nrw.schule.svws.db.converter.migration;

import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (String)
 * zu einer Stringdarstellung in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClassName} 
 * registriert werden. 
 */
@Converter
public class MigrationBoolean01StringConverter extends DBAttributeConverter<Boolean, String> {

	/** Die Instanz des Konverters */
	public final static MigrationBoolean01StringConverter instance = new MigrationBoolean01StringConverter();
	
	@Override
	public String convertToDatabaseColumn(Boolean value) {
		return ((value != null) && value) ? "1" : "0";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
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
