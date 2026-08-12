package de.svws_nrw.repo.schueler.foto;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.repo.RepositoryImpl;

public class SchuelerFotoRepositoryImpl extends RepositoryImpl<DTOSchuelerFoto>  implements SchuelerFotoRepository {

	/**
	 * Erstellt eine neue Instanz des Repositories mit der angegebenen Datenbankverbindung.
	 *
	 * @param conn die zu verwendende {@link DBEntityManager}-Verbindung
	 */
	public SchuelerFotoRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerFoto.class, f -> f.idSchueler, (f, id) -> f.idSchueler = id);
	}

}
