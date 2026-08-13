package de.svws_nrw.controller.schule.katalog.fachklasse;

import java.util.List;

import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragCreateRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragPatchRequest;
import jakarta.ws.rs.core.Response;

public interface FachklasseController {

	/**
	 * Ermittelt alle Fachklassen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Fachklasse und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(FachklasseEintragCreateRequest request);

	/**
	 * Führt einen Patch für eine Fachklasse aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, FachklasseEintragPatchRequest patch);

	/**
	 * Löscht mehrere Fachklassen anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
