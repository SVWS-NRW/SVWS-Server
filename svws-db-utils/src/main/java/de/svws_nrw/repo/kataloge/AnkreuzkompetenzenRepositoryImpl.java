package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Katalog der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzenRepositoryImpl extends RepositoryImpl<DTOAnkreuzfloskeln> implements AnkreuzkompetenzenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AnkreuzkompetenzenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAnkreuzfloskeln.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
