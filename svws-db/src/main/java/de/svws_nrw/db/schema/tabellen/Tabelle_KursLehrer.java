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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KursLehrer.
 */
public class Tabelle_KursLehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Kurses zu denen der Lehrer gehört");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Lehrers");

	/** Die Definition der Tabellenspalte Anteil */
	public SchemaTabelleSpalte col_Anteil = add("Anteil", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Wochenstunden für die Zusatzkraft");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels KursLehrer_Kurs_FK */
	public SchemaTabelleFremdschluessel fk_KursLehrer_Kurs_FK = addForeignKey(
			"KursLehrer_Kurs_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels KursLehrer_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_KursLehrer_Lehrer_FK = addForeignKey(
			"KursLehrer_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle KursLehrer.
	 */
	public Tabelle_KursLehrer() {
		super("KursLehrer", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.kurse");
		setJavaClassName("DTOKursLehrer");
		setJavaComment("Zusätzliche Lehrkräfte in den Kursen");
	}

}
