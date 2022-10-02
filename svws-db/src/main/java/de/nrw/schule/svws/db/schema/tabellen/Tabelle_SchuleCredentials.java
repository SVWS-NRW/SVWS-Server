package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuleCredentials.
 */
public class Tabelle_SchuleCredentials extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schulnummer */
	public SchemaTabelleSpalte col_Schulnummer = add("Schulnummer", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("ID für den Credential-Datensatz einer Schule (also auch für den PublicKey der anderen Schulen)");

	/** Die Definition der Tabellenspalte RSAPublicKey */
	public SchemaTabelleSpalte col_RSAPublicKey = add("RSAPublicKey", SchemaDatentypen.TEXT, false)
		.setJavaComment("RSAPublicKey für den Credential-Datensatz einer Schule");

	/** Die Definition der Tabellenspalte RSAPrivateKey */
	public SchemaTabelleSpalte col_RSAPrivateKey = add("RSAPrivateKey", SchemaDatentypen.TEXT, false)
		.setJavaComment("RSAPrivateKey für den Credential-Datensatz der Schule falls es die eigene ist sonst NULL");

	/** Die Definition der Tabellenspalte AES */
	public SchemaTabelleSpalte col_AES = add("AES", SchemaDatentypen.TEXT, false)
		.setJavaComment("AES-Schlüssel für den Credential-Datensatz der Schule falls es die eigene ist sonst NULL");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuleCredentials.
	 */
	public Tabelle_SchuleCredentials() {
		super("SchuleCredentials", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOSchuleCredentials");
		setJavaComment("Tabelle für die Zugangsdaten von Schulen (PublicKey) und den Keys der eingenene Schule");
	}

}
