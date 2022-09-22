package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.db.converter.current.NationalitaetenConverter;

/**
 * Diese Klasse ist ein Serialisierer für Nationalitäten. Sie serialisiert
 * einen Wert der Aufzählung {@link Nationalitaeten} in die Datenbankdarstellung als String.
 */
public class NationalitaetenConverterSerializer extends StdSerializer<Nationalitaeten> {

	private static final long serialVersionUID = -4990083704215366131L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public NationalitaetenConverterSerializer() {
		super(Nationalitaeten.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public NationalitaetenConverterSerializer(Class<Nationalitaeten> t) {
		super(t);
	}

	@Override
	public void serialize(Nationalitaeten value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(NationalitaetenConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
