package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Fahrschülerarten.
 */
public final class FahrschuelerartRepositoryImpl extends RepositoryImpl<DTOFahrschuelerart> implements FahrschuelerartRepository {

	private static final String QUERY_BEZEICHNUNG_CREATE = "SELECT f FROM DTOFahrschuelerart f WHERE LOWER(f.bezeichnung) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_PATCH = "SELECT f FROM DTOFahrschuelerart f WHERE LOWER(f.bezeichnung) = LOWER(?1) AND f.id != ?2";
	private static final String QUERY_REFERENCED_IDS = "SELECT DISTINCT s.Fahrschueler_ID FROM DTOSchueler s WHERE s.Fahrschueler_ID IN :ids";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FahrschuelerartRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFahrschuelerart.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedCreate(final String bezeichnung) {
		return conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFahrschuelerart.class, bezeichnung);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedPatch(final String bezeichnung, final long id) {
		return conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFahrschuelerart.class, bezeichnung, id);
	}

	@Override
	public boolean existsById(final Long idFahrschuelerart) {
		return conn.existsBy(DTOFahrschuelerart.QUERY_BY_ID, DTOFahrschuelerart.class, idFahrschuelerart);
	}

	@Override
	public Set<Long> existsByIds(final Collection<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		return conn.queryList(DTOFahrschuelerart.QUERY_LIST_PK, DTOFahrschuelerart.class, ids)
				.stream()
				.map(f -> f.id)
				.collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}
		return conn.query(QUERY_REFERENCED_IDS, Long.class)
				.setParameter("ids", idsToCheck)
				.getResultList()
				.stream()
				.collect(Collectors.toSet());
	}

}
