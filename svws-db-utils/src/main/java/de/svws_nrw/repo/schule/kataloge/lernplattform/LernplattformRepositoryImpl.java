package de.svws_nrw.repo.schule.kataloge.lernplattform;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Lernplattformen-Katalog.
 */
public final class LernplattformRepositoryImpl extends RepositoryImpl<DTOLernplattformen> implements LernplattformRepository {

	private static final String QUERY_BEZEICHNUNG_CREATE = "SELECT l FROM DTOLernplattformen l WHERE LOWER(l.bezeichnung) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_PATCH = "SELECT l FROM DTOLernplattformen l WHERE LOWER(l.bezeichnung) = LOWER(?1) AND l.id != ?2";

	/** Die Abfragen der Zuordnungen und Zugangsdaten, welche auf Lernplattformen verweisen */
	private static final List<String> QUERIES_REFERENCED_IDS = List.of(
			"SELECT DISTINCT e.LernplattformID FROM DTOSchuelerLernplattform e WHERE e.LernplattformID IN :ids",
			"SELECT DISTINCT e.LernplattformID FROM DTOLehrerLernplattform e WHERE e.LernplattformID IN :ids",
			"SELECT DISTINCT e.LernplattformID FROM DTOErzieherLernplattform e WHERE e.LernplattformID IN :ids",
			"SELECT DISTINCT e.LernplattformID FROM DTOCredentialsLernplattformen e WHERE e.LernplattformID IN :ids");

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LernplattformRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLernplattformen.class, l -> l.id, (l, id) -> l.id = id);
	}

	@Override
	public List<Long> getAllIds() {
		return conn.queryList("SELECT e.id FROM DTOLernplattformen e ORDER BY e.id", Long.class);
	}

	@Override
	public boolean existsById(final Long idLernplattform) {
		return conn.existsBy(DTOLernplattformen.QUERY_BY_ID, DTOLernplattformen.class, idLernplattform);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedCreate(final String bezeichnung) {
		return conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOLernplattformen.class, bezeichnung);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedPatch(final String bezeichnung, final long id) {
		return conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOLernplattformen.class, bezeichnung, id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}
		final Set<Long> result = new HashSet<>();
		for (final String query : QUERIES_REFERENCED_IDS) {
			result.addAll(conn.query(query, Long.class).setParameter("ids", idsToCheck).getResultList());
		}
		return result;
	}

}
