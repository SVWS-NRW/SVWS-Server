package de.svws_nrw.controller.lehrer.lehrbefaehigung;

import java.util.List;

import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungCreateRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LehrerLehrbefaehigungController {

	/**
	 * Ermittelt alle Lehrbefaehigungen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Ermittelt alle Lehrbefaehigungen für die idLehramt
	 *
	 * @param idLehramt idLehramt
	 * @return die Response
	 */
	Response getByIdLehramt(Long idLehramt);

	/**
	 * Erstellt eine neue Lehrbefaehigung und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(LehrerLehrbefaehigungCreateRequest request);

	/**
	 * Führt einen Patch für eine Lehrbefaehigung aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, LehrerLehrbefaehigungPatchRequest patch);

	/**
	 * Löscht mehrere Lehrbefaehigungen anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
