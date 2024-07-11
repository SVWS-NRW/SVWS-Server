package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.converter.current.PersonTypConverter;

import java.io.IOException;

/**
 * Diese Klasse ist einen Deserialisierer von PersonTyp-Objekten.
 */
public final class PersonTypConverterDeserializer extends StdDeserializer<PersonTyp> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public PersonTypConverterDeserializer() {
		super(PersonTyp.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die PersonTyp-Klasse
	 */
	protected PersonTypConverterDeserializer(final Class<PersonTyp> t) {
		super(t);
	}

	@Override
	public PersonTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return PersonTypConverter.instance.convertToEntityAttribute(p.getText());
	}

}
