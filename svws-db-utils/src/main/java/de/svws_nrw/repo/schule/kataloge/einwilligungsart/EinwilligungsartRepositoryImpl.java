package de.svws_nrw.repo.schule.kataloge.einwilligungsart;

import java.util.List;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Einwilligungsarten-Katalog.
 */
public final class EinwilligungsartRepositoryImpl extends RepositoryImpl<DTOKatalogEinwilligungsart> implements EinwilligungsartRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public EinwilligungsartRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKatalogEinwilligungsart.class, e -> e.ID, (e, id) -> e.ID = id);
	}

	@Override
	public List<Long> getAllIdsByPersonTyp(final PersonTyp personTyp) {
		return conn.queryList(DTOKatalogEinwilligungsart.QUERY_BY_PERSONTYP, DTOKatalogEinwilligungsart.class, personTyp).stream()
				.map(e -> e.ID)
				.sorted()
				.toList();
	}

	@Override
	public boolean existsByIdAndPersonTyp(final long idEinwilligungsart, final PersonTyp personTyp) {
		return conn.existsBy("SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID = ?1 AND e.personTyp = ?2", DTOKatalogEinwilligungsart.class,
				idEinwilligungsart, personTyp);
	}

}
