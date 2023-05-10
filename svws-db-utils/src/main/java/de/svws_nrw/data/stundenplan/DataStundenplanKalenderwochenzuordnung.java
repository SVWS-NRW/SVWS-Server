package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanKalenderwochenZuordnung;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanKalenderwochenzuordnung}.
 */
public final class DataStundenplanKalenderwochenzuordnung extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanKalenderwochenzuordnung}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Kalenderwochen-Zuordnungen abgefragt werden
	 */
	public DataStundenplanKalenderwochenzuordnung(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanKalenderwochenZuordnung} in einen Core-DTO {@link StundenplanKalenderwochenzuordnung}.
	 */
	private static final Function<DTOStundenplanKalenderwochenZuordnung, StundenplanKalenderwochenzuordnung> dtoMapper = (final DTOStundenplanKalenderwochenZuordnung k) -> {
		final StundenplanKalenderwochenzuordnung daten = new StundenplanKalenderwochenzuordnung();
		daten.id = k.ID;
		daten.jahr = k.Jahr;
		daten.kw = k.KW;
		daten.wochentyp = k.Wochentyp;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Kalenderwochenzuordnungen des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Kalenderwochenzuordnungen
	 */
	public static List<StundenplanKalenderwochenzuordnung> getKalenderwochenzuordnungen(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanKalenderwochenZuordnung> zuordnungen = conn.queryNamed("DTOStundenplanKalenderwochenZuordnung.stundenplan_id", idStundenplan, DTOStundenplanKalenderwochenZuordnung.class);
		final ArrayList<StundenplanKalenderwochenzuordnung> daten = new ArrayList<>();
		for (final DTOStundenplanKalenderwochenZuordnung z : zuordnungen)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response getList() {
		final List<StundenplanKalenderwochenzuordnung> daten = getKalenderwochenzuordnungen(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Kalenderwochen-Zuordnung eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanKalenderwochenZuordnung raum = conn.queryByKey(DTOStundenplanKalenderwochenZuordnung.class, id);
		if (raum == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde keine Kalenderwochen-Zuordnung eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanKalenderwochenzuordnung daten = dtoMapper.apply(raum);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
