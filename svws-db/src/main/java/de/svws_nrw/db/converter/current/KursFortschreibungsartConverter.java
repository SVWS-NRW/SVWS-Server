package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Kursfortschreibungsarten in der Tabelle Kurse in Java (Value)
 * zu einer Stringdarstellung in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter(autoApply = true)
public final class KursFortschreibungsartConverter extends DBAttributeConverter<KursFortschreibungsart, String> {

	/** Die Instanz des Konverters */
	public static final KursFortschreibungsartConverter instance = new KursFortschreibungsartConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KursFortschreibungsartConverter() {
		// leer
	}

	@Override
	public String convertToDatabaseColumn(final KursFortschreibungsart attribute) {
		return attribute.kuerzel;
	}

	@Override
	public KursFortschreibungsart convertToEntityAttribute(final String dbData) {
		return KursFortschreibungsart.fromKuerzel(dbData);
	}

	@Override
	public Class<KursFortschreibungsart> getResultType() {
		return KursFortschreibungsart.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
