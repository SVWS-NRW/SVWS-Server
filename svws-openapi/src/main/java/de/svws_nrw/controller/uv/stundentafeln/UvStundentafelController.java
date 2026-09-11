package de.svws_nrw.controller.uv.stundentafeln;

import java.util.Collection;

import de.svws_nrw.service.uv.stundentafeln.UvStundentafelCreateRequest;
import de.svws_nrw.core.data.uv.UvStundentafelImportOptions;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Stundentafeln.
 */
public interface UvStundentafelController {

	/**
	 * Ermittelt eine UV-Stundentafel anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle UV-Stundentafeln.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue UV-Stundentafel.
	 *
	 * @param createRequest   die Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(UvStundentafelCreateRequest createRequest);

	/**
	 * Importiert eine Stundentafel aus Schüler-Leistungsdaten.
	 *
	 * @param importRequest  die Importdaten
	 *
	 * @return die Response
	 */
	Response importiere(UvStundentafelImportOptions importRequest);

	/**
	 * Erstellt mehrere neue UV-Stundentafeln.
	 *
	 * @param createRequests   die Daten für die neuen Einträge
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvStundentafelCreateRequest> createRequests);

	/**
	 * Führt auf der UV-Stundentafel einen Patch aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvStundentafelPatchRequest patch);

	/**
	 * Führt auf mehreren UV-Stundentafeln Patches aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvStundentafelPatchRequest> patches);

	/**
	 * Löscht die UV-Stundentafel mit der angegebenen ID.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere UV-Stundentafeln anhand ihrer IDs.
	 *
	 * @param ids   die IDs der zu löschenden Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
