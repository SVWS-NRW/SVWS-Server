package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.db.converter.current.GeschlechtConverter;

/**
 * Diese Klasse ist ein Deserialisierer für Geschlechter. Sie deserialisiert die
 * Datenbankdarstellung als Zahl (siehe {@link Geschlecht#id}) in ein
 * Objekt der Klasse {@link Geschlecht}.
 */
public final class GeschlechtConverterDeserializer extends StdDeserializer<Geschlecht> {

	private static final long serialVersionUID = 9138277744936801173L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public GeschlechtConverterDeserializer() {
		super(Geschlecht.class);
	}


	/**
	 * Erzeugt einen neuen Deerialisierer unter Angabe der {@link Class}
	 *
	 * @param vc   das Klassen-Objekt
	 */
	protected GeschlechtConverterDeserializer(final Class<Geschlecht> vc) {
		super(vc);
	}

	@Override
	public Geschlecht deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return GeschlechtConverter.instance.convertToEntityAttribute(NumberUtils.toInt(p.getText(), Geschlecht.X.id));
	}

}
