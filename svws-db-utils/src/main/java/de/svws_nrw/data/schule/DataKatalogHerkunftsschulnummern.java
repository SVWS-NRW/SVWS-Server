package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.schule.HerkunftsschulnummerKatalogEintrag;
import de.svws_nrw.core.types.schule.Herkunftsschulnummern;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link HerkunftsschulnummerKatalogEintrag}.
 */
public final class DataKatalogHerkunftsschulnummern extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link HerkunftsschulnummerKatalogEintrag}.
	 */
	public DataKatalogHerkunftsschulnummern() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<HerkunftsschulnummerKatalogEintrag> daten = new ArrayList<>();
		for (final Herkunftsschulnummern sn : Herkunftsschulnummern.values())
			daten.addAll(Arrays.asList(sn.historie));
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
