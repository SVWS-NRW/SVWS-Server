package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaumPK;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtRaumCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Unterricht-Raum-Zuordnungen.
 */
public interface UvUnterrichtRaumController {

	/**
	 * Erstellt eine neue Unterricht-Raum-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvUnterrichtRaumCreateRequest createRequest);

	/**
	 * Löscht eine Unterricht-Raum-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Response
	 */
	Response delete(DTOUvUnterrichtRaumPK id);

	/**
	 * Löscht mehrere Unterricht-Raum-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<DTOUvUnterrichtRaumPK> ids);

}
