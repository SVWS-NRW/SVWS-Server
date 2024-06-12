package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren von Leher-Leitungsfunktionen.
 */
@Converter(autoApply = true)
public final class LehrerLeitungsfunktionConverter extends DBAttributeConverter<BenutzerTyp, Integer> {

	/** Die Instanz des Konverters */
	public static final LehrerLeitungsfunktionConverter instance = new LehrerLeitungsfunktionConverter();

	@Override
	public Integer convertToDatabaseColumn(final BenutzerTyp typ) {
		return typ.id;
	}

	@Override
	public BenutzerTyp convertToEntityAttribute(final Integer dbData) {
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
