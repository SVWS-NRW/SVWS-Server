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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schueler.
 */
public class Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Zwischenergebnis_ID */
	public SchemaTabelleSpalte col_Zwischenergebnis_ID = add("Zwischenergebnis_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Kurs-Schüler-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Zwischenergebnisses");

	/** Die Definition der Tabellenspalte Blockung_Kurs_ID */
	public SchemaTabelleSpalte col_Blockung_Kurs_ID = add("Blockung_Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Kurs-Schüler-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Kurses");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Kurs-Schüler-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Schülers");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_ErgID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_ErgID_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_ErgID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Zwischenergebnis_ID, Schema.tab_Gost_Blockung_Zwischenergebnisse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_Kurs_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_Kurs_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_Kurs_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Blockung_Kurs_ID, Schema.tab_Gost_Blockung_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_Schueler_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Kurs_Schueler_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schueler.
	 */
	public Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler() {
		super("Gost_Blockung_Zwischenergebnisse_Kurs_Schueler", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungZwischenergebnisKursSchueler");
		setJavaComment("Tabelle für die Zuordnung von Schüler zu Kursen bei Zwischenergebnissen, welche einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
