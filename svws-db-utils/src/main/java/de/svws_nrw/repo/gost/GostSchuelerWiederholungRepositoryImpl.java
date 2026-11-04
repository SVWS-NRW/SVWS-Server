package de.svws_nrw.repo.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.repo.RepositoryImpl;


/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Wiederholungen von Schülern im folgenden Abiturjahrgang.
 */
public final class GostSchuelerWiederholungRepositoryImpl extends RepositoryImpl<DTOGostSchuelerWiederholung> implements GostSchuelerWiederholungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	protected GostSchuelerWiederholungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostSchuelerWiederholung.class, o -> o.Schueler_ID, (o, id) -> { /* bewusst leer: die Schüler-ID ist ein Fremdschlüssel und wird vom Aufrufer gesetzt */ });
	}

}
