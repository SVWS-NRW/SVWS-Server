package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.ApiOperationException;
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
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einem Stundenplan mit der ID null ist unzulässig.");
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, id);
		if (stundenplan == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(id));
		final List<StundenplanLehrer> lehrer = DataStundenplanLehrer.getLehrer(conn, id);
		final List<StundenplanSchueler> schueler = DataStundenplanSchueler.getSchueler(conn, id);
		final List<StundenplanFach> faecher = DataStundenplanFaecher.getFaecher(conn, id);
		final List<StundenplanKlasse> klassen = DataStundenplanKlassen.getKlassen(conn, id);
		final List<StundenplanKurs> kurse = DataStundenplanKurse.getKurse(conn, id);
		final List<StundenplanKlassenunterricht> klassenunterricht = DataStundenplanKlassenunterricht.getKlassenunterrichte(conn, id);
		// Prüfe, ob bei den Klassen oder Kursen Lehrer zugeordnet sind, deren ID in der Lehrer-Liste nicht vorhanden ist und füge diese ggf. hinzu
		final List<Long> idsLehrer = lehrer.stream().map(l -> l.id).toList();
		final List<Long> idsLehrerFehlende = Stream.concat(
				kurse.stream().flatMap(k -> k.lehrer.stream()),
				klassenunterricht.stream().flatMap(ku -> ku.lehrer.stream()))
				.filter(l -> !idsLehrer.contains(l)).toList();
		if (!idsLehrerFehlende.isEmpty()) {
			final List<DTOLehrer> lehrerFehlende = conn.queryByKeyList(DTOLehrer.class, idsLehrerFehlende);
			if (lehrerFehlende.size() != idsLehrerFehlende.size())
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Nicht alle Lehrer des Stundenplans mit der ID %d konnten auch in der Lehrer-Tabelle gefunden werden.".formatted(id));
			lehrer.addAll(lehrerFehlende.stream().map(l -> {
				final StundenplanLehrer sl = DataStundenplanLehrer.dtoMapper.apply(l);
				sl.kuerzel = "*" + sl.kuerzel;
				return sl;
			}).toList());
		}
		// Prüfe, ob bei den Klassen oder Kursen Schüler zugeordnet sind, deren ID in der Schüler-Liste nicht vorhanden ist und füge diese ggf. hinzu
		final List<Long> idsSchueler = schueler.stream().map(l -> l.id).toList();
		final List<Long> idsSchuelerFehlende = Stream.concat(Stream.concat(
				kurse.stream().flatMap(k -> k.schueler.stream()),
				klassenunterricht.stream().flatMap(ku -> ku.schueler.stream())),
				klassen.stream().flatMap(k -> k.schueler.stream()))
				.filter(s -> !idsSchueler.contains(s)).toList();
		if (!idsSchuelerFehlende.isEmpty()) {
			final List<DTOSchueler> schuelerFehlende = conn.queryByKeyList(DTOSchueler.class, idsSchuelerFehlende);
			if (schuelerFehlende.size() != idsSchuelerFehlende.size())
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Nicht alle Schüler des Stundenplans mit der ID %d konnten auch in der Schüler-Tabelle gefunden werden.".formatted(id));
			schueler.addAll(schuelerFehlende.stream().map(s -> DataStundenplanSchueler.dtoMapper.apply(s)).toList());
		}
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
