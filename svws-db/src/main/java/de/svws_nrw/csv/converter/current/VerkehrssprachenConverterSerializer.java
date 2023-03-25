package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.db.converter.current.VerkehrssprachenConverter;

/**
 * Diese Klasse ist ein Serialisierer für Nationalitäten. Sie serialisiert
 * einen Wert der Aufzählung {@link Verkehrssprache} in die Datenbankdarstellung als String.
 */
public class VerkehrssprachenConverterSerializer extends StdSerializer<Verkehrssprache> {

	private static final long serialVersionUID = -4990083708155366131L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public VerkehrssprachenConverterSerializer() {
		super(Verkehrssprache.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public VerkehrssprachenConverterSerializer(Class<Verkehrssprache> t) {
		super(t);
	}

	@Override
	public void serialize(Verkehrssprache value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(VerkehrssprachenConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
