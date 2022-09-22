package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.db.converter.current.gost.GOStKursartConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung 
 * als GOStKursart.
 */
public class GOStKursartConverterSerializer extends StdSerializer<GostKursart> {

	private static final long serialVersionUID = -5121032441860213925L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public GOStKursartConverterSerializer() {
		super(GostKursart.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public GOStKursartConverterSerializer(Class<GostKursart> t) {
		super(t);
	}

	@Override
	public void serialize(GostKursart value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(GOStKursartConverter.instance.convertToDatabaseColumn(value));
	}

}
