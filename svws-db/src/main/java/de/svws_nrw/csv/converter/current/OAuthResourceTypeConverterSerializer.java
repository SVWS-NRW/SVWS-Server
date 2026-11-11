package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.oauth2.OAuthResourceType;
import de.svws_nrw.db.converter.current.OAuthResourceTypeConverter;

/**
 * Diese Klasse ist ein Serialisierer von {@link OAuthResourceType}-Objekten.
 */
public final class OAuthResourceTypeConverterSerializer extends StdSerializer<OAuthResourceType> {

	private static final long serialVersionUID = 4120786439190012813L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public OAuthResourceTypeConverterSerializer() {
		super(OAuthResourceType.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die {@link OAuthResourceType}-Klasse
	 */
	public OAuthResourceTypeConverterSerializer(final Class<OAuthResourceType> t) {
		super(t);
	}

	@Override
	public void serialize(final OAuthResourceType value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(OAuthResourceTypeConverter.getConverterInstance().convertToDatabaseColumn(value));
	}

}
