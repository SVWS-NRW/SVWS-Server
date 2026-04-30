package de.svws_nrw.repo.benutzer;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Implementierung des {@link BenutzergruppenMitgliedRepository}-Interfaces.
 * Kapselt alle Datenbankzugriffe auf {@link DTOBenutzergruppenMitglied}-Entitäten.
 */
public final class BenutzergruppenMitgliedRepositoryImpl
		extends RepositoryImpl<DTOBenutzergruppenMitglied>
		implements BenutzergruppenMitgliedRepository {


	/**
	 * Erstellt eine neue Instanz des Repositories.
	 *
	 * @param conn der {@link DBEntityManager} für den Datenbankzugriff
	 */
	public BenutzergruppenMitgliedRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOBenutzergruppenMitglied.class, e -> e.Gruppe_ID, (e, id) -> e.Gruppe_ID = id);
	}

	@Override
	public List<DTOBenutzergruppenMitglied> findByBenutzergruppeId(final long idBenutzergruppe) {
		return conn.queryList(
				DTOBenutzergruppenMitglied.QUERY_BY_GRUPPE_ID,
				DTOBenutzergruppenMitglied.class,
				idBenutzergruppe
		);
	}

	@Override
	public List<DTOBenutzergruppenMitglied> findByBenutzerId(final long idBenutzer) {
		return conn.queryList(
				DTOBenutzergruppenMitglied.QUERY_BY_BENUTZER_ID,
				DTOBenutzergruppenMitglied.class,
				idBenutzer
		);
	}

	@Override
	public boolean hasGroupRights(final long idUser, final long idGruppe) {
		return conn.existsBy(DTOBenutzergruppenMitglied.QUERY_PK, DTOBenutzergruppenMitglied.class, idGruppe, idUser);
	}

}
