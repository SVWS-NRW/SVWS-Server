package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.db.converter.current.gost.GOStBesondereLernleistungConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als GOStBesondereLernleistung.
 */
public final class GOStBesondereLernleistungConverterDeserializer extends StdDeserializer<GostBesondereLernleistung> {

	private static final long serialVersionUID = 1295380111565891607L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public GOStBesondereLernleistungConverterDeserializer() {
		super(GostBesondereLernleistung.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GOStBesondereLernleistungConverterDeserializer(final Class<GostBesondereLernleistung> t) {
		super(t);
	}

	@Override
	public GostBesondereLernleistung deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return GOStBesondereLernleistungConverter.instance.convertToEntityAttribute(p.getText());
	}

}
