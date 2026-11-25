package de.svws_nrw.controller.kurse;

import java.util.List;

import de.svws_nrw.service.kurse.KurslehrerCreateRequest;
import de.svws_nrw.service.kurse.KurslehrerPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf die Zusatzkräfte (Kurslehrer) von Kursen.
 */
public interface KurslehrerController {

	/**
	 * Ermittelt die Zusatzkräfte eines Kurses.
	 *
	 * @param idKurs   die ID des Kurses
	 *
	 * @return die Response mit den Zusatzkräften des Kurses
	 */
	Response getListByIdKurs(long idKurs);

	/**
	 * Legt eine Zusatzkraft bei einem Kurs an.
	 *
	 * @param idKurs    die ID des Kurses
	 * @param request   die Daten der Zusatzkraft
	 *
	 * @return die Response mit den Daten der angelegten Zusatzkraft
	 */
	Response create(long idKurs, KurslehrerCreateRequest request);

	/**
	 * Ändert die im Request gesetzten Attribute einer Zusatzkraft.
	 *
	 * @param idKurs     die ID des Kurses
	 * @param idLehrer   die ID des Lehrers
	 * @param request    die zu ändernden Attribute
	 *
	 * @return die Response mit der geänderten Zusatzkraft
	 */
	Response patch(long idKurs, long idLehrer, KurslehrerPatchRequest request);

	/**
	 * Löscht die Zusatzkräfte mit den angegebenen Lehrer-IDs bei einem Kurs.
	 *
	 * @param idKurs      die ID des Kurses
	 * @param idsLehrer   die IDs der Lehrer, deren Zusatzkraft-Eintrag gelöscht werden soll
	 *
	 * @return die Response mit dem Ergebnis je Lehrer
	 */
	Response delete(long idKurs, List<Long> idsLehrer);

}
