package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Termine_Jahrgaenge.
 */
public class Tabelle_Gost_Klausuren_Termine_Jahrgaenge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Termin_ID des Klausurtermins");

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Der Abiturjahrgang, der zum Klausurtermin zugelassen werden soll.");


	/** Die Definition des Fremdschlüssels Gost_Klausuren_Termine_Jahrgaenge_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Termine_Jahrgaenge_Termin_ID_FK = addForeignKey(
		"Gost_Klausuren_Termine_Jahrgaenge_Termin_ID_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Termine_Jahrgaenge_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Termine_Jahrgaenge_Abi_Jahrgang_FK = addForeignKey(
		"Gost_Klausuren_Termine_Jahrgaenge_Abi_Jahrgang_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
	);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Termine_Jahrgaenge_IDX_Abi_Jahrgang */
	public SchemaTabelleIndex index_Gost_Klausuren_Termine_Jahrgaenge_IDX_Abi_Jahrgang = addIndex("Gost_Klausuren_Termine_Jahrgaenge_IDX_Abi_Jahrgang",
		col_Abi_Jahrgang
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Termine.
	 */
	public Tabelle_Gost_Klausuren_Termine_Jahrgaenge() {
		super("Gost_Klausuren_Termine_Jahrgaenge", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenTermineJahrgaenge");
		setJavaComment("Tabelle für die Definition von zugelassenen Jahrgangsstufen zu Klausurterminen");
	}

}
