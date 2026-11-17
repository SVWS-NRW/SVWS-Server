package de.svws_nrw.controller.klassen;

import java.util.List;

import de.svws_nrw.service.klassen.KlassenCreateRequest;
import de.svws_nrw.service.klassen.KlassenPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für die Klassen-Endpunkte (Lesen, Anlegen, Ändern und Löschen sowie Default-Sortierung).
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

	/**
	 * Legt eine neue Klasse an. Der Request wird mit dem BeanValidator geprüft.
	 *
	 * @param request   der Request mit den Attributen der neuen Klasse
	 *
	 * @return die Response mit den Daten der neuen Klasse
	 */
	Response create(KlassenCreateRequest request);

	/**
	 * Ändert die Attribute einer Klasse. Der Request wird mit dem BeanValidator geprüft.
	 *
	 * @param id        die ID der Klasse
	 * @param request   der Request mit den zu ändernden Attributen
	 *
	 * @return die Response mit den Daten der geänderten Klasse
	 */
	Response patch(long id, KlassenPatchRequest request);

	/**
	 * Löscht die Klassen zu den angegebenen IDs.
	 *
	 * @param ids   die IDs der zu löschenden Klassen
	 *
	 * @return die Response mit den Ergebnissen der Löschoperationen
	 */
	Response delete(List<Long> ids);

	/**
	 * Setzt für die Klassen eines Schuljahresabschnitts die Default-Sortierung.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Response ohne Inhalt
	 */
	Response setDefaultSortierung(long idSchuljahresabschnitt);

}
