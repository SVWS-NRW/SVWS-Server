package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.core.types.schule.Religion;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ReligionKatalogEintrag}.
 */
public final class DataKatalogReligionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ReligionKatalogEintrag}.
	 */
	public DataKatalogReligionen() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<ReligionKatalogEintrag> daten = new ArrayList<>();
		for (final Religion eintrag : Religion.values())
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
