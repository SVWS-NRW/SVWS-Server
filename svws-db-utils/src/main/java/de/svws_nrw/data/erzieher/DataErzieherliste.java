package de.svws_nrw.data.erzieher;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.erzieher.ErzieherListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ErzieherListeEintrag}.
 */
public final class DataErzieherliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ErzieherListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherListeEintrag}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherListeEintrag> dtoMapperErzieher1 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherListeEintrag eintrag = new ErzieherListeEintrag();
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
	private final Function<DTOSchuelerErzieherAdresse, ErzieherListeEintrag> dtoMapperErzieher2 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherListeEintrag eintrag = new ErzieherListeEintrag();
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
	public Response getAll() throws ApiOperationException {
    	final List<DTOSchuelerErzieherAdresse> erzieher = conn.queryAll(DTOSchuelerErzieherAdresse.class);
    	if (erzieher == null)
    		throw new ApiOperationException(Status.NOT_FOUND);
        final List<ErzieherListeEintrag> daten = erzieher.stream().filter(e -> ((e.Name1 != null) && !"".equals(e.Name1.trim()))).map(dtoMapperErzieher1).toList();
        daten.addAll(erzieher.stream().filter(e -> ((e.Name2 != null) && !"".equals(e.Name2.trim()))).map(dtoMapperErzieher2).toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
