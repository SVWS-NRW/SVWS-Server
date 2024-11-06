package de.svws_nrw.db.dto;

import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse dient als Hilfsklasse zum Zugriff auf die Datenbank-DTO-Klassen unterschiedlicher Revisionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOHelper {

    /**
     * Leerer privater Konstruktor, da diese Klasse nicht zur Instantiierung vorgesehen ist.
     */
    private DTOHelper() {
        throw new UnsupportedOperationException("Instantiation not allowed.");
    }


    /**
     * Gibt die DTO-Klasse mit dem angegebenen DTO-Namen zurück.
     *
     * @param name   der DTO-Name
     * @param rev    die Datenbank-Revision für welche die DTO benötigt werden
     *
     * @return die DTO-Klasse
     */
    public static Class<? extends Object> getFromDTOName(final String name, final long rev) {
        if (rev == 0) {
            return MigrationDTOs.getFromDTOName(name);
        } else if ((rev < 0) || (rev <= SchemaRevisionen.maxRevision.revision)) {
            return DTOs.getFromDTOName(name);
        } else if (rev <= SchemaRevisionen.maxDeveloperRevision.revision) {
            return DevDTOs.getFromDTOName(name);
        } else {
            return null;
        }
    }


    /**
     * Gibt die DTO-Klasse mit dem angegebenen Tabellennamen zurück.
     *
     * @param name   der Tabellenname
     * @param rev    die Datenbank-Revision für welche die DTO benötigt werden
     *
     * @return die DTO-Klasse
     */
    public static Class<? extends Object> getFromTableName(final String name, final long rev) {
        if (rev == 0) {
            return MigrationDTOs.getFromTableName(name);
        } else if ((rev < 0) || (rev <= SchemaRevisionen.maxRevision.revision)) {
            return DTOs.getFromTableName(name);
        } else if (rev <= SchemaRevisionen.maxDeveloperRevision.revision) {
            return DevDTOs.getFromTableName(name);
        } else {
            return null;
        }
    }

}
