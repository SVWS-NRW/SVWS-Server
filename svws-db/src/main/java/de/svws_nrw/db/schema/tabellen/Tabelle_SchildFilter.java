package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchildFilter.
 */
public class Tabelle_SchildFilter extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des gespeicherten Filters");

	/** Die Definition der Tabellenspalte Art */
	public SchemaTabelleSpalte col_Art = add("Art", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Art des Filters");

	/** Die Definition der Tabellenspalte Name */
	public SchemaTabelleSpalte col_Name = add("Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Bezeichnender Kurztext des Filters");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Beschreibung zum Filter");

	/** Die Definition der Tabellenspalte Tabellen */
	public SchemaTabelleSpalte col_Tabellen = add("Tabellen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Tabellen die im Filter vorkommen");

	/** Die Definition der Tabellenspalte ZusatzTabellen */
	public SchemaTabelleSpalte col_ZusatzTabellen = add("ZusatzTabellen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Zusätzliche Tabellen die im Filtervorkommen");

	/** Die Definition der Tabellenspalte Bedingung */
	public SchemaTabelleSpalte col_Bedingung = add("Bedingung", SchemaDatentypen.TEXT, false)
		.setJavaComment("SQL-Text des Filters");

	/** Die Definition der Tabellenspalte BedingungKlartext */
	public SchemaTabelleSpalte col_BedingungKlartext = add("BedingungKlartext", SchemaDatentypen.TEXT, false)
		.setJavaComment("Klartext der bedingung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index SchildFilter_UC1 */
	public SchemaTabelleUniqueIndex unique_SchildFilter_UC1 = addUniqueIndex("SchildFilter_UC1",
			col_Name
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchildFilter.
	 */
	public Tabelle_SchildFilter() {
		super("SchildFilter", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchildAuswahlFilter");
		setJavaComment("Gespeicherte Filter aus Filter I");
	}

}
