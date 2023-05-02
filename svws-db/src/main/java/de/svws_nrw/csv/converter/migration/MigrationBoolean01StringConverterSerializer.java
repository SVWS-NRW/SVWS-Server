package de.svws_nrw.csv.converter.migration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.Boolean01StringConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert einen Java-Boolean-Wert in die
 * Datenbankdarstellung als String, der  0 (false) oder 1 (true) sein kann.
 */
public final class MigrationBoolean01StringConverterSerializer extends StdSerializer<Boolean> {

	private static final long serialVersionUID = 5630376392284015049L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public MigrationBoolean01StringConverterSerializer() {
		super(Boolean.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public MigrationBoolean01StringConverterSerializer(final Class<Boolean> t) {
		super(t);
	}

	@Override
	public void serialize(final Boolean value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(Boolean01StringConverter.instance.convertToDatabaseColumn(value));
	}

}
