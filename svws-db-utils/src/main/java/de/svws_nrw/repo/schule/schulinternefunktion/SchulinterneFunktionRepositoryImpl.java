package de.svws_nrw.repo.schule.schulinternefunktion;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchulinterneFunktion;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchulinterneFunktionRepositoryImpl extends RepositoryImpl<DTOSchulinterneFunktion> implements SchulinterneFunktionRepository {
	/**
	 * Erstellt eine neue Instanz.
	 *
	 * @param conn die Datenbankverbindung
	 */
	public SchulinterneFunktionRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchulinterneFunktion.class, l -> l.id, (l, id) -> l.id = id);
	}

}
