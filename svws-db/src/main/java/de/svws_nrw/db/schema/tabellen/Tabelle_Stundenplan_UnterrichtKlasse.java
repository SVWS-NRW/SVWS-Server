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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_UnterrichtKlasse.
 */
public class Tabelle_Stundenplan_UnterrichtKlasse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für die Zuordnung der Klasse zum Unterricht");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Unterricht-Eintrages im Stundenplan");

	/** Die Definition der Tabellenspalte Klasse_ID */
	public SchemaTabelleSpalte col_Klasse_ID = add("Klasse_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID der zugeordneten Klasse.");


	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtKlasse_Klasse_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtKlasse_Klasse_FK = addForeignKey(
			"Stundenplan_UnterrichtKlasse_Klasse_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Klasse_ID, Schema.tab_Klassen.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_UnterrichtKlasse_Unterricht_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_UnterrichtKlasse_Unterricht_FK = addForeignKey(
			"Stundenplan_UnterrichtKlasse_Unterricht_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Unterricht_ID, Schema.tab_Stundenplan_Unterricht.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_UnterrichtKlasse_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_UnterrichtKlasse_UC1 = addUniqueIndex("Stundenplan_UnterrichtKlasse_UC1",
			col_Unterricht_ID,
			col_Klasse_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_UnterrichtKlasse.
	 */
	public Tabelle_Stundenplan_UnterrichtKlasse() {
		super("Stundenplan_UnterrichtKlasse", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanUnterrichtKlasse");
		setJavaComment("Enthält die Zuordnung der Klasse(n) zu den Unterrichten bei einem Zeitraster-Eintrag. Über das Zeitraster ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
