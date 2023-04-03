package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als GOStKursart.
 */
public final class GOStKursartConverterDeserializer extends StdDeserializer<GostKursart> {

	private static final long serialVersionUID = -5121032441860213925L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public GOStKursartConverterDeserializer() {
		super(GostKursart.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GOStKursartConverterDeserializer(final Class<GostKursart> t) {
		super(t);
	}

	@Override
	public GostKursart deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return GOStKursartConverter.instance.convertToEntityAttribute(p.getText());
	}

}
