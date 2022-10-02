package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle CredentialsLernplattformen.
 */
public class Tabelle_CredentialsLernplattformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes für die externen Account-Credentials (Lernplattformen)");

	/** Die Definition der Tabellenspalte LernplattformID */
	public SchemaTabelleSpalte col_LernplattformID = add("LernplattformID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Lernplattform");

	/** Die Definition der Tabellenspalte Benutzername */
	public SchemaTabelleSpalte col_Benutzername = add("Benutzername", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Benutzername für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte BenutzernamePseudonym */
	public SchemaTabelleSpalte col_BenutzernamePseudonym = add("BenutzernamePseudonym", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Der pseudonymisierte Benutzername für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte Initialkennwort */
	public SchemaTabelleSpalte col_Initialkennwort = add("Initialkennwort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Initialkennwort für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte PashwordHash */
	public SchemaTabelleSpalte col_PashwordHash = add("PashwordHash", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Passwordhash für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte RSAPublicKey */
	public SchemaTabelleSpalte col_RSAPublicKey = add("RSAPublicKey", SchemaDatentypen.TEXT, false)
		.setJavaComment("RSAPublicKey für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte RSAPrivateKey */
	public SchemaTabelleSpalte col_RSAPrivateKey = add("RSAPrivateKey", SchemaDatentypen.TEXT, false)
		.setJavaComment("RSAPrivateKey für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte AES */
	public SchemaTabelleSpalte col_AES = add("AES", SchemaDatentypen.TEXT, false)
		.setJavaComment("AES-Schlüssel für den Credential-Datensatz");


	/** Die Definition des Fremdschlüssels CredentialsLernplattformen_Lernplattform_FK */
	public SchemaTabelleFremdschluessel fk_CredentialsLernplattformen_Lernplattform_FK = addForeignKey(
			"CredentialsLernplattformen_Lernplattform_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_LernplattformID, Schema.tab_Lernplattformen.col_ID)
		);


	/** Die Definition des Unique-Index CredentialsLernplattformen_UC1 */
	public SchemaTabelleUniqueIndex unique_CredentialsLernplattformen_UC1 = addUniqueIndex("CredentialsLernplattformen_UC1", 
			col_Benutzername, 
			col_LernplattformID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle CredentialsLernplattformen.
	 */
	public Tabelle_CredentialsLernplattformen() {
		super("CredentialsLernplattformen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOCredentialsLernplattformen");
		setJavaComment("Daten für die Zugänge zu anderen Systemen (Lernplattformen)");
	}

}
