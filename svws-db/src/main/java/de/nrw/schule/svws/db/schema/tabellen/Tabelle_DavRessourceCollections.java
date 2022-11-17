package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.DatumUhrzeitConverter;
import de.nrw.schule.svws.db.converter.current.DavRessourceCollectionTypConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle DavRessourceCollections.
 */
public class Tabelle_DavRessourceCollections extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Benutzer_ID */
	public SchemaTabelleSpalte col_Benutzer_ID = add("Benutzer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Benutzers, zu dem der Datensatz gehört");

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der WebDav-Ressourcensammlung");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setNotNull()
		.setConverter(DavRessourceCollectionTypConverter.class)
		.setJavaComment("Gibt den Typ dieser Sammlung an, bspw Adressbuch oder Kalender");

	/** Die Definition der Tabellenspalte Anzeigename */
	public SchemaTabelleSpalte col_Anzeigename = add("Anzeigename", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setNotNull()
		.setJavaComment("Der Anzeigename der Ressourcensammlung");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2047)
		.setJavaComment("Die Beschreibung der Ressourcensammlung");

	/** Die Definition der Tabellenspalte SyncToken */
	public SchemaTabelleSpalte col_SyncToken = add("SyncToken", SchemaDatentypen.DATETIME, false)
		.setNotNull()
		.setConverter(DatumUhrzeitConverter.class)
		.setJavaComment("Das SyncToken der Ressourcensammlung");


	/** Die Definition des Fremdschlüssels DavRessourceCollection_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_DavRessourceCollection_Benutzer_FK = addForeignKey(
			"DavRessourceCollection_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Benutzer_ID, Schema.tab_Benutzer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle DavRessourceCollections.
	 */
	public Tabelle_DavRessourceCollections() {
		super("DavRessourceCollections", SchemaRevisionen.REV_8);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.dav");
		setJavaClassName("DTODavRessourceCollection");
		setJavaComment("Eine Sammlung von WebDav Ressourcen, vergleichbar mit einem Adressbuch oder einer Sammlung von Kalenderdaten.");
	}

}
