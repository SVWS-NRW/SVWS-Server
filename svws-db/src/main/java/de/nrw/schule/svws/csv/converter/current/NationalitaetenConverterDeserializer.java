package de.nrw.schule.svws.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.db.converter.current.NationalitaetenConverter;

import java.io.IOException;

/**
 * Diese Klasse ist ein Deserialisierer für Nationalitäten. Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link Nationalitaeten}.
 */
public class NationalitaetenConverterDeserializer extends StdDeserializer<Nationalitaeten> {

	private static final long serialVersionUID = -5130396859369645143L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public NationalitaetenConverterDeserializer() {
		super(Nationalitaeten.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected NationalitaetenConverterDeserializer(Class<Nationalitaeten> t) {
		super(t);
	}

	@Override
	public Nationalitaeten deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return NationalitaetenConverter.instance.convertToEntityAttribute(p.getText());
	}
	
}
