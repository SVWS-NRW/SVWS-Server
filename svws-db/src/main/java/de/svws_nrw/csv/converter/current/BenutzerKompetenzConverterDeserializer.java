package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.converter.current.BenutzerKompetenzConverter;

/**
 * Diese Klasse ist einen Deserialisierer von Benutzer-Kompetenz-Objekten.
 */
public final class BenutzerKompetenzConverterDeserializer extends StdDeserializer<BenutzerKompetenz> {

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
	protected BenutzerKompetenzConverterDeserializer(final Class<BenutzerKompetenz> t) {
		super(t);
	}

	@Override
	public BenutzerKompetenz deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return BenutzerKompetenzConverter.instance.convertToEntityAttribute(Long.parseLong(p.getText()));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

}
