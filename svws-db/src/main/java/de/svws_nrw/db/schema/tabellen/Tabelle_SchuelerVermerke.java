package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerVermerke.
 */
public class Tabelle_SchuelerVermerke extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Vermerkeintrages beim Schüler");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerID des Vermerkeintrages beim Schüler");

	/** Die Definition der Tabellenspalte VermerkArt_ID */
	public SchemaTabelleSpalte col_VermerkArt_ID = add("VermerkArt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Art des Vermerkeintrages beim Schüler");

	/** Die Definition der Tabellenspalte Datum */
	public SchemaTabelleSpalte col_Datum = add("Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum des Vermerkeintrages beim Schüler");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.TEXT, false)
		.setJavaComment("Bemerkung des Vermerkeintrages beim Schüler");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte AngelegtVon */
	public SchemaTabelleSpalte col_AngelegtVon = add("AngelegtVon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Angelegt von User des Vermerkeintrages beim Schüler");

	/** Die Definition der Tabellenspalte GeaendertVon */
	public SchemaTabelleSpalte col_GeaendertVon = add("GeaendertVon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Geändert von User des Vermerkeintrages beim Schüler");


	/** Die Definition des Fremdschlüssels SchuelerVermerke_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerVermerke_Schueler_FK = addForeignKey(
			"SchuelerVermerke_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerVermerke_VermerkArt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerVermerke_VermerkArt_FK = addForeignKey(
			"SchuelerVermerke_VermerkArt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_VermerkArt_ID, Schema.tab_K_Vermerkart.col_ID)
		);


	/** Die Definition des Non-Unique-Index SchuelerVermerke_IDX1 */
	public SchemaTabelleIndex index_SchuelerVermerke_IDX1 = addIndex("SchuelerVermerke_IDX1",
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerVermerke.
	 */
	public Tabelle_SchuelerVermerke() {
		super("SchuelerVermerke", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerVermerke");
		setJavaComment("Vermerke zum Schüler");
	}

}
