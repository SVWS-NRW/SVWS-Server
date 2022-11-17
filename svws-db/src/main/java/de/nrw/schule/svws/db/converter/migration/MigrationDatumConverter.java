package de.nrw.schule.svws.db.converter.migration;

import java.sql.Timestamp;
import java.time.LocalDate;

import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Datumswerten in Java (ISO-8601-String)
 * zu einer Datumsdarstellung (siehe {@link Timestamp}) in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass} 
 * registriert werden. 
 */
@Converter
public class MigrationDatumConverter extends DBAttributeConverter<String, Timestamp> {

	/** Die Instanz des Konverters */
	public final static MigrationDatumConverter instance = new MigrationDatumConverter();
	
	@Override
	public Timestamp convertToDatabaseColumn(String attribute) {
		return ((attribute == null) || ("".equals(attribute)))
				? null
			    : Timestamp.valueOf(LocalDate.parse(attribute).atStartOfDay());		
	}

	@Override
	public String convertToEntityAttribute(Timestamp dbData) {
		if (dbData == null)
			return null;
		return dbData.toLocalDateTime().toLocalDate().toString();
	}

	@Override
	public Class<String> getResultType() {
		return String.class;
	}

	@Override
	public Class<Timestamp> getDBType() {
		return Timestamp.class;
	}

}
