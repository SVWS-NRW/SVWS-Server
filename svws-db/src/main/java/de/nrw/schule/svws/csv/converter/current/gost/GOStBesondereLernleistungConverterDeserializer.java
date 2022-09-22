package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.gost.GostBesondereLernleistung;
import de.nrw.schule.svws.db.converter.current.gost.GOStBesondereLernleistungConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung 
 * als GOStBesondereLernleistung.
 */
public class GOStBesondereLernleistungConverterDeserializer extends StdDeserializer<GostBesondereLernleistung> {

	private static final long serialVersionUID = 1295380111565891607L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public GOStBesondereLernleistungConverterDeserializer() {
		super(GostBesondereLernleistung.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected GOStBesondereLernleistungConverterDeserializer(Class<GostBesondereLernleistung> t) {
		super(t);
	}

	@Override
	public GostBesondereLernleistung deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return GOStBesondereLernleistungConverter.instance.convertToEntityAttribute(p.getText());
	}
	
}
