package de.svws_nrw.db.converter.current;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (Boolean)
 * zu einer Stringdarstellung in der Datenbank, bei der die - false repräsentiert
 * und die + true. Dieser Converter setzt den Defaultwertwert +.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass} 
 * registriert werden. 
 */
@Converter
public class BooleanPlusMinusDefaultPlusConverter extends DBAttributeConverter<Boolean, String> {

	/** Die Instanz des Konverters */
	public final static BooleanPlusMinusDefaultPlusConverter instance = new BooleanPlusMinusDefaultPlusConverter();
	
	@Override
	public String convertToDatabaseColumn(Boolean value) {
		return ((value == null) || value) ? "+" : "-";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return !("-".equals(dbData));
	}

	@Override
	public Class<Boolean> getResultType() {
		return Boolean.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
