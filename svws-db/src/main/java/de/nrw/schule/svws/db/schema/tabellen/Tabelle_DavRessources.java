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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle DavRessources.
 */
public class Tabelle_DavRessources extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Dav Ressource");

	/** Die Definition der Tabellenspalte DavRessourceCollection_ID */
	public SchemaTabelleSpalte col_DavRessourceCollection_ID = add("DavRessourceCollection_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Id der Ressourcensammlung, deren Teil diese Ressource ist");

	/** Die Definition der Tabellenspalte UID */
	public SchemaTabelleSpalte col_UID = add("UID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setNotNull()
		.setJavaComment("Die UID der Ressource");

	/** Die Definition der Tabellenspalte lastModified */
	public SchemaTabelleSpalte col_lastModified = add("lastModified", SchemaDatentypen.DATETIME, false)
		.setNotNull()
		.setJavaComment("Das Datum an dem die Ressource zuletzt geändert wurde, als Synctoken einsetzbar");

	/** Die Definition der Tabellenspalte KalenderTyp */
	public SchemaTabelleSpalte col_KalenderTyp = add("KalenderTyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setNotNull()
		.setJavaComment("Die Art der Kalenderressource, wenn es eine Kalenderressource ist");

	/** Die Definition der Tabellenspalte KalenderStart */
	public SchemaTabelleSpalte col_KalenderStart = add("KalenderStart", SchemaDatentypen.DATETIME, false)
		.setNotNull()
		.setJavaComment("Der Start der Kalenderressource, wenn es eine Kalenderressource ist");

	/** Die Definition der Tabellenspalte KalenderEnde */
	public SchemaTabelleSpalte col_KalenderEnde = add("KalenderEnde", SchemaDatentypen.DATETIME, false)
		.setNotNull()
		.setJavaComment("Das Ende der Kalenderressource, wenn es eines Kalenderressource ist");

	/** Die Definition der Tabellenspalte ressource */
	public SchemaTabelleSpalte col_ressource = add("ressource", SchemaDatentypen.LONGBLOB, false)
		.setNotNull()
		.setJavaComment("Die Daten der Ressource");


	/** Die Definition des Fremdschlüssels DavRessources_Collection_FK */
	public SchemaTabelleFremdschluessel fk_DavRessources_Collection_FK = addForeignKey(
			"DavRessources_Collection_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_DavRessourceCollection_ID, Schema.tab_DavRessourceCollections.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle DavRessources.
	 */
	public Tabelle_DavRessources() {
		super("DavRessources", SchemaRevisionen.REV_8);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.dav");
		setJavaClassName("DTODavRessource");
		setJavaComment("Ein WebDav Ressourcen, vergleichbar mit einem Adressbucheintrag oder Kalenderdaten.");
	}

}
