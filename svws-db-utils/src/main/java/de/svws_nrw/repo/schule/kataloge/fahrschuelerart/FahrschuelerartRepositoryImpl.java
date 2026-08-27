package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

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

}
