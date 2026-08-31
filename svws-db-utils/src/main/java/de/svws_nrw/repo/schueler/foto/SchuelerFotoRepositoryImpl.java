package de.svws_nrw.repo.schueler.foto;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchuelerFotoRepositoryImpl extends RepositoryImpl<DTOSchuelerFoto>  implements SchuelerFotoRepository {

	/**
	 * Erstellt eine neue Instanz des Repositories mit der angegebenen Datenbankverbindung.
	 *
	 * @param conn die zu verwendende {@link DBEntityManager}-Verbindung
	 */
	public SchuelerFotoRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerFoto.class, f -> f.idSchueler, (f, id) -> f.idSchueler = id);
	}

	/**
	 * Gibt an, dass beim Anlegen eines neuen Schülerfotos keine automatische ID-Vergabe erfolgt.
	 * <p>
	 * {@link DTOSchuelerFoto#idSchueler} ist kein Auto-Increment-Primärschlüssel, sondern ein
	 * Fremdschlüssel auf {@code Schueler.ID}. Die ID wird daher bereits vor dem Persistieren
	 * korrekt gesetzt und darf nicht durch {@code getNextID()} überschrieben werden.
	 *
	 * @return stets {@code false}
	 */
	@Override
	protected boolean autoAssignId() {
		return false;
	}

}
