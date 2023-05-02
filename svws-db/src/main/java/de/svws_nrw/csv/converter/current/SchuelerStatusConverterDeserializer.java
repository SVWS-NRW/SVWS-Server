package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.db.converter.current.SchuelerStatusConverter;

/**
 * Diese Klasse ist ein Deserialisierer für den Status eine Schülers (z.B. aktiv). Sie deserialisiert
 * die Datenbankdarstellung als Zahl in einen Wert der Aufzählung {@link SchuelerStatus}.
 */
public final class SchuelerStatusConverterDeserializer extends StdDeserializer<SchuelerStatus> {

	private static final long serialVersionUID = -1710825476936663133L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public SchuelerStatusConverterDeserializer() {
		super(SchuelerStatus.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected SchuelerStatusConverterDeserializer(final Class<SchuelerStatus> t) {
		super(t);
	}

	@Override
	public SchuelerStatus deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		Integer i = null;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return SchuelerStatusConverter.instance.convertToEntityAttribute(i);
	}

}
