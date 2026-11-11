package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.oauth2.OAuthResourceType;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von {@link OAuthResourceType}.
 */
@Converter(autoApply = true)
public final class OAuthResourceTypeConverter extends DBAttributeConverter<OAuthResourceType, String> {

	@Override
	public String convertToDatabaseColumn(final OAuthResourceType type) {
		return type.getDbValue();
	}

	@Override
	public OAuthResourceType convertToEntityAttribute(final String dbData) {
		return OAuthResourceType.getByDbValue(dbData);
	}

	@Override
	public Class<OAuthResourceType> getResultType() {
		return OAuthResourceType.class;
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
	public static OAuthResourceTypeConverter getConverterInstance() {
		return DBAttributeConverter.getByClass(OAuthResourceTypeConverter.class);
	}

}
