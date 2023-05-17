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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_UnterrichtSchiene.
 */
public class Tabelle_Stundenplan_UnterrichtSchiene extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für die Zuordnung der Schiene zum Unterricht");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Unterricht-Eintrages im Stundenplan");

	/** Die Definition der Tabellenspalte Schiene_ID */
	public SchemaTabelleSpalte col_Schiene_ID = add("Schiene_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID der zugewiesenen Schiene. Sollten ggf. mehrere Schienen zugewiesen werden, so müssen für eine Unterricht-ID mehrere Datensätze vorliegen");


	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtSchiene_Schienen_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtSchiene_Schienen_FK = addForeignKey(
		"Stundenplan_UnterrichtSchiene_Schienen_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Schiene_ID, Schema.tab_Stundenplan_Schienen.col_ID)
	);

	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtSchiene_Unterricht_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtSchiene_Unterricht_FK = addForeignKey(
		"Stundenplan_UnterrichtSchiene_Unterricht_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Unterricht_ID, Schema.tab_Stundenplan_Unterricht.col_ID)
	);


	/** Die Definition des Unique-Index Stundenplan_UnterrichtSchiene_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_UnterrichtSchiene_UC1 = addUniqueIndex("Stundenplan_UnterrichtSchiene_UC1",
		col_Unterricht_ID,
		col_Schiene_ID
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_UnterrichtRaum.
	 */
	public Tabelle_Stundenplan_UnterrichtSchiene() {
		super("Stundenplan_UnterrichtSchiene", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanUnterrichtSchiene");
		setJavaComment("Enthält die Zuordnung der Schiene(n) zu den Unterrichten bei einem Zeitraster-Eintrag. Über das Zeitraster ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
