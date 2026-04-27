package de.svws_nrw.controller.bk;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für die Handhabung der der API-Zugriffe im Bereich des Beruflichen Gymnasiums.
 */
public interface BKGymController {

	/**
	 * Ermittelt die für die Abiturberechnung des beruflichen Gymnasiums relevanten Daten für den Schüler mit der angegebenen ID
	 * aus den in der Datenbank gespeicherten Leistungsdaten und liefert diese als Response zurück.
	 *
	 * @param id   die ID des Schülers, für den die Abiturdaten zusammengestellt werden.
	 *
	 * @return die Response
	 */
	Response getAbiturdaten(Long id);

	/**
	 * Ermittelt die Leistungsdaten des beruflichen Gymnasiums für den Schüler mit der angegebenen ID aus der Datenbank.
	 *
	 * @param id   die ID des Schülers, für den die Abiturdaten zusammengestellt werden.
	 *
	 * @return die Response
	 */
	Response getLeistungsdaten(Long id);
}
