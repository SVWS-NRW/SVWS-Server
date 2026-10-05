package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.db.converter.current.StatusSchulwechselErfolgreicheBewerbungConverter;

/**
 * Diese Klasse ist einen Serialisierer von StatusSchulwechselErfolgreicheBewerbung-Objekten.
 */
public final class StatusSchulwechselErfolgreicheBewerbungConverterSerializer extends StdSerializer<StatusSchulwechselErfolgreicheBewerbung> {

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public StatusSchulwechselErfolgreicheBewerbungConverterSerializer() {
		super(StatusSchulwechselErfolgreicheBewerbung.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die StatusSchulwechselErfolgreicheBewerbung-Klasse
	 */
	public StatusSchulwechselErfolgreicheBewerbungConverterSerializer(final Class<StatusSchulwechselErfolgreicheBewerbung> t) {
		super(t);
	}

	@Override
	public void serialize(final StatusSchulwechselErfolgreicheBewerbung value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(StatusSchulwechselErfolgreicheBewerbungConverter.getConverterInstance().convertToDatabaseColumn(value));
	}

}
