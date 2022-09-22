package de.nrw.schule.svws.csv.converter.current.gost;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.gost.GostBesondereLernleistung;
import de.nrw.schule.svws.db.converter.current.gost.GOStBesondereLernleistungConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung 
 * als GOStBesondereLernleistung.
 */
public class GOStBesondereLernleistungConverterSerializer extends StdSerializer<GostBesondereLernleistung> {

	private static final long serialVersionUID = 1295380111565891607L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public GOStBesondereLernleistungConverterSerializer() {
		super(GostBesondereLernleistung.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public GOStBesondereLernleistungConverterSerializer(Class<GostBesondereLernleistung> t) {
		super(t);
	}

	@Override
	public void serialize(GostBesondereLernleistung value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(GOStBesondereLernleistungConverter.instance.convertToDatabaseColumn(value));
	}

}
