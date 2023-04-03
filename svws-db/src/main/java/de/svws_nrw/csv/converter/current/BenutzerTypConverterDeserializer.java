package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.db.converter.current.BenutzerTypConverter;

/**
 * Diese Klasse ist einen Deserialisierer von Benutzer-Typ-Objekten.
 */
public final class BenutzerTypConverterDeserializer extends StdDeserializer<BenutzerTyp> {

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
	protected BenutzerTypConverterDeserializer(final Class<BenutzerTyp> t) {
		super(t);
	}

	@Override
	public BenutzerTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return BenutzerTypConverter.instance.convertToEntityAttribute(Integer.parseInt(p.getText()));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

}
