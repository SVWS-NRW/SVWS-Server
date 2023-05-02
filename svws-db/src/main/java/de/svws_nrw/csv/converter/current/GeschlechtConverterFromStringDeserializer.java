package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.db.converter.current.GeschlechtConverterFromString;

/**
 * Diese Klasse ist ein Deserialisierer für Geschlechter. Sie deserialisiert die
 * Datenbankdarstellung der als String verpackten Zahl (siehe {@link Geschlecht#id})
 * in ein Objekt der Klasse {@link Geschlecht}.
 */
public final class GeschlechtConverterFromStringDeserializer extends StdDeserializer<Geschlecht> {

	private static final long serialVersionUID = 670435792297312455L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public GeschlechtConverterFromStringDeserializer() {
		super(Geschlecht.class);
	}

	/**
	 * Erzeugt einen neuen deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GeschlechtConverterFromStringDeserializer(final Class<Geschlecht> t) {
		super(t);
	}

	@Override
	public Geschlecht deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return GeschlechtConverterFromString.instance.convertToEntityAttribute(p.getText());
	}

}
