package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse ist eine Dummy-Klasse für Revisions-Updates
 * die keine SQL-Befehle enthalten.
 */
public class RevisionNoUpdates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für Revisions-Updates ohne
	 * SQL-Befehle.
	 *
	 * @param revision   die Revision
	 */
	public RevisionNoUpdates(final SchemaRevisionen revision) {
		super(revision);
	}

}
