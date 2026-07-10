package de.svws_nrw.repo.schule.kataloge.fachklasse;

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
	public boolean bezeichnungIsAlreadyUsedCreate(final String bezeichnung) {
		final String query = "SELECT m FROM DTOFachklassen m WHERE LOWER(m.bezeichnung) = LOWER(?1)";
		return conn.existsBy(query, DTOFachklassen.class, bezeichnung);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedPatch(final String bezeichnung, final long id) {
		final String query = "SELECT m FROM DTOFachklassen m WHERE LOWER(m.bezeichnung) = LOWER(?1) AND m.id != ?2";
		return conn.existsBy(query, DTOFachklassen.class, bezeichnung, id);
	}
}
