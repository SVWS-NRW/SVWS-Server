package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanRaum}.
 */
public final class DataStundenplanRaeume extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanRaum}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Räume abgefragt werden
	 */
	public DataStundenplanRaeume(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanRaum} in einen Core-DTO {@link StundenplanRaum}.
	 */
	private static final Function<DTOStundenplanRaum, StundenplanRaum> dtoMapper = (final DTOStundenplanRaum r) -> {
		final StundenplanRaum daten = new StundenplanRaum();
		daten.id = r.ID;
		daten.kuerzel = r.Kuerzel;
		daten.beschreibung = r.Beschreibung;
		daten.groesse = r.Groesse;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Räume des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Räume
	 */
	public static List<StundenplanRaum> getRaeume(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanRaum> raeume = conn.queryNamed("DTOStundenplanRaum.stundenplan_id", idStundenplan, DTOStundenplanRaum.class);
		final ArrayList<StundenplanRaum> daten = new ArrayList<>();
		for (final DTOStundenplanRaum r : raeume)
			daten.add(dtoMapper.apply(r));
		return daten;
	}

	@Override
	public Response getList() {
		final List<StundenplanRaum> daten = getRaeume(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Raum eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanRaum raum = conn.queryByKey(DTOStundenplanRaum.class, id);
		if (raum == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Raum eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanRaum daten = dtoMapper.apply(raum);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


}
