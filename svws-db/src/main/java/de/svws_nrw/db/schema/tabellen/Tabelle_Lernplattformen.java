package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Lernplattformen.
 */
public class Tabelle_Lernplattformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes für die verwendete Lernplattform");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Bezeichnung der Lernplattform");

	/** Die Definition der Tabellenspalte BenutzernameSuffixLehrer */
	public SchemaTabelleSpalte col_BenutzernameSuffixLehrer = add("BenutzernameSuffixLehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Suffix für den Benutzernamen bei den Lehrern");

	/** Die Definition der Tabellenspalte BenutzernameSuffixErzieher */
	public SchemaTabelleSpalte col_BenutzernameSuffixErzieher = add("BenutzernameSuffixErzieher", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Suffix für den Benutzernamen bei den Erziehern");

	/** Die Definition der Tabellenspalte BenutzernameSuffixSchueler */
	public SchemaTabelleSpalte col_BenutzernameSuffixSchueler = add("BenutzernameSuffixSchueler", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Suffix für den Benutzernamen bei den Schülern");

	/** Die Definition der Tabellenspalte Konfiguration */
	public SchemaTabelleSpalte col_Konfiguration = add("Konfiguration", SchemaDatentypen.TEXT, false)
		.setJavaComment("Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Lernplattformen.
	 */
	public Tabelle_Lernplattformen() {
		super("Lernplattformen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOLernplattformen");
		setJavaComment("Tabelle mit Lernplattformen die die Schule nutzt und zu denen Credentials existieren");
	}

}
