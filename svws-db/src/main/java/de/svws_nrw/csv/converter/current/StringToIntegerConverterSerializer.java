package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.StringToIntegerConverter;


/**
 * Diese Klasse ist ein Serialisierer für Stringwerte. Sie serialisiert
 * ein String als Intergerwert.
 */
public final class StringToIntegerConverterSerializer extends StdSerializer<Integer> {

	private static final long serialVersionUID = 899602939694388520L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public StringToIntegerConverterSerializer() {
		super(Integer.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public StringToIntegerConverterSerializer(final Class<Integer> t) {
		super(t);
	}

	@Override
	public void serialize(final Integer value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(StringToIntegerConverter.instance.convertToDatabaseColumn(value));
	}

}
