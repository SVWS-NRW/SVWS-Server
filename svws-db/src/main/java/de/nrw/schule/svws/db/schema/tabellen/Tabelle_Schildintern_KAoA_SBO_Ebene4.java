package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_KAoA_SBO_Ebene4.
 */
public class Tabelle_Schildintern_KAoA_SBO_Ebene4 extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Eindeutige ID für den Datensatz");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Der Datensatz ist gültig ab dem Datum");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Der Datensatz ist gültig bis zu dem Datum");

	/** Die Definition der Tabellenspalte Kuerzel_EB4 */
	public SchemaTabelleSpalte col_Kuerzel_EB4 = add("Kuerzel_EB4", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel der übergeorneten SBO");

	/** Die Definition der Tabellenspalte Beschreibung_EB4 */
	public SchemaTabelleSpalte col_Beschreibung_EB4 = add("Beschreibung_EB4", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Bezeichnung des übergeordneten Zusatzmerkmal");

	/** Die Definition der Tabellenspalte Zusatzmerkmal */
	public SchemaTabelleSpalte col_Zusatzmerkmal = add("Zusatzmerkmal", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: ID des übergeordneten Zusatzmerkmal");

	/** Die Definition der Tabellenspalte Zusatzmerkmal_ID */
	public SchemaTabelleSpalte col_Zusatzmerkmal_ID = add("Zusatzmerkmal_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Schildintern Tabelle: DEPRECATED: Übergeordnetes Merkmal zum Zusatzmerkmal");


	/** Die Definition des Fremdschlüssels Schildintern_KAoA_SBO_Ebene4_Zusatzmerkmall_FK */
	public SchemaTabelleFremdschluessel fk_Schildintern_KAoA_SBO_Ebene4_Zusatzmerkmall_FK = addForeignKey(
			"Schildintern_KAoA_SBO_Ebene4_Zusatzmerkmall_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Zusatzmerkmal_ID, Schema.tab_Schildintern_KAoA_Zusatzmerkmal.col_ID)
		);


	/** Die Definition des Non-Unique-Index Schildintern_KAoA_SBO_Ebene4_IDX1 */
	public SchemaTabelleIndex index_Schildintern_KAoA_SBO_Ebene4_IDX1 = addIndex("Schildintern_KAoA_SBO_Ebene4_IDX1", 
			col_Kuerzel_EB4
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_KAoA_SBO_Ebene4.
	 */
	public Tabelle_Schildintern_KAoA_SBO_Ebene4() {
		super("Schildintern_KAoA_SBO_Ebene4", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOKAoASBOEB4");
		setJavaComment("KAOA-Defaultdaten werden vom MSB geliefert");
	}

}
