package de.svws_nrw.repo.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten der Zusatzerhebungen zur amtlichen
 * Schulstatistik. Da der Schlüssel die ID des Schuljahresabschnitts ist, wird beim Anlegen keine neue ID vergeben.
 */
public final class ZusatzerhebungenStatistikRepositoryImpl extends RepositoryBaseImpl<DTOStatistikZusatzerhebungen, Long>
		implements ZusatzerhebungenStatistikRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public ZusatzerhebungenStatistikRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOStatistikZusatzerhebungen.class);
	}

	@Override
	protected Object[] mapIdToParameter(final Long id) {
		return new Object[] { id };
	}

}
