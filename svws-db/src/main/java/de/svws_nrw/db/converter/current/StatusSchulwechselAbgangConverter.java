package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public final class StatusSchulwechselAbgangConverter extends DBAttributeConverter<StatusSchulwechselAbgang, String> {

	@Override
	public Class<StatusSchulwechselAbgang> getResultType() {
		return StatusSchulwechselAbgang.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

	@Override
	public String convertToDatabaseColumn(final StatusSchulwechselAbgang status) {
		return status.toString();
	}

	@Override
	public StatusSchulwechselAbgang convertToEntityAttribute(final String dbStatus) {
		return StatusSchulwechselAbgang.getByBezeichnungOrNull(dbStatus);
	}

	/** @return Die Converter-Instanz, die in der Oberklasse erstellt wurde. */
	public static StatusSchulwechselAbgangConverter getConverterInstance() {
		return DBAttributeConverter.getByClass(StatusSchulwechselAbgangConverter.class);
	}
}
