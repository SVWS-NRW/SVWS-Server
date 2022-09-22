package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerKompetenzKatalogEintrag}.
 */
public class DataBenutzerkompetenzliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzerKompetenzKatalogEintrag}.
	 * 
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzerkompetenzliste(DBEntityManager conn) {
		super(conn);
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
