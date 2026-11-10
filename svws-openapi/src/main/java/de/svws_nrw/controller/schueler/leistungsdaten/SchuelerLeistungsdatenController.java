package de.svws_nrw.controller.schueler.leistungsdaten;

import java.util.List;

import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenCreateRequest;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für die Leistungsdaten eines Schülers.
 */
public interface SchuelerLeistungsdatenController {

	/**
	 * Ermittelt die Schülerleistungsdaten anhand ihrer ID.
	 *
	 * @param id   die ID der Leistungsdaten
	 *
	 * @return die Response (200 OK) mit den Leistungsdaten
	 */
	Response get(long id);

	/**
	 * Legt neue Schülerleistungsdaten an.
	 *
	 * @param dto   die Daten der neuen Leistungsdaten
	 *
	 * @return die Response (201 Created) mit den angelegten Leistungsdaten
	 */
	Response create(SchuelerLeistungsdatenCreateRequest dto);

	/**
	 * Passt die Schülerleistungsdaten mit der angegebenen ID an.
	 *
	 * @param id    die ID der Leistungsdaten
	 * @param dto   die zu ändernden Felder
	 *
	 * @return die Response (200 OK) mit den aktualisierten Leistungsdaten
	 */
	Response patch(long id, SchuelerLeistungsdatenPatchRequest dto);

	/**
	 * Löscht die Schülerleistungsdaten mit den angegebenen IDs.
	 *
	 * @param ids   die IDs der zu löschenden Leistungsdaten
	 *
	 * @return die Response (200 OK) mit dem Ergebnis der einzelnen Löschvorgänge
	 */
	Response deleteMultiple(List<Long> ids);

}
