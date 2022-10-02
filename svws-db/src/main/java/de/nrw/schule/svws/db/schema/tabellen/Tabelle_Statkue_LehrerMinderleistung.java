package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_LehrerMinderleistung.
 */
public class Tabelle_Statkue_LehrerMinderleistung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: ID der Lehrerminderleistung");

	/** Die Definition der Tabellenspalte Kurztext */
	public SchemaTabelleSpalte col_Kurztext = add("Kurztext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Statistikkürzel der Lehrerminderleistung");

	/** Die Definition der Tabellenspalte Langtext */
	public SchemaTabelleSpalte col_Langtext = add("Langtext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Langtext der Lehrerminderleistung");

	/** Die Definition der Tabellenspalte Beginn */
	public SchemaTabelleSpalte col_Beginn = add("Beginn", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Begin der Gültigkeit der Lehrerminderleistung");

	/** Die Definition der Tabellenspalte Ende */
	public SchemaTabelleSpalte col_Ende = add("Ende", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Ende der Gültigkeit der Lehrerminderleistung");

	/** Die Definition der Tabellenspalte Sort */
	public SchemaTabelleSpalte col_Sort = add("Sort", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Sortierung der Lehrerminderleistung");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_LehrerMinderleistung.
	 */
	public Tabelle_Statkue_LehrerMinderleistung() {
		super("Statkue_LehrerMinderleistung", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOStatkueLehrerMinderleistung");
		setJavaComment("Statkue von IT.NRW Schlüssel für die LehrerMinderleistuung");
	}

}
