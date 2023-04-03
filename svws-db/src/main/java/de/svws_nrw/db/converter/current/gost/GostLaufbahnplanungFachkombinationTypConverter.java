package de.svws_nrw.db.converter.current.gost;

import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Kursarten im Abitur.
 *
 */
@Converter(autoApply = true)
public final class GostLaufbahnplanungFachkombinationTypConverter extends DBAttributeConverter<GostLaufbahnplanungFachkombinationTyp, Integer> {

	/** Die Instanz des Konverters */
	public static final GostLaufbahnplanungFachkombinationTypConverter instance = new GostLaufbahnplanungFachkombinationTypConverter();

	@Override
	public Integer convertToDatabaseColumn(final GostLaufbahnplanungFachkombinationTyp value) {
		if (value == null)
			return null;
		return value.getValue();
	}

	@Override
	public GostLaufbahnplanungFachkombinationTyp convertToEntityAttribute(final Integer dbData) {
		return GostLaufbahnplanungFachkombinationTyp.fromValue(dbData);
	}

	@Override
	public Class<GostLaufbahnplanungFachkombinationTyp> getResultType() {
		return GostLaufbahnplanungFachkombinationTyp.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
