package de.svws_nrw.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Daten der Besonderen Lernleistung im Abitur.
 */
@Converter(autoApply = true)
public final class GOStBesondereLernleistungConverter extends DBAttributeConverter<GostBesondereLernleistung, String> {

	/** Die Instanz des Konverters */
	public static final GOStBesondereLernleistungConverter instance = new GOStBesondereLernleistungConverter();

	@Override
	public String convertToDatabaseColumn(final GostBesondereLernleistung value) {
		if (value == null)
			return null;
		return value.toString();
	}

	@Override
	public GostBesondereLernleistung convertToEntityAttribute(final String dbData) {
		return GostBesondereLernleistung.fromKuerzel(dbData);
	}

	@Override
	public Class<GostBesondereLernleistung> getResultType() {
		return GostBesondereLernleistung.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
