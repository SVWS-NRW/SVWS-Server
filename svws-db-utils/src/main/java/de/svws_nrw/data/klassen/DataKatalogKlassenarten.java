package de.svws_nrw.data.klassen;

import java.io.InputStream;
import java.util.ArrayList;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenartKatalogEintrag}.
 */
public final class DataKatalogKlassenarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenartKatalogEintrag}.
	 */
	public DataKatalogKlassenarten() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<KlassenartKatalogEintrag> daten = new ArrayList<>();
		for (final Klassenart ka : Klassenart.values())
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
