package de.svws_nrw.repo.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenVorgaben;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die GOSt-Klausurvorgaben.
 */
public final class GostKlausurenVorgabeRepositoryImpl extends RepositoryImpl<DTOGostKlausurenVorgaben>
		implements GostKlausurenVorgabeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenVorgabeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenVorgaben.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenVorgaben> getListByAbiturjahr(final int abiturjahr) {
		return conn.queryList(DTOGostKlausurenVorgaben.QUERY_BY_ABI_JAHRGANG, DTOGostKlausurenVorgaben.class, abiturjahr);
	}

	@Override
	public List<DTOGostKlausurenVorgaben> getListByAbiturjahrAndHalbjahre(final int abiturjahr, final Collection<GostHalbjahr> halbjahre) {
		return conn.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr IN :hj",
				DTOGostKlausurenVorgaben.class)
				.setParameter("jgid", abiturjahr)
				.setParameter("hj", halbjahre)
				.getResultList();
	}


	@Override
	public List<DTOGostKlausurenVorgaben> getListByAbiturjahrgaenge(final Collection<Integer> abiturjahrgaenge) {
		final List<Integer> tmp = abiturjahrgaenge.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTOGostKlausurenVorgaben.QUERY_LIST_BY_ABI_JAHRGANG, DTOGostKlausurenVorgaben.class, tmp);
	}

	@Override
	public Map<Integer, List<DTOGostKlausurenVorgaben>> getMapByAbiturjahrgaenge(final Collection<Integer> abiturjahrgaenge) {
		return this.getListByAbiturjahrgaenge(abiturjahrgaenge).stream().collect(Collectors.groupingBy(kv -> kv.Abi_Jahrgang));
	}


}
