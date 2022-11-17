package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Personengruppen.
 */
public class Tabelle_Personengruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Personengruppe");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Gruppenname */
	public SchemaTabelleSpalte col_Gruppenname = add("Gruppenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setNotNull()
		.setJavaComment("Gruppenname der Personengruppe");

	/** Die Definition der Tabellenspalte Zusatzinfo */
	public SchemaTabelleSpalte col_Zusatzinfo = add("Zusatzinfo", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Zusatzinfo der Personengruppe");

	/** Die Definition der Tabellenspalte SammelEmail */
	public SchemaTabelleSpalte col_SammelEmail = add("SammelEmail", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Sammel-E-Mail-Adresse der Personengruppe");

	/** Die Definition der Tabellenspalte GruppenArt */
	public SchemaTabelleSpalte col_GruppenArt = add("GruppenArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Gruppenart  der Personengruppe");

	/** Die Definition der Tabellenspalte XMLExport */
	public SchemaTabelleSpalte col_XMLExport = add("XMLExport", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Steuert den LogineoXML-Export");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung der Personengruppe");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sichtbarkeit der Personengruppe");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Personengruppen.
	 */
	public Tabelle_Personengruppen() {
		super("Personengruppen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.personengruppen");
		setJavaClassName("DTOPersonengruppen");
		setJavaComment("Katalog der definierten Personengruppen");
	}

}
