package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für das Erstellen von UV-Lerngruppen aus UV-Klassen.
 */
public interface UvLerngruppeKlassenImportController {

	/**
	 * Erstellt Lerngruppen für die angegebenen Klassen anhand der Stundentafeln.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die Response
	 */
	Response createByKlassen(Collection<Long> idsKlassen);

}
