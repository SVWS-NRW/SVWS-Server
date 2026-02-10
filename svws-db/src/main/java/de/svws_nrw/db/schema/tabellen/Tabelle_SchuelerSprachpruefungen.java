package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.SprachpruefungniveauConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerSprachpruefungen.
 */
public class Tabelle_SchuelerSprachpruefungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Sprachprüfungseintrags");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Schülers des Sprachprüfungseintrags");

	/** Die Definition der Tabellenspalte Sprache */
	public final SchemaTabelleSpalte col_Sprache = add("Sprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setNotNull()
			.setJavaComment("Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher");

	/** Die Definition der Tabellenspalte ASDJahrgang */
	public final SchemaTabelleSpalte col_ASDJahrgang = add("ASDJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaComment("ASDJahrgangsbezeichnung, in der die Sprachprüfung abgelegt wurde");

	/** Die Definition der Tabellenspalte Anspruchsniveau_ID */
	public final SchemaTabelleSpalte col_Anspruchsniveau_ID = add("Anspruchsniveau_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("Anspruchsniveau")
			.setConverter(SprachpruefungniveauConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_1)
			.setJavaComment("Das Anspruchsniveau der Sprachprüfung (angelehnt an einen entsprechenden Schulabschluss)");

	/** Die Definition der Tabellenspalte Pruefungsdatum */
	public final SchemaTabelleSpalte col_Pruefungsdatum = add("Pruefungsdatum", SchemaDatentypen.DATE, false)
			.setJavaComment("Datum der Sprachprüfung");

	/** Die Definition der Tabellenspalte ErsetzteSprache */
	public final SchemaTabelleSpalte col_ErsetzteSprache = add("ErsetzteSprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaComment("Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher für die ersetzte Sprache")
			.setVeraltet(SchemaRevisionen.REV_19);

	/** Die Definition der Tabellenspalte IstHSUPruefung */
	public final SchemaTabelleSpalte col_IstHSUPruefung = add("IstHSUPruefung", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, dass die Prüfung eine Prüfung in der Herkunftssprache ist (BASS 13-61 Nr. 2). Entspricht dem Eintrag P in Schild 2");

	/** Die Definition der Tabellenspalte IstFeststellungspruefung */
	public final SchemaTabelleSpalte col_IstFeststellungspruefung = add("IstFeststellungspruefung", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Prüfung eine Sprachfeststellungsprüfung ist (BASS 13-61 Nr. 1). Entspricht N in Schild 2");

	/** Die Definition der Tabellenspalte KannErstePflichtfremdspracheErsetzen */
	public final SchemaTabelleSpalte col_KannErstePflichtfremdspracheErsetzen = add("KannErstePflichtfremdspracheErsetzen", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Sprachprüfung an die Stelle der ersten Pflichtfremdsprache treten kann");

	/** Die Definition der Tabellenspalte KannZweitePflichtfremdspracheErsetzen */
	public final SchemaTabelleSpalte col_KannZweitePflichtfremdspracheErsetzen = add("KannZweitePflichtfremdspracheErsetzen", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Sprachprüfung an die Stelle der zweiten Pflichtfremdsprache treten kann");

	/** Die Definition der Tabellenspalte KannWahlpflichtfremdspracheErsetzen */
	public final SchemaTabelleSpalte col_KannWahlpflichtfremdspracheErsetzen = add("KannWahlpflichtfremdspracheErsetzen", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Sprachprüfung an die Stelle einer Wahlpflichtfremdsprache der Klassen 05-07 treten kann");

	/** Die Definition der Tabellenspalte KannBelegungAlsFortgefuehrteSpracheErlauben */
	public final SchemaTabelleSpalte col_KannBelegungAlsFortgefuehrteSpracheErlauben = add("KannBelegungAlsFortgefuehrteSpracheErlauben", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Sprachprüfung nachweist, dass die Sprache als fortgeführte Fremdsprache in der Oberstufe belegt werden kann"
					+ " (BASS 13-61 Nr. 1 Abs. 11)");

	/** Die Definition der Tabellenspalte Referenzniveau */
	public final SchemaTabelleSpalte col_Referenzniveau = add("Referenzniveau", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
			.setJavaComment("Das Sprachreferenzniveau der Sprachprüfung gemäß GeR");

	/** Die Definition der Tabellenspalte NotePruefung */
	public final SchemaTabelleSpalte col_NotePruefung = add("NotePruefung", SchemaDatentypen.INT, false)
			.setJavaComment(
					"Note der Sprachprüfung, die herangezogen werden kann, falls die Note der Sprachprüfung an die Stelle einer Fremdsprachennote tritt");

	/** Die Definition der Tabellenspalte Zeugnisbezeichnung */
	public final SchemaTabelleSpalte col_Zeugnisbezeichnung = add("Zeugnisbezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Die Bezeichnung der Sprache auf dem Zeugnis (z.B. nötig für einen Eintrag \"Sonstige Sprache\")")
			.setRevision(SchemaRevisionen.REV_54);


	/** Die Definition des Fremdschlüssels SchuelerSprachpruefungen_Schueler_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerSprachpruefungen_Schueler_FK = addForeignKey(
			"SchuelerSprachpruefungen_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	)
			.setRevision(SchemaRevisionen.REV_1);


	/** Die Definition des Non-Unique-Index SchuelerSprachpruefungen_IDX1 */
	public final SchemaTabelleIndex index_SchuelerSprachpruefungen_IDX1 = addIndex("SchuelerSprachpruefungen_IDX1",
			col_Schueler_ID
	).setRevision(SchemaRevisionen.REV_12);

	/** Die Definition des Non-Unique-Index SchuelerSprachpruefungen_IDX2 */
	public final SchemaTabelleIndex index_SchuelerSprachpruefungen_IDX2 = addIndex("SchuelerSprachpruefungen_IDX2",
			col_Schueler_ID,
			col_Sprache
	).setRevision(SchemaRevisionen.REV_13);


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
