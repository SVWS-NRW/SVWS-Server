package de.svws_nrw.csv.converter.current;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.db.converter.current.UhrzeitConverter;

/**
 * Diese Klasse ist ein Deserialisierer für Uhrzeitwerte. Sie deserialisiert die
 * Datenbankdarstellung als Timestamp in eine Uhrzeit als Integer-Wert in Minuten.
 */
public final class UhrzeitConverterDeserializer extends StdDeserializer<Integer> {

	private static final long serialVersionUID = 1997235870466231273L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public UhrzeitConverterDeserializer() {
		super(Integer.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected UhrzeitConverterDeserializer(final Class<Integer> t) {
		super(t);
	}

	@Override
	public Integer deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		try {
			return UhrzeitConverter.instance.convertToEntityAttribute(Timestamp.valueOf(p.getText()));
		} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
			return null;
		}
	}

}
