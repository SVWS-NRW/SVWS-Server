package de.svws_nrw.data.schild3;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragLaender;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Schild3KatalogEintragLaender}.
 */
public final class DataSchildLaender extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Schild3KatalogEintragLaender}.
	 */
	public DataSchildLaender() {
		super(null);
	}

	@Override
	public Response getAll() throws ApiOperationException {
		final List<Schild3KatalogEintragLaender> katalog = CsvReader.fromResource("daten/csv/schild3/Laender.csv", Schild3KatalogEintragLaender.class);
    	if (katalog == null)
    		throw new ApiOperationException(Status.NOT_FOUND);
    	katalog.sort((a, b) -> Integer.compare(a.Sortierung, b.Sortierung));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(katalog).build();
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
