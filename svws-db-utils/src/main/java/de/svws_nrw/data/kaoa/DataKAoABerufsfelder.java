package de.svws_nrw.data.kaoa;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.kaoa.KAOABerufsfeldEintrag;
import de.svws_nrw.core.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KAOABerufsfeldEintrag}.
 */
public final class DataKAoABerufsfelder extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KAOABerufsfeldEintrag}.
	 */
	public DataKAoABerufsfelder() {
		super(null);
	}

	@Override
	public Response getAll() {
		final Vector<KAOABerufsfeldEintrag> daten = new Vector<>();
		for (final KAOABerufsfeld b : KAOABerufsfeld.values())
			daten.addAll(Arrays.asList(b.historie));
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
