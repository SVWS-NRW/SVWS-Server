package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_SchuelerUebergangsempfehlung5Jg.
 */
public class Tabelle_Statkue_SchuelerUebergangsempfehlung5Jg extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: ID der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte SF */
	public SchemaTabelleSpalte col_SF = add("SF", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Statkue Tabelle IT.NRW: leer");

	/** Die Definition der Tabellenspalte Kurztext */
	public SchemaTabelleSpalte col_Kurztext = add("Kurztext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Kurztext der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte Langtext */
	public SchemaTabelleSpalte col_Langtext = add("Langtext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Langtext der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte Beginn */
	public SchemaTabelleSpalte col_Beginn = add("Beginn", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Beginn der Gültigkeit der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte Ende */
	public SchemaTabelleSpalte col_Ende = add("Ende", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Ende der Gültigkeit der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte Sort */
	public SchemaTabelleSpalte col_Sort = add("Sort", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Sortierung der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte HGSEM */
	public SchemaTabelleSpalte col_HGSEM = add("HGSEM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Statistikschlüssel der Übergangsempfehlung");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_SchuelerUebergangsempfehlung5Jg.
	 */
	public Tabelle_Statkue_SchuelerUebergangsempfehlung5Jg() {
		super("Statkue_SchuelerUebergangsempfehlung5Jg", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOStatkueSchuelerUebergangsempfehlung5Jg");
		setJavaComment("Statkue von IT.NRW Schlüssel für die Schulformempfehlungen");
	}

}
