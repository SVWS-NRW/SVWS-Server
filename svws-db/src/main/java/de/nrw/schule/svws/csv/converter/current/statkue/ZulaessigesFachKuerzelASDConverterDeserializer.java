package de.nrw.schule.svws.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert den CoreType 
 * ZulaessigesFach in das Kürzel der amtlichen Schulstatistik.
 */
public class ZulaessigesFachKuerzelASDConverterDeserializer extends StdDeserializer<ZulaessigesFach> {

	private static final long serialVersionUID = -3520968291156156611L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public ZulaessigesFachKuerzelASDConverterDeserializer() {
		super(ZulaessigesFach.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected ZulaessigesFachKuerzelASDConverterDeserializer(Class<ZulaessigesFach> t) {
		super(t);
	}

	@Override
	public ZulaessigesFach deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return ZulaessigesFach.getByKuerzelASD(p.getText());
	}
	
}
