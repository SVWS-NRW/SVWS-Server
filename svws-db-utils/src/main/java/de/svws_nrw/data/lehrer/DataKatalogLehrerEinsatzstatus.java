package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerEinsatzstatusKatalogEintrag}.
 */
public final class DataKatalogLehrerEinsatzstatus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerEinsatzstatusKatalogEintrag}.
	 */
	public DataKatalogLehrerEinsatzstatus() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<LehrerEinsatzstatusKatalogEintrag> daten = new ArrayList<>();
		for (final LehrerEinsatzstatus status : LehrerEinsatzstatus.values())
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
