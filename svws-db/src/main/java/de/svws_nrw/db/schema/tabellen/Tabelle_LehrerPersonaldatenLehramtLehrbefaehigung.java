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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerPersonaldatenLehramtLehrbefaehigung.
 */
public class Tabelle_LehrerPersonaldatenLehramtLehrbefaehigung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Eine eindeutige ID für den Eintrag zu der Lehrbefähigung zu einem Lehramt eines Lehrers");

	/** Die Definition der Tabellenspalte Lehreramt_ID */
	public SchemaTabelleSpalte col_Lehreramt_ID = add("Lehreramt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Lehramtseintrags des Lehrers zu der die Lehrbefähigung gehört");

	/** Die Definition der Tabellenspalte Lehrbefaehigung_Katalog_ID */
	public SchemaTabelleSpalte col_Lehrbefaehigung_Katalog_ID = add("Lehrbefaehigung_Katalog_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID der Lehrbefähigung aus dem zugehörigen Statistik-Katalog");

	/** Die Definition der Tabellenspalte LehrbefaehigungAnerkennung_Katalog_ID */
	public SchemaTabelleSpalte col_LehrbefaehigungAnerkennung_Katalog_ID = add("LehrbefaehigungAnerkennung_Katalog_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID des Anerkennungsgrundes für die Lehrbefähigung des Lehrers aus dem zugehörigen Statistik-Katalog");


	/** Die Definition des Unique-Index LehrerPersonaldatenLehramtLehrbefaehigung_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerPersonaldatenLehramtLehrbefaehigung_UC1 = addUniqueIndex("LehrerPersonaldatenLehramtLehrbefaehigung_UC1",
			col_Lehreramt_ID,
			col_Lehrbefaehigung_Katalog_ID
	);


	/** Die Definition des Fremdschlüssels LehrerPersonaldatenLehramtLehrbefaehigung_Lehramt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerPersonaldatenLehramtLehrbefaehigung_Lehramt_FK = addForeignKey(
			"LehrerPersonaldatenLehramtLehrbefaehigung_Lehramt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehreramt_ID, Schema.tab_LehrerPersonaldatenLehramt.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLehramtLehrbef.
	 */
	public Tabelle_LehrerPersonaldatenLehramtLehrbefaehigung() {
		super("LehrerPersonaldatenLehramtLehrbefaehigung", SchemaRevisionen.REV_45);
		setPKAutoIncrement();
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerPersonaldatenLehramtBefaehigung");
		setJavaComment("Die Informationen zu den Lehrbefähigungen, welche einem Lehramt bei einem Lehrer zugeordnet sind");
	}

}
