package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.data.schule.SchultraegerKatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchultraegerKatalogEintrag}.
 */
public class DataKatalogSchultraeger extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchultraegerKatalogEintrag}.
	 */
	public DataKatalogSchultraeger() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		List<SchultraegerKatalogEintrag> katalog = CsvReader.fromResource("daten/csv/schulver/Schultraeger.csv", SchultraegerKatalogEintrag.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(katalog).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
