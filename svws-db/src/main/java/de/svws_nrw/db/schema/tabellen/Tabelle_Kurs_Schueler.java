package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Kurs_Schueler.
 */
public class Tabelle_Kurs_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Kurses – verweist auf den Kurs");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Schülers – verweist auf den Schüler");


	/** Die Definition des Fremdschlüssels KursSchueler_Kurse_FK */
	public SchemaTabelleFremdschluessel fk_KursSchueler_Kurse_FK = addForeignKey(
			"KursSchueler_Kurse_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels KursSchueler_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_KursSchueler_Schueler_FK = addForeignKey(
			"KursSchueler_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/** Trigger t_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerLeistungsdaten FOR EACH ROW
			BEGIN
				DECLARE schuelerID INTEGER;
				IF NEW.Kurs_ID IS NOT NULL THEN
					SET schuelerID := (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID);
					INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID) VALUES (NEW.Kurs_ID, schuelerID);
				END IF;
			END
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
			BEGIN
				DECLARE alteSchuelerID, neueSchuelerID INTEGER;
				IF NEW.Kurs_ID IS NOT NULL AND OLD.Kurs_ID IS NOT NULL AND OLD.Kurs_ID <> NEW.Kurs_ID THEN
					SET alteSchuelerID := (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID);
					IF OLD.Abschnitt_ID <> NEW.Abschnitt_ID THEN
						SET neueSchuelerID := (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID);
					ELSE
						SET neueSchuelerID := alteSchuelerID;
					END IF;
					UPDATE Kurs_Schueler SET Kurs_Schueler.Kurs_ID = NEW.Kurs_ID, Kurs_Schueler.Schueler_ID = neueSchuelerID WHERE Kurs_Schueler.Kurs_ID = OLD.Kurs_ID AND Kurs_Schueler.SCHUELER_ID = alteSchuelerID;
				ELSEIF NEW.Kurs_ID IS NULL THEN
					SET alteSchuelerID := (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID);
					DELETE FROM Kurs_Schueler WHERE Kurs_Schueler.Kurs_ID = OLD.Kurs_ID AND Kurs_Schueler.SCHUELER_ID = alteSchuelerID;
				ELSEIF OLD.Kurs_ID IS NULL THEN
					SET neueSchuelerID := (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID);
					INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID) VALUES (NEW.Kurs_ID, neueSchuelerID);
				END IF;
			END
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_MariaDB_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.MARIA_DB,
			"""
			AFTER DELETE ON SchuelerLeistungsdaten FOR EACH ROW
			BEGIN
				DECLARE schuelerID INTEGER;
				IF OLD.Kurs_ID IS NOT NULL THEN
					SET schuelerID := (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID);
					DELETE FROM Kurs_Schueler WHERE Kurs_Schueler.Kurs_ID = OLD.Kurs_ID AND Kurs_Schueler.SCHUELER_ID = schuelerID;
				END IF;
			END
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_SQLite_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.SQLITE,
			"""
			AFTER INSERT ON SchuelerLeistungsdaten FOR EACH ROW
			WHEN NEW.Kurs_ID IS NOT NULL
			BEGIN
				INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID) VALUES (NEW.Kurs_ID, (SELECT Schueler.id FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID));
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_1 */
	public SchemaTabelleTrigger trigger_SQLite_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_1 = addTrigger(
			"t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_1",
			DBDriver.SQLITE,
			"""
			AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
			WHEN NEW.Kurs_ID IS NOT NULL AND
				OLD.Kurs_ID IS NOT NULL AND
				OLD.Kurs_ID <> NEW.Kurs_ID AND
				OLD.Abschnitt_ID <> NEW.Abschnitt_ID
			BEGIN
				UPDATE Kurs_Schueler
				SET
					Kurs_ID = NEW.Kurs_ID,
					Schueler_ID = (
						SELECT Schueler.id
						FROM SchuelerLernabschnittsdaten JOIN Schueler
							ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
					)
				WHERE Kurs_ID = OLD.Kurs_ID
					AND	SCHUELER_ID = (
						SELECT Schueler.id
						FROM SchuelerLernabschnittsdaten JOIN Schueler
							ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
					)
				;
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_2 */
	public SchemaTabelleTrigger trigger_SQLite_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_2 = addTrigger(
			"t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_2",
			DBDriver.SQLITE,
			"""
			AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
			WHEN NEW.Kurs_ID IS NOT NULL AND
				OLD.Kurs_ID IS NOT NULL AND
				OLD.Kurs_ID <> NEW.Kurs_ID AND
				OLD.Abschnitt_ID = NEW.Abschnitt_ID
			BEGIN
				UPDATE Kurs_Schueler
				SET  Kurs_ID = NEW.Kurs_ID
				WHERE
					Kurs_ID = OLD.Kurs_ID AND
					SCHUELER_ID = (
						SELECT Schueler.id
						FROM SchuelerLernabschnittsdaten JOIN Schueler
							ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
					)
				;
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_3 */
	public SchemaTabelleTrigger trigger_SQLite_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_3 = addTrigger(
			"t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_3",
			DBDriver.SQLITE,
			"""
			AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
			WHEN NEW.Kurs_ID IS NULL AND
				OLD.Kurs_ID IS NOT NULL
			BEGIN
				DELETE FROM Kurs_Schueler
				WHERE
					Kurs_ID = OLD.Kurs_ID AND
					SCHUELER_ID = (
						SELECT Schueler.id
						FROM SchuelerLernabschnittsdaten JOIN Schueler
							ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
					)
				;
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_4 */
	public SchemaTabelleTrigger trigger_SQLite_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_4 = addTrigger(
			"t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER_4",
			DBDriver.SQLITE,
			"""
			AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
			WHEN NEW.Kurs_ID IS NOT NULL AND
				OLD.Kurs_ID IS NULL
			BEGIN
				INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID)
				VALUES (
					NEW.Kurs_ID, (
						SELECT Schueler.id
						FROM SchuelerLernabschnittsdaten JOIN Schueler
						ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
					)
				);
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_SQLite_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.SQLITE,
			"""
			AFTER DELETE ON SchuelerLeistungsdaten FOR EACH ROW
			WHEN OLD.Kurs_ID IS NOT NULL
			BEGIN
				DELETE FROM Kurs_Schueler
				WHERE
					Kurs_ID = OLD.Kurs_ID AND
					SCHUELER_ID = (
						SELECT Schueler.id
						FROM SchuelerLernabschnittsdaten JOIN Schueler
						ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
					)
				;
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);


	/** Trigger t_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_MSSQL_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_INSERT_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.MSSQL,
			"""
			ON SchuelerLeistungsdaten AFTER INSERT AS
			BEGIN
			INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID)
				SELECT i.Kurs_ID, sla.Schueler_ID
				FROM Inserted i JOIN SchuelerLernabschnittsdaten sla ON i.Abschnitt_ID = sla.ID
				WHERE i.Kurs_ID IS NOT NULL
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_MSSQL_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_UPDATE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.MSSQL,
			"""
			ON SchuelerLeistungsdaten AFTER UPDATE AS
			BEGIN
				DELETE Kurs_Schueler FROM
					Inserted i JOIN Deleted d ON i.ID = d.ID AND i.Kurs_ID IS NULL AND d.Kurs_ID IS NOT NULL
					JOIN SchuelerLernabschnittsdaten sla ON sla.ID = d.Abschnitt_ID
					JOIN Kurs_Schueler ks ON ks.Kurs_ID = d.Kurs_ID AND ks.Schueler_ID = sla.Schueler_ID
				INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID)
					SELECT i.Kurs_ID, sla.Schueler_ID FROM
						Inserted i JOIN Deleted d ON i.ID = d.ID AND i.Kurs_ID IS NOT NULL AND d.Kurs_ID IS NULL
						JOIN SchuelerLernabschnittsdaten sla ON sla.ID = i.Abschnitt_ID
				UPDATE Kurs_Schueler
					SET Kurs_ID = i.Kurs_ID, Schueler_ID = sla.Schueler_ID
					FROM
						Inserted i JOIN Deleted d ON i.ID = d.ID AND i.Kurs_ID IS NOT NULL AND d.Kurs_ID IS NOT NULL AND i.Kurs_ID <> d.Kurs_ID AND i.Abschnitt_ID = d.Abschnitt_ID
						JOIN SchuelerLernabschnittsdaten sla ON sla.ID = d.Abschnitt_ID
				UPDATE Kurs_Schueler
					SET Kurs_ID = i.Kurs_ID, Schueler_ID = sla.Schueler_ID
					FROM
						Inserted i JOIN Deleted d ON i.ID = d.ID AND i.Kurs_ID IS NOT NULL AND d.Kurs_ID IS NOT NULL AND i.Kurs_ID <> d.Kurs_ID AND i.Abschnitt_ID <> d.Abschnitt_ID
						JOIN SchuelerLernabschnittsdaten sla ON sla.ID = i.Abschnitt_ID
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);

	/** Trigger t_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER */
	public SchemaTabelleTrigger trigger_MSSQL_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER = addTrigger(
			"t_DELETE_SCHUELERLEISTUNGSDATEN_KURS_SCHUELER",
			DBDriver.MSSQL,
			"""
			ON SchuelerLeistungsdaten AFTER DELETE AS
			BEGIN
				DELETE Kurs_Schueler FROM
					Deleted d
					JOIN SchuelerLernabschnittsdaten sla ON sla.ID = d.Abschnitt_ID AND d.Kurs_ID IS NOT NULL
					JOIN Kurs_Schueler ks ON ks.Kurs_ID = d.Kurs_ID AND ks.Schueler_ID = sla.Schueler_ID
			END;
			""",
			Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten)
			.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Kurs_Schueler.
	 */
	public Tabelle_Kurs_Schueler() {
		super("Kurs_Schueler", SchemaRevisionen.REV_1);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.kurse");
		setJavaClassName("DTOKursSchueler");
		setJavaComment("Tabelle mit KursSchueler-Zuordnungen wird ab Schild3.0 getriggert für performanteren Druck");
	}

}
