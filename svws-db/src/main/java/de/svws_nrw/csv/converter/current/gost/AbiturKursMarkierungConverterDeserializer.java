package de.svws_nrw.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.data.gost.AbiturKursMarkierung;
import de.svws_nrw.db.converter.current.gost.AbiturKursMarkierungConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als AbiturkursMarkierung.
 */
public final class AbiturKursMarkierungConverterDeserializer extends StdDeserializer<AbiturKursMarkierung> {

	private static final long serialVersionUID = -4503947939673854979L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public AbiturKursMarkierungConverterDeserializer() {
		super(AbiturKursMarkierung.class);
	}

	/**
	 * Erzeugt einen neuen deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected AbiturKursMarkierungConverterDeserializer(final Class<AbiturKursMarkierung> t) {
		super(t);
	}

	@Override
	public AbiturKursMarkierung deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return AbiturKursMarkierungConverter.instance.convertToEntityAttribute(p.getText());
	}

}
