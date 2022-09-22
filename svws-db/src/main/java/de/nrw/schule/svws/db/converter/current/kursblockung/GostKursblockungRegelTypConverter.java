package de.nrw.schule.svws.db.converter.current.kursblockung;

import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Regel-Typen für die Kursblockung in der gymnasialen 
 * Oberstufe zu eindeutigen Integer-Werten (ID des Core-Types {@link GostKursblockungRegelTyp}) 
 * und umgekehrt.
 */
@Converter(autoApply = true)
public class GostKursblockungRegelTypConverter extends DBAttributeConverter<GostKursblockungRegelTyp, Integer> {

	/** Die Instanz des Konverters */
	public final static GostKursblockungRegelTypConverter instance = new GostKursblockungRegelTypConverter();
	
	@Override
	public Integer convertToDatabaseColumn(GostKursblockungRegelTyp value) {
		if (value == null)
			return null;
		return value.typ;
	}

	@Override
	public GostKursblockungRegelTyp convertToEntityAttribute(Integer dbData) {
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
