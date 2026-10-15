package de.svws_nrw.repo.schulwechsel.erfolgreiche_bewerbung;

import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Implementierung des {@link SchulwechselErfolgreicheBewerbungRepository}.
 */
public final class SchulwechselErfolgreicheBewerbungRepositoryImpl
		extends RepositoryImpl<DTOSchulwechselErfolgreicheBewerbung>
		implements SchulwechselErfolgreicheBewerbungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public SchulwechselErfolgreicheBewerbungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchulwechselErfolgreicheBewerbung.class, s -> s.id, (s, id) -> s.id = id);
	}

	@Override
	public boolean existsByIdSchulkindSchulbewerbung(final String idSchulkindSchulbewerbung) {
		return !conn.queryList(
				DTOSchulwechselErfolgreicheBewerbung.QUERY_BY_IDSCHULKINDSCHULBEWERBUNG,
				DTOSchulwechselErfolgreicheBewerbung.class,
				idSchulkindSchulbewerbung
		).isEmpty();
	}

	@Override
	public Set<String> findAllIdSchulkindSchulbewerbung() {
		return conn.queryAll(DTOSchulwechselErfolgreicheBewerbung.class)
				.stream()
				.map(e -> e.idSchulkindSchulbewerbung)
				.collect(Collectors.toSet());
	}

}
