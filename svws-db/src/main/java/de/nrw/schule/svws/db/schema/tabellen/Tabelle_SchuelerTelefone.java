package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerTelefone.
 */
public class Tabelle_SchuelerTelefone extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Telefonnummerneintrags");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerID des Telefonnummerneintrags");

	/** Die Definition der Tabellenspalte TelefonArt_ID */
	public SchemaTabelleSpalte col_TelefonArt_ID = add("TelefonArt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Art des Telefonnummerneintrags");

	/** Die Definition der Tabellenspalte Telefonnummer */
	public SchemaTabelleSpalte col_Telefonnummer = add("Telefonnummer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telefonnummer des Telefonnummerneintrags");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Bemerkung des Telefonnummerneintrags");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung des Telefonnummerneintrags");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Gesperrt */
	public SchemaTabelleSpalte col_Gesperrt = add("Gesperrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Sperrung des Telefonnummerneintrags");


	/** Die Definition des Fremdschlüssels SchuelerTelefone_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerTelefone_Schueler_FK = addForeignKey(
			"SchuelerTelefone_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerTelefone_Telefonart_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerTelefone_Telefonart_FK = addForeignKey(
			"SchuelerTelefone_Telefonart_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_TelefonArt_ID, Schema.tab_K_TelefonArt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Non-Unique-Index SchuelerTelefone_IDX1 */
	public SchemaTabelleIndex index_SchuelerTelefone_IDX1 = addIndex("SchuelerTelefone_IDX1", 
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerTelefone.
	 */
	public Tabelle_SchuelerTelefone() {
		super("SchuelerTelefone", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.erzieher");
		setJavaClassName("DTOSchuelerTelefon");
		setJavaComment("Telefonnummern zum Schüler");
	}

}
