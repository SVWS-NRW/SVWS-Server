package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Nationalitaeten in Java (Value)
 * zu der String -Darstellung in der Datenbank nach der Defintion des
 * statistischen Bundesamtes (DESTATIS).
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter(autoApply = true)
public final class NationalitaetenConverter extends DBAttributeConverter<Nationalitaeten, String> {

	/** Die Instanz des Konverters */
	public static final NationalitaetenConverter instance = new NationalitaetenConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public NationalitaetenConverter() {
		// leer
	}

	@Override
	public String convertToDatabaseColumn(final Nationalitaeten attribute) {
		return (attribute == null) ? null : attribute.daten.codeDEStatis;
	}

	@Override
	public Nationalitaeten convertToEntityAttribute(final String dbData) {
		return Nationalitaeten.getByDESTATIS(dbData);
	}

	@Override
	public Class<Nationalitaeten> getResultType() {
		return Nationalitaeten.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
