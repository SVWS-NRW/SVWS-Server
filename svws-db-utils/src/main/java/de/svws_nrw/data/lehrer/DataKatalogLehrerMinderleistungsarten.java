package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerMinderleistungsartKatalogEintrag}.
 */
public final class DataKatalogLehrerMinderleistungsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerMinderleistungsartKatalogEintrag}.
	 */
	public DataKatalogLehrerMinderleistungsarten() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<LehrerMinderleistungsartKatalogEintrag> daten = new ArrayList<>();
		for (final LehrerMinderleistungsarten status : LehrerMinderleistungsarten.values())
			daten.addAll(status.historie());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
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
