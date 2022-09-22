package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.db.converter.current.GeschlechtConverter;

/**
 * Diese Klasse ist ein Serialisierer für Geschlechter. Sie serialisiert ein Objekt der Klasse 
 * {@link Geschlecht} in die Datenbankdarstellung der Zahl (siehe {@link Geschlecht#id}). 
 */
public class GeschlechtConverterSerializer extends StdSerializer<Geschlecht> {

	private static final long serialVersionUID = 9138277744936801173L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public GeschlechtConverterSerializer() {
		super(Geschlecht.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public GeschlechtConverterSerializer(Class<Geschlecht> t) {
		super(t);
	}

	@Override
	public void serialize(Geschlecht value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(GeschlechtConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
