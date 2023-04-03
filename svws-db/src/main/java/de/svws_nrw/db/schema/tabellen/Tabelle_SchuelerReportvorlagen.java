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
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchuelerReportvorlagen");
		setJavaComment("Wird im Gruppenprozess \"Zugeordnete Zeugnisformulare drucken\" verwendet. Die Tabelle wird temporär mit den IDs der Schüler gefüllt, die im Gruppenprozess berücksichtigt werden müssen.");
	}

}
