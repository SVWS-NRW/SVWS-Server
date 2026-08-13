package de.svws_nrw.controller.lehrer.funktion;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.service.lehrer.funktion.LehrerFunktionBatchPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LehrerFunktionController {

	/**
	 * Ermittelt eine Lehrerfunktion anhand der ID.
	 *
	 * @param id   die ID des Eintrags
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle Lehrerfunktionen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Ermittelt alle Lehrerfunktionen zu einem Lehrerabschnitt anhand der ID des Abschnitts.
	 *
	 * @param idAbschnitt   die ID der Lehrerabschnittsdaten
	 * @return die Response
	 */
	Response getListByIdAbschnitt(long idAbschnitt);

	/**
	 * Erstellt eine Lehrerfunktion und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(LehrerFunktionCreateRequest request);

	/**
	 * Erstellt mehrere Lehrerfunktionen und gibt die erstellten Einträge zurück.
	 *
	 * @param requests   die Request-Objekte mit den Daten für die neuen Einträge
	 * @return die Response
	 */
	Response createMultiple(List<LehrerFunktionCreateRequest> requests);

	/**
	 * Führt einen Patch für eine Lehrerfunktion aus.
	 * Der Patch enthält die ID des Eintrags, auf den er sich bezieht.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, LehrerFunktionPatchRequest patch);

	/**
	 * Führt einen Patch für mehrere Lehrerfunktionen aus.
	 * Die Patches enthalten die IDs der Einträge, auf die sie sich beziehen.
	 *
	 * @param patches   die Patches
	 * @return die Response
	 */
	Response patchMultiple(List<LehrerFunktionBatchPatchRequest> patches);

	/**
	 * Löscht eine Lehrerfunktion und gibt den gelöschten Eintrag zurück.
	 *
	 * @param id   die ID des Eintrags
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Lehrerfunktionen anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);
}
