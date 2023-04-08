package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerKompetenzKatalogEintrag}.
 */
public final class DataBenutzerkompetenzliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzerKompetenzKatalogEintrag}.
	 */
	public DataBenutzerkompetenzliste() {
		super(null);
	}


	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		final List<BenutzerKompetenzKatalogEintrag> daten = new ArrayList<>();
		for (final BenutzerKompetenz k : BenutzerKompetenz.values())
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
