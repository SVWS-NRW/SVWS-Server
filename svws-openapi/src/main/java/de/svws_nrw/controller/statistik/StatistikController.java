package de.svws_nrw.controller.statistik;

import de.svws_nrw.service.statistik.ZusatzerhebungenStatistikPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für die Handhabung der der API-Zugriffe im Bereich der amtlichen Schulstatistik.
 */
public interface StatistikController {

	/**
	 * Aggregiert die Statistik-Relevanten Daten aus der SVWS-DB und gibt diese
	 * als Response zurück. Dabei wird zunächst geprüft, ob der Benutzer die
	 * notwendigen Berechtigungen hat.
	 *
	 * @return die Response
	 */
	Response getStatistikGesamt();

	/**
	 * Aggregiert die Statistik-Relevanten Daten aus der SVWS-DB und gibt diese
	 * im Export-Format als Response zurück. Dabei wird zunächst geprüft, ob der Benutzer die
	 * notwendigen Berechtigungen hat.
	 *
	 * @return die Response
	 */
	Response getStatistikExport();

	/**
	 * Führt einen Patch auf die Daten der Zusatzerhebungen für den angegebenen Schuljahresabschnitt aus
	 * und gibt die geänderten Daten als Response zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param patch                    der Patch-Request
	 *
	 * @return die Response
	 */
	Response patchZusatzerhebungen(long idSchuljahresabschnitt, ZusatzerhebungenStatistikPatchRequest patch);

}
