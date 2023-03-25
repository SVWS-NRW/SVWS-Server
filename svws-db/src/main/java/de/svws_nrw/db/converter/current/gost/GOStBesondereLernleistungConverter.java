package de.svws_nrw.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Daten der Besonderen Lernleistung im Abitur.
 *  
 */
@Converter(autoApply = true)
public class GOStBesondereLernleistungConverter extends DBAttributeConverter<GostBesondereLernleistung, String> {

	/** Die Instanz des Konverters */
	public final static GOStBesondereLernleistungConverter instance = new GOStBesondereLernleistungConverter();
	
	@Override
	public String convertToDatabaseColumn(GostBesondereLernleistung value) {
		if (value == null)
			return null;
		return value.toString();
	}

	@Override
	public GostBesondereLernleistung convertToEntityAttribute(String dbData) {
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
