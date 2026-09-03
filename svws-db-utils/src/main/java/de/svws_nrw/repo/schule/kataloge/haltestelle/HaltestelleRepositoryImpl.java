package de.svws_nrw.repo.schule.kataloge.haltestelle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Haltestellen.
 */
public final class HaltestelleRepositoryImpl extends RepositoryImpl<DTOHaltestellen> implements HaltestelleRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public HaltestelleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOHaltestellen.class, o -> o.ID, (o, id) -> o.ID = id);
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
				.map(h -> h.ID)
				.collect(Collectors.toCollection(HashSet::new));
	}

}
