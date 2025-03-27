package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;

import java.io.IOException;

/**
 * Diese Klasse ist ein Deserialisierer für Nationalitäten. Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link Nationalitaeten}.
 */
public final class NationalitaetenConverterDeserializer extends StdDeserializer<Nationalitaeten> {

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
	protected NationalitaetenConverterDeserializer(final Class<Nationalitaeten> t) {
		super(t);
	}

	@Override
	public Nationalitaeten deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return NationalitaetenConverter.instance.convertToEntityAttribute(p.getText());
	}

}
