package de.svws_nrw.repo.schulwechsel.dokument;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselDokument;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchulwechselDokumentRepositoryImpl extends RepositoryImpl<DTOSchulwechselDokument> implements SchulwechselDokumentRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchulwechselDokumentRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchulwechselDokument.class, s -> s.id, (s, id) -> s.id = id);
	}

}
