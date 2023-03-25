package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.gost.AbiturBelegungsart;
import de.svws_nrw.db.converter.current.gost.AbiturBelegungsartConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung 
 * als AbiturBelegungsart.
 */
public class AbiturBelegungsartConverterDeserializer extends StdDeserializer<AbiturBelegungsart> {

	private static final long serialVersionUID = 3644365824216711029L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public AbiturBelegungsartConverterDeserializer() {
		super(AbiturBelegungsart.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected AbiturBelegungsartConverterDeserializer(Class<AbiturBelegungsart> t) {
		super(t);
	}

	@Override
	public AbiturBelegungsart deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return AbiturBelegungsartConverter.instance.convertToEntityAttribute(p.getText());
	}
	
}
