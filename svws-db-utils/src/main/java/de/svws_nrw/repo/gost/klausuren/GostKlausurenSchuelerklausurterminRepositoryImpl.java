package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Schülerklausurtermine.
 */
public final class GostKlausurenSchuelerklausurterminRepositoryImpl extends RepositoryImpl<DTOGostKlausurenSchuelerklausurenTermine>
		implements GostKlausurenSchuelerklausurterminRepository {

	private static final String QUERY_LIST_BY_TATSAECHLICHER_TERMIN_ID = """
			SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt
			JOIN DTOGostKlausurenSchuelerklausuren sk ON sk.ID = skt.Schuelerklausur_ID
			JOIN DTOGostKlausurenKursklausuren kk ON kk.ID = sk.Kursklausur_ID
			WHERE (skt.Folge_Nr > 0 AND skt.Termin_ID IN ?1) OR (skt.Folge_Nr = 0 AND kk.Termin_ID IN ?1)
			""";

	private static final String QUERY_LIST_HAUPTTERMINE_BY_KURSKLAUSUR_ID = """
			SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt
			JOIN DTOGostKlausurenSchuelerklausuren sk ON sk.ID = skt.Schuelerklausur_ID
			WHERE sk.Kursklausur_ID IN ?1 AND skt.Folge_Nr = 0
			""";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenSchuelerklausurterminRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenSchuelerklausurenTermine.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermine> getListBySchuelerklausurIds(final Collection<Long> schuelerklausurIds) {
		if ((schuelerklausurIds == null) || schuelerklausurIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_SCHUELERKLAUSUR_ID,
				DTOGostKlausurenSchuelerklausurenTermine.class, schuelerklausurIds);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermine> getListByTerminIds(final Collection<Long> terminIds) {
		if ((terminIds == null) || terminIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(QUERY_LIST_BY_TATSAECHLICHER_TERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermine.class, terminIds);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermine> getListByGesetztenTerminIds(final Collection<Long> terminIds) {
		if ((terminIds == null) || terminIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_TERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermine.class, terminIds);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermine> getListHaupttermineByKursklausurIds(final Collection<Long> kursklausurIds) {
		if ((kursklausurIds == null) || kursklausurIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(QUERY_LIST_HAUPTTERMINE_BY_KURSKLAUSUR_ID,
				DTOGostKlausurenSchuelerklausurenTermine.class, kursklausurIds);
	}

}
