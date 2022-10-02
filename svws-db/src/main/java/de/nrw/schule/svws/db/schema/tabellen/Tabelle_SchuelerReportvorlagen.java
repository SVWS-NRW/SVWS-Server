package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerReportvorlagen.
 */
public class Tabelle_SchuelerReportvorlagen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte User_ID */
	public SchemaTabelleSpalte col_User_ID = add("User_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("UserID des Users der die zugeordneten Reportvorlagen druckt");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Reportvorlage */
	public SchemaTabelleSpalte col_Reportvorlage = add("Reportvorlage", SchemaDatentypen.VARCHAR, true).setDatenlaenge(255)
		.setJavaComment("Pfad zur Reportvorlage");

	/** Die Definition der Tabellenspalte Schueler_IDs */
	public SchemaTabelleSpalte col_Schueler_IDs = add("Schueler_IDs", SchemaDatentypen.TEXT, false)
		.setJavaComment("SchülerID für die gedruckt werden soll (temporär)");


	/** Die Definition des Fremdschlüssels SchuelerReportvorlagen_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerReportvorlagen_Benutzer_FK = addForeignKey(
			"SchuelerReportvorlagen_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_User_ID, Schema.tab_Benutzer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerReportvorlagen.
	 */
	public Tabelle_SchuelerReportvorlagen() {
		super("SchuelerReportvorlagen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchuelerReportvorlagen");
		setJavaComment("Wird im Gruppenprozess \"Zugeordnete Zeugnisformulare drucken\" verwendet. Die Tabelle wird temporär mit den IDs der Schüler gefüllt, die im Gruppenprozess berücksichtigt werden müssen.");
	}

}
