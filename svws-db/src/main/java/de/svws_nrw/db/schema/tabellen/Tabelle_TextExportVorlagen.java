package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TextExportVorlagen.
 */
public class Tabelle_TextExportVorlagen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte VorlageName */
	public SchemaTabelleSpalte col_VorlageName = add("VorlageName", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Name der Export-Textvorlage");

	/** Die Definition der Tabellenspalte Daten */
	public SchemaTabelleSpalte col_Daten = add("Daten", SchemaDatentypen.TEXT, false)
		.setJavaComment("Daten die in der Export-Textvorlage enthalten sind");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle TextExportVorlagen.
	 */
	public Tabelle_TextExportVorlagen() {
		super("TextExportVorlagen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild");
		setJavaClassName("DTOTextExportVorlagen");
		setJavaComment("gespeicherte Vorlagen für den Datenaustausch > Text-/Excel-Export");
	}

}
