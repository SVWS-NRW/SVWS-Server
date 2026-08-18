package de.svws_nrw.repo.schule.kataloge.fachklasse;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.repo.RepositoryImpl;

public final class FachklasseRepositoryImpl extends RepositoryImpl<DTOFachklassen> implements FachklasseRepository {

	/**
	 * @param conn die Datenbank-Verbindung
	 */
	public FachklasseRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFachklassen.class, f -> f.id, (f, id) -> f.id = id);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedCreate(final String kuerzel) {
		final String query = "SELECT m FROM DTOFachklassen m WHERE LOWER(m.kuerzel) = LOWER(?1)";
		return conn.existsBy(query, DTOFachklassen.class, kuerzel);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedPatch(final String kuerzel, final long id) {
		final String query = "SELECT m FROM DTOFachklassen m WHERE LOWER(m.kuerzel) = LOWER(?1) AND m.id != ?2";
		return conn.existsBy(query, DTOFachklassen.class, kuerzel, id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}
		final String zuordnungReportvorlagen = "SELECT DISTINCT a.Fachklasse_ID FROM DTOZuordnungReportvorlagen a WHERE a.Fachklasse_ID IN :ids";
		final String schueler = "SELECT DISTINCT a.FachklasseNSJ_ID FROM DTOSchueler a WHERE a.FachklasseNSJ_ID IN :ids";
		final String schuelerLernabschnittsdaten = "SELECT DISTINCT a.Fachklasse_ID FROM DTOSchuelerLernabschnittsdaten a WHERE a.Fachklasse_ID IN :ids";
		final String klassen = "SELECT DISTINCT a.Fachklasse_ID FROM DTOKlassen a WHERE a.Fachklasse_ID IN :ids";
		final String query = String.join("\nUNION ALL\n", zuordnungReportvorlagen, schueler, schuelerLernabschnittsdaten, klassen);

		return new HashSet<>(conn.query(query, Long.class)
				.setParameter("ids", idsToCheck)
				.getResultList());
	}

}
