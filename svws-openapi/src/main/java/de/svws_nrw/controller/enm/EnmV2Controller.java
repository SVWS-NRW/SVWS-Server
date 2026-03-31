package de.svws_nrw.controller.enm;

import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für die Handhabung der der API-Zugriffe im Bereich der ENM-Daten in Version 2.
 */
public interface EnmV2Controller {

	/**
	 * Aggregiert die ENM-Daten aus der SVWS-DB und gibt diese als Response zurück.
	 *
	 * @param id   eine ID, falls die ENM-Daten für einen speziellen Lehrer erstellt werden sollen,
	 *             und null, falls die ENM-Daten für die ganze Schule erstellt werden sollen
	 *
	 * @return die Response
	 */
	Response get(Long id);

	/**
	 * Aggregiert die ENM-Daten aus der SVWS-DB und gibt diese als Response mit einer GZIP-komprimierten Datei zurück.
	 *
	 * @param id   eine ID, falls die ENM-Daten für einen speziellen Lehrer erstellt werden sollen,
	 *             und null, falls die ENM-Daten für die ganze Schule erstellt werden sollen
	 *
	 * @return die Response
	 */
	Response getGZip(Long id);


	/**
	 * Integriert die Veränderungen bei den importierten ENM-Daten gegenüber dem Stand der SVWS-DB in die SVWS-DB.
	 *
	 * @param daten   die importierten Daten
	 *
	 * @return die Response
	 */
	Response applyLatest(ENMv2Daten daten);

	/**
	 * Entpackt die ENM-Daten und integriert die Veränderungen bei den importierten ENM-Daten gegenüber
	 * dem Stand der SVWS-DB in die SVWS-DB.
	 *
	 * @param daten   die importierten Daten
	 *
	 * @return die Response
	 */
	Response applyLatestGZip(byte[] daten);

}
