package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für das Erstellen von UV-Unterrichten auf Basis von Lerngruppen.
 */
public interface UvUnterrichtLerngruppenCreateController {

	/**
	 * Erstellt fehlende Unterrichte für eine Lerngruppe.
	 *
	 * @param idLerngruppe   die ID der Lerngruppe
	 *
	 * @return die Response
	 */
	Response createByLerngruppe(long idLerngruppe);

	/**
	 * Erstellt fehlende Unterrichte für mehrere Lerngruppen.
	 *
	 * @param idsLerngruppen   die IDs der Lerngruppen
	 *
	 * @return die Response
	 */
	Response createByLerngruppen(Collection<Long> idsLerngruppen);

}
