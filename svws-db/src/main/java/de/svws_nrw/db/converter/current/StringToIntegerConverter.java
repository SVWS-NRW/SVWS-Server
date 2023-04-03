package de.svws_nrw.db.converter.current;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren eines String zu Integer.
 *
 */
@Converter
public final class StringToIntegerConverter extends DBAttributeConverter<Integer, String> {

	/** Die Instanz des Konverters */
	public static final StringToIntegerConverter instance = new StringToIntegerConverter();

	@Override
	public String convertToDatabaseColumn(final Integer value) {
		if (value == null)
			return null;
		return value.toString();
	}

	@Override
	public Integer convertToEntityAttribute(final String dbData) {
		try {
			return Integer.parseInt(dbData);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			return null;
		}
	}

	@Override
	public Class<Integer> getResultType() {
		return Integer.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
