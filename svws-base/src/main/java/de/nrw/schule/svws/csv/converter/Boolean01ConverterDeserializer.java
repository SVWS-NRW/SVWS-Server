package de.nrw.schule.svws.csv.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Zahldarstellung, 
 * die 0 (false) oder 1 (true) sein kann, in einen Java-Boolean-Wert.
 */
public class Boolean01ConverterDeserializer extends StdDeserializer<Boolean> {

	private static final long serialVersionUID = 5630376392284015049L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public Boolean01ConverterDeserializer() {
		super(Boolean.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected Boolean01ConverterDeserializer(Class<Boolean> t) {
		super(t);
	}

	@Override
	public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i = null;
		try {
			i = Integer.parseInt(p.getText());
		} catch (NumberFormatException e) {
		}
		return (i != null) && (i != 0);
	}
	
}
