package de.nrw.schule.svws.db.converter.current.gost;

import de.nrw.schule.svws.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Kursarten im Abitur.
 *  
 */
@Converter(autoApply = true)
public class GostLaufbahnplanungFachkombinationTypConverter extends DBAttributeConverter<GostLaufbahnplanungFachkombinationTyp, Integer> {

	/** Die Instanz des Konverters */
	public final static GostLaufbahnplanungFachkombinationTypConverter instance = new GostLaufbahnplanungFachkombinationTypConverter();
	
	@Override
	public Integer convertToDatabaseColumn(GostLaufbahnplanungFachkombinationTyp value) {
		if (value == null)
			return null;
		return value.getValue();
	}

	@Override
	public GostLaufbahnplanungFachkombinationTyp convertToEntityAttribute(Integer dbData) {
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
