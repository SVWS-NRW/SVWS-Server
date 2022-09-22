package de.nrw.schule.svws.data.kataloge;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.kataloge.KatalogEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOHaltestellen;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintrag}.
 */
public class DataHaltestellen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataHaltestellen(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOHaltestellen} in einen Core-DTO {@link KatalogEintrag}.  
	 */
	private Function<DTOHaltestellen, KatalogEintrag> dtoMapper = (DTOHaltestellen k) -> {
		KatalogEintrag daten = new KatalogEintrag();
		daten.id = k.ID;
		daten.kuerzel = k.ID.toString();
		daten.text = k.Bezeichnung;
		daten.istSichtbar = true;
		daten.istAenderbar = true;
		return daten;
	};

	@Override
	public Response getAll() {
    	List<DTOHaltestellen> katalog = conn.queryAll(DTOHaltestellen.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<KatalogEintrag> daten = katalog.stream().map(dtoMapper).sorted((a,b) -> {
    		if ((a.text == null) && (b.text == null))
    			return 0;
    		if (a.text == null)
    			return -1;
    		if (b.text == null)
    			return 1;
    		return a.text.compareTo(b.text);
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
