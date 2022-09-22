package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.data.gost.AbiturKursMarkierung;
import de.nrw.schule.svws.db.converter.current.gost.AbiturKursMarkierungConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung 
 * als AbiturKursMarkierung.
 */
public class AbiturKursMarkierungConverterSerializer extends StdSerializer<AbiturKursMarkierung> {

	private static final long serialVersionUID = -4503947939673854979L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public AbiturKursMarkierungConverterSerializer() {
		super(AbiturKursMarkierung.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public AbiturKursMarkierungConverterSerializer(Class<AbiturKursMarkierung> t) {
		super(t);
	}

	@Override
	public void serialize(AbiturKursMarkierung value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(AbiturKursMarkierungConverter.instance.convertToDatabaseColumn(value));
	}

}
