package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.db.converter.current.SchuelerStatusConverter;

/**
 * Diese Klasse ist ein Deserialisierer für den Status eine Schülers (z.B. aktiv). Sie deserialisiert
 * die Datenbankdarstellung als Zahl in einen Wert der Aufzählung {@link SchuelerStatus}.
 */
public class SchuelerStatusConverterDeserializer extends StdDeserializer<SchuelerStatus> {

	private static final long serialVersionUID = -1710825476936663133L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public SchuelerStatusConverterDeserializer() {
		super(SchuelerStatus.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected SchuelerStatusConverterDeserializer(Class<SchuelerStatus> t) {
		super(t);
	}

	@Override
	public SchuelerStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i = null;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
			i = null;
		}
		return SchuelerStatusConverter.instance.convertToEntityAttribute(i);
	}
	
}
