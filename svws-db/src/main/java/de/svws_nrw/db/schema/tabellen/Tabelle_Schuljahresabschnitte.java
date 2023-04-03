package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleTrigger;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schuljahresabschnitte.
 */
public class Tabelle_Schuljahresabschnitte extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Schuljahr des Schuljahresabschnitts (z.B. 2012 für 2012/13)");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Abschnitt des Schuljahresabschnitts");

	/** Die Definition der Tabellenspalte VorigerAbschnitt_ID */
	public SchemaTabelleSpalte col_VorigerAbschnitt_ID = add("VorigerAbschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des vorigen Schuljahresabschnitts");

	/** Die Definition der Tabellenspalte FolgeAbschnitt_ID */
	public SchemaTabelleSpalte col_FolgeAbschnitt_ID = add("FolgeAbschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des nachfolgenden Schuljahresabschnitts");


	/** Trigger t_INSERT_EIGENESCHULE_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_EIGENESCHULE_ABSCHNITTE = addTrigger(
			"t_INSERT_EIGENESCHULE_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON EigeneSchule FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Schuljahr IS NOT NULL AND NEW.SchuljahrAbschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Schuljahr AND Schuljahresabschnitte.Abschnitt = NEW.SchuljahrAbschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Schuljahr, NEW.SchuljahrAbschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Schuljahr AND Schuljahresabschnitte.Abschnitt = NEW.SchuljahrAbschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
	    		END IF;
			END
			""",
			Schema.tab_EigeneSchule)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELER_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELER_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELER_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON Schueler FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.AktSchuljahr IS NOT NULL AND NEW.AktAbschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.AktSchuljahr AND Schuljahresabschnitte.Abschnitt = NEW.AktAbschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.AktSchuljahr, NEW.AktAbschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.AktSchuljahr AND Schuljahresabschnitte.Abschnitt = NEW.AktAbschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_Schueler)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERLERNABSCHNITTDATEN_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERLERNABSCHNITTDATEN_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERLERNABSCHNITTDATEN_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerLernabschnittsdaten FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_SchuelerLernabschnittsdaten)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERABITUR_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERABITUR_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERABITUR_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerAbitur FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_SchuelerAbitur)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERANKREUZFLOSKELN_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERANKREUZFLOSKELN_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERANKREUZFLOSKELN_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerAnkreuzfloskeln FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT SchuelerLernabschnittsdaten.ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.Schueler_ID = NEW.Schueler_ID AND SchuelerLernabschnittsdaten.Jahr = NEW.Jahr AND SchuelerLernabschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_SchuelerAnkreuzfloskeln)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERBKABSCHLUSS_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERBKABSCHLUSS_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERBKABSCHLUSS_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerBKAbschluss FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_SchuelerBKAbschluss)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERBKFAECHER_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERBKFAECHER_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERBKFAECHER_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerBKFaecher FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_SchuelerBKFaecher)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERFOERDEREMPFEHLUNGEN_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERFOERDEREMPFEHLUNGEN_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERFOERDEREMPFEHLUNGEN_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerFoerderempfehlungen FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT SchuelerLernabschnittsdaten.ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.Schueler_ID = NEW.Schueler_ID AND SchuelerLernabschnittsdaten.Jahr = NEW.Jahr AND SchuelerLernabschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_SchuelerFoerderempfehlungen)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_SCHUELERKAOADATEN_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_SCHUELERKAOADATEN_ABSCHNITTE = addTrigger(
			"t_INSERT_SCHUELERKAOADATEN_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON SchuelerKAoADaten FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT SchuelerLernabschnittsdaten.ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.Schueler_ID = NEW.Schueler_ID AND SchuelerLernabschnittsdaten.Jahr = NEW.Jahr AND SchuelerLernabschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_SchuelerKAoADaten)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_LEHRERLERNABSCHNITTSDATEN_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_LEHRERLERNABSCHNITTSDATEN_ABSCHNITTE = addTrigger(
			"t_INSERT_LEHRERLERNABSCHNITTSDATEN_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON LehrerAbschnittsdaten FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_LehrerAbschnittsdaten)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_LEHRERANRECHNUNG_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_LEHRERANRECHNUNG_ABSCHNITTE = addTrigger(
			"t_INSERT_LEHRERANRECHNUNG_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON LehrerAnrechnung FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT LehrerAbschnittsdaten.ID FROM LehrerAbschnittsdaten WHERE LehrerAbschnittsdaten.Lehrer_ID = NEW.Lehrer_ID AND LehrerAbschnittsdaten.Jahr = NEW.Jahr AND LehrerAbschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_LehrerAnrechnung)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_LEHRERENTLASTUNG_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_LEHRERENTLASTUNG_ABSCHNITTE = addTrigger(
			"t_INSERT_LEHRERENTLASTUNG_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON LehrerEntlastung FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT LehrerAbschnittsdaten.ID FROM LehrerAbschnittsdaten WHERE LehrerAbschnittsdaten.Lehrer_ID = NEW.Lehrer_ID AND LehrerAbschnittsdaten.Jahr = NEW.Jahr AND LehrerAbschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_LehrerEntlastung)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_LEHRERFUNKTIONEN_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_LEHRERFUNKTIONEN_ABSCHNITTE = addTrigger(
			"t_INSERT_LEHRERFUNKTIONEN_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON LehrerFunktionen FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT LehrerAbschnittsdaten.ID FROM LehrerAbschnittsdaten WHERE LehrerAbschnittsdaten.Lehrer_ID = NEW.Lehrer_ID AND LehrerAbschnittsdaten.Jahr = NEW.Jahr AND LehrerAbschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_LehrerFunktionen)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_LEHRERMEHRLEISTUNG_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_LEHRERMEHRLEISTUNG_ABSCHNITTE = addTrigger(
			"t_INSERT_LEHRERMEHRLEISTUNG_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON LehrerMehrleistung FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
					END IF;
					SET id := (SELECT LehrerAbschnittsdaten.ID FROM LehrerAbschnittsdaten WHERE LehrerAbschnittsdaten.Lehrer_ID = NEW.Lehrer_ID AND LehrerAbschnittsdaten.Jahr = NEW.Jahr AND LehrerAbschnittsdaten.Abschnitt = NEW.Abschnitt);
					IF id IS NOT NULL THEN
						SET NEW.Abschnitt_ID = id;
					END IF;
				END IF;
			END
			""",
			Schema.tab_LehrerMehrleistung)
			.setVeraltet(SchemaRevisionen.REV_1);

	/** Trigger t_INSERT_KURSE_ABSCHNITTE */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_KURSE_ABSCHNITTE = addTrigger(
			"t_INSERT_KURSE_ABSCHNITTE",
			DBDriver.MARIA_DB,
			"""
			BEFORE INSERT ON Kurse FOR EACH ROW
			BEGIN
				DECLARE id INTEGER;
				IF NEW.Jahr IS NOT NULL AND NEW.Abschnitt IS NOT NULL THEN
					SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					IF id IS NULL THEN
						INSERT INTO Schuljahresabschnitte(Jahr, Abschnitt) VALUES (NEW.Jahr, NEW.Abschnitt);
						SET id := (SELECT Schuljahresabschnitte.ID FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Jahr = NEW.Jahr AND Schuljahresabschnitte.Abschnitt = NEW.Abschnitt);
					END IF;
					SET NEW.Schuljahresabschnitts_ID = id;
				END IF;
			END
			""",
			Schema.tab_Kurse)
			.setVeraltet(SchemaRevisionen.REV_1);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schuljahresabschnitte.
	 */
	public Tabelle_Schuljahresabschnitte() {
		super("Schuljahresabschnitte", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchuljahresabschnitte");
		setJavaComment("Tabelle mit den in der DB angelegten Schuljahresabschnitten ");
	}

}
