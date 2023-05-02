package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FaecherListeEintrag}.
 */
public final class DataFaecherliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FaecherListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFaecherliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFach} in einen Core-DTO {@link FaecherListeEintrag}.
	 */
	private final Function<DTOFach, FaecherListeEintrag> dtoMapperFach = (final DTOFach f) -> {
		final FaecherListeEintrag eintrag = new FaecherListeEintrag();
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
    	final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
    	if (faecher == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<FaecherListeEintrag> daten = faecher.stream().map(dtoMapperFach::apply).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
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
