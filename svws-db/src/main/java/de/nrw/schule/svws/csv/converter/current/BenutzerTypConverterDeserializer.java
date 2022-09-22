package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.core.types.benutzer.BenutzerTyp;
import de.nrw.schule.svws.db.converter.current.BenutzerTypConverter;

/**
 * Diese Klasse ist einen Deserialisierer von Benutzer-Typ-Objekten.
 */
public class BenutzerTypConverterDeserializer extends StdDeserializer<BenutzerTyp> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public BenutzerTypConverterDeserializer() {
		super(BenutzerKompetenz.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected BenutzerTypConverterDeserializer(Class<BenutzerTyp> t) {
		super(t);
	}

	@Override
	public BenutzerTyp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i = null;
		try {
			i = Integer.parseInt(p.getText());
		} catch (NumberFormatException e) {
		}
		return BenutzerTypConverter.instance.convertToEntityAttribute(i);
	}
	
}
