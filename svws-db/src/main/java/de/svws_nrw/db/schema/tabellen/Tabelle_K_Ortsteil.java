package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Ortsteil.
 */
public class Tabelle_K_Ortsteil extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Ortsteils");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
			.setNotNull()
			.setJavaComment("Bezeichnung des Ortsteils");

	/** Die Definition der Tabellenspalte Ort_ID */
	public SchemaTabelleSpalte col_Ort_ID = add("Ort_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Fremdschlüssel auf den Ort, dem der Ortsteil zugeordnet ist");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setVeraltet(SchemaRevisionen.REV_3)
			.setJavaComment("PLZ des Ortsteils");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setDefault("32000")
			.setJavaComment("Sortierung des Ortsteils");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Sichbarkeit des Ortsteils");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Änderbarkeit des Ortsteils");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");

	/** Die Definition der Tabellenspalte OrtsteilSchluessel */
	public SchemaTabelleSpalte col_OrtsteilSchluessel = add("OrtsteilSchluessel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
			.setJavaComment("Schlüssel des Ortsteils (Regional?)");


	/** Die Definition des Fremdschlüssels K_Ortsteil_Ort_FK */
	public SchemaTabelleFremdschluessel fk_K_Ortsteil_Ort_FK = addForeignKey(
			"K_Ortsteil_Ort_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Ort_ID, Schema.tab_K_Ort.col_ID)
	);


	/** Die Definition des Unique-Index K_Ortsteil_UC1 */
	public SchemaTabelleUniqueIndex unique_K_Ortsteil_UC1 = addUniqueIndex("K_Ortsteil_UC1",
			col_Ort_ID,
			col_Bezeichnung)
			.setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Ortsteil.
	 */
	public Tabelle_K_Ortsteil() {
		super("K_Ortsteil", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOOrtsteil");
		setJavaComment("Interner Ortsteilkatalog");
	}

}
