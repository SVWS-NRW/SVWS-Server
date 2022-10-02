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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerDatenschutz.
 */
public class Tabelle_SchuelerDatenschutz extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Fremdschlüssel auf Tabelle Schueler");

	/** Die Definition der Tabellenspalte Datenschutz_ID */
	public SchemaTabelleSpalte col_Datenschutz_ID = add("Datenschutz_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Fremdschlüssel auf den Katalog der DSGVO-Merkmale");

	/** Die Definition der Tabellenspalte Status */
	public SchemaTabelleSpalte col_Status = add("Status", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setNotNull()
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob eine Zustimmung zum Merkmal vorliegt.");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Abgefragt */
	public SchemaTabelleSpalte col_Abgefragt = add("Abgefragt", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Status der Abfrage Datenschutz-Eintrags (true/false)");


	/** Die Definition des Fremdschlüssels SchuelerDatenschutz_K_Datenschutz_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerDatenschutz_K_Datenschutz_FK = addForeignKey(
			"SchuelerDatenschutz_K_Datenschutz_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Datenschutz_ID, Schema.tab_K_Datenschutz.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerDatenschutz_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerDatenschutz_Schueler_FK = addForeignKey(
			"SchuelerDatenschutz_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerDatenschutz.
	 */
	public Tabelle_SchuelerDatenschutz() {
		super("SchuelerDatenschutz", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerDatenschutz");
		setJavaComment("Tabelle für die zum Schüler zugeordneten DSGVO Merkmale");
	}

}
