package de.nrw.schule.svws.data.schild3;

import java.io.InputStream;
import java.util.List;

import de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragPruefungsordnung;
import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Schild3KatalogEintragPruefungsordnung}.
 */
public class DataSchildPruefungsordnung extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Schild3KatalogEintragPruefungsordnung}.
	 */
	public DataSchildPruefungsordnung() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		List<Schild3KatalogEintragPruefungsordnung> katalog = CsvReader.fromResource("daten/csv/schild3/PruefungsOrdnung.csv", Schild3KatalogEintragPruefungsordnung.class);
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
