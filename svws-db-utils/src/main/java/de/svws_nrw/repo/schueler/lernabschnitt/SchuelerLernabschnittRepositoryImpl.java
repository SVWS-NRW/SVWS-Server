package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Lernabschnittsdaten von Schülern.
 */
public final class SchuelerLernabschnittRepositoryImpl extends RepositoryImpl<DTOSchuelerLernabschnittsdaten> implements SchuelerLernabschnittRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLernabschnittRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerLernabschnittsdaten.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<Long>> getMapKlassenSchueler(final Collection<Long> idsKlassen) {
		if ((idsKlassen == null) || (idsKlassen.isEmpty())) {
			return Collections.emptyMap();
		}
		final var listAbschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_KLASSEN_ID, DTOSchuelerLernabschnittsdaten.class, idsKlassen);
		final Map<Long, Set<Long>> mapSets = HashMap.newHashMap(idsKlassen.size());
		for (final var la : listAbschnitte) {
			mapSets.computeIfAbsent(la.Klassen_ID, k -> new LinkedHashSet<>()).add(la.Schueler_ID);
		}
		final Map<Long, List<Long>> result = HashMap.newHashMap(mapSets.size());
		for (final var entry : mapSets.entrySet()) {
			result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return result;
	}

	@Override
	public Map<Long, DTOSchuelerLernabschnittsdaten> getMapBySchuelerIDsAndSchuljahreabschnitt(final Collection<Long> idsSchueler,
			final long idSchuljahresabschnitt) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		final var listAbschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.Schuljahresabschnitts_ID = ?3",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idSchuljahresabschnitt);
		return listAbschnitte.stream().collect(Collectors.toMap(a -> a.Schueler_ID, a -> a));
	}


	@Override
	public Map<Long, DTOSchuelerLernabschnittsdaten> getMapByLernabschnittIDAndSchuljahreabschnitt(final Collection<Long> idsSchueler,
			final long idSchuljahresabschnitt) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		final var listAbschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.Schuljahresabschnitts_ID = ?3",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idSchuljahresabschnitt);
		return listAbschnitte.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
	}


	@Override
	public Map<Long, DTOSchuelerLernabschnittsdaten> getMapByLernabschnittID(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		final var listAbschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0);
		return listAbschnitte.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
	}


	@Override
	public Map<Long, List<Long>> getMapAllLernabschnittIDsBySchuelerIDs(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		final var listAbschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler);
		final Map<Long, List<Long>> result = HashMap.newHashMap(idsSchueler.size());
		for (final var la : listAbschnitte) {
			result.computeIfAbsent(la.Schueler_ID, k -> new ArrayList<>()).add(la.ID);
		}
		return result;
	}


	@Override
	public Optional<DTOSchuelerLernabschnittsdaten> findAktuellBySchuelerID(final Long idSchueler) {
		if (idSchueler == null) {
			return Optional.empty();
		}
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e JOIN DTOSchueler s WHERE s.ID = e.Schueler_ID AND s.Schuljahresabschnitts_ID = e.Schuljahresabschnitts_ID AND e.Schueler_ID = ?1 AND e.WechselNr = ?2",
				DTOSchuelerLernabschnittsdaten.class, idSchueler, 0);
		return lernabschnitte.size() == 1 ? Optional.of(lernabschnitte.getFirst()) : Optional.empty();
	}

	@Override
	public Map<Long, DTOSchuelerLernabschnittsdaten> getMapAktuelleBySchuelerIDs(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		return conn.queryList(
				"""
				SELECT e FROM DTOSchuelerLernabschnittsdaten e JOIN DTOSchueler s WHERE s.ID = e.Schueler_ID
				  AND s.Schuljahresabschnitts_ID = e.Schuljahresabschnitts_ID AND e.Schueler_ID IN ?1 AND e.WechselNr = ?2
				""",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0)
				.stream().collect(Collectors.toMap(la -> la.Schueler_ID, la -> la));
	}


	@Override
	public Map<Long, List<DTOSchuelerLernabschnittsdaten>> getMapGostLernabschnitteBySchuelerIDs(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		final List<String> asdJahrgaenge = List.of("EF", "Q1", "Q2");
		final var listAbschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.ASDJahrgang IN ?3 AND e.SemesterWertung = ?4",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, asdJahrgaenge, true);
		final Map<Long, List<DTOSchuelerLernabschnittsdaten>> result = listAbschnitte.stream().collect(Collectors.groupingBy(a -> a.Schueler_ID));
		for (final Long idSchueler : idsSchueler) {
			if (idSchueler == null) {
				continue;
			}
			result.computeIfAbsent(idSchueler, id -> new ArrayList<DTOSchuelerLernabschnittsdaten>());
		}
		return result;
	}


	@Override
	public List<DTOSchuelerLernabschnittsdaten> getGewerteteAbschnittInASDJahrgang(final long idSchueler, final String asdJahrgang) {
		if (asdJahrgang == null) {
			return Collections.emptyList();
		}
		return conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.ASDJahrgang = ?2 AND e.SemesterWertung = true AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class,
				idSchueler, asdJahrgang);
	}

}
