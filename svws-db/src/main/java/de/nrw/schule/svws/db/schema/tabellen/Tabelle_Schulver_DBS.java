package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.Boolean01StringConverter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulver_DBS.
 */
public class Tabelle_Schulver_DBS extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SchulNr */
	public SchemaTabelleSpalte col_SchulNr = add("SchulNr", SchemaDatentypen.VARCHAR, true).setDatenlaenge(6)
		.setNotNull()
		.setJavaComment("Schulver Tabelle IT.NRW: Schulnummer der Schule");

	/** Die Definition der Tabellenspalte RegSchl */
	public SchemaTabelleSpalte col_RegSchl = add("RegSchl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulver Tabelle IT.NRW: Regionalschlüssel der Schule");

	/** Die Definition der Tabellenspalte KoRe */
	public SchemaTabelleSpalte col_KoRe = add("KoRe", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte KoHo */
	public SchemaTabelleSpalte col_KoHo = add("KoHo", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte ABez1 */
	public SchemaTabelleSpalte col_ABez1 = add("ABez1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Schulver Tabelle IT.NRW: Bezeichnung 1 der Schule");

	/** Die Definition der Tabellenspalte ABez2 */
	public SchemaTabelleSpalte col_ABez2 = add("ABez2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Schulver Tabelle IT.NRW: Bezeichnung 2 der Schule");

	/** Die Definition der Tabellenspalte ABez3 */
	public SchemaTabelleSpalte col_ABez3 = add("ABez3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Schulver Tabelle IT.NRW: Bezeichnung 3 der Schule");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulver Tabelle IT.NRW: Postleitzahl der Schule");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(34)
		.setJavaComment("Schulver Tabelle IT.NRW: Ort der Schule");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Schulver Tabelle IT.NRW: Straße der Schule");

	/** Die Definition der Tabellenspalte TelVorw */
	public SchemaTabelleSpalte col_TelVorw = add("TelVorw", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulver Tabelle IT.NRW: Telefonvorwahl der Schule");

	/** Die Definition der Tabellenspalte Telefon */
	public SchemaTabelleSpalte col_Telefon = add("Telefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(12)
		.setJavaComment("Schulver Tabelle IT.NRW: Telefonnummer der Schule");

	/** Die Definition der Tabellenspalte FaxVorw */
	public SchemaTabelleSpalte col_FaxVorw = add("FaxVorw", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulver Tabelle IT.NRW: Faxvorwahl der Schule");

	/** Die Definition der Tabellenspalte Fax */
	public SchemaTabelleSpalte col_Fax = add("Fax", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Schulver Tabelle IT.NRW: Faxnummer der Schule");

	/** Die Definition der Tabellenspalte ModemVorw */
	public SchemaTabelleSpalte col_ModemVorw = add("ModemVorw", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulver Tabelle IT.NRW: Modemvorwahl der Schule");

	/** Die Definition der Tabellenspalte Modem */
	public SchemaTabelleSpalte col_Modem = add("Modem", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Schulver Tabelle IT.NRW: Modem-Telefonnummer der Schule");

	/** Die Definition der Tabellenspalte SF */
	public SchemaTabelleSpalte col_SF = add("SF", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schulver Tabelle IT.NRW: Schulform der Schule");

	/** Die Definition der Tabellenspalte OeffPri */
	public SchemaTabelleSpalte col_OeffPri = add("OeffPri", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte KurzBez */
	public SchemaTabelleSpalte col_KurzBez = add("KurzBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Schulver Tabelle IT.NRW: Kurzbezeichnung der Schule");

	/** Die Definition der Tabellenspalte SchBetrSchl */
	public SchemaTabelleSpalte col_SchBetrSchl = add("SchBetrSchl", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Schulbetriebsschlüssel der Schule");

	/** Die Definition der Tabellenspalte SchBetrSchlDatum */
	public SchemaTabelleSpalte col_SchBetrSchlDatum = add("SchBetrSchlDatum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(8)
		.setJavaComment("Schulver Tabelle IT.NRW: Datum des Schulbetriensschlüssels der Schule");

	/** Die Definition der Tabellenspalte ArtDerTraegerschaft */
	public SchemaTabelleSpalte col_ArtDerTraegerschaft = add("ArtDerTraegerschaft", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schulver Tabelle IT.NRW: Art der Trägerschaft der Schule");

	/** Die Definition der Tabellenspalte SchultraegerNr */
	public SchemaTabelleSpalte col_SchultraegerNr = add("SchultraegerNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulver Tabelle IT.NRW: Schulträgernummer der Schule");

	/** Die Definition der Tabellenspalte Schulgliederung */
	public SchemaTabelleSpalte col_Schulgliederung = add("Schulgliederung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulver Tabelle IT.NRW: Schulgliederung der Schule ???");

	/** Die Definition der Tabellenspalte Schulart */
	public SchemaTabelleSpalte col_Schulart = add("Schulart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte Ganztagsbetrieb */
	public SchemaTabelleSpalte col_Ganztagsbetrieb = add("Ganztagsbetrieb", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt an ob die Schule Ganztagsbetrieb hat");

	/** Die Definition der Tabellenspalte FSP */
	public SchemaTabelleSpalte col_FSP = add("FSP", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schulver Tabelle IT.NRW: Förderschwerpunkte der Schule");

	/** Die Definition der Tabellenspalte Verbund */
	public SchemaTabelleSpalte col_Verbund = add("Verbund", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte Bus */
	public SchemaTabelleSpalte col_Bus = add("Bus", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte Fachberater */
	public SchemaTabelleSpalte col_Fachberater = add("Fachberater", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Fachberater der Schule");

	/** Die Definition der Tabellenspalte FachberHauptamtl */
	public SchemaTabelleSpalte col_FachberHauptamtl = add("FachberHauptamtl", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte TelNrDBSalt */
	public SchemaTabelleSpalte col_TelNrDBSalt = add("TelNrDBSalt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte RP */
	public SchemaTabelleSpalte col_RP = add("RP", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schulver Tabelle IT.NRW: Email-Adresse der Schule");

	/** Die Definition der Tabellenspalte URL */
	public SchemaTabelleSpalte col_URL = add("URL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
		.setJavaComment("Schulver Tabelle IT.NRW: Website der Schule");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.TEXT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Bemerkung zur Schule");

	/** Die Definition der Tabellenspalte CD */
	public SchemaTabelleSpalte col_CD = add("CD", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt an ob die Schule eine CD für ASDPC32 möchte");

	/** Die Definition der Tabellenspalte Stift */
	public SchemaTabelleSpalte col_Stift = add("Stift", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte OGTS */
	public SchemaTabelleSpalte col_OGTS = add("OGTS", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt an ob die Schule offenen Ganztag hat");

	/** Die Definition der Tabellenspalte SELB */
	public SchemaTabelleSpalte col_SELB = add("SELB", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte Internat */
	public SchemaTabelleSpalte col_Internat = add("Internat", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt an ob die Schule Internatsplätze hat");

	/** Die Definition der Tabellenspalte InternatPlaetze */
	public SchemaTabelleSpalte col_InternatPlaetze = add("InternatPlaetze", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setJavaComment("Schulver Tabelle IT.NRW: Anzahl der Internatsplätze");

	/** Die Definition der Tabellenspalte SMail */
	public SchemaTabelleSpalte col_SMail = add("SMail", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schulver Tabelle IT.NRW: Schulmailadresse");

	/** Die Definition der Tabellenspalte SportImAbi */
	public SchemaTabelleSpalte col_SportImAbi = add("SportImAbi", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01StringConverter.class)
		.setJavaComment("Schulver Tabelle IT.NRW: Hat die Schule Sport im Abitur?");

	/** Die Definition der Tabellenspalte Tal */
	public SchemaTabelleSpalte col_Tal = add("Tal", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01StringConverter.class)
		.setJavaComment("Schulver Tabelle IT.NRW: Nimmt die Schule am Projekt Talentschule teil?");

	/** Die Definition der Tabellenspalte KonKop */
	public SchemaTabelleSpalte col_KonKop = add("KonKop", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01StringConverter.class)
		.setJavaComment("Schulver Tabelle IT.NRW: Ist die konfessionelle Kooperation an dieser Schule genehmigt?");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulver_DBS.
	 */
	public Tabelle_Schulver_DBS() {
		super("Schulver_DBS", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schulver");
		setJavaClassName("DTOSchulverDBS");
		setJavaComment("IT.NRW Liste der Schulen mit Schulnummern und Adressen");
	}

}
