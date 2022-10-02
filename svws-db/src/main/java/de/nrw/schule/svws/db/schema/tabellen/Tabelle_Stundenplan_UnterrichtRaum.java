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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_UnterrichtRaum.
 */
public class Tabelle_Stundenplan_UnterrichtRaum extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für die Zuordnung des Raumes zum Unterricht");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Unterricht-Eintrages im Stundenplan");

	/** Die Definition der Tabellenspalte Raum_ID */
	public SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des zugewiesenen Raumes. Sollten ggf. mehrere Räume zugwiesen werden, so müssen für eine Unterricht-ID mehrere Datensätze vorliegen");


	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtRaum_Raeume_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtRaum_Raeume_FK = addForeignKey(
			"Stundenplan_UnterrichtRaum_Raeume_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Raum_ID, Schema.tab_Stundenplan_Raeume.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtRaum_Unterricht_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtRaum_Unterricht_FK = addForeignKey(
			"Stundenplan_UnterrichtRaum_Unterricht_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Unterricht_ID, Schema.tab_Stundenplan_Unterricht.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_UnterrichtRaum_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_UnterrichtRaum_UC1 = addUniqueIndex("Stundenplan_UnterrichtRaum_UC1", 
			col_Unterricht_ID, 
			col_Raum_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_UnterrichtRaum.
	 */
	public Tabelle_Stundenplan_UnterrichtRaum() {
		super("Stundenplan_UnterrichtRaum", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanUnterrichtRaum");
		setJavaComment("Enthält die Zuordnung der Räume zu den Unterrichten bei einem Zeitraster-Eintrag. Über das Zeitraster ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
