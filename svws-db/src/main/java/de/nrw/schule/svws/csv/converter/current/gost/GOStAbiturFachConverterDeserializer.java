package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.db.converter.current.gost.GOStAbiturFachConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung 
 * als GOStAbiturFach.
 */
public class GOStAbiturFachConverterDeserializer extends StdDeserializer<GostAbiturFach> {

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
	protected GOStAbiturFachConverterDeserializer(Class<GostAbiturFach> t) {
		super(t);
	}

	@Override
	public GostAbiturFach deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return GOStAbiturFachConverter.instance.convertToEntityAttribute(p.getText());
	}
	
}
