package de.svws_nrw.db.converter.current.statkue;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von dem Core-Type ZulaessigesFach in Java
 * zu dem ASD-Kürzel des Faches entsprechend der amtlichen Schulstatistik,
 * welches in der SVWS-Datenbank verwendet wird.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class ZulaessigesFachKuerzelASDConverter extends DBAttributeConverter<ZulaessigesFach, String> {

	/** Die Instanz des Konverters */
	public static final ZulaessigesFachKuerzelASDConverter instance = new ZulaessigesFachKuerzelASDConverter();

	@Override
	public String convertToDatabaseColumn(final ZulaessigesFach value) {
		return value.daten.kuerzelASD;
	}

	@Override
	public ZulaessigesFach convertToEntityAttribute(final String dbData) {
		return ZulaessigesFach.getByKuerzelASD(dbData);
	}

	@Override
	public Class<ZulaessigesFach> getResultType() {
		return ZulaessigesFach.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
