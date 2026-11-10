package de.svws_nrw.controller.kurse;

import java.util.List;

import de.svws_nrw.service.kurse.KursCreateRequest;
import de.svws_nrw.service.kurse.KursPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf Kurse.
 */
public interface KursController {

	/**
	 * Ermittelt die Daten eines Kurses einschließlich der Schüler anhand der ID.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Kurse eines Schuljahresabschnitts. Ist der Abschnitt null, so werden die Kurse aller
	 * Abschnitte ermittelt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts oder null
	 *
	 * @return die Response
	 */
	Response getListBySchuljahresabschnitt(Long idSchuljahresabschnitt);

	/**
	 * Legt einen neuen Kurs an.
	 *
	 * @param request   die Daten des neuen Kurses
	 *
	 * @return die Response mit den Daten des angelegten Kurses
	 */
	Response create(KursCreateRequest request);

	/**
	 * Ändert die im Request gesetzten Attribute eines Kurses.
	 *
	 * @param id        die ID des Kurses
	 * @param request   die zu ändernden Attribute
	 *
	 * @return die Response mit den Daten des geänderten Kurses
	 */
	Response patch(long id, KursPatchRequest request);

	/**
	 * Löscht mehrere Kurse.
	 *
	 * @param ids   die IDs der zu löschenden Kurse
	 *
	 * @return die Response mit dem Ergebnis je Kurs
	 */
	Response delete(List<Long> ids);

}
