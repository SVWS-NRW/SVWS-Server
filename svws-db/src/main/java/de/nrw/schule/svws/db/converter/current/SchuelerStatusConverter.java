package de.nrw.schule.svws.db.converter.current;

import jakarta.persistence.Converter;

import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient dem Konvertieren von Statuswerten der Schüler in der Schueler in Java (Value)
 * zu einer Stringdarstellung in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass} 
 * registriert werden. 
 */
@Converter(autoApply = true)
public class SchuelerStatusConverter extends DBAttributeConverter<SchuelerStatus, Integer> {

	/** Die Instanz des Konverters */
	public final static SchuelerStatusConverter instance = new SchuelerStatusConverter();
	
	@Override
	public Integer convertToDatabaseColumn(SchuelerStatus attribute) {
		return attribute.id;
	}

	@Override
	public SchuelerStatus convertToEntityAttribute(Integer dbData) {
		if (dbData == null)
			return SchuelerStatus.NEUAUFNAHME;
		return SchuelerStatus.fromID(dbData);
	}

	@Override
	public Class<SchuelerStatus> getResultType() {
		return SchuelerStatus.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
