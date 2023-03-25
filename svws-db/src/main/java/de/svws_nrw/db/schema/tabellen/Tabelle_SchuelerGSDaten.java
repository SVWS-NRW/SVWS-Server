package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerGSDaten.
 */
public class Tabelle_SchuelerGSDaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("SchülerID zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Note_Sprachgebrauch */
	public SchemaTabelleSpalte col_Note_Sprachgebrauch = add("Note_Sprachgebrauch", SchemaDatentypen.INT, false)
		.setJavaComment("Note Sprachgebrauch zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Lesen */
	public SchemaTabelleSpalte col_Note_Lesen = add("Note_Lesen", SchemaDatentypen.INT, false)
		.setJavaComment("Note Lesen zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Rechtschreiben */
	public SchemaTabelleSpalte col_Note_Rechtschreiben = add("Note_Rechtschreiben", SchemaDatentypen.INT, false)
		.setJavaComment("Note Rechtschreibe zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Sachunterricht */
	public SchemaTabelleSpalte col_Note_Sachunterricht = add("Note_Sachunterricht", SchemaDatentypen.INT, false)
		.setJavaComment("Note Sachunterricht zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Mathematik */
	public SchemaTabelleSpalte col_Note_Mathematik = add("Note_Mathematik", SchemaDatentypen.INT, false)
		.setJavaComment("Note Mathematik zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Englisch */
	public SchemaTabelleSpalte col_Note_Englisch = add("Note_Englisch", SchemaDatentypen.INT, false)
		.setJavaComment("Note Englisch zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_KunstTextil */
	public SchemaTabelleSpalte col_Note_KunstTextil = add("Note_KunstTextil", SchemaDatentypen.INT, false)
		.setJavaComment("Note Kunst und Textil zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Musik */
	public SchemaTabelleSpalte col_Note_Musik = add("Note_Musik", SchemaDatentypen.INT, false)
		.setJavaComment("Note Musik zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Sport */
	public SchemaTabelleSpalte col_Note_Sport = add("Note_Sport", SchemaDatentypen.INT, false)
		.setJavaComment("Note Sport zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Note_Religion */
	public SchemaTabelleSpalte col_Note_Religion = add("Note_Religion", SchemaDatentypen.INT, false)
		.setJavaComment("Note Religion zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Durchschnittsnote_Sprache */
	public SchemaTabelleSpalte col_Durchschnittsnote_Sprache = add("Durchschnittsnote_Sprache", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Durchschnitt Sprache zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Durchschnittsnote_Einfach */
	public SchemaTabelleSpalte col_Durchschnittsnote_Einfach = add("Durchschnittsnote_Einfach", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Durschnit einfach zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Durchschnittsnote_Gewichtet */
	public SchemaTabelleSpalte col_Durchschnittsnote_Gewichtet = add("Durchschnittsnote_Gewichtet", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Durchschnitt gewichtet zum GS-Daten-Eintrag (ohne Religion)");

	/** Die Definition der Tabellenspalte Anrede_Klassenlehrer */
	public SchemaTabelleSpalte col_Anrede_Klassenlehrer = add("Anrede_Klassenlehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Anrede klassenlehrer zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Nachname_Klassenlehrer */
	public SchemaTabelleSpalte col_Nachname_Klassenlehrer = add("Nachname_Klassenlehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Nachname Klassenlehrer zum GS-Daten-Eintrag PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte GS_Klasse */
	public SchemaTabelleSpalte col_GS_Klasse = add("GS_Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Klassenbezeichnung zum GS-Daten-Eintrag (Die Klasse selbst ist nicht in dieser DB definiert!)");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Bemerkungen zum GS-Daten-Eintrag");

	/** Die Definition der Tabellenspalte Geschwisterkind */
	public SchemaTabelleSpalte col_Geschwisterkind = add("Geschwisterkind", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Angabe Geschwisterkind Ja Nein  zum GS-Daten-Eintrag");


	/** Die Definition des Fremdschlüssels SchuelerGSDaten_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerGSDaten_Schueler_FK = addForeignKey(
			"SchuelerGSDaten_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerGSDaten.
	 */
	public Tabelle_SchuelerGSDaten() {
		super("SchuelerGSDaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerGrundschuldaten");
		setJavaComment("Daten und Zeugnisnoten der abgebenden Grundschule zum Schüler");
	}

}
