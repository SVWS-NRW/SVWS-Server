package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Ort.
 */
public class Tabelle_K_Ort extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setJavaName("id")
			.setNotNull()
			.setJavaComment("ID des Ortes");

	/** Die Definition der Tabellenspalte PLZ */
	public final SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaName("plz")
			.setNotNull()
			.setJavaComment("PLZ des Ortes");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public final SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
			.setJavaName("ortsname")
			.setNotNull()
			.setJavaComment("Bezeichnung des Ortes");

	/** Die Definition der Tabellenspalte Kreis */
	public final SchemaTabelleSpalte col_Kreis = add("Kreis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
			.setJavaName("kreis")
			.setJavaComment("Kreis des Ortes");

	/** Die Definition der Tabellenspalte Sortierung */
	public final SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setJavaName("sortierung")
			.setDefault("32000")
			.setJavaComment("Sortierung des Ortes");

	/** Die Definition der Tabellenspalte Sichtbar */
	public final SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaName("istSichtbar")
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Sichbarkeit des Ortes");

	/** Die Definition der Tabellenspalte Aenderbar */
	public final SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaName("istAenderbar")
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Änderbarkeit des Ortes");

	/** Die Definition der Tabellenspalte Land */
	public final SchemaTabelleSpalte col_Land = add("Land", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaName("schluesselBundesland")
			.setJavaComment("Land des Ortes");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");


	/** Die Definition des Non-Unique-Index K_Ort_IDX1 */
	public final SchemaTabelleIndex index_K_Ort_IDX1 = addIndex("K_Ort_IDX1",
			col_PLZ
	);


	/** Die Definition des Unique-Index K_Ort_UC1 */
	public final SchemaTabelleUniqueIndex unique_K_Ort_UC1 = addUniqueIndex("K_Ort_UC1",
			col_Bezeichnung,
			col_PLZ
	).setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Ort.
	 */
	public Tabelle_K_Ort() {
		super("K_Ort", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOOrt");
		setJavaComment("Interner Ortskatalog");
	}

}
