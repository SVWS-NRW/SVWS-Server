package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_UnterrichtLehrer.
 */
public class Tabelle_Stundenplan_UnterrichtLehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für die Zuordnung des Lehrer zum Unterricht");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Unterricht-Eintrages im Stundenplan");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID der unterrichtenden Lehrers. Im Falle von Team-Teaching werden für eine Unterricht-ID einfach mehrere Datensätze erzeugt");


	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtLehrer_K_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtLehrer_K_Lehrer_FK = addForeignKey(
			"Stundenplan_UnterrichtLehrer_K_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtLehrer_Unterricht_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtLehrer_Unterricht_FK = addForeignKey(
			"Stundenplan_UnterrichtLehrer_Unterricht_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Unterricht_ID, Schema.tab_Stundenplan_Unterricht.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_UnterrichtLehrer_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_UnterrichtLehrer_UC1 = addUniqueIndex("Stundenplan_UnterrichtLehrer_UC1", 
			col_Unterricht_ID, 
			col_Lehrer_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_UnterrichtLehrer.
	 */
	public Tabelle_Stundenplan_UnterrichtLehrer() {
		super("Stundenplan_UnterrichtLehrer", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanUnterrichtLehrer");
		setJavaComment("Enthält die Zuordnung der Lehrer zu den Unterrichten bei einem Zeitraster-Eintrag. Über das Zeitraster ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
