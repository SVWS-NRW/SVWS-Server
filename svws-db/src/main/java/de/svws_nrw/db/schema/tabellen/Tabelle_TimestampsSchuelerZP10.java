package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleTrigger;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TimestampsSchuelerZP10.
 */
public class Tabelle_TimestampsSchuelerZP10 extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Facheintrags für den ZP10 Abschluss");

	/** Die Definition der Tabellenspalte tsVornote */
	public final SchemaTabelleSpalte col_tsVornote = add("tsVornote", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Vornote.");

	/** Die Definition der Tabellenspalte tsNoteSchriftlichePruefung */
	public final SchemaTabelleSpalte col_tsNoteSchriftlichePruefung = add("tsNoteSchriftlichePruefung", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Note zur schriftlichen Prüfung.");

	/** Die Definition der Tabellenspalte tsMuendlichePruefung */
	public final SchemaTabelleSpalte col_tsMuendlichePruefung = add("tsMuendlichePruefung", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung zu der Information, ob eine mündliche Prüfung nötig ist oder nicht.");

	/** Die Definition der Tabellenspalte tsMuendlichePruefungFreiwillig */
	public final SchemaTabelleSpalte col_tsMuendlichePruefungFreiwillig = add("tsMuendlichePruefungFreiwillig", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung zu der Information, ob eine freiwillige mündliche Prüfung gewählt wurde oder nicht.");

	/** Die Definition der Tabellenspalte tsNoteMuendlichePruefung */
	public final SchemaTabelleSpalte col_tsNoteMuendlichePruefung = add("tsNoteMuendlichePruefung", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Note zur mündlichen Prüfung.");

	/** Die Definition der Tabellenspalte tsAbschlussnote */
	public final SchemaTabelleSpalte col_tsAbschlussnote = add("tsAbschlussnote", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Abschlussnote.");

	/** Die Definition des Fremdschlüssels TimestampsSchuelerTeilleistungen_FK */
	public final SchemaTabelleFremdschluessel fk_TimestampsSchuelerZP10_FK = addForeignKey(
			"TimestampsSchuelerZP10_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID, Schema.tab_SchuelerZP10.col_ID));


	/** Trigger t_INSERT_TimestampsSchuelerZP10 */
	public final SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsSchuelerZP10 = addTrigger(
			"t_INSERT_TimestampsSchuelerZP10",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerZP10 FOR EACH ROW
			INSERT INTO TimestampsSchuelerZP10(ID, tsVornote, tsNoteSchriftlichePruefung, tsMuendlichePruefung, tsMuendlichePruefungFreiwillig, tsNoteMuendlichePruefung, tsAbschlussnote) VALUES (NEW.ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3));
			""", Schema.tab_SchuelerZP10, Schema.tab_TimestampsSchuelerZP10);

	/** Trigger t_UPDATE_TimestampsSchuelerZP10 */
	public final SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerZP10 = addTrigger(
			"t_UPDATE_TimestampsSchuelerZP10",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerZP10 FOR EACH ROW
			BEGIN
			    IF (OLD.Vornote IS NULL AND NEW.Vornote IS NOT NULL) OR (OLD.Vornote <> NEW.Vornote) THEN
			        UPDATE TimestampsSchuelerZP10 SET tsVornote = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.NoteSchriftlich IS NULL AND NEW.NoteSchriftlich IS NOT NULL) OR (OLD.NoteSchriftlich <> NEW.NoteSchriftlich) THEN
			        UPDATE TimestampsSchuelerZP10 SET tsNoteSchriftlichePruefung = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.MdlPruefung IS NULL AND NEW.MdlPruefung IS NOT NULL) OR (OLD.MdlPruefung <> NEW.MdlPruefung) THEN
			        UPDATE TimestampsSchuelerZP10 SET tsMuendlichePruefung = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.MdlPruefungFW IS NULL AND NEW.MdlPruefungFW IS NOT NULL) OR (OLD.MdlPruefungFW <> NEW.MdlPruefungFW) THEN
			        UPDATE TimestampsSchuelerZP10 SET tsMuendlichePruefungFreiwillig = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.NoteMuendlich IS NULL AND NEW.NoteMuendlich IS NOT NULL) OR (OLD.NoteMuendlich <> NEW.NoteMuendlich) THEN
			        UPDATE TimestampsSchuelerZP10 SET tsNoteMuendlichePruefung = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.NoteAbschluss IS NULL AND NEW.NoteAbschluss IS NOT NULL) OR (OLD.NoteAbschluss <> NEW.NoteAbschluss) THEN
			        UPDATE TimestampsSchuelerZP10 SET tsAbschlussnote = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			END
			""",
			Schema.tab_SchuelerZP10, Schema.tab_TimestampsSchuelerZP10);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsSchuelerZP10.
	 */
	public Tabelle_TimestampsSchuelerZP10() {
		super("TimestampsSchuelerZP10", SchemaRevisionen.REV_60);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsSchuelerZP10");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an ausgewählten Spalten der Datenbanktabelle für die ZP10 Änderungen"
				+ " vorgenommen wurden.");
	}

}
