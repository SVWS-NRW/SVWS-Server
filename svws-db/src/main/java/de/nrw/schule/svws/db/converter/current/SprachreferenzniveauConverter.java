package de.nrw.schule.svws.db.converter.current;

import de.nrw.schule.svws.core.types.fach.Sprachreferenzniveau;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Sprachreferenzniveaus in Java (Value)
 * zu einer String-Darstellung in der Datenbank.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClassName}
 * registriert werden.
 */
@Converter(autoApply = true)
public class SprachreferenzniveauConverter extends DBAttributeConverter<Sprachreferenzniveau, String> {

    /** Die Instanz des Konverters */
    public final static SprachreferenzniveauConverter instance = new SprachreferenzniveauConverter();

    @Override
    public String convertToDatabaseColumn(Sprachreferenzniveau attribute) {
        return attribute == null ? null : attribute.daten.kuerzel;
    }

    @Override
    public Sprachreferenzniveau convertToEntityAttribute(String dbData) {
        return Sprachreferenzniveau.getByKuerzel(dbData);
    }

    @Override
    public Class<Sprachreferenzniveau> getResultType() {
        return Sprachreferenzniveau.class;
    }

    @Override
    public Class<String> getDBType() {
        return String.class;
    }

}
