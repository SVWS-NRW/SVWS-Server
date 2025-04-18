package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Personaltypen bei Lehrkräften in der Tabelle K_lehrer in Java (Value)
 * zu einer Stringdarstellung in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter(autoApply = true)
public final class PersonalTypConverter extends DBAttributeConverter<PersonalTyp, String> {

	/** Die Instanz des Konverters */
	public static final PersonalTypConverter instance = new PersonalTypConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public PersonalTypConverter() {
		// leer
	}

	@Override
	public String convertToDatabaseColumn(final PersonalTyp attribute) {
		return attribute.kuerzel;
	}

	@Override
	public PersonalTyp convertToEntityAttribute(final String dbData) {
		if (dbData == null)
			return PersonalTyp.LEHRKRAFT;
		return PersonalTyp.fromKuerzel(dbData);
	}

	@Override
	public Class<PersonalTyp> getResultType() {
		return PersonalTyp.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
