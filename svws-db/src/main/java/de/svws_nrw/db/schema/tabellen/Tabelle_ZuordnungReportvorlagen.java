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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle ZuordnungReportvorlagen.
 */
public class Tabelle_ZuordnungReportvorlagen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes der einen Zeugnisreport einer Gruppe oder Klasse zuordnet");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Jahrgangs der zum Report zugeordnet wird");

	/** Die Definition der Tabellenspalte Abschluss */
	public SchemaTabelleSpalte col_Abschluss = add("Abschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Bezeichnung des Abschluss der für den Report zugeordnet wird");

	/** Die Definition der Tabellenspalte AbschlussBB */
	public SchemaTabelleSpalte col_AbschlussBB = add("AbschlussBB", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Bezeichnung des berufsbezogenen Abschluss der für den Report zugeordnet wird");

	/** Die Definition der Tabellenspalte AbschlussArt */
	public SchemaTabelleSpalte col_AbschlussArt = add("AbschlussArt", SchemaDatentypen.INT, false)
		.setJavaComment("Art des Abschluss der für den Report zugeordnet wird");

	/** Die Definition der Tabellenspalte VersetzungKrz */
	public SchemaTabelleSpalte col_VersetzungKrz = add("VersetzungKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Kürzel des Versetzungsvermerk das für den Report zugeordnet wird");

	/** Die Definition der Tabellenspalte Fachklasse_ID */
	public SchemaTabelleSpalte col_Fachklasse_ID = add("Fachklasse_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der Fachklasse die für den Report zugeordnet wird");

	/** Die Definition der Tabellenspalte Reportvorlage */
	public SchemaTabelleSpalte col_Reportvorlage = add("Reportvorlage", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Pfad zur Reportvorlage die für das Zeugnis zugeordnet wird");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Beschreibung für die Reportzuordnung zum Zeugnisdruck");

	/** Die Definition der Tabellenspalte Gruppe */
	public SchemaTabelleSpalte col_Gruppe = add("Gruppe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Name der Gruppe die für den Report zugeordnet wird");

	/** Die Definition der Tabellenspalte Zeugnisart */
	public SchemaTabelleSpalte col_Zeugnisart = add("Zeugnisart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Zeugnisart (Laufbahndaten) die für den Report zugeordnet wird");


	/** Die Definition des Fremdschlüssels ZuordnungReportvorlagen_Fachklasse_FK */
	public SchemaTabelleFremdschluessel fk_ZuordnungReportvorlagen_Fachklasse_FK = addForeignKey(
			"ZuordnungReportvorlagen_Fachklasse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Fachklasse_ID, Schema.tab_EigeneSchule_Fachklassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels ZuordnungReportvorlagen_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_ZuordnungReportvorlagen_Jahrgang_FK = addForeignKey(
			"ZuordnungReportvorlagen_Jahrgang_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle ZuordnungReportvorlagen.
	 */
	public Tabelle_ZuordnungReportvorlagen() {
		super("ZuordnungReportvorlagen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild");
		setJavaClassName("DTOZuordnungReportvorlagen");
		setJavaComment("Zuordnungen von Reports für den Zeugnisdruck (automatisierter Druck)");
	}

}
