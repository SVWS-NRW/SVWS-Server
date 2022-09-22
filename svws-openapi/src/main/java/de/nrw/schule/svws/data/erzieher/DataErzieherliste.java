package de.nrw.schule.svws.data.erzieher;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.erzieher.ErzieherListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ErzieherListeEintrag}.
 */
public class DataErzieherliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ErzieherListeEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherliste(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherListeEintrag}.  
	 */
	private Function<DTOSchuelerErzieherAdresse, ErzieherListeEintrag> dtoMapperErzieher1 = (DTOSchuelerErzieherAdresse e) -> {
		ErzieherListeEintrag eintrag = new ErzieherListeEintrag();
		eintrag.id = e.ID * 10 + 1;
        eintrag.idSchueler = e.Schueler_ID;
        eintrag.idErzieherArt = e.ErzieherArt_ID;
        eintrag.anrede = e.Anrede1;
		eintrag.name = e.Name1;
        eintrag.vorname = e.Vorname1;
        eintrag.email = e.ErzEmail;
		return eintrag;
	};

	/**
	 * Lambda-Ausdruck zum Umwandeln des zweiten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherListeEintrag}.  
	 */
	private Function<DTOSchuelerErzieherAdresse, ErzieherListeEintrag> dtoMapperErzieher2 = (DTOSchuelerErzieherAdresse e) -> {
		ErzieherListeEintrag eintrag = new ErzieherListeEintrag();
		eintrag.id = e.ID * 10 + 2;
        eintrag.idSchueler = e.Schueler_ID;
        eintrag.idErzieherArt = e.ErzieherArt_ID;
        eintrag.anrede = e.Anrede2;
		eintrag.name = e.Name2;
        eintrag.vorname = e.Vorname2;
        eintrag.email = e.ErzEmail2;
		return eintrag;
	};

	@Override
	public Response getAll() {
    	List<DTOSchuelerErzieherAdresse> erzieher = conn.queryAll(DTOSchuelerErzieherAdresse.class);
    	if (erzieher == null)
    		return OperationError.NOT_FOUND.getResponse();
        List<ErzieherListeEintrag> daten = erzieher.stream().filter(e -> ((e.Name1 != null) && !"".equals(e.Name1.trim()))).map(dtoMapperErzieher1).collect(Collectors.toList());
        daten.addAll(erzieher.stream().filter(e -> ((e.Name2 != null) && !"".equals(e.Name2.trim()))).map(dtoMapperErzieher2).collect(Collectors.toList()));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
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
