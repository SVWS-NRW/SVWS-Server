package de.svws_nrw.controller.uv;

import de.svws_nrw.core.data.uv.UvKlasseAnlageRequest;
import de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import jakarta.ws.rs.core.Response;

/** HTTP-Schnittstelle für atomare Unterrichtsstrukturoperationen. */
public interface UvUnterrichtsstrukturController {

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param request die Anlageanfrage
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	Response createKlasseMitSchuelergruppe(UvKlasseAnlageRequest request);

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param request die Anlageanfrage
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	Response createKursMitLerngruppe(UvKursunterrichtAnlageRequest request);

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param idKlasse die Ziel-ID
	 * @param request die neue Schülergruppe
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	Response createSchuelergruppeFuerKlasse(long idKlasse, UvSchuelergruppeCreateRequest request);

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param idKurs die Ziel-ID
	 * @param request die neue Schülergruppe
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	Response createSchuelergruppeFuerKurs(long idKurs, UvSchuelergruppeCreateRequest request);
}
