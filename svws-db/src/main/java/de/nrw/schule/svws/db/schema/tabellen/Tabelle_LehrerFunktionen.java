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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerFunktionen.
 */
public class Tabelle_LehrerFunktionen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID für den Eintrag für die schulinterne Funktion eines Lehrers");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrer-ID zu der die schulinterne Funktion gehört, in LehrerAbchnittsdaten enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Lehrerabschnittsdaten");

	/** Die Definition der Tabellenspalte Funktion_ID */
	public SchemaTabelleSpalte col_Funktion_ID = add("Funktion_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der schulinternen Funktion");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr zu dem die schulinterne Funktion gehört");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt zu dem die schulinterne Funktion gehört");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerFunktionen_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerFunktionen_Abschnitt_FK = addForeignKey(
			"LehrerFunktionen_Abschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abschnitt_ID, Schema.tab_LehrerAbschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerFunktionen_Funktion_FK */
	public SchemaTabelleFremdschluessel fk_LehrerFunktionen_Funktion_FK = addForeignKey(
			"LehrerFunktionen_Funktion_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Funktion_ID, Schema.tab_K_Schulfunktionen.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerFunktionen_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerFunktionen_Lehrer_FK = addForeignKey(
			"LehrerFunktionen_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setVeraltet(SchemaRevisionen.REV_1);


	/** Die Definition des Unique-Index LehrerFunktionen_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerFunktionen_UC1 = addUniqueIndex("LehrerFunktionen_UC1", 
			col_Abschnitt_ID, 
			col_Funktion_ID
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerFunktionen.
	 */
	public Tabelle_LehrerFunktionen() {
		super("LehrerFunktionen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerFunktion");
		setJavaComment("Lehrkräfte eingetragene Funktionen aus K_Schulfunktionen");
	}

}
