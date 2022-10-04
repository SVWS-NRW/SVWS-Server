package de.nrw.schule.svws.db.schema.revisionen;

import de.nrw.schule.svws.db.schema.SchemaRevisionUpdateSQL;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

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
	public RevisionNoUpdates(SchemaRevisionen revision) {
		super(revision);
	}

}
