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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerPersonaldatenLehramtFachrichtung.
 */
public class Tabelle_LehrerPersonaldatenLehramtFachrichtung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Eine eindeutige ID für den Eintrag zu der Fachrichtung zu einem Lehramt eines Lehrers");

	/** Die Definition der Tabellenspalte Lehreramt_ID */
	public SchemaTabelleSpalte col_Lehreramt_ID = add("Lehreramt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Lehramtseintrags des Lehrers zu der die Fachrichtung gehört");

	/** Die Definition der Tabellenspalte Fachrichtung_Katalog_ID */
	public SchemaTabelleSpalte col_Fachrichtung_Katalog_ID = add("Fachrichtung_Katalog_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID der Fachrichtung aus dem zugehörigen Statistik-Katalog");

	/** Die Definition der Tabellenspalte FachrichtungAnerkennung_Katalog_ID */
	public SchemaTabelleSpalte col_FachrichtungAnerkennung_Katalog_ID = add("FachrichtungAnerkennung_Katalog_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID des Anerkennungsgrundes für die Fachrichtung des Lehrers aus dem zugehörigen Statistik-Katalog");


	/** Die Definition des Unique-Index LehrerPersonaldatenLehramtFachrichtung_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerPersonaldatenLehramtFachrichtung_UC1 = addUniqueIndex("LehrerPersonaldatenLehramtFachrichtung_UC1",
			col_Lehreramt_ID,
			col_Fachrichtung_Katalog_ID
	);


	/** Die Definition des Fremdschlüssels LehrerPersonaldatenLehramtFachrichtung_Lehramt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerPersonaldatenLehramtFachrichtung_Lehramt_FK = addForeignKey(
			"LehrerPersonaldatenLehramtFachrichtung_Lehramt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehreramt_ID, Schema.tab_LehrerPersonaldatenLehramt.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLehramtFachr.
	 */
	public Tabelle_LehrerPersonaldatenLehramtFachrichtung() {
		super("LehrerPersonaldatenLehramtFachrichtung", SchemaRevisionen.REV_45);
		setPKAutoIncrement();
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerPersonaldatenLehramtFachrichtung");
		setJavaComment("Die Informationen zu den Fachrichtungen, welche einem Lehramt bei einem Lehrer zugeordnet sind");
	}

}
