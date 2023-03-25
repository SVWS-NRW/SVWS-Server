package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Usergroups.
 */
public class Tabelle_Usergroups extends SchemaTabelle {

	/** Die Definition der Tabellenspalte UG_ID */
	public SchemaTabelleSpalte col_UG_ID = add("UG_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Benutzergruppe");

	/** Die Definition der Tabellenspalte UG_Bezeichnung */
	public SchemaTabelleSpalte col_UG_Bezeichnung = add("UG_Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(64)
		.setJavaComment("Bezeichnung der Benutzergruppe");

	/** Die Definition der Tabellenspalte UG_Kompetenzen */
	public SchemaTabelleSpalte col_UG_Kompetenzen = add("UG_Kompetenzen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Kompetenzen der Benutzergrupppe in vorgegebenen Zahlerwerten");

	/** Die Definition der Tabellenspalte UG_Nr */
	public SchemaTabelleSpalte col_UG_Nr = add("UG_Nr", SchemaDatentypen.INT, false)
		.setJavaComment("Nummer der Benutzergruppe ???");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index Usergroups_UC1 */
	public SchemaTabelleUniqueIndex unique_Usergroups_UC1 = addUniqueIndex("Usergroups_UC1", 
			col_UG_Bezeichnung
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Usergroups.
	 */
	public Tabelle_Usergroups() {
		super("Usergroups", SchemaRevisionen.REV_0);
		setVeraltet(SchemaRevisionen.REV_1);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOUserGroups");
		setJavaComment("Usergruppen mit Berechtigungen");
	}

}
