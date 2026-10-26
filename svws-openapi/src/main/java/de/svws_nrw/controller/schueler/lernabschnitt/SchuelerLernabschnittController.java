package de.svws_nrw.controller.schueler.lernabschnitt;

import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittBemerkungenPatchRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittCreateRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittsdatenPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für die Lernabschnittsdaten eines Schülers.
 */
public interface SchuelerLernabschnittController {

	/**
	 * Ermittelt die Schülerlernabschnittsdaten anhand der ID des Lernabschnitts.
	 *
	 * @param id   die ID des Lernabschnitts
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle Schülerlernabschnittsdaten eines Schülers in einem Schuljahresabschnitt.
	 * Existieren keine Lernabschnitte, so wird eine leere Liste zurückgegeben.
	 *
	 * @param idSchueler               die ID des Schülers
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Response
	 */
	Response getListBySchuelerAndSchuljahresabschnitt(long idSchueler, long idSchuljahresabschnitt);

	/**
	 * Legt einen neuen Schülerlernabschnitt an.
	 *
	 * @param dto   die Daten des neuen Lernabschnitts
	 *
	 * @return die Response (201 Created) mit den angelegten Schülerlernabschnittsdaten
	 */
	Response create(SchuelerLernabschnittCreateRequest dto);

	/**
	 * Passt die Schülerlernabschnittsdaten mit der angegebenen ID an.
	 *
	 * @param id    die ID des Lernabschnitts
	 * @param dto   die zu ändernden Felder
	 *
	 * @return die Response (204 No Content)
	 */
	Response patch(long id, SchuelerLernabschnittsdatenPatchRequest dto);

	/**
	 * Passt die Bemerkungen zu den Schülerlernabschnittsdaten mit der angegebenen ID an.
	 *
	 * @param id    die ID des Lernabschnitts
	 * @param dto   die zu ändernden Bemerkungen
	 *
	 * @return die Response (204 No Content)
	 */
	Response patchBemerkungen(long id, SchuelerLernabschnittBemerkungenPatchRequest dto);
}
