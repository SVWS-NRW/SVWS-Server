package de.svws_nrw.controller.klassen;

import java.util.List;

import jakarta.ws.rs.core.Response;

/**
 * Controller für die lesenden Klassen-Endpunkte.
 * Enthält keine Fachlogik und delegiert ausschließlich an den Klassen-Service.
 */
public interface KlassenController {

	/**
	 * Ermittelt die Daten einer Klasse inklusive der Schüler der Klasse anhand der ID.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Daten aller Klassen eines Schuljahresabschnitts inklusive der Schüler der Klassen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Response
	 */
	Response getListBySchuljahresabschnitt(long idSchuljahresabschnitt);

	/**
	 * Ermittelt die Daten mehrerer Klassen inklusive der Schüler der Klassen anhand ihrer IDs.
	 * Die Klassen werden in der Reihenfolge der IDs geliefert.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @return die Response
	 */
	Response getList(List<Long> ids);

}
