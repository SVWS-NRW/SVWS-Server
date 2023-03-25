package de.svws_nrw.csv.converter.current.kursblockung;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;

/**
 * Diese Klasse ist ein Serialisierer und serialisiert die Datenbankdarstellung 
 * als GostKursblockungRegelTyp.
 */
public class GostKursblockungRegelTypConverterSerializer extends StdSerializer<GostKursblockungRegelTyp> {

	private static final long serialVersionUID = -5121032441860213925L;

	/**
	 * Erzeugt einen neuen Serialisierer
	 */
	public GostKursblockungRegelTypConverterSerializer() {
		super(GostKursblockungRegelTyp.class);
	}
	
	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public GostKursblockungRegelTypConverterSerializer(Class<GostKursblockungRegelTyp> t) {
		super(t);
	}

	@Override
	public void serialize(GostKursblockungRegelTyp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(GostKursblockungRegelTypConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
