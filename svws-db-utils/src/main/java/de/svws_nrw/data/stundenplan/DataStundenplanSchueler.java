package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanSchueler}.
 */
public final class DataStundenplanSchueler extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanSchueler}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Schüler abgefragt werden
	 */
	public DataStundenplanSchueler(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchueler} in einen Core-DTO {@link StundenplanSchueler}.
	 */
	private static final Function<DTOSchueler, StundenplanSchueler> dtoMapper = (final DTOSchueler s) -> {
		final StundenplanSchueler daten = new StundenplanSchueler();
		daten.id = s.ID;
		daten.nachname = s.Nachname;
		daten.vorname = s.Vorname;
		return daten;
	};



	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Schüler, die im Stundenplan vorkommen, zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Schüler
	 */
	public static List<StundenplanSchueler> getSchueler(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		// Bestimme alle Klassen-IDs der Schuljahresabschnitts
		final List<Long> klassenIDs = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", stundenplan.Schuljahresabschnitts_ID, DTOKlassen.class)
				.stream().map(k -> k.ID).toList();
		// Bestimme alle Schüler-IDs von SchuelerLernabschnittsdaten, wo die Klasse zugeordnet ist und der Schuljahresabschnitt übereinstimmt
		final Set<Long> schuelerIDs = new HashSet<>();
		final Map<Long, Long> mapSchuelerKlasse = new HashMap<>();
		if (!klassenIDs.isEmpty()) {
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klassen_ID IN ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, stundenplan.Schuljahresabschnitts_ID, klassenIDs);
			mapSchuelerKlasse.putAll(lernabschnitte.stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l.Klassen_ID)));
			schuelerIDs.addAll(lernabschnitte.stream().map(l -> l.Schueler_ID).toList());
		}
		// Bestimme alle Kurs-IDs der Unterrichte
		final List<Long> kursIDs = conn.queryNamed("DTOKurs.schuljahresabschnitts_id", stundenplan.Schuljahresabschnitts_ID, DTOKurs.class).stream().map(k -> k.ID).toList();
		if (!kursIDs.isEmpty()) {
			final List<Long> kursSchuelerIDs = conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursIDs)
					.stream().map(ks -> ks.Schueler_ID).distinct().toList();
			schuelerIDs.addAll(kursSchuelerIDs);
		}
		// Und bestimme nun die Schüler-Daten...
		final List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		final ArrayList<StundenplanSchueler> daten = new ArrayList<>();
		for (final DTOSchueler s : schuelerListe) {
			// TODO Filtere alle Schüler, die ein Abgangsdatum haben, welches vor dem Beginn des Stundenplans liegt
			final Long idKlasse = mapSchuelerKlasse.get(s.ID);
			if (idKlasse == null)
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Für den Schüler mit der ID %d konnte keine Klassen-ID ermittelt werden.".formatted(s.ID));
			final StundenplanSchueler schueler = dtoMapper.apply(s);
			schueler.idKlasse = idKlasse;
			daten.add(schueler);
		}
		return daten;
	}


	@Override
	public Response getList() {
		final List<StundenplanSchueler> daten = getSchueler(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, stundenplanID);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(stundenplanID));
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Schüler mit der ID null ist unzulässig.");
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Schüler mit der ID %d gefunden.".formatted(id));
		final List<DTOSchuelerLernabschnittsdaten> abschnitte = conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, id, stundenplan.Schuljahresabschnitts_ID);
		if (abschnitte.size() != 1)
			return OperationError.NOT_FOUND.getResponse("Der Schüler mit der ID %d hat keinen oder mehr als einen Lernabschnitt.".formatted(id));
		final DTOSchuelerLernabschnittsdaten abschnitt = abschnitte.get(0);
		final StundenplanSchueler daten = dtoMapper.apply(schueler);
		daten.idKlasse = abschnitt.Klassen_ID;
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
