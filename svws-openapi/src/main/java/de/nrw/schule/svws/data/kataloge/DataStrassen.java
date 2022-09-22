package de.nrw.schule.svws.data.kataloge;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.kataloge.KatalogEintragStrassen;
import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintragStrassen}.
 */
public class DataStrassen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintragStrassen}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataStrassen(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
		List<KatalogEintragStrassen> katalog = CsvReader.fromResource("daten/csv/statkue/Strassen.csv", KatalogEintragStrassen.class);
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
