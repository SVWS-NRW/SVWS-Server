package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Notenmodul_Credentials.
 */
public class Tabelle_Notenmodul_Credentials extends SchemaTabelle {

	/** Die Definition der Tabellenspalte idLehrer */
	public final SchemaTabelleSpalte col_idLehrer = add("idLehrer", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die LehrerID des Lehrers, für den die Credentials gelten");

	/** Die Definition der Tabellenspalte initialkennwort */
	public final SchemaTabelleSpalte col_initialkennwort = add("initialkennwort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Initialkennwort für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte passwordHash */
	public final SchemaTabelleSpalte col_passwordHash = add("passwordHash", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Passwordhash für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte art2FA */
	public final SchemaTabelleSpalte col_art2FA = add("art2FA", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setJavaComment("Gibt die Art der verwendeten Zwei-Faktor-Authentifizierung an (0 = Keine, 1 = TOTP, 2 = EMail).")
			.setRevision(SchemaRevisionen.REV_60);

	/** Die Definition der Tabellenspalte totpSecret */
	public final SchemaTabelleSpalte col_totpSecret = add("totpSecret", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Das Shared Secret für 2FA mit TOTP")
			.setRevision(SchemaRevisionen.REV_60);

	/** Die Definition der Tabellenspalte istErstanmeldung */
	public final SchemaTabelleSpalte col_istErstanmeldung = add("istErstanmeldung", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob bei der nächsten Anmeldung eines Lehrer eine Erstanmeldung vorliegt oder nicht.")
			.setRevision(SchemaRevisionen.REV_60);

	/** Die Definition des Fremdschlüssels Notenmodul_Credentials_Lehrer_FK */
	public final SchemaTabelleFremdschluessel fk_Notenmodul_Credentials_Lehrer_FK = addForeignKey(
			"Notenmodul_Credentials_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_idLehrer, Schema.tab_K_Lehrer.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Notenmodul_Credentials.
	 */
	public Tabelle_Notenmodul_Credentials() {
		super("Notenmodul_Credentials", SchemaRevisionen.REV_53);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("notenmodul");
		setJavaClassName("DTONotenmodulCredentials");
		setJavaComment("Die Credentials einer Lehrkraft für die Nutzung des Notenmoduls");
	}

}
