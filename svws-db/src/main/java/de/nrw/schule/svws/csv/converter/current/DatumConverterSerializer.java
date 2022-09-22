package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.db.converter.current.DatumConverter;

/**
 * Diese Klasse ist ein Serialisierer für Datumswerte. Sie serialisiert 
 * ein Datum als ISO-8601-Zeichenkette in die Datenbankdarstellung als {@link Timestamp}.
 */
public class DatumConverterSerializer extends StdSerializer<String> {

	private static final long serialVersionUID = 1997235870466236373L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public DatumConverterSerializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public DatumConverterSerializer(Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(DatumConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
