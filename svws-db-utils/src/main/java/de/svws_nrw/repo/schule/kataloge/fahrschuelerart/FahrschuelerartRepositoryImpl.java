package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Fahrschülerarten.
 */
public final class FahrschuelerartRepositoryImpl extends RepositoryImpl<DTOFahrschuelerart> implements FahrschuelerartRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FahrschuelerartRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFahrschuelerart.class, o -> o.ID, (o, id) -> o.ID = id);
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
				.map(f -> f.ID)
				.collect(Collectors.toCollection(HashSet::new));
	}

}
