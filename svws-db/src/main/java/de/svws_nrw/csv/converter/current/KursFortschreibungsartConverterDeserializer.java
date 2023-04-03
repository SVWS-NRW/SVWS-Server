package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.db.converter.current.KursFortschreibungsartConverter;

/**
 * Diese Klasse ist ein Deserialisierer für die Kurs-Fortschriebungsart. Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link KursFortschreibungsart}.
 */
public final class KursFortschreibungsartConverterDeserializer extends StdDeserializer<KursFortschreibungsart> {

	private static final long serialVersionUID = 1871702485070815240L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public KursFortschreibungsartConverterDeserializer() {
		super(KursFortschreibungsart.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected KursFortschreibungsartConverterDeserializer(final Class<KursFortschreibungsart> t) {
		super(t);
	}

	@Override
	public KursFortschreibungsart deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return KursFortschreibungsartConverter.instance.convertToEntityAttribute(p.getText());
	}

}
