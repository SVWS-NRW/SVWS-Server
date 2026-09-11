package de.svws_nrw.controller.uv;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf versionierte UV-Exporte.
 */
public interface UvExportController {

	/**
	 * Ermittelt den Export der Unterrichtsverteilung im Format v1.
	 *
	 * @return die Response mit dem UV-Export
	 */
	Response getExportV1();

}
