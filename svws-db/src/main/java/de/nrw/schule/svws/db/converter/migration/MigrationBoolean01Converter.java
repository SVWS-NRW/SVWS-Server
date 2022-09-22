package de.nrw.schule.svws.db.converter.migration;

import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (Boolean)
 * zu einer Zahldarstellung in der Datenbank, bei der die 0 false repräsentiert
 * und die  1 true.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClassName} 
 * registriert werden. 
 */
@Converter
public class MigrationBoolean01Converter extends DBAttributeConverter<Boolean, Integer> {

	/** Die Instanz des Konverters */
	public final static MigrationBoolean01Converter instance = new MigrationBoolean01Converter();
	
	@Override
	public Integer convertToDatabaseColumn(Boolean value) {
		return ((value != null) && value) ? 1 : 0;
	}

	@Override
	public Boolean convertToEntityAttribute(Integer dbData) {
		return (dbData != null) && (dbData != 0);
	}

	@Override
	public Class<Boolean> getResultType() {
		return Boolean.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
