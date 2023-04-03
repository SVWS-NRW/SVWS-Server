package de.svws_nrw.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.db.converter.current.DatumConverter;

/**
 * Diese Klasse ist ein Deserialisierer für Datumswerte. Sie deserialisiert die
 * Datenbankdarstellung als Timestamp in ein Datum als Zeichenkette nach ISO-8601.
 */
public final class DatumConverterDeserializer extends StdDeserializer<String> {

	private static final long serialVersionUID = 1997235870466236373L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public DatumConverterDeserializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected DatumConverterDeserializer(final Class<String> t) {
		super(t);
	}

	@Override
	public String deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return DatumConverter.instance.convertToEntityAttribute(Timestamp.valueOf(p.getText()));
		} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
			return null;
		}
	}

}
