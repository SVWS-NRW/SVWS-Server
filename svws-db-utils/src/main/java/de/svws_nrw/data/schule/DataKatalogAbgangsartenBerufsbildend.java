package de.svws_nrw.data.schule;

import java.io.InputStream;

import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.json.JsonDaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link AbgangsartKatalog}.
 */
public class DataKatalogAbgangsartenBerufsbildend extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link AbgangsartKatalog}.
	 */
	public DataKatalogAbgangsartenBerufsbildend() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		AbgangsartKatalog daten = JsonDaten.abgangsartenManager.getKatalogBerufsbildend(); 
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
