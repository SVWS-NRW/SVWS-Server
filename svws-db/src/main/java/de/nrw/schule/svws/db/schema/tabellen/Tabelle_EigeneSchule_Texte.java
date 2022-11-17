package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Texte.
 */
public class Tabelle_EigeneSchule_Texte extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Textes unter Schulverwaltung Eigene Schule bearbeiten");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Kürzel des Schultextes");

	/** Die Definition der Tabellenspalte Inhalt */
	public SchemaTabelleSpalte col_Inhalt = add("Inhalt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Inhalt des Schultextes");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Beschreibung des Schultextes");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an ob der Text änderbar ist");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Texte.
	 */
	public Tabelle_EigeneSchule_Texte() {
		super("EigeneSchule_Texte", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchultexte");
		setJavaComment("Texte, die unter Schule bearbeiten erstellt werden und in Reports abgerufen werden können.");
	}

}
