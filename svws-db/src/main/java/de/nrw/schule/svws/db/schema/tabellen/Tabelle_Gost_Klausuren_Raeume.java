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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Raeume.
 */
public class Tabelle_Gost_Klausuren_Raeume extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Klausurraums (generiert)");
	
	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Termins");

	/** Die Definition der Tabellenspalte Katalog_Raum_ID */
	public SchemaTabelleSpalte col_Katalog_Raum_ID = add("Katalog_Raum_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Raums aus der Tabelle Katalog_Raeume");
	
	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zum Klausurraum");
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raeume_Katalog_Raum_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raeume_Katalog_Raum_ID_FK = addForeignKey(
			"Gost_Klausuren_Raeume_Katalog_Raum_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Katalog_Raum_ID, Schema.tab_Katalog_Raeume.col_ID)
		);
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raeume_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raeume_Termin_ID_FK = addForeignKey(
			"Gost_Klausuren_Raeume_Termin_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
	);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Raeume.
	 */
	public Tabelle_Gost_Klausuren_Raeume() {
		super("Gost_Klausuren_Raeume", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenRaeume");
		setJavaComment("Tabelle für die Definition von Räumen für Klausuren");
	}

}
