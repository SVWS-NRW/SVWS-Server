package de.svws_nrw.controller.lehrer.liste;

import java.util.List;

import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf die Liste der Lehrer ({@code LehrerListeEintrag}) und das Löschen mehrerer Lehrer.
 */
public interface LehrerListeController {

	/**
	 * Ermittelt die sortierte Liste aller Lehrer mit der Information, ob der Lehrer in anderen Tabellen referenziert wird.
	 *
	 * @return die Response mit der Liste der Lehrer
	 */
	Response getList();

	/**
	 * Ermittelt die sortierte Liste aller Lehrer mit der Information, ob der Lehrer im übergebenen Schuljahresabschnitt aktiv
	 * ist und ob er in anderen Tabellen referenziert wird.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Response mit der Liste der Lehrer
	 */
	Response getListBySchuljahresabschnitt(long idSchuljahresabschnitt);

	/**
	 * Löscht die Lehrer mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der zu löschenden Lehrer
	 *
	 * @return die Response mit den Ergebnissen der Löschvorgänge
	 */
	Response delete(List<Long> idsLehrer);

}
