package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.gost.AbiturBelegungsart;
import de.nrw.schule.svws.db.converter.current.gost.AbiturBelegungsartConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung 
 * als AbiturBelegungsart.
 */
public class AbiturBelegungsartConverterSerializer extends StdSerializer<AbiturBelegungsart> {

	private static final long serialVersionUID = 3644365824216711029L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public AbiturBelegungsartConverterSerializer() {
		super(AbiturBelegungsart.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public AbiturBelegungsartConverterSerializer(Class<AbiturBelegungsart> t) {
		super(t);
	}

	@Override
	public void serialize(AbiturBelegungsart value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(AbiturBelegungsartConverter.instance.convertToDatabaseColumn(value));
	}

}
