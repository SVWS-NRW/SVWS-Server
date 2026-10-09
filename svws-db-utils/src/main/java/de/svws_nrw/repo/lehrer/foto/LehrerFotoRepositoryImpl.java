package de.svws_nrw.repo.lehrer.foto;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.repo.RepositoryImpl;

public class LehrerFotoRepositoryImpl extends RepositoryImpl<DTOLehrerFoto> implements LehrerFotoRepository {

	/**
	 * Erstellt eine neue Instanz des Repositories mit der angegebenen Datenbankverbindung.
	 *
	 * @param conn die zu verwendende {@link DBEntityManager}-Verbindung
	 */
	public LehrerFotoRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerFoto.class, f -> f.idLehrer, (f, id) -> f.idLehrer = id);
	}

	/**
	 * Gibt an, dass beim Anlegen eines neuen LehrerFotos keine automatische ID-Vergabe erfolgt.
	 * <p>
	 * {@link DTOLehrerFoto#idLehrer} ist kein Auto-Increment-Primärschlüssel, sondern ein
	 * Fremdschlüssel auf {@code K_Lehrer.ID}. Die ID wird daher bereits vor dem Persistieren
	 * korrekt gesetzt und darf nicht durch {@code getNextID()} überschrieben werden.
	 *
	 * @return stets {@code false}
	 */
	@Override
	protected boolean autoAssignId() {
		return false;
	}

}
