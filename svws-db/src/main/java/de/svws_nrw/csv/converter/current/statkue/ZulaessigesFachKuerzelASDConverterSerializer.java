package de.svws_nrw.csv.converter.current.statkue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.fach.ZulaessigesFach;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert das Kürzel eines ZulaessigesFach in Bezug
 * auf die amtliche Schulstatistik in den CoreType Schulform.
 */
public final class ZulaessigesFachKuerzelASDConverterSerializer extends StdSerializer<ZulaessigesFach> {

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
	public ZulaessigesFachKuerzelASDConverterSerializer(final Class<ZulaessigesFach> t) {
		super(t);
	}

	@Override
	public void serialize(final ZulaessigesFach value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(value.daten.kuerzelASD);
	}

}
