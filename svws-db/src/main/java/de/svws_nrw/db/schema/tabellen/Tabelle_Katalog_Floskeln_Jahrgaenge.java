package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Katalog_Floskeln_Jahrgaenge.
 */
public class Tabelle_Katalog_Floskeln_Jahrgaenge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Floskel-Jahrgangszuordnung (generiert)");

	/** Die Definition der Tabellenspalte Floskel_ID */
	public SchemaTabelleSpalte col_Floskel_ID = add("Floskel_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID der Floskel");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Jahrgangs");

	/** Die Definition des Fremdschlüssels Katalog_Floskeln_Jahrgaenge_Floskel_ID_FK */
	public SchemaTabelleFremdschluessel fk_Katalog_Floskeln_Jahrgaenge_Floskel_ID_FK = addForeignKey(
			"Katalog_Floskeln_Jahrgaenge_Floskel_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Floskel_ID, Schema.tab_Katalog_Floskeln.col_ID));

	/** Die Definition des Fremdschlüssels Katalog_Floskeln_Jahrgaenge_Jahrgang_ID_FK */
	public SchemaTabelleFremdschluessel fk_Katalog_Floskeln_Jahrgaenge_Jahrgang_ID_FK = addForeignKey(
			"Katalog_Floskeln_Jahrgaenge_Jahrgang_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID));

	/** Die Definition des Unique-Index Katalog_Floskeln_Jahrgaenge_UC1 */
	public SchemaTabelleUniqueIndex unique_Katalog_Floskeln_Jahrgaenge_UC1 = addUniqueIndex("Katalog_Floskeln_Jahrgaenge_UC1", col_Jahrgang_ID, col_Floskel_ID);

	/**
	 * Erstellt die Schema-Definition für die Tabelle Katalog_Floskeln_Jahrgaenge.
	 */
	public Tabelle_Katalog_Floskeln_Jahrgaenge() {
		super("Katalog_Floskeln_Jahrgaenge", SchemaRevisionen.REV_52);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("katalog");
		setJavaClassName("DTOFloskelnJahrgaenge");
		setJavaComment("Die Zuordnung von Floskeln zu Jahrgängen, sofern bei den Floskeln Jahrgangseinschränkungen vorliegen.");
	}

}
