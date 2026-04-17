package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;


/**
 * Diese Klasse dient dem Konvertieren der Kennung für Logos.
 */
@Converter(autoApply = true)
public final class ReportingBildDefinitionConverter extends DBAttributeConverter<ReportingBildDefinition, String> {

	/** Die Instanz des Konverters */
	public static final ReportingBildDefinitionConverter instance = new ReportingBildDefinitionConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ReportingBildDefinitionConverter() {
		// leer
	}

	@Override
	public String convertToDatabaseColumn(final ReportingBildDefinition bildDef) {
		return bildDef.getKennung();
	}

	@Override
	public ReportingBildDefinition convertToEntityAttribute(final String dbData) {
		return ReportingBildDefinition.getByKennung(dbData);
	}

	@Override
	public Class<ReportingBildDefinition> getResultType() {
		return ReportingBildDefinition.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
