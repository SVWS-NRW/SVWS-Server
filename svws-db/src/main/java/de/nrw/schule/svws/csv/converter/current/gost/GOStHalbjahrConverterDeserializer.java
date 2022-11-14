package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.db.converter.current.gost.GOStHalbjahrConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung 
 * als GostHalbjahr.
 */
public class GOStHalbjahrConverterDeserializer extends StdDeserializer<GostHalbjahr> {

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
	protected GOStHalbjahrConverterDeserializer(Class<GostHalbjahr> t) {
		super(t);
	}

	@Override
	public GostHalbjahr deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
			i = null;
		}		
		return GOStHalbjahrConverter.instance.convertToEntityAttribute(i);
	}
	
}
