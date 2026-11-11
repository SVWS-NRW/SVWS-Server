package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.oauth2.OAuthResourceType;
import de.svws_nrw.db.converter.current.OAuthResourceTypeConverter;

/**
 * Diese Klasse ist ein Deserialisierer von {@link OAuthResourceType}-Objekten.
 */
public final class OAuthResourceTypeConverterDeserializer extends StdDeserializer<OAuthResourceType> {

	private static final long serialVersionUID = 4120786439190012814L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public OAuthResourceTypeConverterDeserializer() {
		super(OAuthResourceType.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die {@link OAuthResourceType}-Klasse
	 */
	protected OAuthResourceTypeConverterDeserializer(final Class<OAuthResourceType> t) {
		super(t);
	}

	@Override
	public OAuthResourceType deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return OAuthResourceTypeConverter.getConverterInstance().convertToEntityAttribute(p.getText());
	}

}
