package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.svws_nrw.db.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung
 * als GostLaufbahnplanungFachkombinationTyp.
 */
public final class GostLaufbahnplanungFachkombinationTypConverterSerializer extends StdSerializer<GostLaufbahnplanungFachkombinationTyp> {

	private static final long serialVersionUID = -5121131241860213925L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public GostLaufbahnplanungFachkombinationTypConverterSerializer() {
		super(GostLaufbahnplanungFachkombinationTyp.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public GostLaufbahnplanungFachkombinationTypConverterSerializer(final Class<GostLaufbahnplanungFachkombinationTyp> t) {
		super(t);
	}

	@Override
	public void serialize(final GostLaufbahnplanungFachkombinationTyp value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(GostLaufbahnplanungFachkombinationTypConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
