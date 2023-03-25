package de.svws_nrw.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.gost.AbiturBelegungsart;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Belegarten im Abitur.
 *  
 */
@Converter(autoApply = true)
public class AbiturBelegungsartConverter extends DBAttributeConverter<AbiturBelegungsart, String> {

	/** Die Instanz des Konverters */
	public final static AbiturBelegungsartConverter instance = new AbiturBelegungsartConverter();

	@Override
	public String convertToDatabaseColumn(AbiturBelegungsart value) {
		if (value == null)
			return AbiturBelegungsart.NICHT_BELEGT.toString();
		return value.toString();
	}

	@Override
	public AbiturBelegungsart convertToEntityAttribute(String dbData) {
		return AbiturBelegungsart.fromKuerzel(dbData);
	}

	@Override
	public Class<AbiturBelegungsart> getResultType() {
		return AbiturBelegungsart.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
