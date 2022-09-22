package de.nrw.schule.svws.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.statkue.Schulform;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert das Kürzel einer Schulform in Bezug
 * auf die amtliche Schulstatistik in den CoreType Schulform. 
 */
public class SchulformKuerzelConverterSerializer extends StdSerializer<Schulform> {

	private static final long serialVersionUID = 2609387887776489624L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public SchulformKuerzelConverterSerializer() {
		super(Schulform.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public SchulformKuerzelConverterSerializer(Class<Schulform> t) {
		super(t);
	}

	@Override
	public void serialize(Schulform value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.daten.kuerzel);
	}

}
