package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerNotenmodulCredentials.
 */
public class Tabelle_LehrerNotenmodulCredentials extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die LehrerID des Lehrers, für den die Credentials gelten");

	/** Die Definition der Tabellenspalte Initialkennwort */
	public SchemaTabelleSpalte col_Initialkennwort = add("Initialkennwort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Initialkennwort für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte PasswordHash */
	public SchemaTabelleSpalte col_PasswordHash = add("PasswordHash", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Passwordhash für den Credential-Datensatz");


	/** Die Definition des Fremdschlüssels LehrerNotenmodulCredentials_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerNotenmodulCredentials_Lehrer_FK = addForeignKey(
		"LehrerNotenmodulCredentials_Lehrer_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerNotenmodulCredentials.
	 */
	public Tabelle_LehrerNotenmodulCredentials() {
		super("LehrerNotenmodulCredentials", SchemaRevisionen.REV_18);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("lehrer");
		setJavaClassName("DTOLehrerNotenmodulCredentials");
		setJavaComment("Die Credentials einer Lehrkraft für die Nutzung des Notenmoduls");
	}

}
