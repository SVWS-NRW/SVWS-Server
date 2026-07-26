package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenTermine;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Klausurtermine.
 */
public final class GostKlausurenTerminRepositoryImpl extends RepositoryImpl<DTOGostKlausurenTermine>
		implements GostKlausurenTerminRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenTerminRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenTermine.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenTermine> getListByAbiturjahrAndHalbjahre(final int abiturjahr, final Collection<GostHalbjahr> halbjahre) {
		if ((halbjahre == null) || halbjahre.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid AND t.Halbjahr IN :hj",
				DTOGostKlausurenTermine.class)
				.setParameter("jgid", abiturjahr)
				.setParameter("hj", halbjahre)
				.getResultList();
	}

	@Override
	public List<DTOGostKlausurenTermine> getListByAbiturjahr(final int abiturjahr) {
		return conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid", DTOGostKlausurenTermine.class)
				.setParameter("jgid", abiturjahr)
				.getResultList();
	}

	@Override
	public List<DTOGostKlausurenTermine> getListByIds(final Collection<Long> ids) {
		return findListByIds(ids);
	}

	@Override
	public List<DTOGostKlausurenTermine> getListByDatum(final Collection<String> datum) {
		if ((datum == null) || datum.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenTermine.QUERY_LIST_BY_DATUM, DTOGostKlausurenTermine.class, datum);
	}

}
