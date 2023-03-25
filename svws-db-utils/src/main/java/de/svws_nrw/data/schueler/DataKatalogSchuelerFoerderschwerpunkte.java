package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FoerderschwerpunktEintrag}.
 */
public class DataKatalogSchuelerFoerderschwerpunkte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FoerderschwerpunktEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchuelerFoerderschwerpunkte(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFoerderschwerpunkt} in einen Core-DTO {@link FoerderschwerpunktEintrag}.  
	 */
	private Function<DTOFoerderschwerpunkt, FoerderschwerpunktEintrag> dtoMapper = (DTOFoerderschwerpunkt k) -> {
		FoerderschwerpunktEintrag eintrag = new FoerderschwerpunktEintrag();
		eintrag.id = k.ID;
		eintrag.kuerzel = k.ID.toString();
		eintrag.text = k.Bezeichnung;
		eintrag.kuerzelStatistik = k.StatistikKrz;
		eintrag.istAenderbar = k.Aenderbar;
		eintrag.istSichtbar = k.Sichtbar;
		return eintrag;
	};

	@Override
	public Response getAll() {
		List<DTOFoerderschwerpunkt> katalog = conn.queryAll(DTOFoerderschwerpunkt.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<FoerderschwerpunktEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Long id) {
		DTOFoerderschwerpunkt fs = conn.queryByKey(DTOFoerderschwerpunkt.class, id);
		if (fs == null)
			throw OperationError.NOT_FOUND.exception();
		FoerderschwerpunktEintrag daten = dtoMapper.apply(fs);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		// TODO
		throw new UnsupportedOperationException();
	}
	
}
