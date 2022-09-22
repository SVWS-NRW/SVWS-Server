package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.db.converter.current.Boolean01StringConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung 
 * als Zahl, die 0 (false) oder 1 (true) sein kann, in einen Java-String-Wert.
 */
public class Boolean01StringConverterDeserializer extends StdDeserializer<Boolean> {

	private static final long serialVersionUID = 5630376392284015049L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public Boolean01StringConverterDeserializer() {
		super(Boolean.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected Boolean01StringConverterDeserializer(Class<Boolean> t) {
		super(t);
	}

	@Override
	public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return Boolean01StringConverter.instance.convertToEntityAttribute(p.getText());
	}
	
}
