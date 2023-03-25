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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Unterricht.
 */
public class Tabelle_Stundenplan_Unterricht extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für diese Zuordnung des Untericht-Eintrages zu einem Stundenplan");

	/** Die Definition der Tabellenspalte Zeitraster_ID */
	public SchemaTabelleSpalte col_Zeitraster_ID = add("Zeitraster_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des belegten Zeitraster-Eintrags");

	/** Die Definition der Tabellenspalte Wochentyp */
	public SchemaTabelleSpalte col_Wochentyp = add("Wochentyp", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Gibt an, ob es sich um einen Eintrag für jede Woche handelt (0) oder ob es sich um einen unterschiedlichen (!) Eintrag für eine A- bzw. B-Wochen (1 bzw. 2) handelt");

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des Kurses, falls der Unterricht nicht im Klassenverband stattfindet");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Faches, in dem der Unterricht stattfindet");


	/** Die Definition des Fremdschlüssels Stundenplan_Unterricht_EigeneSchule_Faecher_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Unterricht_EigeneSchule_Faecher_FK = addForeignKey(
			"Stundenplan_Unterricht_EigeneSchule_Faecher_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_Unterricht_Kurse_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Unterricht_Kurse_FK = addForeignKey(
			"Stundenplan_Unterricht_Kurse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_Unterricht_Stundenplan_Zeitraster_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Unterricht_Stundenplan_Zeitraster_FK = addForeignKey(
			"Stundenplan_Unterricht_Stundenplan_Zeitraster_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Zeitraster_ID, Schema.tab_Stundenplan_Zeitraster.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Unterricht.
	 */
	public Tabelle_Stundenplan_Unterricht() {
		super("Stundenplan_Unterricht", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanUnterricht");
		setJavaComment("Enthält die Zuordnung der Unterrichte (Kurs, Fach) zu einem Zeitraster-Eintrag. Über das Zeitraster ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet. Die Zuordnung von Lehrern und Räumen erfolgt über die Tabellen Stundenplan_UnterrichtLehrer und Stundenplan_UnterrichtRaum");
	}

}
