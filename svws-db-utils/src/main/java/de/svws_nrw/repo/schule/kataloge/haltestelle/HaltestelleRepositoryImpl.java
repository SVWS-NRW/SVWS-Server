package de.svws_nrw.repo.schule.kataloge.haltestelle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Haltestellen.
 */
public final class HaltestelleRepositoryImpl extends RepositoryImpl<DTOHaltestellen> implements HaltestelleRepository {

	/** Die Datenbankabfrage für die IDs der Haltestellen, die von Schülern referenziert werden. */
	private static final String QUERY_REFERENCED_IDS = "SELECT DISTINCT a.Haltestelle_ID FROM DTOSchueler a WHERE a.Haltestelle_ID IN :ids";

	/** Die Datenbankabfrage, ob eine Haltestelle mit der Bezeichnung existiert (ohne Beachtung der Groß-/Kleinschreibung). */
	private static final String QUERY_BEZEICHNUNG_EXISTS = "SELECT h FROM DTOHaltestellen h WHERE LOWER(h.bezeichnung) = LOWER(?1)";

	/** Die Datenbankabfrage, ob eine andere Haltestelle mit der Bezeichnung existiert (ohne Beachtung der Groß-/Kleinschreibung). */
	private static final String QUERY_BEZEICHNUNG_EXISTS_EXCEPT_ID = QUERY_BEZEICHNUNG_EXISTS + " AND h.id != ?2";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public HaltestelleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOHaltestellen.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public boolean bezeichnungExists(final String bezeichnung) {
		return conn.existsBy(QUERY_BEZEICHNUNG_EXISTS, DTOHaltestellen.class, bezeichnung);
	}

	@Override
	public boolean bezeichnungIstBereitsVergebenExceptId(final String bezeichnung, final long id) {
		return conn.existsBy(QUERY_BEZEICHNUNG_EXISTS_EXCEPT_ID, DTOHaltestellen.class, bezeichnung, id);
	}

	@Override
	public boolean existsById(final Long idHaltestelle) {
		return conn.existsBy(DTOHaltestellen.QUERY_BY_ID, DTOHaltestellen.class, idHaltestelle);
	}

	@Override
	public Set<Long> existsByIds(final Collection<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		return conn.queryList(DTOHaltestellen.QUERY_LIST_PK, DTOHaltestellen.class, ids)
				.stream()
				.map(h -> h.id)
				.collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final List<Long> results = this.conn.query(QUERY_REFERENCED_IDS, Long.class).setParameter("ids", idsToCheck).getResultList();
		return new HashSet<>(results);
	}

}
