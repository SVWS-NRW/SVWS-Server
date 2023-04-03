package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.db.converter.current.gost.GOStAbiturFachConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als GOStAbiturFach.
 */
public final class GOStAbiturFachConverterDeserializer extends StdDeserializer<GostAbiturFach> {

	private static final long serialVersionUID = 3985680930817774032L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public GOStAbiturFachConverterDeserializer() {
		super(GostAbiturFach.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GOStAbiturFachConverterDeserializer(final Class<GostAbiturFach> t) {
		super(t);
	}

	@Override
	public GostAbiturFach deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return GOStAbiturFachConverter.instance.convertToEntityAttribute(p.getText());
	}

}
