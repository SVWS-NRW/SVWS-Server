package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchienePK;
import de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Lerngruppen-Schienen-Zuordnungen.
 */
public interface UvLerngruppenSchieneController {

	/**
	 * Erstellt eine neue Lerngruppen-Schienen-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvLerngruppenSchieneCreateRequest createRequest);

	/**
	 * Löscht eine Lerngruppen-Schienen-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Response
	 */
	Response delete(DTOUvLerngruppeSchienePK id);

	/**
	 * Löscht mehrere Lerngruppen-Schienen-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<DTOUvLerngruppeSchienePK> ids);

}
