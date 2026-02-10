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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TimestampsLehrerNotenmodulCredentials.
 */
public class Tabelle_TimestampsNotenmodulCredentials extends SchemaTabelle {

	/** Die Definition der Tabellenspalte idLehrer */
	public final SchemaTabelleSpalte col_idLehrer = add("idLehrer", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("die ID des Lehrers");

	/** Die Definition der Tabellenspalte tsPasswordHash */
	public final SchemaTabelleSpalte col_tsPasswordHash = add("tsPasswordHash", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an dem Password-Hash der Notenmodul-Credentials.");


	/** Die Definition des Fremdschlüssels TimestampsNotenmodulCredentials_FK */
	public final SchemaTabelleFremdschluessel fk_TimestampsNotenmodulCredentials_FK = addForeignKey(
			"TimestampsNotenmodulCredentials_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_idLehrer, Schema.tab_Notenmodul_Credentials.col_idLehrer));


	/** Trigger t_INSERT_TimestampsNotenmodulCredentials */
	public final SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsNotenmodulCredentials = addTrigger(
			"t_INSERT_TimestampsNotenmodulCredentials",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON Notenmodul_Credentials FOR EACH ROW
			INSERT INTO TimestampsNotenmodulCredentials(idLehrer, tsPasswordHash) VALUES (NEW.idLehrer, CURTIME(3));
			""", Schema.tab_Notenmodul_Credentials, Schema.tab_TimestampsNotenmodulCredentials);

	/** Trigger t_UPDATE_TimestampsNotenmodulCredentials */
	public final SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsNotenmodulCredentials = addTrigger(
			"t_UPDATE_TimestampsNotenmodulCredentials",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON Notenmodul_Credentials FOR EACH ROW
			BEGIN
			    IF (OLD.passwordHash IS NULL AND NEW.passwordHash IS NOT NULL) OR (OLD.passwordHash <> NEW.passwordHash) THEN
			        UPDATE TimestampsNotenmodulCredentials SET tsPasswordHash = CURTIME(3) WHERE idLehrer = NEW.idLehrer;
			    END IF;
			END
			""",
			Schema.tab_Notenmodul_Credentials, Schema.tab_TimestampsNotenmodulCredentials);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsNotenmodulCredentials.
	 */
	public Tabelle_TimestampsNotenmodulCredentials() {
		super("TimestampsNotenmodulCredentials", SchemaRevisionen.REV_53);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsNotenmodulCredentials");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an der Datenbanktabelle für die Notenmodul-Credentials"
				+ " zuletzt Änderungen vorgenommen wurden.");
	}

}
