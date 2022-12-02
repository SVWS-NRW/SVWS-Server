package de.nrw.schule.svws.db.utils;

import java.util.HashMap;
import java.util.List;

import de.nrw.schule.svws.base.CsvReader;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.db.dto.DTOHelper;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;

/**
 * Diese Klasse stellt den Zugriff auf alle Default-Daten aus der CSV-Schema-Definition
 * zur Verfügung.
 */
public class DBDefaultData {

	/** Der Cache mit allen Default-Daten der Revision 0 der SVWS-DB (für die Migration) */
	private static final HashMap<Class<?>, List<?>> cacheRev0 = new HashMap<>();
	
	/** Der Cache mit allen Default-Daten der aktuellen Revision der SVWS-DB */
	private static final HashMap<Class<?>, List<?>> cache = new HashMap<>();

	/** Der Cache mit allen Default-Daten der aktuellen Entwickler-Revision der SVWS-DB */
	private static final HashMap<Class<?>, List<?>> cacheDev = new HashMap<>();

	
	/**
	 * Gibt den Dateinamen zurück, wo sich die CSV-Datei mit den Default-Daten 
	 * für die angegebene Tabelle befindet.
	 * 
	 * @param tab   die Tabelle
	 * 
	 * @return der Dateiname
	 */
	public static String getFileName(SchemaTabelle tab) {
	    return switch (tab.name()) {
            default -> "daten/csv/" + tab.javaSubPackage().replace(".", "/") + "/" + tab.name() + ".csv";
        };
    }

	/**
	 * Lädt den Cache mit den Default-Daten erneut aus den CSV-Dateien
	 * 
	 * @param revision   die Revision, für welche der Cache erstellt werde soll
	 * @param logger     ein Logger, um das befüllen des Caches zu dokumentieren
	 *  
	 * @return true, falls der Cache erfolgreich geladen wurde 
	 */
	private static boolean reload(long revision, Logger logger, HashMap<Class<?>, List<?>> cache) {
		if (cache.size() > 0) {
			logger.log("Leere bestehenden Default-Daten-Cache...");
			cache.clear();
		}
		logger.logLn("Fülle Default-Daten-Cache...");
		logger.modifyIndent(2);
		for (SchemaTabelle tab : Schema.tabellenDefaultDaten) {
			logger.log("Lese Daten für die Tabelle " + tab.javaSubPackage() + "." + tab.name() + "... ");
			Class<?> dtoClass = DTOHelper.getFromTableName(tab.name(), -1);
			if (dtoClass == null) {
				logger.logLn(0, "In dieser DB-Revision nicht vorhanden.");
				continue;
			}
	        var data = CsvReader.fromResource(getFileName(tab), dtoClass);
	        if (data != null) {
	        	cache.put(dtoClass, data);
	        	logger.logLn(0, "OK");
	        } else {
	        	logger.logLn(0, "FEHLER");
		        System.gc();
		        return false;
	        }
	        data = null;
	        System.gc();
		}
        logger.modifyIndent(-2);
		return true;
	}
	
	
	/**
	 * Lädt den Cache mit den Default-Daten erneut aus den CSV-Dateien
	 * 
	 * @param logger     ein Logger, um das befüllen des Caches zu dokumentieren
	 *  
	 * @return true, falls der Cache erfolgreich geladen wurde 
	 */
	public static boolean reload(Logger logger) {
		return reload(-1, logger, cache);
	}
	
	
	/**
	 * Gibt alle Default-Daten des übergebenen Entity-Typs der Revision 0 der SVWS-DB zurück, sofern 
	 * dieser gültig ist.
	 *   
	 * @param <T>        der Typ der angeforderten Default-Daten
	 * @param clazz      die Klasse der angeforderten Default-Daten (siehe {@link Class})
	 * 
	 * @return eine Liste mit allen Default-Daten des übergebenen Entity-Typs
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getRev0(Class<T> clazz) {
		if (cacheRev0.size() == 0)
			reload(0, new Logger(), cacheRev0);
		return (List<T>)cacheRev0.get(clazz);			
	}
	
	
	/**
	 * Gibt alle Default-Daten des übergebenen Entity-Typs der aktuelle DB-Revision zurück, sofern 
	 * dieser gültig ist.
	 *   
	 * @param <T>        der Typ der angeforderten Default-Daten
	 * @param clazz      die Klasse der angeforderten Default-Daten (siehe {@link Class})
	 * 
	 * @return eine Liste mit allen Default-Daten des übergebenen Entity-Typs
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> get(Class<T> clazz) {
		if (cache.size() == 0)
			reload(new Logger());
		return (List<T>)cache.get(clazz);
	}
	

	/**
	 * Gibt alle Default-Daten des übergebenen Entity-Typs der aktuellen Entwickler-DB-Revision zurück, sofern 
	 * dieser gültig ist.
	 *   
	 * @param <T>        der Typ der angeforderten Default-Daten
	 * @param clazz      die Klasse der angeforderten Default-Daten (siehe {@link Class})
	 * 
	 * @return eine Liste mit allen Default-Daten des übergebenen Entity-Typs
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getDev(Class<T> clazz) {
		if (cacheDev.size() == 0)
			reload(SchemaRevisionen.maxDeveloperRevision.revision, new Logger(), cacheDev);
		return (List<T>)cacheDev.get(clazz);			
	}
	
	
}
