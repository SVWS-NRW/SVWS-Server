package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle PersonalTypen.
 */
public class Tabelle_PersonalTypen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Personal-Typs");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Das Kürzel des Personal-Typs");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Die (Lang-)Bezeichnung des Personal-Typs");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Der Datensatz ist gültig ab dem Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Der Datensatz ist gültig bis zu dem Schuljahr");


	/** Die Definition des Unique-Index PersonalTypen_UC1 */
	public SchemaTabelleUniqueIndex unique_PersonalTypen_UC1 = addUniqueIndex("PersonalTypen_UC1", 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle PersonalTypen.
	 */
	public Tabelle_PersonalTypen() {
		super("PersonalTypen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("coretypes");
		setJavaClassName("DTOPersonalTyp");
		setJavaComment("Katalog-Tabelle mit den möglichen Personal-Typen in der Lehrer-Tabelle, damit auch Nicht-Lehrer in der Lehrer-Liste aufgenommen und unterschieden werden können");
	}

}
