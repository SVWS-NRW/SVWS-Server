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
public class PersonalTypConverter extends DBAttributeConverter<PersonalTyp, String> {

	/** Die Instanz des Konverters */
	public final static PersonalTypConverter instance = new PersonalTypConverter();	
	
	@Override
	public String convertToDatabaseColumn(PersonalTyp attribute) {
		return attribute.kuerzel;
	}

	@Override
	public PersonalTyp convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return PersonalTyp.LEHRKRAFT;
		return PersonalTyp.fromBezeichnung(dbData);
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
