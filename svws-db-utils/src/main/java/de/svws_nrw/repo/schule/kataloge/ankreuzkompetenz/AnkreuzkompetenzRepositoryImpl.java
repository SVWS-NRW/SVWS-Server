package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Katalog der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzRepositoryImpl extends RepositoryImpl<DTOAnkreuzfloskeln> implements AnkreuzkompetenzRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AnkreuzkompetenzRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAnkreuzfloskeln.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public boolean existsById(final Long idAnkreuzkompetenz) {
		return conn.existsBy(DTOAnkreuzfloskeln.QUERY_BY_ID, DTOAnkreuzfloskeln.class, idAnkreuzkompetenz);
	}

}
