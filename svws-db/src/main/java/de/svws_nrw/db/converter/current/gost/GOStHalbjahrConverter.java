package de.svws_nrw.db.converter.current.gost;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Halbjahren der gymnasialen
 * Oberstufe zu eindeutigen Integer-Werten (ID des Core-Types {@link GostHalbjahr})
 * und umgekehrt.
 */
@Converter(autoApply = true)
public final class GOStHalbjahrConverter extends DBAttributeConverter<GostHalbjahr, Integer> {

	/** Die Instanz des Konverters */
	public static final GOStHalbjahrConverter instance = new GOStHalbjahrConverter();

	@Override
	public Integer convertToDatabaseColumn(final GostHalbjahr value) {
		if (value == null)
			return null;
		return value.id;
	}

	@Override
	public GostHalbjahr convertToEntityAttribute(final Integer dbData) {
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
