package de.svws_nrw.repo.schueler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
		super(conn, DTOSchuelerLernabschnittsdaten.class, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<Long>> getMapKlassenSchueler(final Collection<Long> idsKlassen) {
		if ((idsKlassen == null) || (idsKlassen.isEmpty()))
			return Collections.emptyMap();
		final var listAbschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_KLASSEN_ID, DTOSchuelerLernabschnittsdaten.class, idsKlassen);
		final Map<Long, Set<Long>> mapSets = HashMap.newHashMap(idsKlassen.size());
		for (final var la : listAbschnitte)
			mapSets.computeIfAbsent(la.Klassen_ID, k -> new LinkedHashSet<>()).add(la.Schueler_ID);
		final Map<Long, List<Long>> result = HashMap.newHashMap(mapSets.size());
		for (final var entry : mapSets.entrySet()) {
			result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return result;
	}

	@Override
	public Map<Long, DTOSchuelerLernabschnittsdaten> getMapBySchuelerIDsAndSchuljahreabschnitt(final Collection<Long> idsSchueler,
			final long idSchuljahresabschnitt) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty()))
			return Collections.emptyMap();
		final var listAbschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.Schuljahresabschnitts_ID = ?3",
				DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idSchuljahresabschnitt);
		return listAbschnitte.stream().collect(Collectors.toMap(a -> a.Schueler_ID, a -> a));
	}

}
