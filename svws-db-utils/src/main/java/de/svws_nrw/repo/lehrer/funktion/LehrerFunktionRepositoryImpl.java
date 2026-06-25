package de.svws_nrw.repo.lehrer.funktion;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.repo.RepositoryImpl;

public final class LehrerFunktionRepositoryImpl extends RepositoryImpl<DTOLehrerFunktion> implements LehrerFunktionRepository {

	/**
	 * @param conn {@link DBEntityManager}
	 */
	public LehrerFunktionRepositoryImpl(final DBEntityManager conn) {
		super(conn,
				DTOLehrerFunktion.class,
				l -> l.id,
				(l, id) -> l.id = id
		);
	}

	@Override
	public List<DTOLehrerFunktion> findAllByIdAbschnitt(final long idAbschnitt) {
		return conn.queryList(DTOLehrerFunktion.QUERY_BY_IDABSCHNITTSDATEN, DTOLehrerFunktion.class, idAbschnitt);
	}

	@Override
	public boolean existsByIdAbschnittAndIdFunktion(final long idAbschnitt, final long idFunktion) {
		return conn.existsBy(
				"SELECT e FROM DTOLehrerFunktion e WHERE e.idAbschnittsdaten = ?1 AND e.idFunktion = ?2",
				DTOLehrerFunktion.class,
				idAbschnitt,
				idFunktion
		);
	}

	@Override
	public boolean existsByIdAbschnittAndIdFunktionExcludingId(final long idAbschnitt, final long idFunktion, final long excludeId) {
		return conn.existsBy(
				"SELECT e FROM DTOLehrerFunktion e WHERE e.idAbschnittsdaten = ?1 AND e.idFunktion = ?2 AND e.id != ?3",
				DTOLehrerFunktion.class,
				idAbschnitt,
				idFunktion,
				excludeId
		);
	}

}
