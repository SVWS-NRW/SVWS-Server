package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Logins.
 */
public class Tabelle_Logins extends SchemaTabelle {

	/** Die Definition der Tabellenspalte LI_UserID */
	public SchemaTabelleSpalte col_LI_UserID = add("LI_UserID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("UserID des Logins");

	/** Die Definition der Tabellenspalte LI_LoginTime */
	public SchemaTabelleSpalte col_LI_LoginTime = add("LI_LoginTime", SchemaDatentypen.DATETIME, true)
		.setJavaComment("Login Zeit");

	/** Die Definition der Tabellenspalte LI_LogoffTime */
	public SchemaTabelleSpalte col_LI_LogoffTime = add("LI_LogoffTime", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Logoff Zeit");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels Logins_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_Logins_Benutzer_FK = addForeignKey(
			"Logins_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_LI_UserID, Schema.tab_Benutzer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Logins.
	 */
	public Tabelle_Logins() {
		super("Logins", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOProtokollLogin");
		setJavaComment("Tabelle zur Speicherung der LogIns mit Benutzernamen und Zeit für die Datenbank-User");
	}

}
