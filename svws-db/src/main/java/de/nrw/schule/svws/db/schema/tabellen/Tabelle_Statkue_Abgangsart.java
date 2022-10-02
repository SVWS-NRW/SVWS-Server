package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_Abgangsart.
 */
public class Tabelle_Statkue_Abgangsart extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SF */
	public SchemaTabelleSpalte col_SF = add("SF", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: zulässige Schulform der Abgangsart");

	/** Die Definition der Tabellenspalte Art */
	public SchemaTabelleSpalte col_Art = add("Art", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: ASD-Kürzel der Abgangsart");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(200)
		.setJavaComment("Statkue Tabelle IT.NRW: Beschreibung der Abgangsart");

	/** Die Definition der Tabellenspalte KZ_Bereich */
	public SchemaTabelleSpalte col_KZ_Bereich = add("KZ_Bereich", SchemaDatentypen.INT, true)
		.setDefault("0")
		.setJavaComment("Statkue Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte KZ_Bereich_JG */
	public SchemaTabelleSpalte col_KZ_Bereich_JG = add("KZ_Bereich_JG", SchemaDatentypen.INT, true)
		.setDefault("0")
		.setJavaComment("Statkue Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte AbgangsJG */
	public SchemaTabelleSpalte col_AbgangsJG = add("AbgangsJG", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setJavaComment("Statkue Tabelle IT.NRW: zulässige Jahrgäng der Abgangsart");

	/** Die Definition der Tabellenspalte Flag */
	public SchemaTabelleSpalte col_Flag = add("Flag", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("1")
		.setJavaComment("Statkue Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte geaendert */
	public SchemaTabelleSpalte col_geaendert = add("geaendert", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Datum der letzten Änderung der Abgangsart");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setJavaComment("Statkue Tabelle IT.NRW: Sortierung der Abgangsart");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_Abgangsart.
	 */
	public Tabelle_Statkue_Abgangsart() {
		super("Statkue_Abgangsart", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOStatkueAbgangsart");
		setJavaComment("Statkue von IT.NRW zulässige Abgangsarten von der Herkunftsschule");
	}

}
