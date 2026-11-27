package de.svws_nrw.controller.uv.regeln;

import java.util.Collection;


import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.regeln.UvRegelService;
import jakarta.ws.rs.core.Response;

/** Bindet die HTTP-Antworten der UV-Blockungsregeln an den Regelservice. */
public final class UvRegelControllerImpl implements UvRegelController {

	private final UvRegelService service;

	/**
	 * Erstellt den Controller.
	 *
	 * @param service der Regelservice
	 */
	public UvRegelControllerImpl(final UvRegelService service) {
		this.service = service;
	}

	@Override
	public Response getList(final long idPlan) {
		return Responses.ok(service.getList(idPlan));
	}

	@Override
	public Response create(final long idPlan, final UvBlockungRegel regel) {
		return Responses.ok(service.create(idPlan, regel));
	}

	@Override
	public Response update(final long idPlan, final long idRegel, final UvBlockungRegel regel) {
		return Responses.ok(service.update(idPlan, idRegel, regel));
	}

	@Override
	public Response delete(final long idPlan, final long idRegel) {
		service.delete(idPlan, idRegel);
		return Response.noContent().build();
	}
	/**
	 * Speichert die zusammengehörigen Änderungen atomar.
	 *
	 * @param idPlan die zugehörigen Daten
	 * @param regeln die zugehörigen Daten
	 * @return die HTTP-Antwort
	 */
	@Override
	public Response updateMultiple(final long idPlan, final Collection<UvBlockungRegel> regeln) {
		return Responses.ok(service.updateMultiple(idPlan, regeln));
	}

}
