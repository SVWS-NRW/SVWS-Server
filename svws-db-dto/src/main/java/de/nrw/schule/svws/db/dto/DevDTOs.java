package de.nrw.schule.svws.db.dto;

import java.util.HashMap;


/**
 * Diese Klasse dient als Verzeichnis aller Datenbank-DTO-Klassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DevDTOs {

    /** Enthält das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den entsprechenden Java-DTO-Klassen. */
    private static HashMap<String, Class<? extends Object>> mapDTOName2DTOClass = null;

    /** Enthält das Mapping der Datenbank-Tabellennamen zu den zugehörigen Java-DTO-Klassen für die SVWS-DB. */
    private static HashMap<String, Class<? extends Object>> mapTablename2DTOClass = null;


    /**
     * Gibt das Mapping der Datenbank-Tabellennamen zu den zugehörigen Java-DTO-Klassen für die SVWS-DB zurück.
     *
     * @return eine Hashmap mit dem Mapping
     */
     private static final HashMap<String, Class<? extends Object>> getMapDTOName2DTOClass() {
         if (mapDTOName2DTOClass == null) {
             mapDTOName2DTOClass = new HashMap<>();
         }
         return mapDTOName2DTOClass;
     }


    /**
     * Gibt die DTO-Klasse mit dem angegebenen DTO-Namen zurück.
     *
     * @param name   der DTO-Name
     *
     * @return die DTO-Klasse
     */
    public static Class<? extends Object> getFromDTOName(String name) {
    	return getMapDTOName2DTOClass().get(name);
    }


    /**
     * Gibt das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den zugehörigen
     * Java-DTO-Klassen zurück.
     *
     * @return eine Hashmap mit dem Mapping
     */
     private static final HashMap<String, Class<? extends Object>> getMapTablename2DTOClass() {
         if (mapTablename2DTOClass == null) {
             mapTablename2DTOClass = new HashMap<>();
         }
         return mapTablename2DTOClass;
     }


    /**
     * Gibt die DTO-Klasse mit dem angegebenen Tabellennamen zurück.
     *
     * @param name   der Tabellenname
     *
     * @return die DTO-Klasse
     */
    public static Class<? extends Object> getFromTableName(String name) {
    	return getMapTablename2DTOClass().get(name);
    }

}
