package de.svws_nrw.controller.lehrer.fachrichtung;

import java.util.List;

import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungCreateRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LehrerFachrichtungController {

	/**
	 * Ermittelt alle LehrerFachrichtungen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Ermittelt alle LehrerFachrichtungen für die idLehramt
	 *
	 * @param idLehramt idLehramt
	 * @return die Response
	 */
	Response getByIdLehramt(Long idLehramt);

	/**
	 * Erstellt eine neue LehrerFachrichtung und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(LehrerFachrichtungCreateRequest request);

	/**
	 * Führt einen Patch für eine LehrerFachrichtung aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, LehrerFachrichtungPatchRequest patch);

	/**
	 * Löscht mehrere LehrerFachrichtungen anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
