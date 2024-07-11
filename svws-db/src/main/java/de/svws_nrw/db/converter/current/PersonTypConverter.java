package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren von Personentypen für Einwilligungsarten.
 */
@Converter(autoApply = true)
public final class PersonTypConverter extends DBAttributeConverter<PersonTyp, String> {

	/** Die Instanz des Konverters */
	public static final PersonTypConverter instance = new PersonTypConverter();

	@Override
	public String convertToDatabaseColumn(final PersonTyp typ) {
		return typ.kuerzel;
	}

	@Override
	public PersonTyp convertToEntityAttribute(final String dbData) {
		final PersonTyp typ = PersonTyp.getByKuerzel(dbData);
		return (typ == null) ? PersonTyp.SCHUELER : typ;
	}

	@Override
	public Class<PersonTyp> getResultType() {
		return PersonTyp.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
