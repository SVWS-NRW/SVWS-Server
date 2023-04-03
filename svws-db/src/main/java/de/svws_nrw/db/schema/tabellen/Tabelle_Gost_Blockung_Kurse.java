package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Kurse.
 */
public class Tabelle_Gost_Blockung_Kurse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Kurses in der Blockung (generiert)");

	/** Die Definition der Tabellenspalte Blockung_ID */
	public SchemaTabelleSpalte col_Blockung_ID = add("Blockung_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Blockung");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Faches");

	/** Die Definition der Tabellenspalte Kursart */
	public SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setDefault("GK")
		.setNotNull()
		.setConverter(GOStKursartConverter.class)
		.setJavaComment("ID der Kursart (siehe ID des Core-Types GostKursart)");

	/** Die Definition der Tabellenspalte Kursnummer */
	public SchemaTabelleSpalte col_Kursnummer = add("Kursnummer", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Die Nummer des Kurses in Bezug auf das Fach (Kurse eines Faches sind in einer Blockung üblicherweise von 1 ab durchnummeriert)");

	/** Die Definition der Tabellenspalte IstKoopKurs */
	public SchemaTabelleSpalte col_IstKoopKurs = add("IstKoopKurs", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um einen Kooperations-Kurs mit einer anderen Schule handelt oder nicht: 1 - true, 0 - false ");

	/** Die Definition der Tabellenspalte BezeichnungSuffix */
	public SchemaTabelleSpalte col_BezeichnungSuffix = add("BezeichnungSuffix", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Ein Suffix, welches der Kursbezeichnung ggf. angehangen wird (kann z.B. zum Kennzeichnen von Kooperationskursen verwendet werden)");

	/** Die Definition der Tabellenspalte Schienenanzahl */
	public SchemaTabelleSpalte col_Schienenanzahl = add("Schienenanzahl", SchemaDatentypen.INT, false)
	    .setDefault("1")
	    .setNotNull()
	    .setJavaComment("Gibt die Anzahl der Schienen an, die für den Kurs in der Blockung verwendet werden soll (normalerweise 1)");

	/** Die Definition der Tabellenspalte Wochenstunden */
	public SchemaTabelleSpalte col_Wochenstunden = add("Wochenstunden", SchemaDatentypen.INT, false)
		.setDefault("3")
		.setNotNull()
		.setJavaComment("Die Anzahl der Wochenstunden für den Kurs");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Kurse_Blockung_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Kurse_Blockung_FK = addForeignKey(
			"Gost_Blockung_Kurse_Blockung_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Blockung_ID, Schema.tab_Gost_Blockung.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Blockung_Kurse_Fach_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Kurse_Fach_FK = addForeignKey(
			"Gost_Blockung_Kurse_Fach_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Kurse.
	 */
	public Tabelle_Gost_Blockung_Kurse() {
		super("Gost_Blockung_Kurse", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungKurs");
		setJavaComment("Tabelle für die Kurse, welche einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
