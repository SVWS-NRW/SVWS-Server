package de.svws_nrw.repo.lehrer.leitungsfunktion;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.repo.RepositoryImpl;

public final class LehrerLeitungsfunktionRepositoryImpl extends RepositoryImpl<DTOLeitungsfunktion> implements LehrerLeitungsfunktionRepository {
	/**
	 * Erstellt eine neue Instanz.
	 *
	 * @param conn die Datenbankverbindung
	 */
	public LehrerLeitungsfunktionRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLeitungsfunktion.class, l -> l.ID, (l, id) -> l.ID = id);
	}

}
