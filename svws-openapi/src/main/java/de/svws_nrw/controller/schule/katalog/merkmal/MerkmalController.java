package de.svws_nrw.controller.schule.katalog.merkmal;

import java.util.List;

import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import jakarta.ws.rs.core.Response;

public interface MerkmalController {

	/**
	 * Ermittelt alle Merkmale.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt ein neues Merkmal und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(MerkmalCreateRequest request);

	/**
	 * Führt einen Patch für ein Merkmal aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, MerkmalPatchRequest patch);

	/**
	 * Löscht mehrere Merkmale anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
