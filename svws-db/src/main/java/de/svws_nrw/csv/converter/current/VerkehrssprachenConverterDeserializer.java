package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.db.converter.current.VerkehrssprachenConverter;

/**
 * Diese Klasse ist ein Deserialisierer für Nationalitäten. Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link Verkehrssprache}.
 */
public final class VerkehrssprachenConverterDeserializer extends StdDeserializer<Verkehrssprache> {

	private static final long serialVersionUID = -5130396859369645143L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public VerkehrssprachenConverterDeserializer() {
		super(Verkehrssprache.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected VerkehrssprachenConverterDeserializer(final Class<Verkehrssprache> t) {
		super(t);
	}

	@Override
	public Verkehrssprache deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return VerkehrssprachenConverter.instance.convertToEntityAttribute(p.getText());
	}

}
