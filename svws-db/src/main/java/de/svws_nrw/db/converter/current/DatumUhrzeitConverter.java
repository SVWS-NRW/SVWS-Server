package de.svws_nrw.db.converter.current;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Datumswerten in Java
 * (ISO-8601-String) zu einer Datums- und Uhrzeitdarstellung (siehe
 * {@link Timestamp}) in der Datenbank. Sie ist abgeleitet von der Basisklasse
 * {@link DBAttributeConverter}, welche die grundlegende Funktionalität von
 * Konvertern zur Verfügung stellt. Dort muss der Konverter auch in der Methode
 * {@link DBAttributeConverter#getByClass} registriert werden.
 */
@Converter
public final class DatumUhrzeitConverter extends DBAttributeConverter<String, Timestamp> {

	/** Die Instanz des Konverters */
	public static final DatumUhrzeitConverter instance = new DatumUhrzeitConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public DatumUhrzeitConverter() {
		// leer
	}

	@Override
	public Timestamp convertToDatabaseColumn(final String attribute) {
		return ((attribute == null) || ("".equals(attribute))) ? null
				: Timestamp.valueOf(LocalDateTime.parse(attribute));
	}

	@Override
	public String convertToEntityAttribute(final Timestamp dbData) {
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
