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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KlassenLehrer.
 */
public class Tabelle_KlassenLehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Klassen_ID */
	public SchemaTabelleSpalte col_Klassen_ID = add("Klassen_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klasse");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Lehrers");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setJavaComment("Die Reihenfolge, in welcher die Klassenlehrer in der Klassen angegeben werden. Kann zur Unterscheidung zwischen Klassenlehrern (1) und deren Stellvertretern (2, ...) genutzt werden, wenn keine alphabetische Reihenfolge gewünscht ist. ");


	/** Die Definition des Fremdschlüssels Klassenlehrer_Klasse_FK */
	public SchemaTabelleFremdschluessel fk_Klassenlehrer_Klasse_FK = addForeignKey(
			"Klassenlehrer_Klasse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Klassen_ID, Schema.tab_Klassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Klassenlehrer_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Klassenlehrer_Lehrer_FK = addForeignKey(
			"Klassenlehrer_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle KlassenLehrer.
	 */
	public Tabelle_KlassenLehrer() {
		super("KlassenLehrer", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.klassen");
		setJavaClassName("DTOKlassenLeitung");
		setJavaComment("Tabelle für die Schuljahresabschnitts-spezifische Zuordnung von Klassenleitungen zu den Klassen ");
	}

}
