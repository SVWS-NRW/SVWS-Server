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

	/** Die Definition der Tabellenspalte LernabschnittWechselNr */
	public SchemaTabelleSpalte col_LernabschnittWechselNr = add("LernabschnittWechselNr", SchemaDatentypen.SMALLINT, true)
		.setDefault("0")
		.setJavaComment("Wird für Wiederholungen im Laufenden Schuljahresabschnitt genutzt 0=aktueller/neuester Lernabschnitt 1=vor dem ersten Wechsel 2=vor dem zweiten Wechsel usw");

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
			    DECLARE schuelerID BIGINT;
			    DECLARE wechselNr SMALLINT;
			    IF NEW.Kurs_ID IS NOT NULL THEN
			        SELECT Schueler.id, SchuelerLernabschnittsdaten.WechselNr INTO schuelerID, wechselNr FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID;
			        INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID, LernabschnittWechselNr) VALUES (NEW.Kurs_ID, schuelerID, wechselNr);
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
			    DECLARE alteSchuelerID, neueSchuelerID BIGINT;
			    DECLARE alteWechselNr, neueWechselNr SMALLINT;
			    IF NEW.Kurs_ID IS NOT NULL AND OLD.Kurs_ID IS NOT NULL AND OLD.Kurs_ID <> NEW.Kurs_ID THEN
			        SELECT Schueler.id, SchuelerLernabschnittsdaten.WechselNr INTO alteSchuelerID, alteWechselNr FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID;
			        IF OLD.Abschnitt_ID <> NEW.Abschnitt_ID THEN
			            SELECT Schueler.id, SchuelerLernabschnittsdaten.WechselNr INTO neueSchuelerID, neueWechselNr FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID;
			        ELSE
			            SET neueSchuelerID := alteSchuelerID;
			            SET neueWechselNr := alteWechselNr;
			        END IF;
			        UPDATE Kurs_Schueler SET Kurs_Schueler.Kurs_ID = NEW.Kurs_ID, Kurs_Schueler.Schueler_ID = neueSchuelerID, Kurs_Schueler.LernabschnittWechselNr = neueWechselNr WHERE Kurs_Schueler.Kurs_ID = OLD.Kurs_ID AND Kurs_Schueler.Schueler_ID = alteSchuelerID AND Kurs_Schueler.LernabschnittWechselNr = alteWechselNr;
			    ELSEIF NEW.Kurs_ID IS NULL THEN
			        SELECT Schueler.id, SchuelerLernabschnittsdaten.WechselNr INTO alteSchuelerID, alteWechselNr FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID;
			        DELETE FROM Kurs_Schueler WHERE Kurs_Schueler.Kurs_ID = OLD.Kurs_ID AND Kurs_Schueler.Schueler_ID = alteSchuelerID AND Kurs_Schueler.LernabschnittWechselNr = alteWechselNr;
			    ELSEIF OLD.Kurs_ID IS NULL THEN
			        SELECT Schueler.id, SchuelerLernabschnittsdaten.WechselNr INTO neueSchuelerID, neueWechselNr FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID;
			        INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID, LernabschnittWechselNr) VALUES (NEW.Kurs_ID, neueSchuelerID, neueWechselNr);
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
			    DECLARE schuelerID BIGINT;
			    DECLARE wechselNr SMALLINT;
			    IF OLD.Kurs_ID IS NOT NULL THEN
			        SELECT Schueler.id, SchuelerLernabschnittsdaten.WechselNr INTO schuelerID, wechselNr FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID;
			        DELETE FROM Kurs_Schueler WHERE Kurs_Schueler.Kurs_ID = OLD.Kurs_ID AND Kurs_Schueler.Schueler_ID = schuelerID AND Kurs_Schueler.LernabschnittWechselNr = wechselNr;
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
			    INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID, LernabschnittWechselNr) VALUES (NEW.Kurs_ID, (SELECT Schueler.ID FROM SchuelerLernabschnittsdaten JOIN Schueler ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID), (SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID));
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
			        ),
			        LernabschnittWechselNr = (
			            SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID
			        )
			    WHERE Kurs_ID = OLD.Kurs_ID
			        AND Schueler_ID = (
			            SELECT Schueler.id
			            FROM SchuelerLernabschnittsdaten JOIN Schueler
			                ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			        )
			        AND LernabschnittWechselNr = (
			            SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID
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
			    SET Kurs_ID = NEW.Kurs_ID
			    WHERE
			        Kurs_ID = OLD.Kurs_ID AND
			        Schueler_ID = (
			            SELECT Schueler.id
			            FROM SchuelerLernabschnittsdaten JOIN Schueler
			                ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			        ) AND
			        LernabschnittWechselNr = (
			            SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID
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
			        Schueler_ID = (
			            SELECT Schueler.id
			            FROM SchuelerLernabschnittsdaten JOIN Schueler
			                ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			        ) AND
			        LernabschnittWechselNr = (
			            SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID
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
			    INSERT INTO Kurs_Schueler(Kurs_ID, Schueler_ID, LernabschnittWechselNr)
			    VALUES (
			        NEW.Kurs_ID, (
			            SELECT Schueler.id
			            FROM SchuelerLernabschnittsdaten JOIN Schueler
			            ON SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			        ), (
			            SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = NEW.Abschnitt_ID
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
			        Schueler_ID = (
			            SELECT Schueler.id
			            FROM SchuelerLernabschnittsdaten JOIN Schueler
			            ON SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID AND SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			        ) AND
			        LernabschnittWechselNr = (
			            SELECT SchuelerLernabschnittsdaten.WechselNr FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = OLD.Abschnitt_ID
			        )
			    ;
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
		setImportExport(true);
		setJavaSubPackage("schild.kurse");
		setJavaClassName("DTOKursSchueler");
		setJavaComment("Tabelle mit KursSchueler-Zuordnungen für performanteren Zugriff, welcher über Trigger befüllt wird.");
	}

}
