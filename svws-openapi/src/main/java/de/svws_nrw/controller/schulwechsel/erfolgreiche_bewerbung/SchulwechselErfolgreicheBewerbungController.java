package de.svws_nrw.controller.schulwechsel.erfolgreiche_bewerbung;

import java.util.List;

import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungCreateRequest;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für API-Requests zu erfolgreichen Bewerbungen.
 */
public interface SchulwechselErfolgreicheBewerbungController {

	/**
	 * Gibt einen Eintrag anhand seiner ID zurück.
	 *
	 * @param id die ID des Eintrags
	 * @return die Response
	 */
	Response getById(long id);

	/**
	 * Gibt alle Einträge zurück.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt einen neuen Eintrag.
	 *
	 * @param dto die Eingabedaten
	 * @return die Response
	 */
	Response create(SchulwechselErfolgreicheBewerbungCreateRequest dto);

	/**
	 * Aktualisiert einen bestehenden Eintrag teilweise.
	 *
	 * @param id  die ID des Eintrags
	 * @param dto die zu aktualisierenden Felder
	 * @return die Response
	 */
	Response patch(long id, SchulwechselErfolgreicheBewerbungPatchRequest dto);

	/**
	 * Löscht mehrere Einträge anhand ihrer IDs.
	 *
	 * @param ids die zu löschenden IDs
	 * @return die Response
	 */
	Response delete(List<Long> ids);

	/**
	 * Synchronisiert erfolgreiche Bewerbungen vom Schulbewerbungs-Service.
	 *
	 * @return die Response
	 */
	Response sync();

	/**
	 * Prüft wie viele neue erfolgreiche Bewerbungen vom Remote-Server verfügbar sind.
	 *
	 * @return die Response mit der Anzahl neuer Einträge
	 */
	Response syncPreview();

}
