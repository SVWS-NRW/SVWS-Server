package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.oauth2.OAuthServerMode;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von {@link OAuthServerMode}.
 */
@Converter(autoApply = true)
public final class OAuthServerModeConverter extends DBAttributeConverter<OAuthServerMode, String> {

	@Override
	public String convertToDatabaseColumn(final OAuthServerMode mode) {
		return mode.getDbValue();
	}

	@Override
	public OAuthServerMode convertToEntityAttribute(final String dbData) {
		return OAuthServerMode.getByDbValue(dbData);
	}

	@Override
	public Class<OAuthServerMode> getResultType() {
		return OAuthServerMode.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

	/**
	 * Gibt die in der Oberklasse registrierte Converter-Instanz zurück.
	 *
	 * @return die Converter-Instanz
	 */
	public static OAuthServerModeConverter getConverterInstance() {
		return DBAttributeConverter.getByClass(OAuthServerModeConverter.class);
	}

}
