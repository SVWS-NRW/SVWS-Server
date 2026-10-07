package de.svws_nrw.repo.schule.kataloge.jahrgang;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Jahrgangsdaten.
 */
public final class JahrgangRepositoryImpl extends RepositoryImpl<DTOJahrgang> implements JahrgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public JahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOJahrgang.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public boolean existsById(final Long idJahrgang) {
		return conn.existsBy(DTOJahrgang.QUERY_BY_ID, DTOJahrgang.class, idJahrgang);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedCreate(final String kuerzel) {
		final String query = "SELECT j FROM DTOJahrgang j WHERE LOWER(j.InternKrz) = LOWER(?1)";
		return conn.existsBy(query, DTOJahrgang.class, kuerzel);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedCreate(final String bezeichnung) {
		final String query = "SELECT j FROM DTOJahrgang j WHERE LOWER(j.ASDBezeichnung) = LOWER(?1)";
		return conn.existsBy(query, DTOJahrgang.class, bezeichnung);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedPatch(final String kuerzel, final long id) {
		final String query = "SELECT j FROM DTOJahrgang j WHERE LOWER(j.InternKrz) = LOWER(?1) AND j.ID != ?2";
		return conn.existsBy(query, DTOJahrgang.class, kuerzel, id);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedPatch(final String bezeichnung, final long id) {
		final String query = "SELECT j FROM DTOJahrgang j WHERE LOWER(j.ASDBezeichnung) = LOWER(?1) AND j.ID != ?2";
		return conn.existsBy(query, DTOJahrgang.class, bezeichnung, id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || (idsToCheck.isEmpty())) {
			return Collections.emptySet();
		}

		final String querySchueler = "SELECT DISTINCT a.Entlassjahrgang_ID FROM DTOSchueler a WHERE a.Entlassjahrgang_ID IN :ids";
		final String querySchuelerLernabschnittsdaten = "SELECT DISTINCT b.Jahrgang_ID FROM DTOSchuelerLernabschnittsdaten b WHERE b.Jahrgang_ID IN :ids";
		final String queryKlassen = "SELECT DISTINCT c.Jahrgang_ID FROM DTOKlassen c WHERE c.Jahrgang_ID IN :ids";
		final String queryStundenplanSchienen = "SELECT DISTINCT d.Jahrgang_ID FROM DTOStundenplanSchienen d WHERE d.Jahrgang_ID IN :ids";
		final String queryKurse = "SELECT DISTINCT e.Jahrgang_ID FROM DTOKurs e WHERE e.Jahrgang_ID IN :ids";

		final String query = String.join("\nUNION ALL\n", querySchueler, querySchuelerLernabschnittsdaten, queryKlassen,
				queryStundenplanSchienen, queryKurse);
		final List<Long> results = conn.query(query, Long.class).setParameter("ids", idsToCheck).getResultList();
		return new HashSet<>(results);

	}
}
