package de.svws_nrw.db.converter.current;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Datumswerten in Java (ISO-8601-String)
 * zu einer Uhrzeitdarstellung (siehe {@link Timestamp}) in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class UhrzeitConverterString extends DBAttributeConverter<String, Timestamp> {

	/** Die Instanz des Konverters */
	public static final UhrzeitConverterString instance = new UhrzeitConverterString();

	@Override
	public Timestamp convertToDatabaseColumn(final String attribute) {
		return ((attribute == null) || ("".equals(attribute)))
				? null
			    : Timestamp.valueOf(LocalTime.parse(attribute).atDate(LocalDate.of(1970, 1, 1)));
	}

	@Override
	public String convertToEntityAttribute(final Timestamp dbData) {
		if (dbData == null)
			return null;
		return dbData.toLocalDateTime().toLocalTime().toString();
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
