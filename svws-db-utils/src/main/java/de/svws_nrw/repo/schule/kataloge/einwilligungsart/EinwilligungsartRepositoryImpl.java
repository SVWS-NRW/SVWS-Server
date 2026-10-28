package de.svws_nrw.repo.schule.kataloge.einwilligungsart;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Einwilligungsarten-Katalog.
 */
public final class EinwilligungsartRepositoryImpl extends RepositoryImpl<DTOEinwilligungsart> implements EinwilligungsartRepository {

	private static final String QUERY_IDS_BY_PERSONTYP =
			"SELECT e.id FROM DTOEinwilligungsart e WHERE e.personTyp = ?1 ORDER BY e.id";
	private static final String QUERY_EXISTS_BY_ID_AND_PERSONTYP =
			"SELECT e FROM DTOEinwilligungsart e WHERE e.id = ?1 AND e.personTyp = ?2";
	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.bezeichnung) = LOWER(?1) AND e.personTyp = ?2";
	private static final String QUERY_BEZEICHNUNG_PATCH =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.bezeichnung) = LOWER(?1) AND e.personTyp = ?2 AND e.id != ?3";
	private static final String QUERY_SCHLUESSEL_CREATE =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.schluessel) = LOWER(?1) AND e.personTyp = ?2";
	private static final String QUERY_SCHLUESSEL_PATCH =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.schluessel) = LOWER(?1) AND e.personTyp = ?2 AND e.id != ?3";

	/** Die Abfragen der Einwilligungen von Schülern, Lehrern und Erziehern, welche auf Einwilligungsarten verweisen */
	private static final List<String> QUERIES_REFERENCED_IDS = List.of(
			"SELECT DISTINCT e.Datenschutz_ID FROM DTOSchuelerDatenschutz e WHERE e.Datenschutz_ID IN :ids",
			"SELECT DISTINCT e.DatenschutzID FROM DTOLehrerDatenschutz e WHERE e.DatenschutzID IN :ids",
			"SELECT DISTINCT e.DatenschutzID FROM DTOErzieherDatenschutz e WHERE e.DatenschutzID IN :ids");

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public EinwilligungsartRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOEinwilligungsart.class, e -> e.id, (e, id) -> e.id = id);
	}

	@Override
	public List<Long> getAllIdsByPersonTyp(final PersonTyp personTyp) {
		return conn.queryList(QUERY_IDS_BY_PERSONTYP, Long.class, personTyp);
	}

	@Override
	public boolean existsByIdAndPersonTyp(final long idEinwilligungsart, final PersonTyp personTyp) {
		return conn.existsBy(QUERY_EXISTS_BY_ID_AND_PERSONTYP, DTOEinwilligungsart.class, idEinwilligungsart, personTyp);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedCreate(final String bezeichnung, final PersonTyp personTyp) {
		return conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOEinwilligungsart.class, bezeichnung, personTyp);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedPatch(final String bezeichnung, final PersonTyp personTyp, final long id) {
		return conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOEinwilligungsart.class, bezeichnung, personTyp, id);
	}

	@Override
	public boolean schluesselIsAlreadyUsedCreate(final String schluessel, final PersonTyp personTyp) {
		return conn.existsBy(QUERY_SCHLUESSEL_CREATE, DTOEinwilligungsart.class, schluessel, personTyp);
	}

	@Override
	public boolean schluesselIsAlreadyUsedPatch(final String schluessel, final PersonTyp personTyp, final long id) {
		return conn.existsBy(QUERY_SCHLUESSEL_PATCH, DTOEinwilligungsart.class, schluessel, personTyp, id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}
		final Set<Long> result = new HashSet<>();
		for (final String query : QUERIES_REFERENCED_IDS) {
			result.addAll(conn.query(query, Long.class).setParameter("ids", idsToCheck).getResultList());
		}
		return result;
	}

}
