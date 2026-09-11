package de.svws_nrw.repo.uv.lerngruppen;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Lerngruppen
 * (Tabelle UV_Lerngruppen).
 */
public final class UvLerngruppeRepositoryImpl extends RepositoryImpl<DTOUvLerngruppe> implements UvLerngruppeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLerngruppeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLerngruppe.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvLerngruppe> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvLerngruppe.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvLerngruppe.class, idPlanungsabschnitt);
	}

	@Override
	public List<DTOUvLerngruppe> getListByKlassenIds(final Collection<Long> idsKlassen) {
		return idsKlassen.isEmpty() ? List.of() : conn.queryList(DTOUvLerngruppe.QUERY_LIST_BY_KLASSE_ID, DTOUvLerngruppe.class, idsKlassen);
	}

	@Override
	public List<DTOUvLerngruppe> getListByKursIds(final Collection<Long> idsKurse) {
		return idsKurse.isEmpty() ? List.of() : conn.queryList(DTOUvLerngruppe.QUERY_LIST_BY_KURS_ID, DTOUvLerngruppe.class, idsKurse);
	}

}
