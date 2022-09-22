package de.nrw.schule.svws.csv.converter.migration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.db.converter.current.StringToIntegerConverter;


/**
 * Diese Klasse ist ein Serialisierer für Stringwerte. Sie serialisiert 
 * ein String als Intergerwert.
 */
public class MigrationStringToIntegerConverterSerializer extends StdSerializer<Integer> {

	private static final long serialVersionUID = 899602939694388520L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public MigrationStringToIntegerConverterSerializer() {
		super(Integer.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public MigrationStringToIntegerConverterSerializer(Class<Integer> t) {
		super(t);
	}

	@Override
	public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(StringToIntegerConverter.instance.convertToDatabaseColumn(value));
	}

}
