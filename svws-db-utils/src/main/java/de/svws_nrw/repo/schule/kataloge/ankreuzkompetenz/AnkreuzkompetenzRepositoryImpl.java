package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Katalog der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzRepositoryImpl extends RepositoryImpl<DTOAnkreuzkompetenz> implements AnkreuzkompetenzRepository {

	private static final String QUERY_FLOSKELTEXT_MIT_FACH =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach = ?2";

	private static final String QUERY_FLOSKELTEXT_OHNE_FACH =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach IS NULL";

	private static final String QUERY_FLOSKELTEXT_MIT_FACH_EXCEPT_ID =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach = ?2 AND a.id != ?3";

	private static final String QUERY_FLOSKELTEXT_OHNE_FACH_EXCEPT_ID =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach IS NULL AND a.id != ?2";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AnkreuzkompetenzRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAnkreuzkompetenz.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public boolean existsById(final Long idAnkreuzkompetenz) {
		return conn.existsBy(DTOAnkreuzkompetenz.QUERY_BY_ID, DTOAnkreuzkompetenz.class, idAnkreuzkompetenz);
	}

	@Override
	public boolean floskelTextIsAlreadyUsedCreate(final String floskelText, final Long idFach) {
		if (idFach == null) {
			return conn.existsBy(QUERY_FLOSKELTEXT_OHNE_FACH, DTOAnkreuzkompetenz.class, floskelText);
		}
		return conn.existsBy(QUERY_FLOSKELTEXT_MIT_FACH, DTOAnkreuzkompetenz.class, floskelText, idFach);
	}

	@Override
	public boolean floskelTextIsAlreadyUsedPatch(final String floskelText, final Long idFach, final long id) {
		if (idFach == null) {
			return conn.existsBy(QUERY_FLOSKELTEXT_OHNE_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, floskelText, id);
		}
		return conn.existsBy(QUERY_FLOSKELTEXT_MIT_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, floskelText, idFach, id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final var query = "SELECT DISTINCT a.Floskel_ID FROM DTOSchuelerAnkreuzfloskeln a WHERE a.Floskel_ID IN :ids";
		final var results = conn.query(query, Long.class).setParameter("ids", idsToCheck).getResultList();
		return new HashSet<>(results);
	}

}
