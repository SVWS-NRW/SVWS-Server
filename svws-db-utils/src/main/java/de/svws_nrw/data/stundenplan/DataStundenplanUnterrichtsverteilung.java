package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanUnterrichtsverteilung}.
 */
public final class DataStundenplanUnterrichtsverteilung extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanUnterrichtsverteilung}.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataStundenplanUnterrichtsverteilung(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Stundenplan mit der ID null ist unzulässig.");
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, id);
		if (stundenplan == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(id));
		final List<StundenplanLehrer> lehrer = DataStundenplanLehrer.getLehrer(conn, id);
		final List<StundenplanSchueler> schueler = DataStundenplanSchueler.getSchueler(conn, id);
		final List<StundenplanFach> faecher = DataStundenplanFaecher.getFaecher(conn, id);
		final List<StundenplanKlasse> klassen = DataStundenplanKlassen.getKlassen(conn, id);
		final List<StundenplanKurs> kurse = DataStundenplanKurse.getKurse(conn, id);
		final List<StundenplanKlassenunterricht> klassenunterricht = DataStundenplanKlassenunterricht.getKlassenunterrichte(conn, id);
		// Erstelle das Core-DTO-Objekt für die Response
		final StundenplanUnterrichtsverteilung daten = new StundenplanUnterrichtsverteilung();
		daten.id = stundenplan.ID;
		daten.lehrer.addAll(lehrer);
		daten.schueler.addAll(schueler);
		daten.faecher.addAll(faecher);
		daten.klassen.addAll(klassen);
		daten.kurse.addAll(kurse);
		daten.klassenunterricht.addAll(klassenunterricht);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


}
