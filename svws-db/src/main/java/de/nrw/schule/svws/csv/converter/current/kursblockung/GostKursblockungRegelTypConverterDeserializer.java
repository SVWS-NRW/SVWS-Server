package de.nrw.schule.svws.csv.converter.current.kursblockung;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung 
 * als GostKursblockungRegelTyp.
 */
public class GostKursblockungRegelTypConverterDeserializer extends StdDeserializer<GostKursblockungRegelTyp> {

	private static final long serialVersionUID = -5121032441860213925L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public GostKursblockungRegelTypConverterDeserializer() {
		super(GostKursblockungRegelTyp.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected GostKursblockungRegelTypConverterDeserializer(Class<GostKursblockungRegelTyp> t) {
		super(t);
	}

	@Override
	public GostKursblockungRegelTyp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
			i = null;
		}		
		return GostKursblockungRegelTypConverter.instance.convertToEntityAttribute(i);
	}
	
}
