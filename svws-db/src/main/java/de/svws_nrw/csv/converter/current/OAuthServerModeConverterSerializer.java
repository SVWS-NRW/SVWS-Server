package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.oauth2.OAuthServerMode;
import de.svws_nrw.db.converter.current.OAuthServerModeConverter;

/**
 * Diese Klasse ist ein Serialisierer von {@link OAuthServerMode}-Objekten.
 */
public final class OAuthServerModeConverterSerializer extends StdSerializer<OAuthServerMode> {

	private static final long serialVersionUID = 4120786439190012811L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public OAuthServerModeConverterSerializer() {
		super(OAuthServerMode.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die {@link OAuthServerMode}-Klasse
	 */
	public OAuthServerModeConverterSerializer(final Class<OAuthServerMode> t) {
		super(t);
	}

	@Override
	public void serialize(final OAuthServerMode value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(OAuthServerModeConverter.getConverterInstance().convertToDatabaseColumn(value));
	}

}
