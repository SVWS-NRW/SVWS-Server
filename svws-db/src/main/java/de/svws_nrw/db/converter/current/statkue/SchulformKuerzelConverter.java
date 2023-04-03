package de.svws_nrw.db.converter.current.statkue;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von dem Core-Type Schulform in Java
 * zu dem Kürzel der Schulform entsprechend der amtlichen Schulstatistik,
 * welches in der SVWS-Datenbank verwendet wird.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class SchulformKuerzelConverter extends DBAttributeConverter<Schulform, String> {

	/** Die Instanz des Konverters */
	public static final SchulformKuerzelConverter instance = new SchulformKuerzelConverter();

	@Override
	public String convertToDatabaseColumn(final Schulform value) {
		return ((value == null) || (value.daten == null)) ? null : value.daten.kuerzel;
	}

	@Override
	public Schulform convertToEntityAttribute(final String dbData) {
		return Schulform.getByKuerzel(dbData);
	}

	@Override
	public Class<Schulform> getResultType() {
		return Schulform.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
