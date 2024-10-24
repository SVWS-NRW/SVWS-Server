package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerRechtsverhaeltnisKatalogEintrag}.
 */
public final class DataKatalogLehrerRechtsverhaeltnis extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerRechtsverhaeltnisKatalogEintrag}.
	 */
	public DataKatalogLehrerRechtsverhaeltnis() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<LehrerRechtsverhaeltnisKatalogEintrag> daten = new ArrayList<>();
		for (final LehrerRechtsverhaeltnis verhaeltnis : LehrerRechtsverhaeltnis.values())
			daten.addAll(verhaeltnis.historie());
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
