package de.nrw.schule.svws.db.utils;

import java.util.HashMap;
import java.util.List;

import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.db.dto.DTOHelper;
import de.nrw.schule.svws.db.schema.DBSchemaDefinition;
import de.nrw.schule.svws.db.schema.csv.Tabelle;
import de.nrw.schule.svws.logger.Logger;

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
	 * Lädt den Cache mit den Default-Daten erneut aus den CSV-Dateien
	 * 
	 * @param revision   die Revision, für welche der Cache erstellt werde soll
	 * @param logger     ein Logger, um das befüllen des Caches zu dokumentieren
	 *  
	 * @return true, falls der Cache erfolgreich geladen wurde 
	 */
	private static boolean reload(int revision, Logger logger, HashMap<Class<?>, List<?>> cache) {
		if (cache.size() > 0) {
			logger.log("Leere bestehenden Default-Daten-Cache...");
			cache.clear();
		}
		logger.logLn("Fülle Default-Daten-Cache...");
		logger.modifyIndent(2);
		for (Tabelle tab : DBSchemaDefinition.getInstance().getTabellenDefaultDatenSortiert(-1)) {
			logger.log("Lese Daten für die Tabelle " + tab.JavaPackage + "." + tab.Name + "... ");
			Class<?> dtoClass = DTOHelper.getFromTableName(tab.Name, -1);
// TODO ungültige Tabellen sollten von getTabellenDefaultDatenSortiert nicht geliefert werden !!!  			
			if (dtoClass == null) {
				logger.logLn(0, "In dieser DB-Revision nicht vorhanden.");
				continue;
			}
	        var data = CsvReader.fromResource("schema/csv/" + tab.JavaPackage.replace(".", "/") + "/" + tab.Name + ".csv", dtoClass);
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
			reload(DBSchemaDefinition.getInstance().maxRevisionDeveloper, new Logger(), cacheDev);
		return (List<T>)cacheDev.get(clazz);			
	}
	
	
}
