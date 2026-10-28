package de.svws_nrw.controller.lehrer.lehramt;

import java.util.List;

import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtCreateRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtPatchRequest;
import jakarta.ws.rs.core.Response;

/** LehrerLehramtController */
public interface LehrerLehramtController {

	/**
	 * Gibt alle Lehrämter eines Lehrers zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @return die Response mit den Lehrämtern des Lehrers
	 */
	Response getByIdLehrer(long idLehrer);

	/**
	 * Erstellt ein neues Lehramt und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(LehrerLehramtCreateRequest request);

	/**
	 * Führt einen Patch für ein Lehramt aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, LehrerLehramtPatchRequest patch);

	/**
	 * Löscht mehrere Lehrämter anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
