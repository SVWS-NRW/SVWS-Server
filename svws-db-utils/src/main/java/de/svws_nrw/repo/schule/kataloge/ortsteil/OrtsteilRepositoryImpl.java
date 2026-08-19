package de.svws_nrw.repo.schule.kataloge.ortsteil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.repo.RepositoryImpl;

public final class OrtsteilRepositoryImpl extends RepositoryImpl<DTOOrtsteil> implements OrtsteilRepository {



	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public OrtsteilRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOOrtsteil.class, o -> o.id, (o, id) -> o.id = id);
	}


	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final String lehrer = "SELECT DISTINCT a.Ortsteil_ID FROM DTOLehrer a WHERE a.Ortsteil_ID IN :ids";
		final String schueler = "SELECT DISTINCT b.Ortsteil_ID FROM DTOSchueler b WHERE b.Ortsteil_ID IN :ids";
		final String erzieher = "SELECT DISTINCT c.ErzOrtsteil_ID FROM DTOSchuelerErzieherAdresse c WHERE c.ErzOrtsteil_ID IN :ids";
		final String betriebe = "SELECT DISTINCT d.ortsteil_id FROM DTOBetrieb d WHERE d.ortsteil_id IN :ids";
		final String query = String.join("\nUNION ALL\n", lehrer, schueler, erzieher, betriebe);

		return new HashSet<>(conn.query(query, Long.class)
				.setParameter("ids", idsToCheck)
				.getResultList());
	}

	@Override
	public boolean ortsteilnameIsUniqueForIdOrtCreate(final String ortsteil, final Long idOrt) {
		final String query = "SELECT o FROM DTOOrtsteil o WHERE LOWER(o.ortsteil) = LOWER(?1) AND o.idOrt = ?2";
		return !conn.existsBy(query, DTOOrtsteil.class, ortsteil, idOrt);
	}

	@Override
	public boolean ortsteilnameIsUniqueForIdOrtPatch(final String ortsteil, final Long idOrt, final long idOrtsteil) {
		final String query = "SELECT o FROM DTOOrtsteil o WHERE LOWER(o.ortsteil) = LOWER(?1) AND o.idOrt = ?2 AND o.id != ?3";
		return !conn.existsBy(query, DTOOrtsteil.class, ortsteil, idOrt, idOrtsteil);
	}
}
