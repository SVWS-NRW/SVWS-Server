package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KursFortschreibungsarten.
 */
public class Tabelle_KursFortschreibungsarten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Kurs-Fortschreibungsart");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setDefault("N")
		.setNotNull()
		.setJavaComment("Das Kürzel der Kurs-Fortschreibungsart");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Die Bezeichnung der Kurs-Fortschreibungsart");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Der Datensatz ist gültig ab dem Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Der Datensatz ist gültig bis zu dem Schuljahr");


	/** Die Definition des Unique-Index KursFortschreibungsarten_UC1 */
	public SchemaTabelleUniqueIndex unique_KursFortschreibungsarten_UC1 = addUniqueIndex("KursFortschreibungsarten_UC1", 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle KursFortschreibungsarten.
	 */
	public Tabelle_KursFortschreibungsarten() {
		super("KursFortschreibungsarten", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("coretypes");
		setJavaClassName("DTOKursFortschreibungsart");
		setJavaComment("Tabelle für den Core-Type der Kurs-Fortschreibungsarten");
	}

}
