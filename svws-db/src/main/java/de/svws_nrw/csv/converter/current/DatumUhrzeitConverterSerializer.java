package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.DatumUhrzeitConverter;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Diese Klasse ist ein Serialisierer für Datumswerte. Sie serialisiert 
 * ein Datum als ISO-8601-Zeichenkette in die Datenbankdarstellung als {@link Timestamp}.
 */
public class DatumUhrzeitConverterSerializer extends StdSerializer<String> {


	private static final long serialVersionUID = 2066486893789871316L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public DatumUhrzeitConverterSerializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public DatumUhrzeitConverterSerializer(Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(DatumUhrzeitConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
