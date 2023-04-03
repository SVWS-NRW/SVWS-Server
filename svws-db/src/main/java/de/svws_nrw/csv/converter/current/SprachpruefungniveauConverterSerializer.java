package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.db.converter.current.SprachpruefungniveauConverter;

import java.io.IOException;

/**
 * Diese Klasse ist ein Serialisierer für das Niveau einer Sprachprüfung. Sie serialisiert
 * einen Wert der Aufzählung {@link Sprachpruefungniveau} in die Datenbankdarstellung als Zahl.
 */
public final class SprachpruefungniveauConverterSerializer extends StdSerializer<Sprachpruefungniveau> {

	private static final long serialVersionUID = -4990083704215366131L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public SprachpruefungniveauConverterSerializer() {
		super(Sprachpruefungniveau.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public SprachpruefungniveauConverterSerializer(final Class<Sprachpruefungniveau> t) {
		super(t);
	}

	@Override
	public void serialize(final Sprachpruefungniveau value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(SprachpruefungniveauConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
