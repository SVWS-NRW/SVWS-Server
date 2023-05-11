package de.svws_nrw.data;

import java.util.Map;

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
	 * @param t       das DTO
	 * @param value   der zu mappende Attributwert des JSON-Objektes
	 * @param map     die Map mit den JSON-Attributwerten
	 */
    void map(DTO t, Object value, Map<String, Object> map);

}
