package de.nrw.schule.svws.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert das Kürzel eines ZulaessigesFach in Bezug
 * auf die amtliche Schulstatistik in den CoreType Schulform. 
 */
public class ZulaessigesFachKuerzelASDConverterSerializer extends StdSerializer<ZulaessigesFach> {

	private static final long serialVersionUID = 2609387887776489624L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public ZulaessigesFachKuerzelASDConverterSerializer() {
		super(ZulaessigesFach.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public ZulaessigesFachKuerzelASDConverterSerializer(Class<ZulaessigesFach> t) {
		super(t);
	}

	@Override
	public void serialize(ZulaessigesFach value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.daten.kuerzelASD);
	}

}
