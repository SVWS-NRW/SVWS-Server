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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_FachTeilleistungen.
 */
public class Tabelle_EigeneSchule_FachTeilleistungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Teilleistung_ID */
	public SchemaTabelleSpalte col_Teilleistung_ID = add("Teilleistung_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaName("Teilleistungsart_ID")
		.setJavaComment("Die eindeutige ID der Teilleistungsart – verweist auf die Teilleistungsart");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Faches – verweist auf das Fach");

	/** Die Definition der Tabellenspalte Kursart */
	public SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setJavaComment("Gibt an, bei welcher Kursart die Teilleistungsart zugeordnet werden soll");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels EigeneSchule_FachTeilleistungen_Fach_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_FachTeilleistungen_Fach_FK = addForeignKey(
			"EigeneSchule_FachTeilleistungen_Fach_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_FachTeilleistungen.
	 */
	public Tabelle_EigeneSchule_FachTeilleistungen() {
		super("EigeneSchule_FachTeilleistungen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.faecher");
		setJavaClassName("DTOFachTeilleistungsarten");
		setJavaComment("Teilleistungsarten, die einem Fach über die Vorlagen zugewiesen sind damit diese automatisch über den Gruppenprozess eingetragen werden können");
	}

}
