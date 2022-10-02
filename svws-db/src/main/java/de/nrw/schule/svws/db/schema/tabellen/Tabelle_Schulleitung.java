package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulleitung.
 */
public class Tabelle_Schulleitung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Schulleitungsfunktionseintrags");

	/** Die Definition der Tabellenspalte LeitungsfunktionID */
	public SchemaTabelleSpalte col_LeitungsfunktionID = add("LeitungsfunktionID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Schulleitungsfunktion (Fremdschlüssel aus der Tabelle SchulleitungFunktion)");

	/** Die Definition der Tabellenspalte Funktionstext */
	public SchemaTabelleSpalte col_Funktionstext = add("Funktionstext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Beschreibung der Funktion (Default Text aus SchulleitungFunktion aber änderbar)");

	/** Die Definition der Tabellenspalte LehrerID */
	public SchemaTabelleSpalte col_LehrerID = add("LehrerID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Lehrerdatensatzes");

	/** Die Definition der Tabellenspalte Von */
	public SchemaTabelleSpalte col_Von = add("Von", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Beginndatum");

	/** Die Definition der Tabellenspalte Bis */
	public SchemaTabelleSpalte col_Bis = add("Bis", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Endedatum");


	/** Die Definition des Fremdschlüssels Schulleitung_Leitungsfunktion_FK */
	public SchemaTabelleFremdschluessel fk_Schulleitung_Leitungsfunktion_FK = addForeignKey(
			"Schulleitung_Leitungsfunktion_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_LeitungsfunktionID, Schema.tab_SchulleitungFunktion.col_ID)
		);

	/** Die Definition des Fremdschlüssels Schulleitung_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Schulleitung_Lehrer_FK = addForeignKey(
			"Schulleitung_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_LehrerID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulleitung.
	 */
	public Tabelle_Schulleitung() {
		super("Schulleitung", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOSchulleitung");
		setJavaComment("Tabelle für die Zuweisung von Schulleitungsfunktionen zu Lehrern");
	}

}
