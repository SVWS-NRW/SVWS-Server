package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.db.converter.current.SprachpruefungniveauConverter;

import java.io.IOException;

/**
 * Diese Klasse ist ein Deserialisierer für das Niveau einer Sprachprüfung. Sie deserialisiert
 * die Datenbankdarstellung als Zahl in einen Wert der Aufzählung {@link Sprachpruefungniveau}.
 */
public final class SprachpruefungniveauConverterDeserializer extends StdDeserializer<Sprachpruefungniveau> {

	private static final long serialVersionUID = -5130396859369645143L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public SprachpruefungniveauConverterDeserializer() {
		super(Sprachpruefungniveau.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected SprachpruefungniveauConverterDeserializer(final Class<Sprachpruefungniveau> t) {
		super(t);
	}

	@Override
	public Sprachpruefungniveau deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return SprachpruefungniveauConverter.instance.convertToEntityAttribute(i);
	}

}
