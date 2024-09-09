package de.svws_nrw.data.jahrgaenge;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgaengeKatalogEintrag}.
 */
public final class DataKatalogJahrgaenge extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgaengeKatalogEintrag}.
	 */
	public DataKatalogJahrgaenge() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<JahrgaengeKatalogEintrag> daten = new ArrayList<>();
		for (final Jahrgaenge jg : Jahrgaenge.values())
			daten.addAll(jg.historie());
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
