package de.svws_nrw.repo.schule.kataloge.merkmal;

import java.util.Optional;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.repo.RepositoryImpl;

public final class MerkmalRepositoryImpl extends RepositoryImpl<DTOMerkmale> implements MerkmalRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public MerkmalRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOMerkmale.class, m -> m.id, (m, id) -> m.id = id);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedCreate(final String kuerzel) {
		final String query = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.kuerzel) = LOWER(?1)";
		return conn.existsBy(query, DTOMerkmale.class, kuerzel);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedPatch(final String kuerzel, final long id) {
		final String query = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.kuerzel) = LOWER(?1) AND m.id != ?2";
		return conn.existsBy(query, DTOMerkmale.class, kuerzel, id);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedCreate(final String bezeichnung) {
		final String query = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.bezeichnung) = LOWER(?1)";
		return conn.existsBy(query, DTOMerkmale.class, bezeichnung);
	}

	@Override
	public boolean bezeichnungIsAlreadyUsedPatch(final String bezeichnung, final long id) {
		final String query = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.bezeichnung) = LOWER(?1) AND m.id != ?2";
		return conn.existsBy(query, DTOMerkmale.class, bezeichnung, id);
	}

	@Override
	public Optional<DTOMerkmale> getByKuerzel(final String kuerzel) {
		if (kuerzel == null) {
			return Optional.empty();
		}
		final var result = conn.queryList(DTOMerkmale.QUERY_BY_KUERZEL, DTOMerkmale.class, kuerzel);
		return (result.size() == 1) ? Optional.of(result.getFirst()) : Optional.empty();
	}
}

