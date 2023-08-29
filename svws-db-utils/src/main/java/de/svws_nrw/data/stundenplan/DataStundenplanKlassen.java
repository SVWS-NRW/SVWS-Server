package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtKlasse;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanKlasse}.
 */
public final class DataStundenplanKlassen extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanKlasse}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Klassen abgefragt werden
	 */
	public DataStundenplanKlassen(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKlassen} in
	 * einen Core-DTO {@link StundenplanKlasse}.
	 */
	private static final Function<DTOKlassen, StundenplanKlasse> dtoMapper = (final DTOKlassen k) -> {
		final StundenplanKlasse daten = new StundenplanKlasse();
		daten.id = k.ID;
		daten.kuerzel = k.Klasse;
		daten.bezeichnung = k.Bezeichnung == null ? "" : k.Bezeichnung;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}


	/**
	 * Gibt die Klassen des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Klassen
	 */
	public static List<StundenplanKlasse> getKlassen(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", stundenplan.Schuljahresabschnitts_ID, DTOKlassen.class);
		if (klassen.isEmpty())
			return new ArrayList<>();
		final List<Long> klassenIDs = klassen.stream().map(k -> k.ID).toList();
		final List<Long> jahrgaengsIDs = DataStundenplanJahrgaenge.getJahrgaenge(conn, idStundenplan).stream().map(j -> j.id).toList();
		// Bestimme die Schüler-Lernabschnitte für die Zuordnung der Schüler zu den Klassen
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klassen_ID IN ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, idStundenplan, klassenIDs);
		final Map<Long, List<Long>> mapKlasseSchuelerIDs = lernabschnitte.stream()
				.collect(Collectors.groupingBy(la -> la.Klassen_ID, Collectors.mapping(la -> la.Schueler_ID, Collectors.toList())));
		// Erstelle die Core-DTOs
		final ArrayList<StundenplanKlasse> daten = new ArrayList<>();
		for (final DTOKlassen k : klassen) {
			final StundenplanKlasse klasse = dtoMapper.apply(k);
			if (k.Jahrgang_ID == null) {
				klasse.jahrgaenge.addAll(jahrgaengsIDs);
			} else {
				klasse.jahrgaenge.add(k.Jahrgang_ID);
			}
			final List<Long> schuelerIDs = mapKlasseSchuelerIDs.get(klasse.id);
			if ((schuelerIDs != null) && (!schuelerIDs.isEmpty()))
				klasse.schueler.addAll(schuelerIDs);
			daten.add(klasse);
		}
		return daten;
	}


	@Override
	public Response getList() {
		final List<StundenplanKlasse> daten = getKlassen(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Ermittelt die Informationen zu der angegebenen Klasse für den angegebenen Stundenplan.
	 *
	 * @param conn             die Datenbank-Verbindung
	 * @param idStundenplan    die ID des Stundenplans
	 * @param idKlasse         die ID der Klasse
	 *
	 * @return die Informationen zu der angegebenen Klasse für den angegebenen Stundenplan
	 */
	public static StundenplanKlasse getById(final DBEntityManager conn, final long idStundenplan, final long idKlasse) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Klasse mit der ID %d gefunden.".formatted(idKlasse));
		if (klasse.Schuljahresabschnitts_ID != stundenplan.Schuljahresabschnitts_ID)
			throw OperationError.BAD_REQUEST.exception("Der Schuljahresabschnitt %d der Klasse mit der ID %d stimmt nicht mit dem Schuljahresabschitt %d bei dem Stundenplan mit der ID %d überein."
					.formatted(klasse.Schuljahresabschnitts_ID, klasse.ID, stundenplan.Schuljahresabschnitts_ID, stundenplan.ID));
		// Jahrgänge bestimmen
		final List<Long> jahrgangsIDs = new ArrayList<>();
		if (klasse.Jahrgang_ID == null) {
			jahrgangsIDs.addAll(conn.queryAll(DTOJahrgang.class).stream().map(j -> j.ID).toList());
		} else {
			jahrgangsIDs.add(klasse.Jahrgang_ID);
		}
		// Bestimme die Schüler-Lernabschnitte für die Zuordnung der Schüler zu den Klassen
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klassen_ID = ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, idStundenplan, klasse.ID);
		final List<Long> schuelerIDs = lernabschnitte.stream().map(la -> la.Schueler_ID).distinct().toList();
		// DTO erstellen
		final StundenplanKlasse daten = dtoMapper.apply(klasse);
		daten.jahrgaenge.addAll(jahrgangsIDs);
		daten.schueler.addAll(schuelerIDs);
		return daten;
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Klasse mit der ID null ist unzulässig.");
		final StundenplanKlasse daten = getById(conn, stundenplanID, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt eine Map, in der Klassen den gegebenen UnterrichtIds eines
	 * Stundenplans zugeordnet werden.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param unterrichtIds   die unterrichte, denen die Klassen zugeordnet werden sollen
	 *
	 * @return eine Map, in der allen UnterrichtsIds die Klassen zugeordnet werden
	 */
	public static Map<Long, List<StundenplanKlasse>> getKlassenByUnterrichtIds(final DBEntityManager conn,
			final Long idStundenplan, final List<Long> unterrichtIds) {
		final Map<Long, List<StundenplanKlasse>> result = new HashMap<>();
		if (unterrichtIds == null)
			throw OperationError.NOT_FOUND.exception("Keine Unterricht-IDs gegeben.");
		if (unterrichtIds.isEmpty())
			return result;
		final List<StundenplanKlasse> klassen = DataStundenplanKlassen.getKlassen(conn, idStundenplan);
		if (klassen.isEmpty())
			return result;
		final Map<Long, StundenplanKlasse> klasseById = klassen.stream().collect(Collectors.toMap(k -> k.id, Function.identity()));
		final List<DTOStundenplanUnterrichtKlasse> unterrichtKlassen = conn.queryNamed(
				"DTOStundenplanUnterrichtKlasse.unterricht_id.multiple", unterrichtIds, DTOStundenplanUnterrichtKlasse.class);
		List<StundenplanKlasse> klassenByUnterrichtId;
		for (final DTOStundenplanUnterrichtKlasse unterrichtKlasse : unterrichtKlassen) {
			klassenByUnterrichtId = result.computeIfAbsent(unterrichtKlasse.Unterricht_ID, unterrichtId -> new ArrayList<>());
			klassenByUnterrichtId.add(klasseById.get(unterrichtKlasse.Klasse_ID));
		}
		return result;
	}

}
