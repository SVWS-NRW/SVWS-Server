package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.db.converter.current.OAuthServiceDomainConverter;

/**
 * Diese Klasse ist einen Deserialisierer von {@link OAuthServiceDomain}-Objekten.
 */
public final class OAuthServiceDomainConverterDeserializer extends StdDeserializer<OAuthServiceDomain> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public OAuthServiceDomainConverterDeserializer() {
		super(OAuthServiceDomain.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die {@link OAuthServiceDomain}-Klasse
	 */
	protected OAuthServiceDomainConverterDeserializer(final Class<OAuthServiceDomain> t) {
		super(t);
	}

	@Override
	public OAuthServiceDomain deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return OAuthServiceDomainConverter.instance.convertToEntityAttribute(p.getText());
	}

}
