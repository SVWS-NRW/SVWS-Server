package de.svws_nrw.controller.schueler.stammdaten;

import java.util.List;

import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenBatchPatchRequest;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenPatchRequest;
import jakarta.ws.rs.core.Response;

public interface SchuelerStammdatenController {

	/**
	 * Ermittelt die Stammdaten eines Schülers anhand der ID.
	 *
	 * @param id die ID des Schülers
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Stammdaten mehrerer Schüler anhand ihrer IDs.
	 *
	 * @param ids die IDs der Schüler
	 * @return die Response
	 */
	Response getList(List<Long> ids);

	/**
	 * Erstellt einen neuen Schüler und gibt dessen Stammdaten zurück.
	 *
	 * @param request das Request-Objekt mit den Importdaten
	 * @return die Response
	 */
	Response create(SchuelerImportData request);

	/**
	 * Führt einen Patch für die Stammdaten eines Schülers aus.
	 *
	 * @param id die ID des Schülers
	 * @param patch der Patch
	 * @return die Response
	 */
	Response patch(long id, SchuelerStammdatenPatchRequest patch);

	/**
	 * Führt einen Patch für die Stammdaten von Schülern aus.
	 * Die Patches enthalten die IDs der Einträge, auf die sie sich beziehen.
	 *
	 * @param patches   die Patches
	 * @return die Response
	 */
	Response patchMultiple(List<SchuelerStammdatenBatchPatchRequest> patches);

	/**
	 * Löscht mehrere Schüler anhand ihrer IDs.
	 *
	 * @param ids die IDs der zu löschenden Schüler
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
