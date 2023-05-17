package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Schienen.
 */
public class Tabelle_Stundenplan_Schienen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID identifiziert einen Schieneneintrag für einen Stundenplan eindeutig");

	/** Die Definition der Tabellenspalte Stundenplan_ID */
	public SchemaTabelleSpalte col_Stundenplan_ID = add("Stundenplan_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Stundenplans, dem dieser Schieneneintrag zugeordnet wird");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Jahrgangs dem die Schiene zugeordnet wird");

	/** Die Definition der Tabellenspalte Nummer */
	public SchemaTabelleSpalte col_Nummer = add("Nummer", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Die Nummer der Schiene");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Schiene");


	/** Die Definition des Fremdschlüssels Stundenplan_Schienen_Stundenplan_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Schienen_Stundenplan_FK = addForeignKey(
		"Stundenplan_Schienen_Stundenplan_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Stundenplan_ID, Schema.tab_Stundenplan.col_ID)
	);


	/** Die Definition des Fremdschlüssels Stundenplan_Schienen_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Schienen_Jahrgang_FK = addForeignKey(
		"Stundenplan_Schienen_Jahrgang_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
		new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
	);


	/** Die Definition des Unique-Index Stundenplan_Schienen_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Schienen_UC1 = addUniqueIndex("Stundenplan_Schienen_UC1",
		col_Stundenplan_ID,
		col_Jahrgang_ID,
		col_Nummer
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Schienen.
	 */
	public Tabelle_Stundenplan_Schienen() {
		super("Stundenplan_Schienen", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanSchienen");
		setJavaComment("Enthält die Liste von Schienen, welche im Stundenplan verwendet werden.");
	}

}
