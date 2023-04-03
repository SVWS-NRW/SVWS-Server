package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerMerkmale.
 */
public class Tabelle_SchuelerMerkmale extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Eintrag bei besondere Merkmale zum Schüler");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Schüler-ID des Eintrag bei besondere Merkmale zum Schüler");

	/** Die Definition der Tabellenspalte Kurztext */
	public SchemaTabelleSpalte col_Kurztext = add("Kurztext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kurztext des Merkmals des Eintrag bei besondere Merkmale zum Schüler");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte DatumVon */
	public SchemaTabelleSpalte col_DatumVon = add("DatumVon", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum Beginn des Eintrag bei besondere Merkmale zum Schüler");

	/** Die Definition der Tabellenspalte DatumBis */
	public SchemaTabelleSpalte col_DatumBis = add("DatumBis", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum Ende des Eintrag bei besondere Merkmale zum Schüler");


	/** Die Definition des Fremdschlüssels SchuelerMerkmale_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerMerkmale_Schueler_FK = addForeignKey(
		"SchuelerMerkmale_Schueler_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerMerkmale.
	 */
	public Tabelle_SchuelerMerkmale() {
		super("SchuelerMerkmale", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerMerkmale");
		setJavaComment("Einträge der Merkmale (Individualdaten II) zum Schüler");
	}

}
