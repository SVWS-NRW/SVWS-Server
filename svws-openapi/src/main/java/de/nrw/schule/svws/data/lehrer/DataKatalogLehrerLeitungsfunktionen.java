package de.nrw.schule.svws.data.lehrer;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogLeitungsfunktionEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOSchulleitungFunktion;
import de.nrw.schule.svws.db.utils.DBDefaultData;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogLeitungsfunktionEintrag}.
 */
public class DataKatalogLehrerLeitungsfunktionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogLeitungsfunktionEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogLehrerLeitungsfunktionen(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchulleitungFunktion} in einen Core-DTO {@link LehrerKatalogLeitungsfunktionEintrag}.  
	 */
	private Function<DTOSchulleitungFunktion, LehrerKatalogLeitungsfunktionEintrag> dtoMapper = (DTOSchulleitungFunktion k) -> {
		var eintrag = new LehrerKatalogLeitungsfunktionEintrag();
		eintrag.id = k.ID;
		eintrag.text = k.Funktionstext;
		return eintrag;
	};

	@Override
	public Response getAll() {
    	List<DTOSchulleitungFunktion> katalog = DBDefaultData.get(DTOSchulleitungFunktion.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<LehrerKatalogLeitungsfunktionEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
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
