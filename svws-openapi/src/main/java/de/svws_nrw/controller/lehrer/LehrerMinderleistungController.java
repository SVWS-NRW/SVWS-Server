package de.svws_nrw.controller.lehrer;

import java.util.List;

import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungBatchPatchRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungCreateRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LehrerMinderleistungController {

	/**
	 * Ermittelt den Eintrag für Minderleistungsstunden eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Einträge für Minderleistungsstunden von Lehrern anhand der IDs.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response getList(List<Long> ids);

	/**
	 * Führt einen Patch für die Minderleistungsstunden eines Lehrers aus.
	 * Der Patch enthält die ID des Eintrages auf welchen er sich bezieht.
	 *
	 * @param patch   der Patch
	 * @param id      ID des Patches
	 *
	 * @return die Response
	 */
	Response patch(LehrerMinderleistungPatchRequest patch, Long id);

	/**
	 * Führt einen Patch für die Minderleistungsstunden von Lehrern aus.
	 * Die Patches enthalten die IDs der Einträge, auf die sie sich beziehen.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(List<LehrerMinderleistungBatchPatchRequest> patches);

	/**
	 * Erstellt Minderleistungsstunden eines Lehrers mithilfe des Patches
	 * und gibt das Ergebnis zurück.
	 *
	 * @param request   der Patch
	 *
	 * @return die Response
	 */
	Response create(LehrerMinderleistungCreateRequest request);

	/**
	 * Erstellt multiple Minderleistungsstunden eines Lehrers mithilfe der Patches
	 * und gibt die Ergebnisse zurück.
	 *
	 * @param requests   die Patches
	 *
	 * @return die Response
	 */
	Response createMultiple(List<LehrerMinderleistungCreateRequest> requests);

	/**
	 * Löscht Minderleistungsstunden und gibt den gelöschten Eintrag zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht Minderleistungsstunden mit den
	 * angegebenen IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(List<Long> ids);
}
