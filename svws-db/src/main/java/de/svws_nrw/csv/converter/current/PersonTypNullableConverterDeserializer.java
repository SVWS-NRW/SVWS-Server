package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.converter.current.PersonTypNullableConverter;

import java.io.IOException;

/**
 * Diese Klasse ist einen Deserialisierer von PersonTyp-Objekten.
 */
public final class PersonTypNullableConverterDeserializer extends StdDeserializer<PersonTyp> {

	private static final long serialVersionUID = -1745427357127293977L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public PersonTypNullableConverterDeserializer() {
		super(PersonTyp.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die PersonTyp-Klasse
	 */
	protected PersonTypNullableConverterDeserializer(final Class<PersonTyp> t) {
		super(t);
	}

	@Override
	public PersonTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return PersonTypNullableConverter.instance.convertToEntityAttribute(p.getText());
	}

}
