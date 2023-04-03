package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.db.converter.current.BenutzerTypConverter;

/**
 * Diese Klasse ist einen Serialisierer von Benutzer-Typ-Objekten.
 */
public final class BenutzerTypConverterSerializer extends StdSerializer<BenutzerTyp> {

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
	public BenutzerTypConverterSerializer(final Class<BenutzerTyp> t) {
		super(t);
	}

	@Override
	public void serialize(final BenutzerTyp value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(BenutzerTypConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
