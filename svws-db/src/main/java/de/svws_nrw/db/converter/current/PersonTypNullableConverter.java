package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren von Personentypen für Einwilligungsarten.
 */
@Converter(autoApply = true)
public final class PersonTypNullableConverter extends DBAttributeConverter<PersonTyp, String> {

	/** Die Instanz des Konverters */
	public static final PersonTypNullableConverter instance = new PersonTypNullableConverter();

	@Override
	public String convertToDatabaseColumn(final PersonTyp typ) {
		return (typ == null) ? null : typ.kuerzel;
	}

	@Override
	public PersonTyp convertToEntityAttribute(final String dbData) {
		return PersonTyp.getByKuerzel(dbData);
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
