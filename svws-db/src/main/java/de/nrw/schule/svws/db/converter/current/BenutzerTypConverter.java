package de.nrw.schule.svws.db.converter.current;

import de.nrw.schule.svws.core.types.benutzer.BenutzerTyp;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren von Benutzertypen.
 */
@Converter(autoApply = true)
public class BenutzerTypConverter extends DBAttributeConverter<BenutzerTyp, Integer> {

	/** Die Instanz des Konverters */
	public final static BenutzerTypConverter instance = new BenutzerTypConverter();
	
	@Override
	public Integer convertToDatabaseColumn(BenutzerTyp typ) {
		return typ.id;
	}

	@Override
	public BenutzerTyp convertToEntityAttribute(Integer dbData) {
		return BenutzerTyp.getByID(dbData);
	}

	@Override
	public Class<BenutzerTyp> getResultType() {
		return BenutzerTyp.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
