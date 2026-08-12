package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren von {@link OAuthServiceDomain}
 */
@Converter(autoApply = true)
public final class OAuthServiceDomainConverter extends DBAttributeConverter<OAuthServiceDomain, String> {

	/** Die Instanz des Konverters */
	public static final OAuthServiceDomainConverter instance = new OAuthServiceDomainConverter();


	@Override
	public String convertToDatabaseColumn(final OAuthServiceDomain typ) {
		return typ.getDbValue();
	}

	@Override
	public OAuthServiceDomain convertToEntityAttribute(final String dbData) {
		return OAuthServiceDomain.getByDbValue(dbData);
	}

	@Override
	public Class<OAuthServiceDomain> getResultType() {
		return OAuthServiceDomain.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
