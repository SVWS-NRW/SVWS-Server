package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerKompetenzKatalogEintrag}.
 */
public class DataBenutzerkompetenzliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzerKompetenzKatalogEintrag}.
	 */
	public DataBenutzerkompetenzliste() {
		super(null);
	}
	
	
	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		List<BenutzerKompetenzKatalogEintrag> daten = new Vector<>();
		for (BenutzerKompetenz k : BenutzerKompetenz.values())
			daten.add(k.daten);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
