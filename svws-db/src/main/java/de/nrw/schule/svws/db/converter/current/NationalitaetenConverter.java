package de.nrw.schule.svws.db.converter.current;

import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
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
public class NationalitaetenConverter extends DBAttributeConverter<Nationalitaeten, String> {

    /** Die Instanz des Konverters */
    public final static NationalitaetenConverter instance = new NationalitaetenConverter();

    @Override
    public String convertToDatabaseColumn(Nationalitaeten attribute) {
        return attribute == null ? null : attribute.daten.codeDEStatis;
    }

    @Override
    public Nationalitaeten convertToEntityAttribute(String dbData) {
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
