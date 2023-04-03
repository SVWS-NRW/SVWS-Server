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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle BenutzerAllgemein.
 */
public class Tabelle_BenutzerAllgemein extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des allgemeinen Benutzers");

	/** Die Definition der Tabellenspalte AnzeigeName */
	public SchemaTabelleSpalte col_AnzeigeName = add("AnzeigeName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Der Anzeigename für den allgemeinen Benutzer");

	/** Die Definition der Tabellenspalte CredentialID */
	public SchemaTabelleSpalte col_CredentialID = add("CredentialID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des Credential-Eintrags");


	/** Die Definition des Fremdschlüssels BenutzerAllgemein_Credential_FK */
	public SchemaTabelleFremdschluessel fk_BenutzerAllgemein_Credential_FK = addForeignKey(
			"BenutzerAllgemein_Credential_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_CredentialID, Schema.tab_Credentials.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle BenutzerAllgemein.
	 */
	public Tabelle_BenutzerAllgemein() {
		super("BenutzerAllgemein", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOBenutzerAllgemein");
		setJavaComment("Die Definition von Benutzern, welchen sich am Server anmelden können, aber nicht als Lehrer bzw. Personal, Schüler oder Erzieher erfasst sind");
	}

}
