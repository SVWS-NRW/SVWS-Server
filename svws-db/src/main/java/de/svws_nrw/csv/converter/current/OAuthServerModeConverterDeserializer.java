package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.oauth2.OAuthServerMode;
import de.svws_nrw.db.converter.current.OAuthServerModeConverter;

/**
 * Diese Klasse ist ein Deserialisierer von {@link OAuthServerMode}-Objekten.
 */
public final class OAuthServerModeConverterDeserializer extends StdDeserializer<OAuthServerMode> {

	private static final long serialVersionUID = 4120786439190012812L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public OAuthServerModeConverterDeserializer() {
		super(OAuthServerMode.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die {@link OAuthServerMode}-Klasse
	 */
	protected OAuthServerModeConverterDeserializer(final Class<OAuthServerMode> t) {
		super(t);
	}

	@Override
	public OAuthServerMode deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return OAuthServerModeConverter.getConverterInstance().convertToEntityAttribute(p.getText());
	}

}
