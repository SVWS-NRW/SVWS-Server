package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.NoteConverterFromInteger;
import de.svws_nrw.db.converter.current.SprachpruefungniveauConverter;
import de.svws_nrw.db.converter.current.SprachreferenzniveauConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerSprachpruefungen.
 */
public class Tabelle_SchuelerSprachpruefungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Sprachprüfungseintrags");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schülers des Sprachprüfungseintrags");

	/** Die Definition der Tabellenspalte Sprache */
	public SchemaTabelleSpalte col_Sprache = add("Sprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher");

	/** Die Definition der Tabellenspalte ASDJahrgang */
	public SchemaTabelleSpalte col_ASDJahrgang = add("ASDJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("ASDJahrgangsbezeichnung, in der die Sprachprüfung abgelegt wurde");

	/** Die Definition der Tabellenspalte Anspruchsniveau_ID */
	public SchemaTabelleSpalte col_Anspruchsniveau_ID = add("Anspruchsniveau_ID", SchemaDatentypen.BIGINT, false)
		.setJavaName("Anspruchsniveau")
		.setConverter(SprachpruefungniveauConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Das Anspruchsniveau der Sprachprüfung (angelehnt an einen entsprechenden Schulabschluss)");

	/** Die Definition der Tabellenspalte Pruefungsdatum */
	public SchemaTabelleSpalte col_Pruefungsdatum = add("Pruefungsdatum", SchemaDatentypen.DATE, false)
		.setJavaComment("Datum der Sprachprüfung");

	/** Die Definition der Tabellenspalte ErsetzteSprache */
	public SchemaTabelleSpalte col_ErsetzteSprache = add("ErsetzteSprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher für die ersetzte Sprache");

	/** Die Definition der Tabellenspalte IstHSUPruefung */
	public SchemaTabelleSpalte col_IstHSUPruefung = add("IstHSUPruefung", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, dass die Prüfung eine Prüfung in der Herkunftssprache ist (BASS 13-61 Nr. 2). Entspricht dem Eintrag P in Schild 2");

	/** Die Definition der Tabellenspalte IstFeststellungspruefung */
	public SchemaTabelleSpalte col_IstFeststellungspruefung = add("IstFeststellungspruefung", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Prüfung eine Sprachfeststellungsprüfung ist (BASS 13-61 Nr. 1). Entspricht N in Schild 2");

	/** Die Definition der Tabellenspalte KannErstePflichtfremdspracheErsetzen */
	public SchemaTabelleSpalte col_KannErstePflichtfremdspracheErsetzen = add("KannErstePflichtfremdspracheErsetzen", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Sprachprüfung an die Stelle der ersten Pflichtfremdsprache treten kann");

	/** Die Definition der Tabellenspalte KannZweitePflichtfremdspracheErsetzen */
	public SchemaTabelleSpalte col_KannZweitePflichtfremdspracheErsetzen = add("KannZweitePflichtfremdspracheErsetzen", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Sprachprüfung an die Stelle der zweiten Pflichtfremdsprache treten kann");

	/** Die Definition der Tabellenspalte KannWahlpflichtfremdspracheErsetzen */
	public SchemaTabelleSpalte col_KannWahlpflichtfremdspracheErsetzen = add("KannWahlpflichtfremdspracheErsetzen", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Sprachprüfung an die Stelle einer Wahlpflichtfremdsprache der Klassen 05-07 treten kann");

	/** Die Definition der Tabellenspalte KannBelegungAlsFortgefuehrteSpracheErlauben */
	public SchemaTabelleSpalte col_KannBelegungAlsFortgefuehrteSpracheErlauben = add("KannBelegungAlsFortgefuehrteSpracheErlauben", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Sprachprüfung nachweist, dass die Sprache als fortgeführte Fremdsprache in der Oberstufe belegt werden kann (BASS 13-61 Nr. 1 Abs. 11)");

	/** Die Definition der Tabellenspalte Referenzniveau */
	public SchemaTabelleSpalte col_Referenzniveau = add("Referenzniveau", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setConverter(SprachreferenzniveauConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Das Sprachreferenzniveau der Sprachprüfung gemäß GeR");

	/** Die Definition der Tabellenspalte NotePruefung */
	public SchemaTabelleSpalte col_NotePruefung = add("NotePruefung", SchemaDatentypen.INT, false)
		.setConverter(NoteConverterFromInteger.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Note der Sprachprüfung, die herangezogen werden kann, falls die Note der Sprachprüfung an die Stelle einer Fremdsprachennote tritt");


	/** Die Definition des Fremdschlüssels SchuelerSprachpruefungen_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerSprachpruefungen_Schueler_FK = addForeignKey(
			"SchuelerSprachpruefungen_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerSprachpruefungen.
	 */
	public Tabelle_SchuelerSprachpruefungen() {
		super("SchuelerSprachpruefungen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerSprachpruefungen");
		setJavaComment("Einträge zu Sprachprüfungen des Schülers");
	}

}
