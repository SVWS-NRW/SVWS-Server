package de.svws_nrw.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Kursarten im Abitur.
 *
 */
@Converter(autoApply = true)
public final class GOStKursartConverter extends DBAttributeConverter<GostKursart, String> {

	/** Die Instanz des Konverters */
	public static final GOStKursartConverter instance = new GOStKursartConverter();

	@Override
	public String convertToDatabaseColumn(final GostKursart value) {
		if (value == null)
			return null;
		return value.toString();
	}

	@Override
	public GostKursart convertToEntityAttribute(final String dbData) {
		return GostKursart.fromKuerzel(dbData);
	}

	@Override
	public Class<GostKursart> getResultType() {
		return GostKursart.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
