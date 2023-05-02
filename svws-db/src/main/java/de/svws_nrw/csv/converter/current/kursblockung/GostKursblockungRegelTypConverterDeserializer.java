package de.svws_nrw.csv.converter.current.kursblockung;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als GostKursblockungRegelTyp.
 */
public final class GostKursblockungRegelTypConverterDeserializer extends StdDeserializer<GostKursblockungRegelTyp> {

	private static final long serialVersionUID = -5121032441860213925L;

	/**
	 * Erzeugt einen neuen Deserialisierer
	 */
	public GostKursblockungRegelTypConverterDeserializer() {
		super(GostKursblockungRegelTyp.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected GostKursblockungRegelTypConverterDeserializer(final Class<GostKursblockungRegelTyp> t) {
		super(t);
	}

	@Override
	public GostKursblockungRegelTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return GostKursblockungRegelTypConverter.instance.convertToEntityAttribute(i);
	}

}
