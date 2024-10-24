package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanKlassenunterricht}.
 */
public final class DataStundenplanKlassenunterricht extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanKlassenunterricht}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Kurse abgefragt werden
	 */
	public DataStundenplanKlassenunterricht(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<StundenplanKlassenunterricht> daten = getKlassenunterrichte(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long idKlasse) throws ApiOperationException {
		final List<StundenplanKlassenunterricht> daten = getKlassenunterricht(conn, idKlasse);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long idKlasse, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	private static List<StundenplanKlassenunterricht> getKlassenunterrichteFuerKlassen(final @NotNull DBEntityManager conn, final long idSchuljahresabschnitt,
			final Map<Long, DTOKlassen> mapKlassen) {
		// TODO Man könnte die Daten des Klassenunterrichtes auch aus der Vorlage beziehen, wenn noch keine Lernabschnitte oder Leistungsdaten vorliegen
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(idSchuljahresabschnitt);
		// Bestimme alle Schüler-Lernabschnitte, welche der Klasse zugeordnet sind
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klassen_ID IN ?2 AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class, idSchuljahresabschnitt, mapKlassen.keySet());
		if (lernabschnitte.isEmpty())
			return new ArrayList<>();
		final Map<Long, List<Long>> mapKlassenLernabschnittIDs = lernabschnitte.stream()
				.collect(Collectors.groupingBy(la -> la.Klassen_ID, Collectors.mapping(la -> la.ID, Collectors.toList())));
		final Map<Long, Long> mapLernabschnittSchuelerID = lernabschnitte.stream().collect(Collectors.toMap(la -> la.ID, la -> la.Schueler_ID));
		// Bestimme aus den Lernabschnitte die Klassenunterrichte. Gehe dabei Klassenweise vor...
		final HashMap2D<Long, Long, StundenplanKlassenunterricht> klassenunterrichte = new HashMap2D<>();
		final List<StundenplanKlassenunterricht> daten = new ArrayList<>();
		final Set<Long> faecherIDs = new HashSet<>();
		for (final Map.Entry<Long, List<Long>> entry : mapKlassenLernabschnittIDs.entrySet()) {
			final long klassenID = entry.getKey();
			final List<Long> lernabschnittIDs = entry.getValue();
			if (lernabschnittIDs.isEmpty())
				continue;
			// Bestimme die Leistungsdaten zu den Lernabschnitten, welche keine Kurse sind, d.h. wo der Kurseintrag null, leer oder die Kursart PUK ist
			final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList(
					"SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND e.Kurs_ID IS NULL AND (e.Kursart IS NULL OR e.Kursart = '' OR e.Kursart = '%s')"
							.formatted(ZulaessigeKursart.PUK.daten(schuljahresabschnitt.schuljahr).kuerzel),
					DTOSchuelerLeistungsdaten.class, lernabschnittIDs);
			if (leistungsdaten.isEmpty())
				continue;
			for (final DTOSchuelerLeistungsdaten ls : leistungsdaten) {
				StundenplanKlassenunterricht ku = klassenunterrichte.getOrNull(klassenID, ls.Fach_ID);
				if (ku == null) {
					ku = new StundenplanKlassenunterricht();
					ku.idKlasse = klassenID;
					ku.idFach = ls.Fach_ID;
					ku.wochenstunden = (ls.Wochenstunden == null) ? 1 : ls.Wochenstunden;
					klassenunterrichte.put(ku.idKlasse, ku.idFach, ku);
					daten.add(ku);
					faecherIDs.add(ku.idFach);
				} else {
					if ((ls.Wochenstunden != null) && (ku.wochenstunden < ls.Wochenstunden))
						ku.wochenstunden = ls.Wochenstunden;
				}
				final Long schuelerID = mapLernabschnittSchuelerID.get(ls.Abschnitt_ID);
				if (schuelerID != null)
					ku.schueler.add(schuelerID);
				if ((ls.Fachlehrer_ID != null) && (!ku.lehrer.contains(ls.Fachlehrer_ID)))
					ku.lehrer.add(ls.Fachlehrer_ID);
				if ((ls.Zusatzkraft_ID != null) && (!ku.lehrer.contains(ls.Zusatzkraft_ID)))
					ku.lehrer.add(ls.Zusatzkraft_ID);
			}
		}
		// Ergänze die Fachinformationen
		if (!faecherIDs.isEmpty()) {
			final Map<Long, DTOFach> mapFaecher = conn.queryByKeyList(DTOFach.class, faecherIDs).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
			for (final StundenplanKlassenunterricht ku : daten) {
				final DTOFach fach = mapFaecher.get(ku.idFach);
				final DTOKlassen kl = mapKlassen.get(ku.idKlasse);
				ku.bezeichnung = "%s (%s)".formatted((fach == null) ? "???" : fach.Kuerzel, (kl == null) ? "???" : kl.Klasse);
			}
		}
		return daten;
	}

	/**
	 * Gibt den Klassenunterricht des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Klassenunterrichte
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<StundenplanKlassenunterricht> getKlassenunterrichte(final @NotNull DBEntityManager conn, final long idStundenplan)
			throws ApiOperationException {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		// Klassen bestimmen
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, stundenplan.Schuljahresabschnitts_ID);
		if (klassen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden keine Klassen für den Schuljahresabschnitt des Stundenplans mit der ID %d gefunden.".formatted(idStundenplan));
		final Map<Long, DTOKlassen> mapKlassen = klassen.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		return getKlassenunterrichteFuerKlassen(conn, stundenplan.Schuljahresabschnitts_ID, mapKlassen);
	}


	/**
	 * Gibt den Klassenunterricht für die angegebene Klasse und das angegebene Fach zurück.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idKlasse   die ID der Klasse
	 * @param idFach     die ID des Faches
	 *
	 * @return der Klassenunterricht
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static StundenplanKlassenunterricht getKlassenunterrichtFuerFach(final @NotNull DBEntityManager conn, final long idKlasse, final long idFach)
			throws ApiOperationException {
		// TODO Man könnte die Daten des Klassenunterrichtes auch aus der Vorlage beziehen, wenn noch keine Lernabschnitte oder Leistungsdaten vorliegen
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse mit der ID %d gefunden.".formatted(idKlasse));
		final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
		if (fach == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Fach mit der ID %d gefunden.".formatted(idFach));
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(klasse.Schuljahresabschnitts_ID);
		// Bestimme die Daten anhand der Leistungsdaten, die einem Lernabschnitt der Klasse zugeordnet sind.
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klassen_ID = ?2 AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class, klasse.Schuljahresabschnitts_ID, klasse.ID);
		final List<Long> lernabschnittIDs = lernabschnitte.stream().map(l -> l.ID).toList();
		if (lernabschnittIDs.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Lernabschnitt für die Klasse mit der ID %d gefunden.".formatted(idKlasse));
		final Map<Long, Long> mapLernabschnittSchuelerID = lernabschnitte.stream().collect(Collectors.toMap(la -> la.ID, la -> la.Schueler_ID));
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList(
				"SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND e.Fach_ID = ?2 AND e.Kurs_ID IS NULL AND (e.Kursart IS NULL OR e.Kursart = '' OR e.Kursart = '%s')"
						.formatted(ZulaessigeKursart.PUK.daten(schuljahresabschnitt.schuljahr).kuerzel),
				DTOSchuelerLeistungsdaten.class, lernabschnittIDs, fach.ID);
		if (leistungsdaten.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Leistungsdaten für die Klasse mit der ID %d und das Fach mit der ID %d gefunden.".formatted(klasse.ID, fach.ID));
		// Aggregiere die Klassenunterrichte aus den Leistungsdaten
		StundenplanKlassenunterricht daten = null;
		for (final DTOSchuelerLeistungsdaten ls : leistungsdaten) {
			if (daten == null) {
				daten = new StundenplanKlassenunterricht();
				daten.idKlasse = idKlasse;
				daten.idFach = ls.Fach_ID;
				daten.wochenstunden = (ls.Wochenstunden == null) ? 1 : ls.Wochenstunden;
			} else {
				if ((ls.Wochenstunden != null) && (daten.wochenstunden < ls.Wochenstunden))
					daten.wochenstunden = ls.Wochenstunden;
			}
			final Long schuelerID = mapLernabschnittSchuelerID.get(ls.Abschnitt_ID);
			if (schuelerID != null)
				daten.schueler.add(schuelerID);
			if ((ls.Fachlehrer_ID != null) && (!daten.lehrer.contains(ls.Fachlehrer_ID)))
				daten.lehrer.add(ls.Fachlehrer_ID);
			if ((ls.Zusatzkraft_ID != null) && (!daten.lehrer.contains(ls.Zusatzkraft_ID)))
				daten.lehrer.add(ls.Zusatzkraft_ID);
		}
		return daten;
	}


	/**
	 * Gibt die Liste mit dem Klassenunterricht für die angegebene Klasse zurück.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idKlasse   die ID der Klasse
	 *
	 * @return die Liste mit dem Klassenunterricht
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<StundenplanKlassenunterricht> getKlassenunterricht(final @NotNull DBEntityManager conn, final long idKlasse)
			throws ApiOperationException {
		// Klasse bestimmen
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse mit der ID %d gefunden.".formatted(idKlasse));
		final Map<Long, DTOKlassen> mapKlassen = new HashMap<>();
		mapKlassen.put(klasse.ID, klasse);
		return getKlassenunterrichteFuerKlassen(conn, klasse.Schuljahresabschnitts_ID, mapKlassen);
	}


}
