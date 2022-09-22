package de.nrw.schule.svws.db.converter.current.gost;

import jakarta.persistence.Converter;

import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Abiturfächern im Abitur.
 *  
 */
@Converter(autoApply = true)
public class GOStAbiturFachConverter extends DBAttributeConverter<GostAbiturFach, String> {

	/** Die Instanz des Konverters */
	public final static GOStAbiturFachConverter instance = new GOStAbiturFachConverter();

	@Override
	public String convertToDatabaseColumn(GostAbiturFach value) {
		if (value == null)
			return null;
		return "" + value.id;
	}

	@Override
	public GostAbiturFach convertToEntityAttribute(String dbData) {
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
