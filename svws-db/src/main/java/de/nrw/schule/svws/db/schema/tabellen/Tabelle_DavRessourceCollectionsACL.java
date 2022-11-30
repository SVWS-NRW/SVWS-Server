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
 * Definition der Tabelle für DavRessourceCollectionACL
 */
public class Tabelle_DavRessourceCollectionsACL extends SchemaTabelle {
	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des ACL Eintrags");
	
	/** Die Definition der Tabellenspalte Benutzer_ID */
	public SchemaTabelleSpalte col_Benutzer_ID = add("Benutzer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Benutzers dieses ACL-Eintrags");

	/** Definition der Tabellenspalte RessourceCollection_ID */
	public SchemaTabelleSpalte col_RessourceCollection_ID = add("RessourceCollection_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID der RessourceCollection dieses ACL-Eintrags");

	/** Die Definition der Tabellenspalte Berechtigungen */
	public SchemaTabelleSpalte col_Typ = add("berechtigungen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Gibt die Berechtigungen dieses ACL-Eintrags wieder, ähnlich einer unix-file permission - bspw. 'r-' für nur Leserecht oder 'rw' für Lese- und Schreibrecht.");

	/** Die Definition des Fremdschlüssels DavRessourceCollectionsACL_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_DavRessourceCollectionsACL_Benutzer_FK = addForeignKey(
			"DavRessourceCollectionsACL_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Benutzer_ID, Schema.tab_Benutzer.col_ID)
		);

	/** Die Definition des Fremdschlüssels DavRessourceCollectionACL_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_DavRessourceCollectionsACL_RessourceCollection_FK = addForeignKey(
			"DavRessourceCollectionsACL_RessourceCollection_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_RessourceCollection_ID, Schema.tab_DavRessourceCollections.col_ID)
		);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle DavRessourceCollections.
	 */
	public Tabelle_DavRessourceCollectionsACL() {
		super("DavRessourceCollectionsACL", SchemaRevisionen.REV_8);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.dav");
		setJavaClassName("DTODavRessourceCollectionsACL");
		setJavaComment("Ein Eintrag zur Berechtigung eines Nutzers für eine DavRessourceCollection.");
	}

}
