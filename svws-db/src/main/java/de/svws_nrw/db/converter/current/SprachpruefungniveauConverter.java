package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Statuswerten der Prüfungsniveaus einer Sprachprüfung in Java (Value)
 * zu einer ID in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass} 
 * registriert werden. 
 */
@Converter(autoApply = true)
public class SprachpruefungniveauConverter extends DBAttributeConverter<Sprachpruefungniveau, Integer> {

	/** Die Instanz des Konverters */
	public final static SprachpruefungniveauConverter instance = new SprachpruefungniveauConverter();
	
	@Override
	public Integer convertToDatabaseColumn(Sprachpruefungniveau attribute) {
		return attribute.daten.id;
	}

	@Override
	public Sprachpruefungniveau convertToEntityAttribute(Integer dbData) {
		return Sprachpruefungniveau.getByID(dbData);
	}

	@Override
	public Class<Sprachpruefungniveau> getResultType() {
		return Sprachpruefungniveau.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
