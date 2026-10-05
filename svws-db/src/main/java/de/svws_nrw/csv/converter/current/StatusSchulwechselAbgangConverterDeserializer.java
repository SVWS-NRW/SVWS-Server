package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.db.converter.current.StatusSchulwechselAbgangConverter;

/**
 * Diese Klasse ist einen Deserialisierer von StatusSchulwechselAbgang-Objekten.
 */
public final class StatusSchulwechselAbgangConverterDeserializer extends StdDeserializer<StatusSchulwechselAbgang> {

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public StatusSchulwechselAbgangConverterDeserializer() {
		super(StatusSchulwechselAbgang.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die StatusSchulwechselAbgang-Klasse
	 */
	public StatusSchulwechselAbgangConverterDeserializer(final Class<StatusSchulwechselAbgang> t) {
		super(t);
	}

	@Override
	public StatusSchulwechselAbgang deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return StatusSchulwechselAbgangConverter.getConverterInstance().convertToEntityAttribute(p.getText());
	}

}
