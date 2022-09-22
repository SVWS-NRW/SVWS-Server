package de.nrw.schule.svws.db.converter.current.gost;

import jakarta.persistence.Converter;
import de.nrw.schule.svws.core.data.gost.AbiturKursMarkierung;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Kursmarkierungen im Abitur.
 *  
 */
@Converter(autoApply = true)
public class AbiturKursMarkierungConverter extends DBAttributeConverter<AbiturKursMarkierung, String> {

	/** Die Instanz des Konverters */
	public final static AbiturKursMarkierungConverter instance = new AbiturKursMarkierungConverter();

	@Override
	public String convertToDatabaseColumn(AbiturKursMarkierung value) {
		if (value == null)
			return "-";
		return value.aufAbiturZeugnis ? (value.fuerBerechnung ? "+" : "-") : "/";
	}

	@Override
	public AbiturKursMarkierung convertToEntityAttribute(String dbData) {
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
