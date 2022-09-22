package de.nrw.schule.svws.data.kataloge;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.kataloge.OrtKatalogEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOOrt;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link OrtKatalogEintrag}.
 */
public class DataOrte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OrtKatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOrte(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOOrt} in einen Core-DTO {@link OrtKatalogEintrag}.  
	 */
	private Function<DTOOrt, OrtKatalogEintrag> dtoMapper = (DTOOrt k) -> {
		OrtKatalogEintrag daten = new OrtKatalogEintrag();
		daten.id = k.ID;
		daten.plz = k.PLZ;
		daten.ortsname = k.Bezeichnung;
		daten.kreis = k.Kreis;
		daten.kuerzelBundesland = k.Land;
		daten.sortierung = k.Sortierung;
		daten.istSichtbar = k.Sichtbar;
		daten.istAenderbar = k.Aenderbar;
		return daten;
	};

	@Override
	public Response getAll() {
		List<DTOOrt> katalog = conn.queryAll(DTOOrt.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<OrtKatalogEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
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
