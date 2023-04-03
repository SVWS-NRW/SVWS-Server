package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.db.converter.current.SchuelerStatusConverter;

/**
 * Diese Klasse ist ein Serialisierer für den Status eine Schülers (z.B. aktiv). Sie serialisiert
 * einen Wert der Aufzählung {@link SchuelerStatus} in die Datenbankdarstellung als Zahl.
 */
public final class SchuelerStatusConverterSerializer extends StdSerializer<SchuelerStatus> {

	private static final long serialVersionUID = -1710825476936663133L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public SchuelerStatusConverterSerializer() {
		super(SchuelerStatus.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public SchuelerStatusConverterSerializer(final Class<SchuelerStatus> t) {
		super(t);
	}

	@Override
	public void serialize(final SchuelerStatus value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(SchuelerStatusConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
