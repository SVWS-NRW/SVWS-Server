package de.svws_nrw.controller.schule.logoverwaltung;

import java.util.List;

import de.svws_nrw.service.schule.logoverwaltung.LogoCreateRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LogoverwaltungController {

	/**
	 * Ruft alle Logo-Entitäten ab.
	 *
	 * @return Response mit Logo-Entitäten als Body
	 */
	Response getAll();

	/**
	 * Gibt mehrere Logos als ZIP-Archiv zurück.
	 *
	 * @param ids Liste der Logo-IDs, für die das ZIP-Archiv erstellt werden soll.
	 *
	 * @return Response mit ZIP-Archiv als Body
	 */
	Response getByIdsAsZip(List<Long> ids);

	/**
	 * Erstellt eine neue Logo-Entität.
	 *
	 * @param createRequest das Request-Objekt mit den Daten für die neue Logo-Entität
	 *
	 * @return eine Response mit der erstellten Logo-Entität
	 */
	Response create(LogoCreateRequest createRequest);

	/**
	 * Aktualisiert eine bestehende Logo-Entität teilweise.
	 *
	 * @param id die ID der zu aktualisierenden Logo-Entität
	 * @param patchRequest das Request-Objekt mit den zu aktualisierenden Feldern
	 *
	 * @return eine Response mit der aktualisierten Logo-Entität
	 */
	Response patch(long id, LogoPatchRequest patchRequest);

	/**
	 * Löscht eine Logo-Entität anhand ihrer ID.
	 *
	 * @param id die ID der zu löschenden Logo-Entität
	 *
	 * @return eine Response mit dem Löschergebnis
	 */
	Response delete(Long id);

	/**
	 * Löscht mehrere Logo-Entitäten anhand ihrer IDs.
	 *
	 * @param ids eine Liste von IDs der zu löschenden Logo-Entitäten
	 *
	 * @return eine Response mit den Löschergebnissen
	 */
	Response delete(List<Long> ids);
}
