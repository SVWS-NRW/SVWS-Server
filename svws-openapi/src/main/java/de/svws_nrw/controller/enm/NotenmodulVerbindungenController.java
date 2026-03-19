package de.svws_nrw.controller.enm;

import java.util.Collection;
import java.util.Map;

import de.svws_nrw.service.enm.NotenmodulVerbindungenCreateRequest;
import de.svws_nrw.service.enm.NotenmodulVerbindungenPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für die API-Zugriffe des Notenmoduls
 */
public interface NotenmodulVerbindungenController {

	/**
	 * Ermittelt die Notenmodul-Verbindung anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Notenmodul-Verbindungen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response getList(Collection<Long> ids);

	/**
	 * Ermittelt alle Notenmodul-Verbindungen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Führt auf der Notenmodul-Verbindung mit der angegebenen ID einen Patch aus und gibt das Ergebnis zurück.
	 *
	 * @param id      die ID
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, NotenmodulVerbindungenPatchRequest patch);

	/**
	 * Führt auf den Notenmodul-Verbindungen mit den angegebenen IDs die zugeordneten Patches aus
	 * und gibt die Ergebnisse zurück.
	 *
	 * @param patches   eine Map mit der Zuordnung der Patches zu den IDs
	 *
	 * @return die Response
	 */
	Response patchMultiple(Map<Long, NotenmodulVerbindungenPatchRequest> patches);

	/**
	 * Erstellt eine neue Notenmodul-Verbindung und gibt das Ergebnis zurück.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die Response
	 */
	Response create(NotenmodulVerbindungenCreateRequest createRequest);

	/**
	 * Erstellt neue Notenmodul-Verbindungen und gibt die Ergebnisse zurück.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<NotenmodulVerbindungenCreateRequest> createRequests);

	/**
	 * Löscht die Notenmodul-Verbindung mit der angegebenen ID und gibt die gelöschte Verbindung zurück.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht die Notenmodul-Verbindungen mit den angegebenen IDs und gibt die gelöschten Verbindungen zurück.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
