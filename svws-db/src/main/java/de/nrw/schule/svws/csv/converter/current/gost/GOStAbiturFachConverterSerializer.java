package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.db.converter.current.gost.GOStAbiturFachConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung 
 * als GOStAbiturfach.
 */
public class GOStAbiturFachConverterSerializer extends StdSerializer<GostAbiturFach> {

	private static final long serialVersionUID = 3985680930817774032L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public GOStAbiturFachConverterSerializer() {
		super(GostAbiturFach.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public GOStAbiturFachConverterSerializer(Class<GostAbiturFach> t) {
		super(t);
	}

	@Override
	public void serialize(GostAbiturFach value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(GOStAbiturFachConverter.instance.convertToDatabaseColumn(value));
	}

}
