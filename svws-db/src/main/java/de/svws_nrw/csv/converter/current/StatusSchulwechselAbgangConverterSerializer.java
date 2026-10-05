package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.db.converter.current.StatusSchulwechselAbgangConverter;

/**
 * Diese Klasse ist einen Serialisierer von StatusSchulwechselAbgang-Objekten.
 */
public final class StatusSchulwechselAbgangConverterSerializer extends StdSerializer<StatusSchulwechselAbgang> {

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public StatusSchulwechselAbgangConverterSerializer() {
		super(StatusSchulwechselAbgang.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die StatusSchulwechselAbgang-Klasse
	 */
	public StatusSchulwechselAbgangConverterSerializer(final Class<StatusSchulwechselAbgang> t) {
		super(t);
	}

	@Override
	public void serialize(final StatusSchulwechselAbgang value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(StatusSchulwechselAbgangConverter.getConverterInstance().convertToDatabaseColumn(value));
	}

}
