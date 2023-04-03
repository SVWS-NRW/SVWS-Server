package de.svws_nrw.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Abiturfächern im Abitur.
 */
@Converter(autoApply = true)
public final class GOStAbiturFachConverter extends DBAttributeConverter<GostAbiturFach, String> {

	/** Die Instanz des Konverters */
	public static final GOStAbiturFachConverter instance = new GOStAbiturFachConverter();

	@Override
	public String convertToDatabaseColumn(final GostAbiturFach value) {
		if (value == null)
			return null;
		return "" + value.id;
	}

	@Override
	public GostAbiturFach convertToEntityAttribute(final String dbData) {
		return GostAbiturFach.fromIDString(dbData);
	}

	@Override
	public Class<GostAbiturFach> getResultType() {
		return GostAbiturFach.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
