package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.converter.current.BenutzerKompetenzConverter;

/**
 * Diese Klasse ist einen Serialisierer von Benutzer-Kompetenz-Objekten.
 */
public class BenutzerKompetenzConverterSerializer extends StdSerializer<BenutzerKompetenz> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public BenutzerKompetenzConverterSerializer() {
		super(BenutzerKompetenz.class);
	}
	
	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 * 
	 * @param t   ein Klassenobjekt für die Benutzer-Kompetenz-Klasse
	 */
	public BenutzerKompetenzConverterSerializer(Class<BenutzerKompetenz> t) {
		super(t);
	}

	@Override
	public void serialize(BenutzerKompetenz value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(BenutzerKompetenzConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
