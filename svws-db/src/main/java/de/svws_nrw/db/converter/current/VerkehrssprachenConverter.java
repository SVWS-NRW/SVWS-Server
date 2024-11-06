package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Verkehrsprachen in Java (Value)
 * zu der String -Darstellung in der Datenbank nach der Defintion des
 * ISO 639-2-Standards.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter(autoApply = true)
public final class VerkehrssprachenConverter extends DBAttributeConverter<Verkehrssprache, String> {

	/** Die Instanz des Konverters */
	public static final VerkehrssprachenConverter instance = new VerkehrssprachenConverter();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public VerkehrssprachenConverter() {
		// leer
	}

	@Override
	public String convertToDatabaseColumn(final Verkehrssprache attribute) {
		return (attribute == null) ? null : attribute.daten.kuerzel;
	}

	@Override
	public Verkehrssprache convertToEntityAttribute(final String dbData) {
		return Verkehrssprache.getByKuerzelAuto(dbData);
	}

	@Override
	public Class<Verkehrssprache> getResultType() {
		return Verkehrssprache.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
