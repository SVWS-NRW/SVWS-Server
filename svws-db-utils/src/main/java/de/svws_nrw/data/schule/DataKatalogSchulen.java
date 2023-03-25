package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.data.schule.SchulenKatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchulenKatalogEintrag}.
 */
public class DataKatalogSchulen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchulenKatalogEintrag}.
	 */
	public DataKatalogSchulen() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		List<SchulenKatalogEintrag> katalog = getKatalog();
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
	
	/**
	 * Liest den Katalog der Schulen ein.
	 * 
	 * @return der Katalog der Schulen
	 */
	public static List<SchulenKatalogEintrag> getKatalog() {
		return CsvReader.fromResource("daten/csv/schulver/Schulen.csv", SchulenKatalogEintrag.class);		
	}
	
}
