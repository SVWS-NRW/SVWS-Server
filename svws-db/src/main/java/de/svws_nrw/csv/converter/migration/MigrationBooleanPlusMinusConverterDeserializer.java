package de.svws_nrw.csv.converter.migration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.db.converter.current.BooleanPlusMinusConverter;

/**
 * Diese Klasse ist ein Deserialisierer und deserialisiert die Datenbankdarstellung
 * als String,  - (false) oder + (true) sein kann, in einen Java-String-Wert.
 */
public final class MigrationBooleanPlusMinusConverterDeserializer extends StdDeserializer<Boolean> {

	private static final long serialVersionUID = -1327227762966985248L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public MigrationBooleanPlusMinusConverterDeserializer() {
		super(Boolean.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected MigrationBooleanPlusMinusConverterDeserializer(final Class<Boolean> t) {
		super(t);
	}

	@Override
	public Boolean deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return BooleanPlusMinusConverter.instance.convertToEntityAttribute(p.getText());
	}

}
