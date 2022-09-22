package de.nrw.schule.svws.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.statkue.Schulform;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert den CoreType 
 * Schulform in das Kürzel der amtlichen Schulstatistik.
 */
public class SchulformKuerzelConverterDeserializer extends StdDeserializer<Schulform> {

	private static final long serialVersionUID = -3520968291156156611L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public SchulformKuerzelConverterDeserializer() {
		super(Schulform.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected SchulformKuerzelConverterDeserializer(Class<Schulform> t) {
		super(t);
	}

	@Override
	public Schulform deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return Schulform.getByKuerzel(p.getText());
	}
	
}
