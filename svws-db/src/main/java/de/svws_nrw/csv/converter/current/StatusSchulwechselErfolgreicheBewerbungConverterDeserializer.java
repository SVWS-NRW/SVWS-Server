package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.db.converter.current.StatusSchulwechselErfolgreicheBewerbungConverter;

/**
 * Diese Klasse ist einen Deserialisierer von StatusSchulwechselErfolgreicheBewerbung-Objekten.
 */
public final class StatusSchulwechselErfolgreicheBewerbungConverterDeserializer extends StdDeserializer<StatusSchulwechselErfolgreicheBewerbung> {

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public StatusSchulwechselErfolgreicheBewerbungConverterDeserializer() {
		super(StatusSchulwechselErfolgreicheBewerbung.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die StatusSchulwechselErfolgreicheBewerbung-Klasse
	 */
	public StatusSchulwechselErfolgreicheBewerbungConverterDeserializer(final Class<StatusSchulwechselErfolgreicheBewerbung> t) {
		super(t);
	}

	@Override
	public StatusSchulwechselErfolgreicheBewerbung deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return StatusSchulwechselErfolgreicheBewerbungConverter.getConverterInstance().convertToEntityAttribute(p.getText());
	}

}
