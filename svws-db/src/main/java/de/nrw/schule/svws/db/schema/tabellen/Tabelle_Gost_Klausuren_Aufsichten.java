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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Aufsichten.
 */
public class Tabelle_Gost_Klausuren_Aufsichten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausuraufsicht (generiert)");

	/** Die Definition der Tabellenspalte Raum_ID */
	public SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Klausurraums");
	
	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Lehrers");
	
	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setJavaComment("Die Startzeit der Aufsicht");

	/** Die Definition der Tabellenspalte Endzeit */
	public SchemaTabelleSpalte col_Endzeit = add("Endzeit", SchemaDatentypen.TIME, false)
		.setJavaComment("Die Endzeit der Aufsicht");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zur Klausurvorlage");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Aufsichten_Raum_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Aufsichten_Raum_ID_FK = addForeignKey(
			"Gost_Klausuren_Aufsichten_Raum_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Raum_ID, Schema.tab_Gost_Klausuren_Raeume.col_ID)
		);
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Aufsichten_Lehrer_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Aufsichten_Lehrer_ID_FK = addForeignKey(
			"Gost_Klausuren_Aufsichten_Lehrer_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Raum_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Aufsichten.
	 */
	public Tabelle_Gost_Klausuren_Aufsichten() {
		super("Gost_Klausuren_Aufsichten", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenAufsichten");
		setJavaComment("Tabelle für die Definition von Aufsichten für Klausuren");
	}

}
