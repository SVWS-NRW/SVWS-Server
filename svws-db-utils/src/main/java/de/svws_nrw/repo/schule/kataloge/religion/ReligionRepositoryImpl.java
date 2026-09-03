package de.svws_nrw.repo.schule.kataloge.religion;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Konfessionen.
 */
public final class ReligionRepositoryImpl extends RepositoryImpl<DTOReligion> implements ReligionRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public ReligionRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOReligion.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public boolean bezeichnungIstBereitsVergeben(final String bezeichnung) {
		final String query = "SELECT m FROM DTOReligion m WHERE LOWER(m.bezeichnung) = LOWER(?1)";
		return conn.existsBy(query, DTOReligion.class, bezeichnung);
	}

	@Override
	public boolean bezeichnungIstBereitsVergebenExceptId(final String bezeichnung, final long id) {
		final String query = """
            SELECT m FROM DTOReligion m
            WHERE LOWER(m.bezeichnung) = LOWER(?1)
              AND m.id != ?2
            """;
		return conn.existsBy(query, DTOReligion.class, bezeichnung, id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT s.Religion_ID FROM DTOSchueler s WHERE s.Religion_ID IN :ids";
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", idsToCheck).getResultList();
		return new HashSet<>(results);
	}

	@Override
	public boolean existsById(final Long idReligion) {
		return conn.existsBy(DTOReligion.QUERY_BY_ID, DTOReligion.class, idReligion);
	}

	@Override
	public Set<Long> existsByIds(final Collection<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		return conn.queryList(DTOReligion.QUERY_LIST_PK, DTOReligion.class, ids)
				.stream()
				.map(r -> r.id)
				.collect(Collectors.toCollection(HashSet::new));
	}

}
