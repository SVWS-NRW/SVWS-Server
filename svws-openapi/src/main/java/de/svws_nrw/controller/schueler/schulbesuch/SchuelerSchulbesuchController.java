package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchPatchRequest;
import jakarta.ws.rs.core.Response;

public interface SchuelerSchulbesuchController {

	/**
	 * Ermittelt die Schulbesuch-Daten eines Schülers anhand der Schüler-ID.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @return die Response
	 */
	Response getByIdSchueler(long idSchueler);

	/**
	 * Führt einen Patch für eine Schulbesuch-Entität aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, SchuelerSchulbesuchPatchRequest patch);
}
