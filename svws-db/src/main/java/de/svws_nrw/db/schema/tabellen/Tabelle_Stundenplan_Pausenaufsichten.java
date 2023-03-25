package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Pausenaufsichten.
 */
public class Tabelle_Stundenplan_Pausenaufsichten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für diese Zuordnung des Pausenaufsichts-Eintrages zu einem Stundenplan");

	/** Die Definition der Tabellenspalte Pausenzeit_ID */
	public SchemaTabelleSpalte col_Pausenzeit_ID = add("Pausenzeit_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Pausenzeit-Eintrags");

	/** Die Definition der Tabellenspalte Wochentyp */
	public SchemaTabelleSpalte col_Wochentyp = add("Wochentyp", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Gibt an, ob es sich um einen Eintrag für jede Woche handelt (0) oder ob es sich um einen unterschiedlichen (!) Eintrag für eine A- bzw. B-Wochen (1 bzw. 2) handelt");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des aufsichtsführenden Lehrers. Im Falle von mehreren Aufsichten werden für eine Pausenzeit-ID einfach mehrere Datensätze erzeugt");


	/** Die Definition des Fremdschlüssels Stundenplan_Pausenaufsichten_K_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Pausenaufsichten_K_Lehrer_FK = addForeignKey(
			"Stundenplan_Pausenaufsichten_K_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_Pausenaufsichten_Pausenzeit_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Pausenaufsichten_Pausenzeit_FK = addForeignKey(
			"Stundenplan_Pausenaufsichten_Pausenzeit_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Pausenzeit_ID, Schema.tab_Stundenplan_Pausenzeit.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_Pausenaufsichten_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Pausenaufsichten_UC1 = addUniqueIndex("Stundenplan_Pausenaufsichten_UC1", 
			col_Lehrer_ID, 
			col_Pausenzeit_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Pausenaufsichten.
	 */
	public Tabelle_Stundenplan_Pausenaufsichten() {
		super("Stundenplan_Pausenaufsichten", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanPausenaufsichten");
		setJavaComment("Enthält die Zuordnung von Lehrern zu einem Pausenzeit-Eintrag. Über die Pausenzeit ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
