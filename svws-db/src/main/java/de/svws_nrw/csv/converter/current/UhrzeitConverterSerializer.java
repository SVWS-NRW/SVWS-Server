package de.svws_nrw.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.UhrzeitConverter;

/**
 * Diese Klasse ist ein Serialisierer für Uhrzeiten. Sie serialisiert 
 * eine Uhrzeit als ISO-8601-Zeichenkette in die Datenbankdarstellung als {@link Timestamp}.
 */
public class UhrzeitConverterSerializer extends StdSerializer<String> {

	private static final long serialVersionUID = 1997212110466236373L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public UhrzeitConverterSerializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public UhrzeitConverterSerializer(Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(UhrzeitConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
