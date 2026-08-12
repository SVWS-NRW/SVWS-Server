package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.db.converter.current.OAuthServiceDomainConverter;

/**
 * Diese Klasse ist einen Serialisierer von {@link OAuthServiceDomain}-Objekten.
 */
public final class OAuthServiceDomainConverterSerializer extends StdSerializer<OAuthServiceDomain> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public OAuthServiceDomainConverterSerializer() {
		super(OAuthServiceDomain.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die PersonTyp-Klasse
	 */
	public OAuthServiceDomainConverterSerializer(final Class<OAuthServiceDomain> t) {
		super(t);
	}

	@Override
	public void serialize(final OAuthServiceDomain value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(OAuthServiceDomainConverter.instance.convertToDatabaseColumn(value));
	}

}
