package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Users.
 */
public class Tabelle_Users extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte US_Name */
	public SchemaTabelleSpalte col_US_Name = add("US_Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Name des Datenbankbenutzers ");

	/** Die Definition der Tabellenspalte US_LoginName */
	public SchemaTabelleSpalte col_US_LoginName = add("US_LoginName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("LoginName des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte US_Password */
	public SchemaTabelleSpalte col_US_Password = add("US_Password", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("DEPRECATED: nicht mehr genutzt altes Passwort des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte US_UserGroups */
	public SchemaTabelleSpalte col_US_UserGroups = add("US_UserGroups", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Zugehörigkeit zu Usergruops des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte US_Privileges */
	public SchemaTabelleSpalte col_US_Privileges = add("US_Privileges", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Berechtigungen des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("E-Mail-Adresse des Datenbankbenutzers für des Versand aus Schild-NRW");

	/** Die Definition der Tabellenspalte EmailName */
	public SchemaTabelleSpalte col_EmailName = add("EmailName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Name des Datenbankbenutzers für den Mailversand aus Schild-NRW");

	/** Die Definition der Tabellenspalte SMTPUsername */
	public SchemaTabelleSpalte col_SMTPUsername = add("SMTPUsername", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("SMTP Username des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte SMTPPassword */
	public SchemaTabelleSpalte col_SMTPPassword = add("SMTPPassword", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("SMTP Passwort des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte EmailSignature */
	public SchemaTabelleSpalte col_EmailSignature = add("EmailSignature", SchemaDatentypen.TEXT, false)
		.setJavaComment("E-Mail-Signatur des Datenbankbenutzers");

	/** Die Definition der Tabellenspalte HeartbeatDate */
	public SchemaTabelleSpalte col_HeartbeatDate = add("HeartbeatDate", SchemaDatentypen.INT, false)
		.setJavaComment("Datum des Heartbeats bei einigen Datenbank verwende um Sleepmodus zu verhindern");

	/** Die Definition der Tabellenspalte ComputerName */
	public SchemaTabelleSpalte col_ComputerName = add("ComputerName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Name des Computers an dem der Benutzer zuletzt angemeldet war");

	/** Die Definition der Tabellenspalte US_PasswordHash */
	public SchemaTabelleSpalte col_US_PasswordHash = add("US_PasswordHash", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("PasswortHash des Users mit BeCrypt generiert");


	/** Die Definition des Unique-Index Users_UC1 */
	public SchemaTabelleUniqueIndex unique_Users_UC1 = addUniqueIndex("Users_UC1", 
			col_US_LoginName
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Users.
	 */
	public Tabelle_Users() {
		super("Users", SchemaRevisionen.REV_0);
		setVeraltet(SchemaRevisionen.REV_1);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOUsers");
		setJavaComment("Tabelle der Schild-User");
	}

}
