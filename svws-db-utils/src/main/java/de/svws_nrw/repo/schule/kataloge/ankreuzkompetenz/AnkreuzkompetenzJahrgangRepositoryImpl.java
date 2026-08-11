package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Jahrgangszuordnungen für den Katalog der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzJahrgangRepositoryImpl extends RepositoryImpl<DTOAnkreuzkompetenzJahrgang>
		implements AnkreuzkompetenzJahrgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AnkreuzkompetenzJahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAnkreuzkompetenzJahrgang.class, o -> o.id, (o, id) -> o.id = id);
	}

}
