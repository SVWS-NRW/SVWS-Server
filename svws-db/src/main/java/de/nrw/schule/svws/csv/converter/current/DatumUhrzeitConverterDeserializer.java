package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.db.converter.current.DatumUhrzeitConverter;

/**
 * Diese Klasse ist ein Deserialisierer für Datumswerte. Sie deserialisiert die
 * Datenbankdarstellung als Timestamp in ein Datum als Zeichenkette nach
 * ISO-8601.
 */
public class DatumUhrzeitConverterDeserializer extends StdDeserializer<String> {

	private static final long serialVersionUID = 1997235870466236373L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public DatumUhrzeitConverterDeserializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t das Klassen-Objekt
	 */
	protected DatumUhrzeitConverterDeserializer(Class<String> t) {
		super(t);
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return DatumUhrzeitConverter.instance.convertToEntityAttribute(Timestamp.valueOf(p.getText()));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
