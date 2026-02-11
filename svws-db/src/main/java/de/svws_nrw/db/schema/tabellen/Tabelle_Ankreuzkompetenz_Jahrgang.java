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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Ankreuzkompetenz_Jahrgang.
 */
public class Tabelle_Ankreuzkompetenz_Jahrgang extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setJavaName("id")
			.setNotNull()
			.setJavaComment("ID der Ankreuzkompetenz-Jahrgangszuordnung (generiert)");

	/** Die Definition der Tabellenspalte ID_Ankreuzkompetenz */
	public SchemaTabelleSpalte col_ID_Ankreuzkompetenz = add("ID_Ankreuzkompetenz", SchemaDatentypen.BIGINT, false)
			.setJavaName("idAnkreuzkompetenz")
			.setNotNull()
			.setJavaComment("Die ID der Ankreuzkompetenz");

	/** Die Definition der Tabellenspalte ID_Jahrgang */
	public SchemaTabelleSpalte col_ID_Jahrgang = add("ID_Jahrgang", SchemaDatentypen.BIGINT, false)
			.setJavaName("idJahrgang")
			.setNotNull()
			.setJavaComment("Die ID des Jahrgangs");

	/** Die Definition des Fremdschlüssels Ankreuzkompetenz_Jahrgang_ID_Ankreuzkompetenz_FK */
	public SchemaTabelleFremdschluessel fk_Ankreuzkompetenz_Jahrgang_ID_Ankreuzkompetenz_FK = addForeignKey(
			"Ankreuzkompetenz_Jahrgang_ID_Ankreuzkompetenz_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID_Ankreuzkompetenz, Schema.tab_K_Ankreuzfloskeln.col_ID));

	/** Die Definition des Fremdschlüssels Ankreuzkompetenz_Jahrgang_ID_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Ankreuzkompetenz_Jahrgang_ID_Jahrgang_FK = addForeignKey(
			"Ankreuzkompetenz_Jahrgang_ID_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID_Jahrgang, Schema.tab_EigeneSchule_Jahrgaenge.col_ID));

	/** Die Definition des Unique-Index Ankreuzkompetenz_Jahrgang_UC1 */
	public SchemaTabelleUniqueIndex unique_Ankreuzkompetenz_Jahrgang_UC1 = addUniqueIndex(
			"Ankreuzkompetenz_Jahrgang_UC1",
			col_ID_Jahrgang,
			col_ID_Ankreuzkompetenz);

	/**
	 * Erstellt die Schema-Definition für die Tabelle Ankreuzkompetenz_Jahrgang.
	 */
	public Tabelle_Ankreuzkompetenz_Jahrgang() {
		super("Ankreuzkompetenz_Jahrgang", SchemaRevisionen.REV_55);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("katalog");
		setJavaClassName("DTOAnkreuzkompetenzJahrgang");
		setJavaComment("Die Zuordnung einer Ankreuzkompetenz zu einem Jahrgang.");
	}

}
