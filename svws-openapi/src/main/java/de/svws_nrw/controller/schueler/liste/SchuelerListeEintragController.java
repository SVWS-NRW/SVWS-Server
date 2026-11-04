package de.svws_nrw.controller.schueler.liste;

import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf die Liste der Schüler ({@code SchuelerListeEintrag}).
 */
public interface SchuelerListeEintragController {

	/**
	 * Ermittelt die sortierte Liste der Schüler für den aktuellen Schuljahresabschnitt der Schule.
	 *
	 * @param nurAktive   gibt an, ob nur Schüler mit dem Status aktiv berücksichtigt werden
	 *
	 * @return die Response mit der Liste der Schüler
	 */
	Response getListeAktuell(boolean nurAktive);

	/**
	 * Ermittelt die sortierte Liste der Schüler für den übergebenen Schuljahresabschnitt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param nurAktive                gibt an, ob nur Schüler mit dem Status aktiv berücksichtigt werden
	 *
	 * @return die Response mit der Liste der Schüler
	 */
	Response getListe(long idSchuljahresabschnitt, boolean nurAktive);

}
