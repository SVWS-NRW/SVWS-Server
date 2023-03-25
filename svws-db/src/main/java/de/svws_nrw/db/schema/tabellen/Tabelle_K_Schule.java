package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Schule.
 */
public class Tabelle_K_Schule extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte SchulNr */
	public SchemaTabelleSpalte col_SchulNr = add("SchulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setNotNull()
		.setJavaComment("Schulnummer des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Name */
	public SchemaTabelleSpalte col_Name = add("Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Nachname der Lehrkraft PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte SchulformNr */
	public SchemaTabelleSpalte col_SchulformNr = add("SchulformNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulformnummer des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte SchulformKrz */
	public SchemaTabelleSpalte col_SchulformKrz = add("SchulformKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulformkürzel des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte SchulformBez */
	public SchemaTabelleSpalte col_SchulformBez = add("SchulformBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schulformbezeichnung des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Strassenname */
	public SchemaTabelleSpalte col_Strassenname = add("Strassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname der Schule");

	/** Die Definition der Tabellenspalte HausNr */
	public SchemaTabelleSpalte col_HausNr = add("HausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte HausNrZusatz */
	public SchemaTabelleSpalte col_HausNrZusatz = add("HausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Hausnummerzusatz wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Straße des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("PLZ des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Ort des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Telefon */
	public SchemaTabelleSpalte col_Telefon = add("Telefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telefonnummer des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Fax */
	public SchemaTabelleSpalte col_Fax = add("Fax", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Faxnummer des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("E-MailAdresse des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Schulleiter */
	public SchemaTabelleSpalte col_Schulleiter = add("Schulleiter", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Schulleitername des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sichbarkeit des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Änderbarkeit des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte SchulNr_SIM */
	public SchemaTabelleSpalte col_SchulNr_SIM = add("SchulNr_SIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Statistiklürzel Schulnummer des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte KurzBez */
	public SchemaTabelleSpalte col_KurzBez = add("KurzBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Kutbezeichnung des Eintrags der Schulen");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index K_Schule_UC1 */
	public SchemaTabelleUniqueIndex unique_K_Schule_UC1 = addUniqueIndex("K_Schule_UC1", 
			col_SchulNr
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Schule.
	 */
	public Tabelle_K_Schule() {
		super("K_Schule", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOSchuleNRW");
		setJavaComment("Interner Katalog der verwendeten Schulen für die Herkunft der Schüler (Reiter Schulbesuch)");
	}

}
