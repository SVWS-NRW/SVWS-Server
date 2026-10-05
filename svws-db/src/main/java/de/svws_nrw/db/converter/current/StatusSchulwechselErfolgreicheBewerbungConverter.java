package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public final class StatusSchulwechselErfolgreicheBewerbungConverter extends DBAttributeConverter<StatusSchulwechselErfolgreicheBewerbung, String> {

	@Override
	public Class<StatusSchulwechselErfolgreicheBewerbung> getResultType() {
		return StatusSchulwechselErfolgreicheBewerbung.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

	@Override
	public String convertToDatabaseColumn(final StatusSchulwechselErfolgreicheBewerbung status) {
		return status.getBezeichnung();
	}

	@Override
	public StatusSchulwechselErfolgreicheBewerbung convertToEntityAttribute(final String dbStatus) {
		return StatusSchulwechselErfolgreicheBewerbung.getByBezeichnungOrNull(dbStatus);
	}

	/** @return Die Converter-Instanz, die in der Oberklasse erstellt wurde. */
	public static StatusSchulwechselErfolgreicheBewerbungConverter getConverterInstance() {
		return DBAttributeConverter.getByClass(StatusSchulwechselErfolgreicheBewerbungConverter.class);
	}
}
