package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_Reformpaedagogik.
 */
public class Tabelle_Statkue_Reformpaedagogik extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SF */
	public SchemaTabelleSpalte col_SF = add("SF", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: zulässige Schulform für Reformpädagogik");

	/** Die Definition der Tabellenspalte RPG */
	public SchemaTabelleSpalte col_RPG = add("RPG", SchemaDatentypen.VARCHAR, true).setDatenlaenge(1)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Statstikkürzel Reformpädagogik");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Beschreibung Reformpädagogik");

	/** Die Definition der Tabellenspalte geaendert */
	public SchemaTabelleSpalte col_geaendert = add("geaendert", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Datum der letzten Änderung");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_Reformpaedagogik.
	 */
	public Tabelle_Statkue_Reformpaedagogik() {
		super("Statkue_Reformpaedagogik", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOStatkueReformpaedagogik");
		setJavaComment("Statkue von IT.NRW Eintragungen für Reformpädagogik ASDPC ***** könnte in Schild-NRW überflüssig sein ****");
	}

}
