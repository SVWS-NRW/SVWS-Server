package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link HerkunftsartKatalogEintrag}.
 */
public final class DataKatalogHerkunftsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link HerkunftsartKatalogEintrag}.
	 */
	public DataKatalogHerkunftsarten() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<HerkunftsartKatalogEintrag> daten = new ArrayList<>();
		for (final Herkunftsarten eintrag : Herkunftsarten.values())
			daten.addAll(Arrays.asList(eintrag.historie));
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
