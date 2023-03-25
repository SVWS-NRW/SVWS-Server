package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.converter.current.PersonalTypConverter;

/**
 * Diese Klasse ist ein Serialisierer für die unterschiedlichen Personal-Typen. Sie serialisiert
 * einen Wert der Aufzählung {@link PersonalTyp} in die Datenbankdarstellung als String.
 */
public class PersonalTypConverterSerializer extends StdSerializer<PersonalTyp> {

	private static final long serialVersionUID = 8347463080558723603L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public PersonalTypConverterSerializer() {
		super(PersonalTyp.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 * 
	 * @param t   ein Klassenobjekt für die Personaltyp-Klasse
	 */
	public PersonalTypConverterSerializer(Class<PersonalTyp> t) {
		super(t);
	}

	@Override
	public void serialize(PersonalTyp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(PersonalTypConverter.instance.convertToDatabaseColumn(value));
	}

}
