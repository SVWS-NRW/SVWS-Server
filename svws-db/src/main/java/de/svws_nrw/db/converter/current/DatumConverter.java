package de.svws_nrw.db.converter.current;

import java.sql.Timestamp;
import java.time.LocalDate;

import de.svws_nrw.db.converter.DBAttributeConverter;
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
public final class DatumConverter extends DBAttributeConverter<String, Timestamp> {

	/** Die Instanz des Konverters */
	public static final DatumConverter instance = new DatumConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public DatumConverter() {
		// leer
	}

	@Override
	public Timestamp convertToDatabaseColumn(final String attribute) {
		return ((attribute == null) || ("".equals(attribute)))
				? null
				: Timestamp.valueOf(LocalDate.parse(attribute).atStartOfDay());
	}

	@Override
	public String convertToEntityAttribute(final Timestamp dbData) {
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
