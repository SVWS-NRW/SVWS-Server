package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerStatusKatalogEintrag}.
 */
public final class DataSchuelerStatus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerStatusKatalogEintrag}.
	 */
	public DataSchuelerStatus() {
		super(null);
	}

	@Override
	public Response getAll() throws ApiOperationException {
		final ArrayList<SchuelerStatusKatalogEintrag> daten = new ArrayList<>();
		for (final SchuelerStatus status : SchuelerStatus.values())
			daten.addAll(status.historie());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
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
