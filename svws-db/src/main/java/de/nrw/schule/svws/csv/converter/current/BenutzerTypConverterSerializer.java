package de.nrw.schule.svws.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.nrw.schule.svws.core.types.benutzer.BenutzerTyp;
import de.nrw.schule.svws.db.converter.current.BenutzerTypConverter;

/**
 * Diese Klasse ist einen Serialisierer von Benutzer-Typ-Objekten.
 */
public class BenutzerTypConverterSerializer extends StdSerializer<BenutzerTyp> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public BenutzerTypConverterSerializer() {
		super(BenutzerTyp.class);
	}
	
	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 * 
	 * @param t   ein Klassenobjekt für die Benutzer-Typ-Klasse
	 */
	public BenutzerTypConverterSerializer(Class<BenutzerTyp> t) {
		super(t);
	}

	@Override
	public void serialize(BenutzerTyp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(BenutzerTypConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
