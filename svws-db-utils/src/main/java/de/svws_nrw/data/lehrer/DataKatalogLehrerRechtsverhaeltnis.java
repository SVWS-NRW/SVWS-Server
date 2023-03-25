package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.lehrer.LehrerKatalogRechtsverhaeltnisEintrag;
import de.svws_nrw.core.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogRechtsverhaeltnisEintrag}.
 */
public class DataKatalogLehrerRechtsverhaeltnis extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogRechtsverhaeltnisEintrag}.
	 */
	public DataKatalogLehrerRechtsverhaeltnis() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<LehrerKatalogRechtsverhaeltnisEintrag> daten = new Vector<>();
		for (LehrerRechtsverhaeltnis verhaeltnis : LehrerRechtsverhaeltnis.values())
			daten.addAll(Arrays.asList(verhaeltnis.historie));
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
