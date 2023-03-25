package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.SprachreferenzniveauConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerSprachenfolge.
 */
public class Tabelle_SchuelerSprachenfolge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerID des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte Sprache */
	public SchemaTabelleSpalte col_Sprache = add("Sprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher");

	/** Die Definition der Tabellenspalte ReihenfolgeNr */
	public SchemaTabelleSpalte col_ReihenfolgeNr = add("ReihenfolgeNr", SchemaDatentypen.INT, false)
		.setRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Reihenfolge Nummer des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte ASDJahrgangVon */
	public SchemaTabelleSpalte col_ASDJahrgangVon = add("ASDJahrgangVon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setRevision(SchemaRevisionen.REV_1)
		.setJavaComment("ASDJahrgang Beginn des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte ASDJahrgangBis */
	public SchemaTabelleSpalte col_ASDJahrgangBis = add("ASDJahrgangBis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setRevision(SchemaRevisionen.REV_1)
		.setJavaComment("ASDJahrgang Ende des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte AbschnittVon */
	public SchemaTabelleSpalte col_AbschnittVon = add("AbschnittVon", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Abschnitt Beginn des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte AbschnittBis */
	public SchemaTabelleSpalte col_AbschnittBis = add("AbschnittBis", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Abschnitt Ende des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte Referenzniveau */
	public SchemaTabelleSpalte col_Referenzniveau = add("Referenzniveau", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setConverter(SprachreferenzniveauConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Referenzniveau GeR des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte KleinesLatinumErreicht */
	public SchemaTabelleSpalte col_KleinesLatinumErreicht = add("KleinesLatinumErreicht", SchemaDatentypen.INT, false)
		.setRevision(SchemaRevisionen.REV_1)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob der Schüler das kleine Latinum erreicht hat");

	/** Die Definition der Tabellenspalte LatinumErreicht */
	public SchemaTabelleSpalte col_LatinumErreicht = add("LatinumErreicht", SchemaDatentypen.INT, false)
		.setRevision(SchemaRevisionen.REV_1)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob der Schüler das Latinum erreicht hat");

	/** Die Definition der Tabellenspalte GraecumErreicht */
	public SchemaTabelleSpalte col_GraecumErreicht = add("GraecumErreicht", SchemaDatentypen.INT, false)
		.setRevision(SchemaRevisionen.REV_1)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob der Schüler das Graecum erreicht hat");

	/** Die Definition der Tabellenspalte HebraicumErreicht */
	public SchemaTabelleSpalte col_HebraicumErreicht = add("HebraicumErreicht", SchemaDatentypen.INT, false)
		.setRevision(SchemaRevisionen.REV_1)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob der Schüler das Hebraicum erreicht hat");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: FachID des Sprachenfolgeeintrags, Ersetzt durch das Sprachenkürzel");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Reihenfolge Nummer des Sprachenfolgeeintrags bzw. N oder P, ersetzt durch ReihenfolgeNr und Sprachprüfung-Tabelle");

	/** Die Definition der Tabellenspalte JahrgangVon */
	public SchemaTabelleSpalte col_JahrgangVon = add("JahrgangVon", SchemaDatentypen.SMALLINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Jahrgang Beginn des Sprachenfolgeeintrags, durch ASDJahrgang ersetzt");

	/** Die Definition der Tabellenspalte JahrgangBis */
	public SchemaTabelleSpalte col_JahrgangBis = add("JahrgangBis", SchemaDatentypen.SMALLINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Jahrgang Ende des Sprachenfolgeeintrags, durch ASDJahrgang ersetzt");


	/** Die Definition des Fremdschlüssels SchuelerSprachenfolge_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerSprachenfolge_Schueler_FK = addForeignKey(
			"SchuelerSprachenfolge_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/** Die Definition des Non-Unique-Index SchuelerSprachenfolge_IDX1 */
	public SchemaTabelleIndex index_SchuelerSprachenfolge_IDX1 = addIndex("SchuelerSprachenfolge_IDX1", 
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerSprachenfolge.
	 */
	public Tabelle_SchuelerSprachenfolge() {
		super("SchuelerSprachenfolge", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerSprachenfolge");
		setJavaComment("Einträge zur Sprachenfolge zum Schüler");
	}

}
