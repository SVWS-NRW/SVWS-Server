package de.svws_nrw.repo.schule.logoverwaltung;

import java.util.Optional;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.repo.RepositoryImpl;

public final class LogoverwaltungRepositoryImpl extends RepositoryImpl<DTOLogo> implements LogoverwaltungRepository {

	/**
	 * Erstellt eine neue Instanz.
	 *
	 * @param conn die Datenbankverbindung
	 */
	public LogoverwaltungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLogo.class, l -> l.id, (l, id) -> l.id = id);
	}

	@Override
	public boolean existsByKennung(final ReportingBildDefinition kennung) {
		return conn.existsBy("SELECT e FROM DTOLogo e WHERE e.kennung = ?1", DTOLogo.class, kennung);
	}

	@Override
	public Optional<DTOLogo> findByKennung(final ReportingBildDefinition kennung) {
		return conn.queryFirst(DTOLogo.QUERY_BY_KENNUNG, DTOLogo.class, kennung);
	}

}
