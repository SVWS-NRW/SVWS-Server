package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schueler_AllgAdr.
 */
public class Tabelle_Schueler_AllgAdr extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerID des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Adresse_ID */
	public SchemaTabelleSpalte col_Adresse_ID = add("Adresse_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("AdressID des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Vertragsart_ID */
	public SchemaTabelleSpalte col_Vertragsart_ID = add("Vertragsart_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("VertragsArtID des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Vertragsbeginn */
	public SchemaTabelleSpalte col_Vertragsbeginn = add("Vertragsbeginn", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum Vertragsbeginn des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Vertragsende */
	public SchemaTabelleSpalte col_Vertragsende = add("Vertragsende", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum des Vertragsende des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Ausbilder */
	public SchemaTabelleSpalte col_Ausbilder = add("Ausbilder", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Ausbildername des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte AllgAdrAnschreiben */
	public SchemaTabelleSpalte col_AllgAdrAnschreiben = add("AllgAdrAnschreiben", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Betrieb erhält Anschreiben Ja/Nein");

	/** Die Definition der Tabellenspalte Praktikum */
	public SchemaTabelleSpalte col_Praktikum = add("Praktikum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Ansprechpartner_ID */
	public SchemaTabelleSpalte col_Ansprechpartner_ID = add("Ansprechpartner_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("AnsprechpartnerID des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte Betreuungslehrer_ID */
	public SchemaTabelleSpalte col_Betreuungslehrer_ID = add("Betreuungslehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("BetreuungslehrerID des Betriebeeintrags beim Schüler");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Adresse_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Adresse_FK = addForeignKey(
			"SchuelerAllgAdr_Adresse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Adresse_ID, Schema.tab_K_AllgAdresse.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Ansprech_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Ansprech_FK = addForeignKey(
			"SchuelerAllgAdr_Ansprech_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Ansprechpartner_ID, Schema.tab_AllgAdrAnsprechpartner.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Schueler_FK = addForeignKey(
			"SchuelerAllgAdr_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Vertragsart_Beschaeftigungsart_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Vertragsart_Beschaeftigungsart_FK = addForeignKey(
			"SchuelerAllgAdr_Vertragsart_Beschaeftigungsart_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Vertragsart_ID, Schema.tab_K_BeschaeftigungsArt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Non-Unique-Index Schueler_AllgAdr_IDX1 */
	public SchemaTabelleIndex index_Schueler_AllgAdr_IDX1 = addIndex("Schueler_AllgAdr_IDX1", 
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schueler_AllgAdr.
	 */
	public Tabelle_Schueler_AllgAdr() {
		super("Schueler_AllgAdr", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerAllgemeineAdresse");
		setJavaComment("weitere Adressen und Betriebe zum Schüler");
	}

}
