package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanLehrer}.
 */
public final class DataStundenplanLehrer extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanRaum}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Lehrer abgefragt werden
	 */
	public DataStundenplanLehrer(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link StundenplanLehrer}.
	 */
	private static final Function<DTOLehrer, StundenplanLehrer> dtoMapper = (final DTOLehrer l) -> {
		final StundenplanLehrer daten = new StundenplanLehrer();
		daten.id = l.ID;
		daten.kuerzel = l.Kuerzel;
		daten.nachname = l.Nachname;
		daten.vorname = l.Vorname;
		return daten;
	};



	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die auswählbaren Lehrer für den Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Lehrer
	 */
	public static List<StundenplanLehrer> getLehrer(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<DTOLehrer> lehrerliste = conn.queryNamed("DTOLehrer.sichtbar", true, DTOLehrer.class);
		final ArrayList<StundenplanLehrer> daten = new ArrayList<>();
		for (final DTOLehrer l : lehrerliste) {
			if ((l.PersonTyp != PersonalTyp.LEHRKRAFT) && (l.PersonTyp != PersonalTyp.EXTERN))
				continue;
			if ((l.DatumAbgang != null)) {
				// TODO DatumAbgang bei Filterung berücksichtigen, wenn gesetzt
			}
			final StundenplanLehrer lehrer = dtoMapper.apply(l);
			// TODO Fächer des Lehrers ergänzen - hier besteht die Problematik, dass bei den Fachrichtungen/Lehrbefähigungen in der DB die Statistik-Kürzel stehen und hier Fach-IDs benötigt werden...
			daten.add(lehrer);
		}
		return daten;
	}


	@Override
	public Response getList() {
		final List<StundenplanLehrer> daten = getLehrer(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Ermittelt die Informationen zu dem angegebenen Lehrer für den angegebenen Stundenplan.
	 *
	 * @param conn             die Datenbank-Verbindung
	 * @param idStundenplan    die ID des Stundenplans
	 * @param idLehrer         die ID des Lehrers
	 *
	 * @return die Informationen zu dem angegebenen Lehrer für den angegebenen Stundenplan
	 */
	public static StundenplanLehrer getById(final DBEntityManager conn, final long idStundenplan, final long idLehrer) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, idLehrer);
		if ((lehrer == null) || (lehrer.Sichtbar != null && !lehrer.Sichtbar) || ((lehrer.PersonTyp != PersonalTyp.LEHRKRAFT) && (lehrer.PersonTyp != PersonalTyp.EXTERN)))
			throw OperationError.NOT_FOUND.exception("Es wurde keine Lehrkraft mit der ID %d gefunden.".formatted(idLehrer));
		if ((lehrer.DatumAbgang != null)) {
			// TODO DatumAbgang bei Filterung berücksichtigen, wenn gesetzt
		}
		final StundenplanLehrer daten = dtoMapper.apply(lehrer);
		// TODO Fächer des Lehrers ergänzen - hier besteht die Problematik, dass bei den Fachrichtungen/Lehrbefähigungen in der DB die Statistik-Kürzel stehen und hier Fach-IDs benötigt werden...
		return daten;
	}


	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Lehrer mit der ID null ist unzulässig.");
		final StundenplanLehrer daten = getById(conn, stundenplanID, id);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
