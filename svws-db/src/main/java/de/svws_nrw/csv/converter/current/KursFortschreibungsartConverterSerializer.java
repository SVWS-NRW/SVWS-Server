package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.db.converter.current.KursFortschreibungsartConverter;

/**
 * Diese Klasse ist ein Serialisierer für die Kurs-Fortschriebungsart. Sie serialisiert 
 * einen Wert der Aufzählung {@link KursFortschreibungsart} in die Datenbankdarstellung als String.
 */
public class KursFortschreibungsartConverterSerializer extends StdSerializer<KursFortschreibungsart> {

	private static final long serialVersionUID = 1871702485070815240L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public KursFortschreibungsartConverterSerializer() {
		super(KursFortschreibungsart.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public KursFortschreibungsartConverterSerializer(Class<KursFortschreibungsart> t) {
		super(t);
	}

	@Override
	public void serialize(KursFortschreibungsart value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(KursFortschreibungsartConverter.instance.convertToDatabaseColumn(value));
	}

}
