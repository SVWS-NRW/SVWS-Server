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
public class Tabelle_TimestampsLehrerNotenmodulCredentials extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("die ID des Lehrers");

	/** Die Definition der Tabellenspalte tsPasswordHash */
	public SchemaTabelleSpalte col_tsPasswordHash = add("tsPasswordHash", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an dem Password-Hash der Lehrer-Notenmodul-Credentials.");


	/** Die Definition des Fremdschlüssels TimestampsLehrerNotenmodulCredentials_FK */
	public SchemaTabelleFremdschluessel fk_TimestampsLehrerNotenmodulCredentials_FK = addForeignKey(
			"TimestampsLehrerNotenmodulCredentials_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_LehrerNotenmodulCredentials.col_Lehrer_ID));


	/** Trigger t_INSERT_TimestampsLehrerNotenmodulCredentials */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsLehrerNotenmodulCredentials = addTrigger(
			"t_INSERT_TimestampsLehrerNotenmodulCredentials",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON LehrerNotenmodulCredentials FOR EACH ROW
			INSERT INTO TimestampsLehrerNotenmodulCredentials(Lehrer_ID, tsPasswordHash) VALUES (NEW.Lehrer_ID, CURTIME(3));
			""", Schema.tab_LehrerNotenmodulCredentials, Schema.tab_TimestampsLehrerNotenmodulCredentials);

	/** Trigger t_UPDATE_TimestampsLehrerNotenmodulCredentials */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsLehrerNotenmodulCredentials = addTrigger(
			"t_UPDATE_TimestampsLehrerNotenmodulCredentials",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON LehrerNotenmodulCredentials FOR EACH ROW
			BEGIN
			    IF (OLD.PasswordHash IS NULL AND NEW.PasswordHash IS NOT NULL) OR (OLD.PasswordHash <> NEW.PasswordHash) THEN
			        UPDATE TimestampsLehrerNotenmodulCredentials SET tsPasswordHash = CURTIME(3) WHERE Lehrer_ID = NEW.Lehrer_ID;
			    END IF;
			END
			""",
			Schema.tab_LehrerNotenmodulCredentials, Schema.tab_TimestampsLehrerNotenmodulCredentials);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsLehrerNotenmodulCredentials.
	 */
	public Tabelle_TimestampsLehrerNotenmodulCredentials() {
		super("TimestampsLehrerNotenmodulCredentials", SchemaRevisionen.REV_26);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsLehrerNotenmodulCredentials");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an der Datenbanktabelle für die Lehrer-Notenmodul-Credentials"
				+ " zuletzt Änderungen vorgenommen wurden.");
	}

}
