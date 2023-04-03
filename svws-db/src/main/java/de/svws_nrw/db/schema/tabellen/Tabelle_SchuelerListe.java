package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerListe.
 */
public class Tabelle_SchuelerListe extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der individuellen Schülerliste");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Bezeichnung der individuellen Schülerliste");

	/** Die Definition der Tabellenspalte Erzeuger */
	public SchemaTabelleSpalte col_Erzeuger = add("Erzeuger", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("UserID Erzeuger der individuellen Schülerliste");

	/** Die Definition der Tabellenspalte Privat */
	public SchemaTabelleSpalte col_Privat = add("Privat", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Schülerliste Privat oder Öffentlich");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index SchuelerListe_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerListe_UC1 = addUniqueIndex("SchuelerListe_UC1",
			col_Bezeichnung
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerListe.
	 */
	public Tabelle_SchuelerListe() {
		super("SchuelerListe", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchuelerIndividuelleGruppe");
		setJavaComment("Liste mit allen angelegten individuellen Schülerlisten");
	}

}
