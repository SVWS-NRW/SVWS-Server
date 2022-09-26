package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.kataloge.KatalogEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintrag}.
 */
public class DataKatalogSchuelerFahrschuelerarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchuelerFahrschuelerarten(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFahrschuelerart} in einen Core-DTO {@link KatalogEintrag}.  
	 */
	private Function<DTOFahrschuelerart, KatalogEintrag> dtoMapper = (DTOFahrschuelerart k) -> {
		KatalogEintrag eintrag = new KatalogEintrag();
		eintrag.id = k.ID;
		eintrag.kuerzel = k.ID.toString();
		eintrag.text = k.Bezeichnung;
		eintrag.istAenderbar = k.Aenderbar;
		eintrag.istSichtbar = k.Sichtbar;
		return eintrag;
	};

	@Override
	public Response getAll() {
		List<DTOFahrschuelerart> katalog = conn.queryAll(DTOFahrschuelerart.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<KatalogEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
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
