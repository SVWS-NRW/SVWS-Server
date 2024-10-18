package de.svws_nrw.data.kurse;

import java.io.InputStream;
import java.util.ArrayList;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ZulaessigeKursartKatalogEintrag}.
 */
public final class DataKatalogKursarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ZulaessigeKursartKatalogEintrag}.
	 */
	public DataKatalogKursarten() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<ZulaessigeKursartKatalogEintrag> daten = new ArrayList<>();
		for (final ZulaessigeKursart ka : ZulaessigeKursart.values())
			daten.addAll(ka.historie());
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
