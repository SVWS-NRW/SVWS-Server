package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.svws_nrw.db.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als GostLaufbahnplanungFachkombinationTyp.
 */
public final class GostLaufbahnplanungFachkombinationTypConverterDeserializer extends StdDeserializer<GostLaufbahnplanungFachkombinationTyp> {

	private static final long serialVersionUID = -5121032441860234225L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public GostLaufbahnplanungFachkombinationTypConverterDeserializer() {
		super(GostLaufbahnplanungFachkombinationTyp.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GostLaufbahnplanungFachkombinationTypConverterDeserializer(final Class<GostLaufbahnplanungFachkombinationTyp> t) {
		super(t);
	}

	@Override
	public GostLaufbahnplanungFachkombinationTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return GostLaufbahnplanungFachkombinationTypConverter.instance.convertToEntityAttribute(i);
	}

}
