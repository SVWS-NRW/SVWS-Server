package de.nrw.schule.svws.db.converter.current;

import de.nrw.schule.svws.db.converter.DBAttributeConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Datumswerten in Java
 * (ISO-8601-String) zu einer Datums- und Uhrzeitdarstellung (siehe
 * {@link Timestamp}) in der Datenbank. Sie ist abgeleitet von der Basisklasse
 * {@link DBAttributeConverter}, welche die grundlegende Funktionalität von
 * Konvertern zur Verfügung stellt. Dort muss der Konverter auch in der Methode
 * {@link DBAttributeConverter#getByClassName} registriert werden.
 */
@Converter
public class DatumUhrzeitConverter extends DBAttributeConverter<String, Timestamp> {

	/** Die Instanz des Konverters */
	public static final DatumUhrzeitConverter instance = new DatumUhrzeitConverter();

	@Override
	public Timestamp convertToDatabaseColumn(String attribute) {
		return ((attribute == null) || ("".equals(attribute))) ? null
				: Timestamp.valueOf(LocalDateTime.parse(attribute));
	}

	@Override
	public String convertToEntityAttribute(Timestamp dbData) {
		if (dbData == null)
			return null;
		return dbData.toLocalDateTime().toString();
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
