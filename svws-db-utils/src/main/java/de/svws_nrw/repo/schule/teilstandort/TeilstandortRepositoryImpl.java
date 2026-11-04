package de.svws_nrw.repo.schule.teilstandort;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Tabelle für die Teilstandorte.
 */
public final class TeilstandortRepositoryImpl extends RepositoryBaseImpl<DTOTeilstandorte, String>
		implements TeilstandortRepository {

	/**
	 * Erstellt ein neues Repository
	 *
	 *  @param conn     die aktuelle Datenbank-Verbindung
	 */
	public TeilstandortRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTeilstandorte.class);
	}


	/**
	 * Führt ein Mapping von der übergebenen Primary-Key-Klasse auf eine Folge von Objekte
	 * durch, aus welchen sich der Primary zusammensetzt.
	 *
	 * @param id die Primary-Key-Klasse
	 *
	 * @return die einzelnen Attribute des Primary-Keys
	 */
	@Override
	protected Object[] mapIdToParameter(final String id) {
		return new Object[] { id };
	}

	@Override
	public Set<String> getReferencedIds(final Collection<String> adrMerkmale) {
		if ((adrMerkmale == null) || (adrMerkmale.isEmpty())) {
			return Collections.emptySet();
		}
		final String query = "SELECT DISTINCT k.AdrMerkmal FROM DTOKlassen k WHERE k.AdrMerkmal IN :adrMerkmale";

		return new HashSet<>(conn.query(query, String.class)
				.setParameter("adrMerkmale", adrMerkmale)
				.getResultList());
	}

	@Override
	public boolean existsById(final String adrMerkmal) {
		return conn.existsBy(DTOTeilstandorte.QUERY_BY_ADRMERKMAL, DTOTeilstandorte.class, adrMerkmal);
	}
}
