package de.svws_nrw.data;

import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Dieses Interface definiert das Format für Mapping-Funktionen, die
 * einen JSON-Patch auf ein DTO anwenden.
 *
 * @param <DTO> der Typ des DTO-Objektes
 */
@FunctionalInterface
public interface DataBasicMapper<DTO> {

	/**
	 * Führt ein Mapping von dem JSON-Attributwert value
	 * auf das übergebenene DTO-Objekt aus. Für den Zugriff
	 * auf weitere Attribute des JSON-Objektes wird die Map
	 * mit den Attribute als dritter Parameter übergeben.
	 *
	 * @param conn    die aktuelle Datenbank-Verbindung
	 * @param t       das DTO
	 * @param value   der zu mappende Attributwert des JSON-Objektes
	 * @param map     die Map mit den JSON-Attributwerten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
    void map(DBEntityManager conn, DTO t, Object value, Map<String, Object> map) throws ApiOperationException;

}
