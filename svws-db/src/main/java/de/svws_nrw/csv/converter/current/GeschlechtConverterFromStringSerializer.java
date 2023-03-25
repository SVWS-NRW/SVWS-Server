package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.db.converter.current.GeschlechtConverterFromString;

/**
 * Diese Klasse ist ein Serialisierer für Geschlechter. Sie serialisiert ein Objekt der Klasse 
 * {@link Geschlecht} in die Datenbankdarstellung der als String verpackten Zahl 
 * (siehe {@link Geschlecht#id}). 
 */
public class GeschlechtConverterFromStringSerializer extends StdSerializer<Geschlecht> {

	private static final long serialVersionUID = 670435792297312455L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public GeschlechtConverterFromStringSerializer() {
		super(Geschlecht.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public GeschlechtConverterFromStringSerializer(Class<Geschlecht> t) {
		super(t);
	}

	@Override
	public void serialize(Geschlecht value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(GeschlechtConverterFromString.instance.convertToDatabaseColumn(value));
	}

}
