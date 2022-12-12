package de.nrw.schule.svws.data.kataloge;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.kataloge.OrtsteilKatalogEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOOrtsteil;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link OrtsteilKatalogEintrag}.
 */
public class DataOrtsteile extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OrtsteilKatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOrtsteile(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
    	var katalog = conn.queryAll(DTOOrtsteil.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<OrtsteilKatalogEintrag> daten = katalog.stream().map(k -> {
    		var eintrag = new OrtsteilKatalogEintrag();
    		eintrag.id = k.ID;
    		eintrag.ort_id = k.Ort_ID;
    		eintrag.ortsteil = k.Bezeichnung;
    		eintrag.sortierung = k.Sortierung;
    		eintrag.istSichtbar = k.Sichtbar;
    		eintrag.istAenderbar = k.Aenderbar;
    		return eintrag;
    	}).collect(Collectors.toList());
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
