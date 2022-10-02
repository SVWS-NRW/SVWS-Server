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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_KAoA_Zusatzmerkmal.
 */
public class Tabelle_Schildintern_KAoA_Zusatzmerkmal extends SchemaTabelle {

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

	/** Die Definition der Tabellenspalte ZM_Kuerzel */
	public SchemaTabelleSpalte col_ZM_Kuerzel = add("ZM_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel des Zusatzmerkmals");

	/** Die Definition der Tabellenspalte Merkmal_ID */
	public SchemaTabelleSpalte col_Merkmal_ID = add("Merkmal_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ID des übergeordneten Merkmal zum Zusatzmerkmal");

	/** Die Definition der Tabellenspalte ZM_Beschreibung */
	public SchemaTabelleSpalte col_ZM_Beschreibung = add("ZM_Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Beschreibung (Klartext) des Zusatzmerkmal");

	/** Die Definition der Tabellenspalte ZM_Option */
	public SchemaTabelleSpalte col_ZM_Option = add("ZM_Option", SchemaDatentypen.VARCHAR, false).setDatenlaenge(25)
		.setJavaComment("Schildintern Tabelle: Zusatz Option die zum Zusatzmerkmal eintragbar ist");

	/** Die Definition der Tabellenspalte ZM_Merkmal */
	public SchemaTabelleSpalte col_ZM_Merkmal = add("ZM_Merkmal", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: DEPRECATED: Übergeordnetes Merkmal zum Zusatzmerkmal");


	/** Die Definition des Fremdschlüssels Schildintern_KAoA_Zusatzmerkmal_Merkmal_FK */
	public SchemaTabelleFremdschluessel fk_Schildintern_KAoA_Zusatzmerkmal_Merkmal_FK = addForeignKey(
			"Schildintern_KAoA_Zusatzmerkmal_Merkmal_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Merkmal_ID, Schema.tab_Schildintern_KAoA_Merkmal.col_ID)
		);


	/** Die Definition des Non-Unique-Index Schildintern_KAoA_Zusatzmerkmal_IDX1 */
	public SchemaTabelleIndex index_Schildintern_KAoA_Zusatzmerkmal_IDX1 = addIndex("Schildintern_KAoA_Zusatzmerkmal_IDX1", 
			col_ZM_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_KAoA_Zusatzmerkmal.
	 */
	public Tabelle_Schildintern_KAoA_Zusatzmerkmal() {
		super("Schildintern_KAoA_Zusatzmerkmal", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOKAoAZusatzmerkmal");
		setJavaComment("KAOA-Defaultdaten werden vom MSB geliefert");
	}

}
