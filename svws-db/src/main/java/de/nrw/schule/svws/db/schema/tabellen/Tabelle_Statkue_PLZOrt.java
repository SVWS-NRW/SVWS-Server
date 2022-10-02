package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_PLZOrt.
 */
public class Tabelle_Statkue_PLZOrt extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: ID der PLZ und ORT");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Statkue Tabelle IT.NRW: Postleitzahl der PLZ und ORT");

	/** Die Definition der Tabellenspalte RegSchl */
	public SchemaTabelleSpalte col_RegSchl = add("RegSchl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Statkue Tabelle IT.NRW: Regionalschlüssel der PLZ und ORT");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Statkue Tabelle IT.NRW: Ortsbezeichnung der PLZ und ORT");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setJavaComment("Statkue Tabelle IT.NRW: Sortierung");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_PLZOrt.
	 */
	public Tabelle_Statkue_PLZOrt() {
		super("Statkue_PLZOrt", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOStatkuePLZOrt");
		setJavaComment("Statkue von IT.NRW PLZ und zugehörige Orte");
	}

}
