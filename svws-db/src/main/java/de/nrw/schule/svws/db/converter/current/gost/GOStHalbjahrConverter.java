package de.nrw.schule.svws.db.converter.current.gost;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Halbjahren der gymnasialen 
 * Oberstufe zu eindeutigen Integer-Werten (ID des Core-Types {@link GostHalbjahr}) 
 * und umgekehrt.
 */
@Converter(autoApply = true)
public class GOStHalbjahrConverter extends DBAttributeConverter<GostHalbjahr, Integer> {

	/** Die Instanz des Konverters */
	public final static GOStHalbjahrConverter instance = new GOStHalbjahrConverter();
	
	@Override
	public Integer convertToDatabaseColumn(GostHalbjahr value) {
		if (value == null)
			return null;
		return value.id;
	}

	@Override
	public GostHalbjahr convertToEntityAttribute(Integer dbData) {
		return GostHalbjahr.fromID(dbData);
	}

	@Override
	public Class<GostHalbjahr> getResultType() {
		return GostHalbjahr.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
