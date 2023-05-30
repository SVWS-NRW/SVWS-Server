package de.svws_nrw.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.UhrzeitConverterString;

/**
 * Diese Klasse ist ein Serialisierer für Uhrzeiten. Sie serialisiert
 * eine Uhrzeit als ISO-8601-Zeichenkette in die Datenbankdarstellung als {@link Timestamp}.
 */
public final class UhrzeitConverterStringSerializer extends StdSerializer<String> {

	private static final long serialVersionUID = 3667850471299029075L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public UhrzeitConverterStringSerializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public UhrzeitConverterStringSerializer(final Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(final String value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(UhrzeitConverterString.instance.convertToDatabaseColumn(value).toString());
	}

}
