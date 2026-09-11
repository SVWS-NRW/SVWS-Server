package de.svws_nrw.controller.uv;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Grunddaten.
 */
public interface UvGrunddatenBundleController {

	/**
	 * Ermittelt die UV-Grunddaten (Räume, Raumgruppen, Stundentafeln, Fächer, Lehrer,
	 * Lehrer-Unterrichtsfächer, Zeitraster, Anrechnungsstunden und Pflichtstundensoll).
	 *
	 * @return die Response mit den UV-Grunddaten
	 */
	Response getGrunddatenBundle();

}
