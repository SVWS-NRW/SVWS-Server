package de.nrw.schule.svws.csv.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert einen Java-Boolean-Wert in die 
 * Zahldarstellung, die 0 (false) oder 1 (true) sein kann.
 */
public class Boolean01ConverterSerializer extends StdSerializer<Boolean> {

	private static final long serialVersionUID = 5630376392284015049L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public Boolean01ConverterSerializer() {
		super(Boolean.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public Boolean01ConverterSerializer(Class<Boolean> t) {
		super(t);
	}

	@Override
	public void serialize(Boolean value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString( (((value != null) && value) ? "1" : "0"));
	}

}
