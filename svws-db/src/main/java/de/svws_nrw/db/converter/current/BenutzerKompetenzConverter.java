package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.converter.DBAttributeConverter;


/**
 * Diese Klasse dient dem Konvertieren von Benutzerkompetenzen.
 *
 */
@Converter(autoApply = true)
public final class BenutzerKompetenzConverter extends DBAttributeConverter<BenutzerKompetenz, Long> {

	/** Die Instanz des Konverters */
	public static final BenutzerKompetenzConverter instance = new BenutzerKompetenzConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BenutzerKompetenzConverter() {
		// leer
	}

	@Override
	public Long convertToDatabaseColumn(final BenutzerKompetenz kompetenz) {
		return (kompetenz == null) ? null : kompetenz.daten.id;
	}

	@Override
	public BenutzerKompetenz convertToEntityAttribute(final Long dbData) {
		return (dbData == null) ? null : BenutzerKompetenz.getByID(dbData);
	}

	@Override
	public Class<BenutzerKompetenz> getResultType() {
		return BenutzerKompetenz.class;
	}

	@Override
	public Class<Long> getDBType() {
		return Long.class;
	}

}
