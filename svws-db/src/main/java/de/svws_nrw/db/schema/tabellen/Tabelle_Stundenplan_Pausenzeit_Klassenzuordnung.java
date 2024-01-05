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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Pausenzeit_Klassenzuordnung.
 */
public class Tabelle_Stundenplan_Pausenzeit_Klassenzuordnung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für die Zuordnung einer Klasse zu einer Pausenzeit");

	/** Die Definition der Tabellenspalte Pausenzeit_ID */
	public SchemaTabelleSpalte col_Pausenzeit_ID = add("Pausenzeit_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Pausenzeit-Eintrages im Stundenplan");

	/** Die Definition der Tabellenspalte Klassen_ID */
	public SchemaTabelleSpalte col_Klassen_ID = add("Klassen_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID der zugeordneten Klasse.");


	/** Die Definition des Fremdschlüssels Stundenplan_Pausenzeit_Klassenzuordnung_Klassen_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Pausenzeit_Klassenzuordnung_Klassen_FK = addForeignKey(
		"Stundenplan_Pausenzeit_Klassenzuordnung_Klassen_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Klassen_ID, Schema.tab_Klassen.col_ID)
	);

	/** Die Definition des Fremdschlüssels Stundenplan_Pausenzeit_Klassenzuordnung_Pausenzeit_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Pausenzeit_Klassenzuordnung_Pausenzeit_FK = addForeignKey(
		"Stundenplan_Pausenzeit_Klassenzuordnung_Pausenzeit_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Pausenzeit_ID, Schema.tab_Stundenplan_Pausenzeit.col_ID)
	);


	/** Die Definition des Unique-Index Stundenplan_Pausenzeit_Klassenzuordnung_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Pausenzeit_Klassenzuordnung_UC1 = addUniqueIndex("Stundenplan_Pausenzeit_Klassenzuordnung_UC1",
		col_Pausenzeit_ID,
		col_Klassen_ID
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Pausenzeit_Klassenzuordnung.
	 */
	public Tabelle_Stundenplan_Pausenzeit_Klassenzuordnung() {
		super("Stundenplan_Pausenzeit_Klassenzuordnung", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanPausenzeitKlassenzuordnung");
		setJavaComment("Enthält die Zuordnung der Klassen zu einem Pausenzeiteintrag. Über die Pausenzeit ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
