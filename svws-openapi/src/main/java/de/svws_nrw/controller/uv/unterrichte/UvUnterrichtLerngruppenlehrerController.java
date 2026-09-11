package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrerPK;
import de.svws_nrw.service.uv.lerngruppen.UvUnterrichtLerngruppenlehrerCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Unterricht-Lerngruppenlehrer-Zuordnungen.
 */
public interface UvUnterrichtLerngruppenlehrerController {

	/**
	 * Erstellt eine neue Unterricht-Lerngruppenlehrer-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvUnterrichtLerngruppenlehrerCreateRequest createRequest);

	/**
	 * Löscht eine Unterricht-Lerngruppenlehrer-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Response
	 */
	Response delete(DTOUvUnterrichteLerngruppenlehrerPK id);

	/**
	 * Löscht mehrere Unterricht-Lerngruppenlehrer-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<DTOUvUnterrichteLerngruppenlehrerPK> ids);

}
