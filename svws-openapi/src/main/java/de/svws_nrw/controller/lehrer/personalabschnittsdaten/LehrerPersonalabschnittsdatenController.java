package de.svws_nrw.controller.lehrer.personalabschnittsdaten;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenCreateRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LehrerPersonalabschnittsdatenController {

	/**
	 * Ermittelt die Personalabschnittsdaten eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Eintrags
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Personalabschnittsdaten von Lehrern anhand der IDs.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response getList(List<Long> ids);

	/**
	 * Erstellt Personalabschnittsdaten eines Lehrers und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(LehrerPersonalabschnittsdatenCreateRequest request);

	/**
	 * Erstellt mehrere Personalabschnittsdaten eines Lehrers und gibt die erstellten Einträge zurück.
	 *
	 * @param requests   die Request-Objekte mit den Daten für die neuen Einträge
	 * @return die Response
	 */
	Response createMultiple(List<LehrerPersonalabschnittsdatenCreateRequest> requests);

	/**
	 * Führt einen Patch für die Personalabschnittsdaten eines Lehrers aus.
	 * Der Patch enthält die ID des Eintrags, auf den er sich bezieht.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, LehrerPersonalabschnittsdatenPatchRequest patch);

	/**
	 * Führt einen Patch für die Personalabschnittsdaten von Lehrern aus.
	 * Die Patches enthalten die IDs der Einträge, auf die sie sich beziehen.
	 *
	 * @param patches   die Patches
	 * @return die Response
	 */
	Response patchMultiple(List<LehrerPersonalabschnittsdatenBatchPatchRequest> patches);

	/**
	 * Löscht Personalabschnittsdaten und gibt den gelöschten Eintrag zurück.
	 *
	 * @param id   die ID des Eintrags
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Personalabschnittsdaten anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);
}
