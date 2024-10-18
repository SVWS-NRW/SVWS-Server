package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.converter.current.PersonTypNullableConverter;

import java.io.IOException;

/**
 * Diese Klasse ist einen Serialisierer von PersonTyp-Objekten.
 */
public final class PersonTypNullableConverterSerializer extends StdSerializer<PersonTyp> {

	private static final long serialVersionUID = -1745427357127293975L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public PersonTypNullableConverterSerializer() {
		super(PersonTyp.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die PersonTyp-Klasse
	 */
	public PersonTypNullableConverterSerializer(final Class<PersonTyp> t) {
		super(t);
	}

	@Override
	public void serialize(final PersonTyp value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(PersonTypNullableConverter.instance.convertToDatabaseColumn(value));
	}

}
