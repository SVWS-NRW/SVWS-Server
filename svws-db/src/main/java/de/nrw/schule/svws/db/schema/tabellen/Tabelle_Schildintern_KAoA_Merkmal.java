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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_KAoA_Merkmal.
 */
public class Tabelle_Schildintern_KAoA_Merkmal extends SchemaTabelle {

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

	/** Die Definition der Tabellenspalte M_Kuerzel */
	public SchemaTabelleSpalte col_M_Kuerzel = add("M_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel des Merkmals");

	/** Die Definition der Tabellenspalte Kategorie_ID */
	public SchemaTabelleSpalte col_Kategorie_ID = add("Kategorie_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ID der übergeordneten Kategorie zum Merkmal");

	/** Die Definition der Tabellenspalte M_Beschreibung */
	public SchemaTabelleSpalte col_M_Beschreibung = add("M_Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Beschreibung (Klartext) des Merkmals");

	/** Die Definition der Tabellenspalte M_Option */
	public SchemaTabelleSpalte col_M_Option = add("M_Option", SchemaDatentypen.VARCHAR, false).setDatenlaenge(25)
		.setJavaComment("Schildintern Tabelle: Zusatz Option die zum Merkmal eintragbar ist");

	/** Die Definition der Tabellenspalte M_Kategorie */
	public SchemaTabelleSpalte col_M_Kategorie = add("M_Kategorie", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Schildintern Tabelle: DEPRECATED: Übergeordnete Kategorie zum Merkmal");

	/** Die Definition der Tabellenspalte BK_Anlagen */
	public SchemaTabelleSpalte col_BK_Anlagen = add("BK_Anlagen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Feld für die Anzeige in der Schulform BK");


	/** Die Definition des Fremdschlüssels Schildintern_KAoA_Merkmal_Kategorie_FK */
	public SchemaTabelleFremdschluessel fk_Schildintern_KAoA_Merkmal_Kategorie_FK = addForeignKey(
			"Schildintern_KAoA_Merkmal_Kategorie_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kategorie_ID, Schema.tab_Schildintern_KAoA_Kategorie.col_ID)
		);


	/** Die Definition des Non-Unique-Index Schildintern_KAoA_Merkmal_IDX1 */
	public SchemaTabelleIndex index_Schildintern_KAoA_Merkmal_IDX1 = addIndex("Schildintern_KAoA_Merkmal_IDX1", 
			col_M_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_KAoA_Merkmal.
	 */
	public Tabelle_Schildintern_KAoA_Merkmal() {
		super("Schildintern_KAoA_Merkmal", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOKAoAMerkmal");
		setJavaComment("KAOA-Defaultdaten werden vom MSB geliefert");
	}

}
