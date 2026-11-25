package de.svws_nrw.repo.schule.kataloge.teilleistungsart;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Arten von Teilleistungen
 */
public final class TeilleistungsartRepositoryImpl extends RepositoryImpl<DTOTeilleistungsarten> implements TeilleistungsartRepository {

	private static final String QUERY_REFERENCED_IDS = "SELECT DISTINCT a.Art_ID FROM DTOSchuelerTeilleistung a WHERE a.Art_ID IN :ids";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public TeilleistungsartRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTeilleistungsarten.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		return new HashSet<>(conn.query(QUERY_REFERENCED_IDS, Long.class)
				.setParameter("ids", idsToCheck)
				.getResultList());
	}

	@Override
	public boolean existsBy(final String bezeichnung) {
		return conn.existsBy(DTOTeilleistungsarten.QUERY_BY_BEZEICHNUNG, DTOTeilleistungsarten.class, bezeichnung);
	}
}
