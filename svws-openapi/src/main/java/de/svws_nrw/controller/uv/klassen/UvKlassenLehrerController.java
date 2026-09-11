package de.svws_nrw.controller.uv.klassen;

import java.util.Collection;

import de.svws_nrw.service.uv.klassen.UvKlassenLehrerCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Klassen-Lehrer-Zuordnungen.
 */
public interface UvKlassenLehrerController {

	/**
	 * Erstellt eine neue Klassen-Lehrer-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvKlassenLehrerCreateRequest createRequest);

	/**
	 * Patcht eine Klassen-Lehrer-Zuordnung.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvKlassenLehrerPatchRequest patch);

	/**
	 * Löscht eine Klassen-Lehrer-Zuordnung.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Klassen-Lehrer-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
