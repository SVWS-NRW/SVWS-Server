package de.svws_nrw.controller.schulwechsel.abgang;

import java.util.List;

import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangCreateRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für API-Requests zu Wechselvorgängen (Abgang)
 */
public interface SchulwechselAbgangController {

	/**
	 * Ermittelt den Eintrag für allgemeine Anrechnungsstunden eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response getById(long id);

	/**
	 * Ermittelt alle Einträge für Wechselvorgänge (Abgang).
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Führt einen Patch für den Wechselvorgang (Abgang) aus.
	 * Der Patch enthält die Id des Eintrages auf welchen er sich bezieht.
	 *
	 * @param id 	die Id des zu patchenden Wechselvorgangs
	 * @param patch der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, SchulwechselAbgangPatchRequest patch);

	/**
	 * Erstellt einen neuen Eintrag für allgemeine Anrechnungsstunden eines Lehrers mithilfe des Patches
	 * und gibt das Ergebnis zurück.
	 *
	 * @param patch der Patch
	 *
	 * @return die Response
	 */
	Response create(SchulwechselAbgangCreateRequest patch);

	/**
	 * Löscht den Eintrag für allgemeine Anrechnungsstunden eines Lehrers mit der
	 * angegebenen ID und gibt den gelöschten Eintrag zurück.
	 *
	 * @param ids die Id des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
