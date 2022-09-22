package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.db.converter.current.BenutzerKompetenzConverter;

/**
 * Diese Klasse ist einen Deserialisierer von Benutzer-Kompetenz-Objekten.
 */
public class BenutzerKompetenzConverterDeserializer extends StdDeserializer<BenutzerKompetenz> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public BenutzerKompetenzConverterDeserializer() {
		super(BenutzerKompetenz.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected BenutzerKompetenzConverterDeserializer(Class<BenutzerKompetenz> t) {
		super(t);
	}

	@Override
	public BenutzerKompetenz deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Long i = null;
		try {
			i = Long.parseLong(p.getText());
		} catch (NumberFormatException e) {
		}
		return BenutzerKompetenzConverter.instance.convertToEntityAttribute(i);
	}
	
}
