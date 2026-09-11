package de.svws_nrw.controller.uv.schueler;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchuelerPK;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Schülergruppe-Schüler-Zuordnungen.
 */
public interface UvSchuelergruppeSchuelerController {

	/**
	 * Erstellt eine neue Schülergruppe-Schüler-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvSchuelergruppeSchuelerCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue Schülergruppe-Schüler-Zuordnungen.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvSchuelergruppeSchuelerCreateRequest> createRequests);

	/**
	 * Löscht eine Schülergruppe-Schüler-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Response
	 */
	Response delete(DTOUvSchuelergruppeSchuelerPK id);

	/**
	 * Löscht mehrere Schülergruppe-Schüler-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<DTOUvSchuelergruppeSchuelerPK> ids);

}
