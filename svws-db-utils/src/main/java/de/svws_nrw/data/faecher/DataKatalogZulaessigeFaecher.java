package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.fach.FachKatalogEintrag;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FachKatalogEintrag}.
 */
public class DataKatalogZulaessigeFaecher extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FachKatalogEintrag}.
	 */
	public DataKatalogZulaessigeFaecher() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<FachKatalogEintrag> daten = new Vector<>();
		for (ZulaessigesFach f : ZulaessigesFach.values())
			daten.addAll(Arrays.asList(f.historie));
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
