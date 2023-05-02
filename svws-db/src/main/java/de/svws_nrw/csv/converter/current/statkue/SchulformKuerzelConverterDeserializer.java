package de.svws_nrw.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.schule.Schulform;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert den CoreType
 * Schulform in das Kürzel der amtlichen Schulstatistik.
 */
public final class SchulformKuerzelConverterDeserializer extends StdDeserializer<Schulform> {

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
	protected SchulformKuerzelConverterDeserializer(final Class<Schulform> t) {
		super(t);
	}

	@Override
	public Schulform deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return Schulform.getByKuerzel(p.getText());
	}

}
