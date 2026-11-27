package de.svws_nrw.controller.uv;

import de.svws_nrw.core.data.uv.UvKlasseAnlageRequest;
import de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.UvUnterrichtsstrukturService;
import jakarta.ws.rs.core.Response;

/** Bindet atomare Fachoperationen an HTTP-Antworten. */
public final class UvUnterrichtsstrukturControllerImpl implements UvUnterrichtsstrukturController {

	private final UvUnterrichtsstrukturService service;

	/**
	 * Erstellt den Controller.
	 *
	 * @param service der Fachservice
	 */
	public UvUnterrichtsstrukturControllerImpl(final UvUnterrichtsstrukturService service) {
		this.service = service;
	}

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param request die Anlageanfrage
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	@Override
	public Response createKlasseMitSchuelergruppe(final UvKlasseAnlageRequest request) {
		return Responses.created(service.createKlasseMitSchuelergruppe(request));
	}

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param request die Anlageanfrage
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	@Override
	public Response createKursMitLerngruppe(final UvKursunterrichtAnlageRequest request) {
		return Responses.created(service.createKursMitLerngruppe(request));
	}

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param idKlasse die Ziel-ID
	 * @param request die neue Schülergruppe
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	@Override
	public Response createSchuelergruppeFuerKlasse(final long idKlasse, final UvSchuelergruppeCreateRequest request) {
		return Responses.created(service.createSchuelergruppeFuerKlasse(idKlasse, request));
	}

	/**
	 * Führt die atomare Fachoperation aus.
	 *
	 * @param idKurs die Ziel-ID
	 * @param request die neue Schülergruppe
	 * @return die HTTP-Antwort mit den angelegten beziehungsweise geänderten Daten
	 */
	@Override
	public Response createSchuelergruppeFuerKurs(final long idKurs, final UvSchuelergruppeCreateRequest request) {
		return Responses.created(service.createSchuelergruppeFuerKurs(idKurs, request));
	}
}
