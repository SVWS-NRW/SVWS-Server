package de.svws_nrw.db.converter.current.kursblockung;

import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Regel-Typen für die Kursblockung in der gymnasialen
 * Oberstufe zu eindeutigen Integer-Werten (ID des Core-Types {@link GostKursblockungRegelTyp})
 * und umgekehrt.
 */
@Converter(autoApply = true)
public final class GostKursblockungRegelTypConverter extends DBAttributeConverter<GostKursblockungRegelTyp, Integer> {

	/** Die Instanz des Konverters */
	public static final GostKursblockungRegelTypConverter instance = new GostKursblockungRegelTypConverter();

	@Override
	public Integer convertToDatabaseColumn(final GostKursblockungRegelTyp value) {
		if (value == null)
			return null;
		return value.typ;
	}

	@Override
	public GostKursblockungRegelTyp convertToEntityAttribute(final Integer dbData) {
		return GostKursblockungRegelTyp.fromTyp(dbData);
	}

	@Override
	public Class<GostKursblockungRegelTyp> getResultType() {
		return GostKursblockungRegelTyp.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
