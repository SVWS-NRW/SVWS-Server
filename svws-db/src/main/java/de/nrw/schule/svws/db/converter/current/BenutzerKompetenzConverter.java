package de.nrw.schule.svws.db.converter.current;

import jakarta.persistence.Converter;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;


/**
 * Diese Klasse dient dem Konvertieren von Benutzerkompetenzen.
 *  
 */
@Converter(autoApply = true)
public class BenutzerKompetenzConverter extends DBAttributeConverter<BenutzerKompetenz, Long> {

	/** Die Instanz des Konverters */
	public final static BenutzerKompetenzConverter instance = new BenutzerKompetenzConverter();
	
	@Override
	public Long convertToDatabaseColumn(BenutzerKompetenz kompetenz) {
		return kompetenz.daten.id;
	}

	@Override
	public BenutzerKompetenz convertToEntityAttribute(Long dbData) {
		return BenutzerKompetenz.getByID(dbData);
	}

	@Override
	public Class<BenutzerKompetenz> getResultType() {
		return BenutzerKompetenz.class;
	}

	@Override
	public Class<Long> getDBType() {
		return Long.class;
	}

}
