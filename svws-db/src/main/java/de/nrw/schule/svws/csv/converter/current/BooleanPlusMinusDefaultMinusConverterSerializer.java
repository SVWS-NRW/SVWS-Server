package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert einen Java-String-Wert in die 
 * Datenbankdarstellung als String, der  - (false) oder + (true) sein kann.
 * Hier wird der Default auf - (false) gesetzt.
 */
public class BooleanPlusMinusDefaultMinusConverterSerializer extends StdSerializer<Boolean> {

	private static final long serialVersionUID = -1327227762966985248L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public BooleanPlusMinusDefaultMinusConverterSerializer() {
		super(Boolean.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public BooleanPlusMinusDefaultMinusConverterSerializer(Class<Boolean> t) {
		super(t);
	}

	@Override
	public void serialize(Boolean value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(BooleanPlusMinusDefaultMinusConverter.instance.convertToDatabaseColumn(value));
	}

}
