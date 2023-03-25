package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Benutzergruppen.
 */
public class Tabelle_Benutzergruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der Benutzergruppe");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Benutzergruppe");

	/** Die Definition der Tabellenspalte IstAdmin */
	public SchemaTabelleSpalte col_IstAdmin = add("IstAdmin", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Benutzergruppe Administrator-Rechte hat (1) oder nicht (0)");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Benutzergruppen.
	 */
	public Tabelle_Benutzergruppen() {
		super("Benutzergruppen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOBenutzergruppe");
		setJavaComment("Die Definition von Benutzergruppen (Rollen). Diese dienen als Vorlage für die Rechtezuweisung an Benutzer.");
	}

}
