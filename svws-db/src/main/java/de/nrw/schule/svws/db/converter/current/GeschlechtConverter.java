package de.nrw.schule.svws.db.converter.current;

import jakarta.persistence.Converter;

import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;


/**
 * Diese Klasse dient dem Konvertieren von Geschlechtern in Java (Value)
 * zu einer Integer-Darstellung (siehe {@link Geschlecht#id}) in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClassName} 
 * registriert werden. 
 */
@Converter(autoApply = true)
public class GeschlechtConverter extends DBAttributeConverter<Geschlecht, Integer> {

	/** Die Instanz des Konverters */
	public final static GeschlechtConverter instance = new GeschlechtConverter();
	
	@Override
	public Integer convertToDatabaseColumn(Geschlecht geschlecht) {
		return geschlecht.id;
	}

	@Override
	public Geschlecht convertToEntityAttribute(Integer dbData) {
		if (dbData == null)
			return Geschlecht.X;
		return Geschlecht.fromValue(dbData);
	}

	@Override
	public Class<Geschlecht> getResultType() {
		return Geschlecht.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
