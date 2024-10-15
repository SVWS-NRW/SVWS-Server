package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 28.
 */
public class Revision28Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 28.
	 */
	public Revision28Updates() {
		super(SchemaRevisionen.REV_28);
		updateZeitstempel();
	}

	private void updateZeitstempel() {
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmLernabschnittsdaten.trigger_MariaDB_INSERT_EnmLernabschnittsdaten.name()),
				Schema.tab_EnmLernabschnittsdaten.trigger_MariaDB_INSERT_EnmLernabschnittsdaten.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_EnmLernabschnittsdaten, Schema.tab_SchuelerLernabschnittsdaten
		);
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmLernabschnittsdaten.trigger_MariaDB_UPDATE_EnmLernabschnittsdaten.name()),
				Schema.tab_EnmLernabschnittsdaten.trigger_MariaDB_UPDATE_EnmLernabschnittsdaten.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_EnmLernabschnittsdaten, Schema.tab_SchuelerLernabschnittsdaten
		);
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmLernabschnittsdaten.trigger_MariaDB_UPDATE_EnmLernabschnittsdaten_Bemerkungen.name()),
				Schema.tab_EnmLernabschnittsdaten.trigger_MariaDB_UPDATE_EnmLernabschnittsdaten_Bemerkungen.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_EnmLernabschnittsdaten, Schema.tab_SchuelerLD_PSFachBem
		);
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmLeistungsdaten.trigger_MariaDB_INSERT_EnmLeistungsdaten.name()),
				Schema.tab_EnmLeistungsdaten.trigger_MariaDB_INSERT_EnmLeistungsdaten.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_SchuelerLeistungsdaten, Schema.tab_EnmLeistungsdaten
		);
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmLeistungsdaten.trigger_MariaDB_UPDATE_EnmLeistungsdaten.name()),
				Schema.tab_EnmLeistungsdaten.trigger_MariaDB_UPDATE_EnmLeistungsdaten.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_SchuelerLeistungsdaten, Schema.tab_EnmLeistungsdaten
		);
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmTeilleistungen.trigger_MariaDB_INSERT_EnmTeilleistungen.name()),
				Schema.tab_EnmTeilleistungen.trigger_MariaDB_INSERT_EnmTeilleistungen.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_SchuelerEinzelleistungen, Schema.tab_EnmTeilleistungen
		);
		add("Entferne alten Trigger %s.".formatted(Schema.tab_EnmTeilleistungen.trigger_MariaDB_UPDATE_EnmTeilleistungen.name()),
				Schema.tab_EnmTeilleistungen.trigger_MariaDB_UPDATE_EnmTeilleistungen.getSQL(DBDriver.MARIA_DB, false),
				Schema.tab_SchuelerEinzelleistungen, Schema.tab_EnmTeilleistungen
		);
	}

}
