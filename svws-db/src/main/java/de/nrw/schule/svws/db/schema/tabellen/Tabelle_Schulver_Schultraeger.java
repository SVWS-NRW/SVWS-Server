package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulver_Schultraeger.
 */
public class Tabelle_Schulver_Schultraeger extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SchulNr */
	public SchemaTabelleSpalte col_SchulNr = add("SchulNr", SchemaDatentypen.VARCHAR, true).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Schulver Tabelle IT.NRW: Schulträgernummer des Schulträgers");

	/** Die Definition der Tabellenspalte RegSchl */
	public SchemaTabelleSpalte col_RegSchl = add("RegSchl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Regionalschlüssel des Schulträgers");

	/** Die Definition der Tabellenspalte KoRe */
	public SchemaTabelleSpalte col_KoRe = add("KoRe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte KoHo */
	public SchemaTabelleSpalte col_KoHo = add("KoHo", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: ???");

	/** Die Definition der Tabellenspalte ABez1 */
	public SchemaTabelleSpalte col_ABez1 = add("ABez1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Bezeichnung 1 des Schulträgers");

	/** Die Definition der Tabellenspalte ABez2 */
	public SchemaTabelleSpalte col_ABez2 = add("ABez2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Bezeichnung 2  des Schulträgers");

	/** Die Definition der Tabellenspalte ABez3 */
	public SchemaTabelleSpalte col_ABez3 = add("ABez3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Bezeichnung 3 des Schulträgers");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: PLZ des Schulträgers");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Ort des Schulträgers");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Straße des Schulträgers");

	/** Die Definition der Tabellenspalte TelVorw */
	public SchemaTabelleSpalte col_TelVorw = add("TelVorw", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Vorwahl des Schulträgers");

	/** Die Definition der Tabellenspalte Telefon */
	public SchemaTabelleSpalte col_Telefon = add("Telefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Telefonnummer des Schulträgers");

	/** Die Definition der Tabellenspalte SF */
	public SchemaTabelleSpalte col_SF = add("SF", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Ist immer 00 ???");

	/** Die Definition der Tabellenspalte OeffPri */
	public SchemaTabelleSpalte col_OeffPri = add("OeffPri", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Öffentlicher oder privater Schulträger");

	/** Die Definition der Tabellenspalte KurzBez */
	public SchemaTabelleSpalte col_KurzBez = add("KurzBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Kurzbezeichnung des Schulträgers");

	/** Die Definition der Tabellenspalte SchBetrSchl */
	public SchemaTabelleSpalte col_SchBetrSchl = add("SchBetrSchl", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Schulbetriebsschlüssel des Schulträgers");

	/** Die Definition der Tabellenspalte SchBetrSchlDatum */
	public SchemaTabelleSpalte col_SchBetrSchlDatum = add("SchBetrSchlDatum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Datum des Schulbetriebsschlüssels");

	/** Die Definition der Tabellenspalte SchuelerZahlASD */
	public SchemaTabelleSpalte col_SchuelerZahlASD = add("SchuelerZahlASD", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setJavaComment("Schulver Tabelle IT.NRW: Schülerzahl laut ASD");

	/** Die Definition der Tabellenspalte SchuelerZahlVS */
	public SchemaTabelleSpalte col_SchuelerZahlVS = add("SchuelerZahlVS", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setJavaComment("Schulver Tabelle IT.NRW: Schülerzahl laut VS");

	/** Die Definition der Tabellenspalte ArtDerTraegerschaft */
	public SchemaTabelleSpalte col_ArtDerTraegerschaft = add("ArtDerTraegerschaft", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Art der Trägerschaft des Schulträgers");

	/** Die Definition der Tabellenspalte SchultraegerNr */
	public SchemaTabelleSpalte col_SchultraegerNr = add("SchultraegerNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: leer siehe SchulNr");

	/** Die Definition der Tabellenspalte Schulgliederung */
	public SchemaTabelleSpalte col_Schulgliederung = add("Schulgliederung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: leer Gliederung");

	/** Die Definition der Tabellenspalte Ganztagsbetrieb */
	public SchemaTabelleSpalte col_Ganztagsbetrieb = add("Ganztagsbetrieb", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schulver Tabelle IT.NRW: Leer Ganztagsbetrieb");

	/** Die Definition der Tabellenspalte Aktiv */
	public SchemaTabelleSpalte col_Aktiv = add("Aktiv", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setJavaComment("Schulver Tabelle IT.NRW: Akitv ja nein des Schulträgers");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulver_Schultraeger.
	 */
	public Tabelle_Schulver_Schultraeger() {
		super("Schulver_Schultraeger", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schulver");
		setJavaClassName("DTOSchulverSchultraeger");
		setJavaComment("IT.NRW Liste der Schulträger (wird in Schild-Zentral verwendet)");
	}

}
