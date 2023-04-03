package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als GostHalbjahr.
 */
public final class GOStHalbjahrConverterDeserializer extends StdDeserializer<GostHalbjahr> {

	private static final long serialVersionUID = -5121032441860213925L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public GOStHalbjahrConverterDeserializer() {
		super(GostHalbjahr.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GOStHalbjahrConverterDeserializer(final Class<GostHalbjahr> t) {
		super(t);
	}

	@Override
	public GostHalbjahr deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return GOStHalbjahrConverter.instance.convertToEntityAttribute(i);
	}

}
