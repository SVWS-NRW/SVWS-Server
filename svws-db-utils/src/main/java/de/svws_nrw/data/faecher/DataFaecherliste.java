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
	private static final Function<DTOFach, FaecherListeEintrag> dtoMapperFach = (final DTOFach f) -> {
		final FaecherListeEintrag daten = new FaecherListeEintrag();
		daten.id = f.ID;
		daten.kuerzel = (f.Kuerzel == null) ? "" : f.Kuerzel;
		daten.kuerzelStatistik = f.StatistikFach.daten.kuerzelASD;
		daten.bezeichnung = (f.Bezeichnung == null) ? "" : f.Bezeichnung;
		daten.istOberstufenFach = f.IstOberstufenFach;
		daten.sortierung = f.SortierungAllg;
		daten.istSichtbar = f.Sichtbar;
		return daten;
	};


	/**
	 * Bestimmt die Liste aller Fächer.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Fächer
	 */
	public static List<FaecherListeEintrag> getFaecherListe(final DBEntityManager conn) {
    	final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
    	if (faecher == null)
    		throw OperationError.NOT_FOUND.exception("Es wurden keine Fächer in der Datenbank gefunden.");
    	return faecher.stream().map(dtoMapperFach::apply).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
	}


	@Override
	public Response getAll() {
    	final List<FaecherListeEintrag> daten = getFaecherListe(conn);
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
