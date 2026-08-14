package de.svws_nrw.repo.schule.kataloge.ort;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Ortsdaten.
 */
public final class OrtRepositoryImpl extends RepositoryImpl<DTOOrt> implements OrtRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public OrtRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOOrt.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
		if ((idsToCheck == null) || idsToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final String lehrer = "SELECT DISTINCT a.Ort_ID FROM DTOLehrer a WHERE a.Ort_ID IN :ids";
		final String schueler = "SELECT DISTINCT b.Ort_ID FROM DTOSchueler b WHERE b.Ort_ID IN :ids";
		final String erzieher = "SELECT DISTINCT c.ErzOrt_ID FROM DTOSchuelerErzieherAdresse c WHERE c.ErzOrt_ID IN :ids";
		final String betriebe = "SELECT DISTINCT d.ort_id FROM DTOBetrieb d WHERE d.ort_id IN :ids";
		final String ortsteile = "SELECT DISTINCT e.Ort_ID FROM DTOOrtsteil e WHERE e.Ort_ID IN :ids";
		final String query = String.join("\nUNION ALL\n", lehrer, schueler, erzieher, betriebe, ortsteile);

		return new HashSet<>(conn.query(query, Long.class)
				.setParameter("ids", idsToCheck)
				.getResultList());
	}

	@Override
	public boolean ortsnameIsUniqueForPlzCreate(final String ortsname, final String plz) {
		final String query = "SELECT o FROM DTOOrt o WHERE LOWER(o.ortsname) = LOWER(?1) AND LOWER(o.plz) = LOWER(?2)";
		return !conn.existsBy(query, DTOOrt.class, ortsname, plz);
	}

	@Override
	public boolean ortsnameIsUniqueForPlzPatch(final String ortsname, final String plz, final long idOrt) {
		final String query = "SELECT o FROM DTOOrt o WHERE LOWER(o.ortsname) = LOWER(?1) AND LOWER(o.plz) = LOWER(?2) AND o.id != ?3";
		return !conn.existsBy(query, DTOOrt.class, ortsname, plz, idOrt);
	}

}
