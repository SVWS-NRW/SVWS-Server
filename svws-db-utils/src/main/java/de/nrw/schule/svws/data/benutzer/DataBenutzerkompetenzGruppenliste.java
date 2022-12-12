package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzGruppenKatalogEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenzGruppe;
import de.nrw.schule.svws.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerKompetenzGruppenKatalogEintrag}.
 */
public class DataBenutzerkompetenzGruppenliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzerKompetenzGruppenKatalogEintrag}.
	 */
	public DataBenutzerkompetenzGruppenliste() {
		super(null);
	}
	
	
	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		List<BenutzerKompetenzGruppenKatalogEintrag> daten = new Vector<>();
		for (BenutzerKompetenzGruppe k : BenutzerKompetenzGruppe.values())
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
