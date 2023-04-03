package de.svws_nrw.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.schule.Schulgliederung;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert den CoreType
 * Schulgliederung in das Kürzel der amtlichen Schulstatistik.
 */
public final class SchulgliederungKuerzelConverterDeserializer extends StdDeserializer<Schulgliederung> {

	private static final long serialVersionUID = -3520968291156156611L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public SchulgliederungKuerzelConverterDeserializer() {
		super(Schulgliederung.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected SchulgliederungKuerzelConverterDeserializer(final Class<Schulgliederung> t) {
		super(t);
	}

	@Override
	public Schulgliederung deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return Schulgliederung.getByKuerzel(p.getText());
	}

}
