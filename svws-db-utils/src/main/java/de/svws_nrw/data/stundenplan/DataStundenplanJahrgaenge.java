package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanJahrgang}.
 */
public final class DataStundenplanJahrgaenge extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanJahrgang}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Jahrgänge abgefragt werden
	 */
	public DataStundenplanJahrgaenge(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOJahrgang} in einen Core-DTO {@link StundenplanJahrgang}.
	 */
	private static final Function<DTOJahrgang, StundenplanJahrgang> dtoMapper = (final DTOJahrgang j) -> {
		final StundenplanJahrgang daten = new StundenplanJahrgang();
		daten.id = j.ID;
		daten.kuerzel = j.InternKrz;
		daten.bezeichnung = j.ASDBezeichnung;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Jahrgänge des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Jahrgänge
	 */
	public static List<StundenplanJahrgang> getJahrgaenge(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		final ArrayList<StundenplanJahrgang> daten = new ArrayList<>();
		for (final DTOJahrgang j : jahrgaenge) {
			// TODO Prüfe die Gültigkeit des Jahrgangs (Schuljahresabschnitt: GueltigVon - GueltigBis) in Bezug auf den Stundenplan (Datum: Beginn - Ende)
			daten.add(dtoMapper.apply(j));
		}
		return daten;
	}


	@Override
	public Response getList() {
		final List<StundenplanJahrgang> daten = getJahrgaenge(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, stundenplanID);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(stundenplanID));
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Jahrgang mit der ID null ist unzulässig.");
		final DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, id);
		if (jahrgang == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Jahrgang mit der ID %d gefunden.".formatted(id));
		// TODO Prüfe die Gültigkeit des Jahrgangs (Schuljahresabschnitt: GueltigVon - GueltigBis) in Bezug auf den Stundenplan (Datum: Beginn - Ende)
		final StundenplanJahrgang daten = dtoMapper.apply(jahrgang);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
