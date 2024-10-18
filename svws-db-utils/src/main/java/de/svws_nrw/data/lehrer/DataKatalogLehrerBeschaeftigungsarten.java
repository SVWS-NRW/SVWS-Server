package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerBeschaeftigungsartKatalogEintrag}.
 */
public final class DataKatalogLehrerBeschaeftigungsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerBeschaeftigungsartKatalogEintrag}.
	 */
	public DataKatalogLehrerBeschaeftigungsarten() {
		super(null);
	}


	@Override
	public Response getAll() {
		final ArrayList<LehrerBeschaeftigungsartKatalogEintrag> daten = new ArrayList<>();
		for (final LehrerBeschaeftigungsart art : LehrerBeschaeftigungsart.values())
			daten.addAll(art.historie());
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
