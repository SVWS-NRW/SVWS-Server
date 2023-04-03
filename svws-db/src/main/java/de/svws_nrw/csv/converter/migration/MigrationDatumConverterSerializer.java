package de.svws_nrw.csv.converter.migration;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.db.converter.current.DatumConverter;

/**
 * Diese Klasse ist ein Serialisierer für Datumswerte. Sie serialisiert
 * ein Datum als ISO-8601-Zeichenkette in die Datenbankdarstellung als {@link Timestamp}.
 */
public final class MigrationDatumConverterSerializer extends StdSerializer<String> {

	private static final long serialVersionUID = 1997235870466236373L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public MigrationDatumConverterSerializer() {
		super(String.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public MigrationDatumConverterSerializer(final Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(final String value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(DatumConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
