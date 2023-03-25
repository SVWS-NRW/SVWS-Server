package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FaecherListeEintrag}.
 */
public class DataFaecherliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FaecherListeEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFaecherliste(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFach} in einen Core-DTO {@link FaecherListeEintrag}.  
	 */
	private Function<DTOFach, FaecherListeEintrag> dtoMapperFach = (DTOFach f) -> {
		FaecherListeEintrag eintrag = new FaecherListeEintrag();
		eintrag.id = f.ID;
		eintrag.kuerzel = f.Kuerzel;
		eintrag.kuerzelStatistik = f.StatistikFach.daten.kuerzelASD;
		eintrag.bezeichnung = f.Bezeichnung;
		eintrag.istOberstufenFach = f.IstOberstufenFach;
		eintrag.sortierung = f.SortierungAllg;
		eintrag.istSichtbar = f.Sichtbar;
		eintrag.istAenderbar = f.Aenderbar;
		return eintrag;
	};

	@Override
	public Response getAll() {
    	List<DTOFach> faecher = conn.queryAll(DTOFach.class);
    	if (faecher == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<FaecherListeEintrag> daten = faecher.stream().map(f -> dtoMapperFach.apply(f)).sorted((a,b) -> Long.compare(a.sortierung, b.sortierung)).collect(Collectors.toList());
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
