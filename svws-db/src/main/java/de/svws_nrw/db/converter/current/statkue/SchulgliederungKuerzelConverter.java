package de.svws_nrw.db.converter.current.statkue;

import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von dem Core-Type Schulgliederung in Java
 * zu dem Kürzel der Schulform entsprechend der amtlichen Schulstatistik,
 * welches in der SVWS-Datenbank verwendet wird.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class SchulgliederungKuerzelConverter extends DBAttributeConverter<Schulgliederung, String> {

	/** Die Instanz des Konverters */
	public static final SchulgliederungKuerzelConverter instance = new SchulgliederungKuerzelConverter();

	@Override
	public String convertToDatabaseColumn(final Schulgliederung value) {
		if (value == null)
			return null;
		return value.daten.kuerzel;
	}

	@Override
	public Schulgliederung convertToEntityAttribute(final String dbData) {
		return Schulgliederung.getByKuerzel(dbData);
	}

	@Override
	public Class<Schulgliederung> getResultType() {
		return Schulgliederung.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
