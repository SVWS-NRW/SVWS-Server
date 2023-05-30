package de.svws_nrw.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.UhrzeitConverter;

/**
 * Diese Klasse ist ein Serialisierer für Uhrzeiten. Sie serialisiert
 * eine Uhrzeit in Minuten als Integer in die Datenbankdarstellung als {@link Timestamp}.
 */
public final class UhrzeitConverterSerializer extends StdSerializer<Integer> {

	private static final long serialVersionUID = 1997212110466236373L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public UhrzeitConverterSerializer() {
		super(Integer.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public UhrzeitConverterSerializer(final Class<Integer> t) {
		super(t);
	}

	@Override
	public void serialize(final Integer value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(UhrzeitConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
