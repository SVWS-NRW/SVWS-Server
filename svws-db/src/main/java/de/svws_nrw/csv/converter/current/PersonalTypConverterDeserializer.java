package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.converter.current.PersonalTypConverter;

/**
 * Diese Klasse ist ein Deserialisierer für die unterschiedlichen Personal-Typen. Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link PersonalTyp}.
 */
public final class PersonalTypConverterDeserializer extends StdDeserializer<PersonalTyp> {

	private static final long serialVersionUID = 8347463080558723603L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public PersonalTypConverterDeserializer() {
		super(PersonalTyp.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected PersonalTypConverterDeserializer(final Class<PersonalTyp> t) {
		super(t);
	}

	@Override
	public PersonalTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return PersonalTypConverter.instance.convertToEntityAttribute(p.getText());
	}

}
