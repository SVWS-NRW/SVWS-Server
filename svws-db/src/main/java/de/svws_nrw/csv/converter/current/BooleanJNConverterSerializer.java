package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.BooleanJNConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert einen Java-String-Wert in die
 * Datenbankdarstellung als String, der "N" (false) oder "J" (true) sein kann.
 */
public final class BooleanJNConverterSerializer extends StdSerializer<Boolean> {

	private static final long serialVersionUID = -1327227762966985248L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public BooleanJNConverterSerializer() {
		super(Boolean.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public BooleanJNConverterSerializer(final Class<Boolean> t) {
		super(t);
	}

	@Override
	public void serialize(final Boolean value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(BooleanJNConverter.instance.convertToDatabaseColumn(value));
	}

}
