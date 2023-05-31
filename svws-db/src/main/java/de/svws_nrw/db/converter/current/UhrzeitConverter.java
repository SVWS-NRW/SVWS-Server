package de.svws_nrw.db.converter.current;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Datumswerten in Java (Integer-Wert in Minuten)
 * zu einer Uhrzeitdarstellung (siehe {@link Timestamp}) in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class UhrzeitConverter extends DBAttributeConverter<Integer, Timestamp> {

	/** Die Instanz des Konverters */
	public static final UhrzeitConverter instance = new UhrzeitConverter();

	@Override
	public Timestamp convertToDatabaseColumn(final Integer attribute) {
		if ((attribute == null) || (attribute < 0) || (attribute >= 1440)) // 24*60 = 1440
			return null;
		final String timeStr = "%02d:%02d".formatted(attribute / 60, attribute % 60);
		return Timestamp.valueOf(LocalTime.parse(timeStr).atDate(LocalDate.of(1970, 1, 1)));
	}

	@Override
	public Integer convertToEntityAttribute(final Timestamp dbData) {
		if (dbData == null)
			return null;
		final LocalTime time = dbData.toLocalDateTime().toLocalTime();
		return time.getHour() * 60 + time.getMinute();
	}

	@Override
	public Class<Integer> getResultType() {
		return Integer.class;
	}

	@Override
	public Class<Timestamp> getDBType() {
		return Timestamp.class;
	}

}
