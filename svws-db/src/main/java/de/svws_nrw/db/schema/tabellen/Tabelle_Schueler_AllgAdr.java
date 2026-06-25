package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
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
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaName("id")
			.setJavaComment("Die ID des Betriebseintrags beim Schüler");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaName("idSchueler")
			.setJavaComment("Die ID des Schülers");

	/** Die Definition der Tabellenspalte Adresse_ID */
	public final SchemaTabelleSpalte col_Adresse_ID = add("Adresse_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaName("idBetrieb")
			.setJavaComment("Die ID des Betriebs");

	/** Die Definition der Tabellenspalte Vertragsart_ID */
	public final SchemaTabelleSpalte col_Vertragsart_ID = add("Vertragsart_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idBeschaeftigungsart")
			.setJavaComment("Die ID der Beschäftigungsart");

	/** Die Definition der Tabellenspalte Vertragsbeginn */
	public final SchemaTabelleSpalte col_Vertragsbeginn = add("Vertragsbeginn", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaName("vertragsbeginn")
			.setJavaComment("Das Datum des Vertragsbeginns");

	/** Die Definition der Tabellenspalte Vertragsende */
	public final SchemaTabelleSpalte col_Vertragsende = add("Vertragsende", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaName("vertragsende")
			.setJavaComment("Das Datum des Vertragsendes");

	/** Die Definition der Tabellenspalte Ausbilder */
	public final SchemaTabelleSpalte col_Ausbilder = add("Ausbilder", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
			.setJavaName("nameAusbilder")
			.setJavaComment("Der Name des Ausbilders");

	/** Die Definition der Tabellenspalte AllgAdrAnschreiben */
	public final SchemaTabelleSpalte col_AllgAdrAnschreiben = add("AllgAdrAnschreiben", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("-")
			.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
			.setJavaName("erhaeltAnschreiben")
			.setJavaComment("Betrieb erhält Anschreiben");

	/** Die Definition der Tabellenspalte Praktikum */
	public final SchemaTabelleSpalte col_Praktikum = add("Praktikum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("-")
			.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
			.setJavaName("istPraktikum")
			.setJavaComment("Gibt an ob es ein Praktikum ist");

	/** Die Definition der Tabellenspalte Sortierung */
	public final SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setJavaName("sortierung")
			.setJavaComment("Die Sortierung des Betriebseintrags");

	/** Die Definition der Tabellenspalte Ansprechpartner_ID */
	public final SchemaTabelleSpalte col_Ansprechpartner_ID = add("Ansprechpartner_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idAnsprechpartner")
			.setJavaComment("Die ID des Ansprechpartners");

	/** Die Definition der Tabellenspalte Betreuungslehrer_ID */
	public final SchemaTabelleSpalte col_Betreuungslehrer_ID = add("Betreuungslehrer_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idBetreuungslehrer")
			.setJavaComment("Die ID des Betreuungslehrers");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Adresse_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Adresse_FK = addForeignKey(
			"SchuelerAllgAdr_Adresse_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Adresse_ID, Schema.tab_K_AllgAdresse.col_ID)
	);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Ansprech_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Ansprech_FK = addForeignKey(
			"SchuelerAllgAdr_Ansprech_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Ansprechpartner_ID, Schema.tab_AllgAdrAnsprechpartner.col_ID)
	).setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Schueler_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Schueler_FK = addForeignKey(
			"SchuelerAllgAdr_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Vertragsart_Beschaeftigungsart_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Vertragsart_Beschaeftigungsart_FK = addForeignKey(
			"SchuelerAllgAdr_Vertragsart_Beschaeftigungsart_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Vertragsart_ID, Schema.tab_K_BeschaeftigungsArt.col_ID)
	).setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerAllgAdr_Betreuungslehrer_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerAllgAdr_Betreuungslehrer_FK = addForeignKey(
			"SchuelerAllgAdr_Betreuungslehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Betreuungslehrer_ID, Schema.tab_K_Lehrer.col_ID)
	).setRevision(SchemaRevisionen.REV_71);


	/** Die Definition des Non-Unique-Index Schueler_AllgAdr_IDX1 */
	public final SchemaTabelleIndex index_Schueler_AllgAdr_IDX1 = addIndex("Schueler_AllgAdr_IDX1",
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
		setJavaClassName("DTOSchuelerBetrieb");
		setJavaComment("Betriebsdaten eines Schülers");
	}

}
