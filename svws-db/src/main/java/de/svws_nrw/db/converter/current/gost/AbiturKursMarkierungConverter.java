package de.svws_nrw.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.svws_nrw.core.data.gost.AbiturKursMarkierung;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Kursmarkierungen im Abitur.
 *
 */
@Converter(autoApply = true)
public final class AbiturKursMarkierungConverter extends DBAttributeConverter<AbiturKursMarkierung, String> {

	/** Die Instanz des Konverters */
	public static final AbiturKursMarkierungConverter instance = new AbiturKursMarkierungConverter();

	@Override
	public String convertToDatabaseColumn(final AbiturKursMarkierung value) {
		if (value == null)
			return "-";
		return value.aufAbiturZeugnis ? (value.fuerBerechnung ? "+" : "-") : "/";
	}

	@Override
	public AbiturKursMarkierung convertToEntityAttribute(final String dbData) {
		if ((dbData == null) || (!"+".equals(dbData) && !"-".equals(dbData) && !"/".equals(dbData)))
			return new AbiturKursMarkierung();
		return new AbiturKursMarkierung("+".equals(dbData), !"/".equals(dbData));
	}

	@Override
	public Class<AbiturKursMarkierung> getResultType() {
		return AbiturKursMarkierung.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
