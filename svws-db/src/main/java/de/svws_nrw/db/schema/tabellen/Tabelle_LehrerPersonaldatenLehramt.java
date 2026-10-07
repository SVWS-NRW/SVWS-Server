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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerPersonaldatenLehramt.
 */
public class Tabelle_LehrerPersonaldatenLehramt extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setJavaName("id")
			.setNotNull()
			.setJavaComment("Eine eindeutige ID für den Eintrag zum Lehramt eines Lehrers");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public final SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idLehrer")
			.setNotNull()
			.setJavaComment("Die ID des Lehrers zu der das Lehramt gehört");

	/** Die Definition der Tabellenspalte Lehramt_Katalog_ID */
	public final SchemaTabelleSpalte col_Lehramt_Katalog_ID = add("Lehramt_Katalog_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idKatalogLehramt")
			.setJavaComment("Die ID des Lehramtes aus dem zugehörigen Statistik-Katalog");

	/** Die Definition der Tabellenspalte LehramtAnerkennung_Katalog_ID */
	public final SchemaTabelleSpalte col_LehramtAnerkennung_Katalog_ID = add("LehramtAnerkennung_Katalog_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idAnerkennungsgrund")
			.setJavaComment("Die ID der Lehramts-Anerkennung aus dem zugehörigen Statistik-Katalog");


	/** Die Definition des Unique-Index LehrerPersonaldatenLehramt_UC1 */
	public final SchemaTabelleUniqueIndex unique_LehrerPersonaldatenLehramt_UC1 = addUniqueIndex("LehrerPersonaldatenLehramt_UC1",
			col_Lehrer_ID,
			col_Lehramt_Katalog_ID
	);


	/** Die Definition des Fremdschlüssels LehrerPersonaldatenLehramt_Lehrer_FK */
	public final SchemaTabelleFremdschluessel fk_LehrerPersonaldatenLehramt_Lehrer_FK = addForeignKey(
			"LehrerPersonaldatenLehramt_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLehramt.
	 */
	public Tabelle_LehrerPersonaldatenLehramt() {
		super("LehrerPersonaldatenLehramt", SchemaRevisionen.REV_45);
		setPKAutoIncrement();
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerPersonaldatenLehramt");
		setJavaComment("Die Informationen zu den Lehrämtern einer Lehrkraft");
	}

}
