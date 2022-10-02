package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Zwischenergebnisse.
 */
public class Tabelle_Gost_Blockung_Zwischenergebnisse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: ID der Zwischenergebnisses (generiert)");

	/** Die Definition der Tabellenspalte Blockung_ID */
	public SchemaTabelleSpalte col_Blockung_ID = add("Blockung_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: ID der Blockung");

	/** Die Definition der Tabellenspalte AnzahlUmwaehler */
	public SchemaTabelleSpalte col_AnzahlUmwaehler = add("AnzahlUmwaehler", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Die Anzahl der Umwähler");

	/** Die Definition der Tabellenspalte Bewertung */
	public SchemaTabelleSpalte col_Bewertung = add("Bewertung", SchemaDatentypen.BIGINT, false)
		.setDefault("-1")
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Ein Wert zur Bewertung der Blockung");

	/** Die Definition der Tabellenspalte IstMarkiert */
	public SchemaTabelleSpalte col_IstMarkiert = add("IstMarkiert", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Gibt an, ob das Zwischenergebnis von einem Benutzer markiert wurde oder nicht: 1 - true, 0 - false ");

	/** Die Definition der Tabellenspalte IstDupliziert */
	public SchemaTabelleSpalte col_IstDupliziert = add("IstDupliziert", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Gibt an, ob das Zwischenergebnis mit einer anderen Blockungsdefinition dupliziert wurde und nur als Vorlage für Regeldefinitionen dient oder nicht: 1 - true, 0 - false ");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Blockung_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Blockung_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Blockung_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Blockung_ID, Schema.tab_Gost_Blockung.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Zwischenergebnisse.
	 */
	public Tabelle_Gost_Blockung_Zwischenergebnisse() {
		super("Gost_Blockung_Zwischenergebnisse", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungZwischenergebnis");
		setJavaComment("Tabelle für Zwischenergebnisse, welche einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
