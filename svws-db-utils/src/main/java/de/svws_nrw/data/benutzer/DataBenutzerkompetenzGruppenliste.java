package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.benutzer.BenutzerKompetenzGruppenKatalogEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerKompetenzGruppenKatalogEintrag}.
 */
public final class DataBenutzerkompetenzGruppenliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzerKompetenzGruppenKatalogEintrag}.
	 */
	public DataBenutzerkompetenzGruppenliste() {
		super(null);
	}


	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		final List<BenutzerKompetenzGruppenKatalogEintrag> daten = new Vector<>();
		for (final BenutzerKompetenzGruppe k : BenutzerKompetenzGruppe.values())
			daten.add(k.daten);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
