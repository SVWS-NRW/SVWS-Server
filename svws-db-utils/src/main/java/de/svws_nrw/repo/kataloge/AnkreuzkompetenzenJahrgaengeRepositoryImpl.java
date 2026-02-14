package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Jahrgangszuordnungen für den Katalog der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzenJahrgaengeRepositoryImpl extends RepositoryImpl<DTOAnkreuzkompetenzJahrgang>
		implements AnkreuzkompetenzenJahrgaengeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AnkreuzkompetenzenJahrgaengeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAnkreuzkompetenzJahrgang.class, o -> o.id, (o, id) -> o.id = id);
	}

}
