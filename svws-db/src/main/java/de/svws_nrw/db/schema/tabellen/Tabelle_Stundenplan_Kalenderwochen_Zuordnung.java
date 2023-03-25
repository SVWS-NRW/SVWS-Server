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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Kalenderwochen_Zuordnung.
 */
public class Tabelle_Stundenplan_Kalenderwochen_Zuordnung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine ID, die einen Eintrag für die Kalenderwochen-Zuordnung eindeutig identifiziert.");

	/** Die Definition der Tabellenspalte Stundenplan_ID */
	public SchemaTabelleSpalte col_Stundenplan_ID = add("Stundenplan_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Stundenplans, dem die Kalenderwochenzuordnung zugeordnet ist");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Das Jahr (nicht Schuljahr) zu dem die Kalenderwoche gehört.");

	/** Die Definition der Tabellenspalte KW */
	public SchemaTabelleSpalte col_KW = add("KW", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Die Kalenderwoche im Jahr.");
	
	/** Die Definition der Tabellenspalte Wochentyp */
	public SchemaTabelleSpalte col_Wochentyp = add("Wochentyp", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Gibt den Wochentyp an, der der Kalenderwoche zugeordnet ist (z.B. eine A- bzw. B-Wochen, d.h. 1 bzw. 2)");

	/** Die Definition des Fremdschlüssels Stundenplan_Kalenderwochen_Zuordnung_Stundenplan_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Kalenderwochen_Zuordnung_Stundenplan_FK = addForeignKey(
		"Stundenplan_Kalenderwochen_Zuordnung_Stundenplan_FK", 
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
		new Pair<>(col_Stundenplan_ID, Schema.tab_Stundenplan.col_ID)
	);

	/** Die Definition des Unique-Index Stundenplan_Kalenderwochen_Zuordnung_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Kalenderwochen_Zuordnung_UC1 = addUniqueIndex("Stundenplan_Kalenderwochen_Zuordnung_UC1", 
		col_Stundenplan_ID,
		col_Jahr, 
		col_KW
	);
	
	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Kalenderwochen_Zuordnung.
	 */
	public Tabelle_Stundenplan_Kalenderwochen_Zuordnung() {
		super("Stundenplan_Kalenderwochen_Zuordnung", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanKalenderwochenZuordnung");
		setJavaComment("Enthält die Zuordnung von Kalenderwochen zu den Wochentypen eines Stundenplans.");
	}

}
