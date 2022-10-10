package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle DavSyncTokenLehrer.
 */
public class Tabelle_DavSyncTokenLehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Lehrers");

	/** Die Definition der Tabellenspalte SyncToken */
	public SchemaTabelleSpalte col_SyncToken = add("SyncToken", SchemaDatentypen.DATETIME, false)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an für einen Card-DAV-Eintrag relevanten Lehrerdaten.");


	/** Die Definition des Fremdschlüssels DavSyncTokenLehrer_FK */
	public SchemaTabelleFremdschluessel fk_DavSyncTokenLehrer_FK = addForeignKey(
			"DavSyncTokenLehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ID, Schema.tab_K_Lehrer.col_ID)
		);

    // TODO Trigger für MariaDB 

    // TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Defintion für die Tabelle DavSyncTokenLehrer.
	 */
	public Tabelle_DavSyncTokenLehrer() {
		super("DavSyncTokenLehrer", SchemaRevisionen.REV_8);
		setMigrate(false);
		setImportExport(false);
		setPKAutoIncrement();
		setJavaSubPackage("svws.dav");
		setJavaClassName("DTODavSyncTokenLehrer");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an den für Card-DAV relevanten Datenbanktabellen "
		        + "für einen Lehrer Änderungen vorgenommen wurden. "
		        + "Diese Zeitstempel dienen als Sync-Token für das Protokoll.");
	}

}
