package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Floskelgruppen.
 */
public class Tabelle_Floskelgruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Kürzel der Floskelgruppe");

	/** Die Definition der Tabellenspalte Hauptgruppe */
	public SchemaTabelleSpalte col_Hauptgruppe = add("Hauptgruppe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaComment("Hauptgruppe der Floskelgruppe ermöglich es Floskel Unterkategorien zuzuordnen");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Bezeichnung der Floskelgruppe");

	/** Die Definition der Tabellenspalte Farbe */
	public SchemaTabelleSpalte col_Farbe = add("Farbe", SchemaDatentypen.INT, false)
		.setJavaComment("Farbe für die Flsokelgruppe");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Floskelgruppen.
	 */
	public Tabelle_Floskelgruppen() {
		super("Floskelgruppen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOFloskelgruppen");
		setJavaComment("Liste der Floskelgruppen, diese Liste kann erweitert werden um Textbausteinen zu verwalten und die Anzeige in den Editoren zu steuern");
	}

}
