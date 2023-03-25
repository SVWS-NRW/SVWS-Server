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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_PausenaufsichtenBereich.
 */
public class Tabelle_Stundenplan_PausenaufsichtenBereich extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID für die Zuordnung des Aufsichtsbereichs zur Pausenaufsicht");

	/** Die Definition der Tabellenspalte Pausenaufsicht_ID */
	public SchemaTabelleSpalte col_Pausenaufsicht_ID = add("Pausenaufsicht_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Pausenaufsicht-Eintrages im Stundenplan");

	/** Die Definition der Tabellenspalte Aufsichtsbereich_ID */
	public SchemaTabelleSpalte col_Aufsichtsbereich_ID = add("Aufsichtsbereich_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des zugewiesenen Aufsichtsbereichs. Sollten ggf. mehrere Aufsichtsbereiche zugwiesen werden, so müssen für eine Pausenaufsicht_ID mehrere Datensätze vorliegen");


	/** Die Definition des Fremdschlüssels Stundenplan_PausenaufsichtenBereich_Aufsichtsbereiche_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_PausenaufsichtenBereich_Aufsichtsbereiche_FK = addForeignKey(
			"Stundenplan_PausenaufsichtenBereich_Aufsichtsbereiche_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Aufsichtsbereich_ID, Schema.tab_Stundenplan_Aufsichtsbereiche.col_ID)
		);

	/** Die Definition des Fremdschlüssels Stundenplan_PausenaufsichtenBereich_Aufsicht_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_PausenaufsichtenBereich_Aufsicht_FK = addForeignKey(
			"Stundenplan_PausenaufsichtenBereich_Aufsicht_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Pausenaufsicht_ID, Schema.tab_Stundenplan_Pausenaufsichten.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_PausenaufsichtenBereich_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_PausenaufsichtenBereich_UC1 = addUniqueIndex("Stundenplan_PausenaufsichtenBereich_UC1", 
			col_Pausenaufsicht_ID, 
			col_Aufsichtsbereich_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_PausenaufsichtenBereich.
	 */
	public Tabelle_Stundenplan_PausenaufsichtenBereich() {
		super("Stundenplan_PausenaufsichtenBereich", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanPausenaufsichtenBereiche");
		setJavaComment("Enthält die Zuordnung der Aufsichtsbereiche zu den Pausenaufsichten bei einem Pausenzeit-Eintrag. Über die Pausenzeit ist diese Zuordnung auch immer eindeutig einem Stundenplan zugeordnet.");
	}

}
