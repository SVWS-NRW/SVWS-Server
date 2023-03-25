package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.lehrer.LehrerKatalogEinsatzstatusEintrag;
import de.svws_nrw.core.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogEinsatzstatusEintrag}.
 */
public class DataKatalogLehrerEinsatzstatus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogEinsatzstatusEintrag}.
	 */
	public DataKatalogLehrerEinsatzstatus() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<LehrerKatalogEinsatzstatusEintrag> daten = new Vector<>();
		for (LehrerEinsatzstatus status : LehrerEinsatzstatus.values())
			daten.addAll(Arrays.asList(status.historie));
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
