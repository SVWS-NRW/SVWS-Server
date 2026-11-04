package de.svws_nrw.controller.schule.teilstandort;

import java.util.List;

import de.svws_nrw.service.schule.teilstandort.TeilstandortCreateRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortPatchRequest;
import jakarta.ws.rs.core.Response;

public interface TeilstandortController {

	/**
	 *
	 * Ermittelt alle Teilstandorte
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt einen neuen Teilstandort und gibt den erstellten Eintrag zurück
	 *
	 * @param request das Request-Objekt mit den Daten für den neuen Teilstandort
	 *
	 * @return die Response
	 */
	Response create(TeilstandortCreateRequest request);

	/**
	 * Führt einen Patch für einen Teilstandort aus.
	 *
	 * @param id die ID des Eintrags
	 * @param patch der Patch
	 * @return die Response
	 */
	Response patch(String id, TeilstandortPatchRequest patch);

	/**
	 * Löscht mehrere Teilstandorte anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<String> ids);
}
