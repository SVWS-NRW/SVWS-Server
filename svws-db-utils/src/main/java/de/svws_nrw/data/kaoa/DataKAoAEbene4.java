package de.svws_nrw.data.kaoa;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.kaoa.KAOAEbene4Eintrag;
import de.svws_nrw.core.types.kaoa.KAOAEbene4;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KAOAEbene4Eintrag}.
 */
public final class DataKAoAEbene4 extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KAOAEbene4Eintrag}.
	 */
	public DataKAoAEbene4() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<KAOAEbene4Eintrag> daten = new ArrayList<>();
		for (final KAOAEbene4 e : KAOAEbene4.values())
			daten.addAll(Arrays.asList(e.historie));
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
