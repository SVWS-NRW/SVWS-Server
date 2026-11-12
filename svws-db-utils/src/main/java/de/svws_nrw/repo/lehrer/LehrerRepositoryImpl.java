package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Lehrerdaten.
 */
public final class LehrerRepositoryImpl extends RepositoryImpl<DTOLehrer> implements LehrerRepository {

	private static final String QUERY_ALL_IDS = "SELECT e.ID FROM DTOLehrer e ORDER BY e.ID";
	private static final String QUERY_BY_KUERZEL_EXCLUDING_ID = "SELECT e FROM DTOLehrer e WHERE e.Kuerzel = ?1 AND e.ID <> ?2";
	private static final String QUERY_IDS_BY_IDS = "SELECT e.ID FROM DTOLehrer e WHERE e.ID IN ?1";

	/** Die Abfragen auf die Tabellen, in denen Lehrer-IDs referenziert werden. Jede Abfrage liefert die referenzierten Lehrer-IDs aus {@code :ids}. */
	private static final List<String> QUERIES_REFERENCED_IDS = List.of(
			"SELECT DISTINCT a.Lehrer_ID FROM DTOKurs a WHERE a.Lehrer_ID IN :ids",
			"SELECT DISTINCT b.Fachlehrer_ID FROM DTOSchuelerLeistungsdaten b WHERE b.Fachlehrer_ID IN :ids",
			"SELECT DISTINCT c.Tutor_ID FROM DTOSchuelerLernabschnittsdaten c WHERE c.Tutor_ID IN :ids",
			"SELECT DISTINCT d.Lehrer_ID FROM DTOKlassenLeitung d WHERE d.Lehrer_ID IN :ids",
			"SELECT DISTINCT e.Lehrer_ID FROM DTOKursLehrer e WHERE e.Lehrer_ID IN :ids",
			"SELECT DISTINCT f.LehrerID FROM DTOSchulleitung f WHERE f.LehrerID IN :ids",
			"SELECT DISTINCT g.idBetreuungslehrer FROM DTOSchuelerBetrieb g WHERE g.idBetreuungslehrer IN :ids");

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrer.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	/**
	 * Gibt eine Liste aller Statistik-relevanten Lehrer zurück.
	 *
	 * @return die Liste mit den Statistik-relevanten Lehrer-DB-DTOs
	 */
	@Override
	public List<DTOLehrer> getAllStatistikRelevant() {
		return conn.queryList(DTOLehrer.QUERY_BY_STATISTIKRELEVANT, DTOLehrer.class, true);
	}

	@Override
	public boolean existsById(final Long idLehrer) {
		return conn.existsBy(DTOLehrer.QUERY_BY_ID, DTOLehrer.class, idLehrer);
	}

	@Override
	public Set<Long> existsByIds(final Collection<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		return new HashSet<>(conn.queryList(QUERY_IDS_BY_IDS, Long.class, ids));
	}

	@Override
	public List<Long> getAllIds() {
		return conn.queryList(QUERY_ALL_IDS, Long.class);
	}

	@Override
	public boolean existsByKuerzel(final String kuerzel) {
		return conn.existsBy(DTOLehrer.QUERY_BY_KUERZEL, DTOLehrer.class, kuerzel);
	}

	@Override
	public boolean existsByKuerzelExcludingId(final String kuerzel, final long idLehrer) {
		return conn.existsBy(QUERY_BY_KUERZEL_EXCLUDING_ID, DTOLehrer.class, kuerzel, idLehrer);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final var query = buildUnionAllQuery(QUERIES_REFERENCED_IDS);
		final var result = conn.query(query, Long.class)
				.setParameter("ids", idsToCheck)
				.getResultList();

		return new HashSet<>(result);
	}

	private static String buildUnionAllQuery(final List<String> queries) {
		return String.join("\nUNION ALL\n", queries);
	}

}
