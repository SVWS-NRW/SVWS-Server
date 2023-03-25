package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.db.converter.DBAttributeConverter;


/**
 * Diese Klasse dient dem Konvertieren von Geschlechtern in Java (Value)
 * zu einer String-Darstellung in der Datenbank. Dies String Darstellung stellt
 * hier aber lediglich die Zahldarstellung (siehe {@link Geschlecht#id}) 
 * als String dar.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass} 
 * registriert werden. 
 */
@Converter
public class GeschlechtConverterFromString extends DBAttributeConverter<Geschlecht, String> {

	/** Die Instanz des Konverters */
	public final static GeschlechtConverterFromString instance = new GeschlechtConverterFromString();	
	
	@Override
	public String convertToDatabaseColumn(Geschlecht geschlecht) {
		return "" + geschlecht.id;
	}

	@Override
	public Geschlecht convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return Geschlecht.X;
		try {
			return Geschlecht.fromValue(Integer.parseInt(dbData));
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
			return Geschlecht.X;
		}
	}

	@Override
	public Class<Geschlecht> getResultType() {
		return Geschlecht.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
