package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Konfiguration der Stufen
 * und der sonstigen Rubrik bei den Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzenKonfigurationRepositoryImpl extends RepositoryImpl<DTOAnkreuzdaten> implements AnkreuzkompetenzenKonfigurationRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AnkreuzkompetenzenKonfigurationRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAnkreuzdaten.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
