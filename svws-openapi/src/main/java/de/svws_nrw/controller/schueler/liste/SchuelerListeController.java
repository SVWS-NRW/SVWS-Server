package de.svws_nrw.controller.schueler.liste;

import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf die Daten der Schüler-Auswahlliste ({@code SchuelerListe}).
 */
public interface SchuelerListeController {

	/**
	 * Ermittelt die Daten der Schüler-Auswahlliste für den übergebenen Schuljahresabschnitt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Response mit den Daten der Schüler-Auswahlliste
	 */
	Response getBySchuljahresabschnitt(long idSchuljahresabschnitt);

}
