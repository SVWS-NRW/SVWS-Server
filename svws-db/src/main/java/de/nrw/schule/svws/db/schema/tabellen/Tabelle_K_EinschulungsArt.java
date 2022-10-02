package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_EinschulungsArt.
 */
public class Tabelle_K_EinschulungsArt extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Einschulungsart");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setNotNull()
		.setJavaComment("Bezeichnung der Einschulungsart");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung der Einschulungsart");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Sichbarkeit der Einschulungsart");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Änderbarkeit der Einschulungsart");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index K_EinschulungsArt_UC1 */
	public SchemaTabelleUniqueIndex unique_K_EinschulungsArt_UC1 = addUniqueIndex("K_EinschulungsArt_UC1", 
			col_Bezeichnung
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_EinschulungsArt.
	 */
	public Tabelle_K_EinschulungsArt() {
		super("K_EinschulungsArt", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOEinschulungsart");
		setJavaComment("War urspünglich mal für alle Schulformen verfügbar, ist aber bei den allg.-bildenen Schulen durch die Statkue-Tabelle ersetzt worden. Bei WBKs und BKs wird sie aber noch verwendet (der Umstieg auf die Statkue-Tabelle führte da zu Protesten, weil die Schulen keine eigenen Einträge mehr machen konnten).");
	}

}
