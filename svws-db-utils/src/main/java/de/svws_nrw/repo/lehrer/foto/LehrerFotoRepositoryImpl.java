package de.svws_nrw.repo.lehrer.foto;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.repo.RepositoryImpl;

public class LehrerFotoRepositoryImpl extends RepositoryImpl<DTOLehrerFoto> implements LehrerFotoRepository {

	/**
	 * Erstellt eine neue Instanz des Repositories mit der angegebenen Datenbankverbindung.
	 *
	 * @param conn die zu verwendende {@link DBEntityManager}-Verbindung
	 */
	public LehrerFotoRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerFoto.class, f -> f.idLehrer, (f, id) -> { /* bewusst leer: idLehrer wird vom Aufrufer gesetzt */ });
	}


}
