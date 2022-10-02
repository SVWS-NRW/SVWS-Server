package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Versetzung.
 */
public class Tabelle_Versetzung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klasse in der Klassen- Versetzuungstabelle");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(150)
		.setJavaComment("Bezeichnender Text für die Klasse");

	/** Die Definition der Tabellenspalte ASDKlasse */
	public SchemaTabelleSpalte col_ASDKlasse = add("ASDKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("ASD-Jahrgang der Klasse");

	/** Die Definition der Tabellenspalte Klasse */
	public SchemaTabelleSpalte col_Klasse = add("Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setNotNull()
		.setJavaComment("Kürzel der Klasse");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des ASD-Jahrgangs");

	/** Die Definition der Tabellenspalte FKlasse */
	public SchemaTabelleSpalte col_FKlasse = add("FKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Folgeklasse");

	/** Die Definition der Tabellenspalte VKlasse */
	public SchemaTabelleSpalte col_VKlasse = add("VKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Vorgängerklasse");

	/** Die Definition der Tabellenspalte OrgFormKrz */
	public SchemaTabelleSpalte col_OrgFormKrz = add("OrgFormKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Organisationsform der Klasse Kürzel IT.NRW");

	/** Die Definition der Tabellenspalte KlassenlehrerKrz */
	public SchemaTabelleSpalte col_KlassenlehrerKrz = add("KlassenlehrerKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel des Klassenlehrers");

	/** Die Definition der Tabellenspalte StvKlassenlehrerKrz */
	public SchemaTabelleSpalte col_StvKlassenlehrerKrz = add("StvKlassenlehrerKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel des stellvertretenden Klassenlehrers");

	/** Die Definition der Tabellenspalte Restabschnitte */
	public SchemaTabelleSpalte col_Restabschnitte = add("Restabschnitte", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("DEPRECATED: Restabschnitte der Klasse");

	/** Die Definition der Tabellenspalte ASDSchulformNr */
	public SchemaTabelleSpalte col_ASDSchulformNr = add("ASDSchulformNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulgliederung in der Klasse");

	/** Die Definition der Tabellenspalte Fachklasse_ID */
	public SchemaTabelleSpalte col_Fachklasse_ID = add("Fachklasse_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("FID des Fachklasse nur BK SBK");

	/** Die Definition der Tabellenspalte PruefOrdnung */
	public SchemaTabelleSpalte col_PruefOrdnung = add("PruefOrdnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Prüfungsordnung für die Klasse");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Gibt an ob eine Klasse sichtbar ist");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierungnummer der Klasse");

	/** Die Definition der Tabellenspalte Klassenart */
	public SchemaTabelleSpalte col_Klassenart = add("Klassenart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Klassenart");

	/** Die Definition der Tabellenspalte SommerSem */
	public SchemaTabelleSpalte col_SommerSem = add("SommerSem", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Beginn im Sommersemester nur WBK");

	/** Die Definition der Tabellenspalte NotenGesperrt */
	public SchemaTabelleSpalte col_NotenGesperrt = add("NotenGesperrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Noteneingabe für die Klasse gesperrt");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte AdrMerkmal */
	public SchemaTabelleSpalte col_AdrMerkmal = add("AdrMerkmal", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("A")
		.setJavaComment("Adressmerkmal des Teilstandorts für die Klasse");

	/** Die Definition der Tabellenspalte WebNotenGesperrt */
	public SchemaTabelleSpalte col_WebNotenGesperrt = add("WebNotenGesperrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("DEPRECATED: nicht mehr genutzt SchildWeb");

	/** Die Definition der Tabellenspalte KoopKlasse */
	public SchemaTabelleSpalte col_KoopKlasse = add("KoopKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob die Klasse eine KOOP-Klasse ist");

	/** Die Definition der Tabellenspalte Ankreuzzeugnisse */
	public SchemaTabelleSpalte col_Ankreuzzeugnisse = add("Ankreuzzeugnisse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob in der Klasse Ankreuzeugnisse (GS) oder Kompentenzschreiben (andere) verwendet werden");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Versetzung.
	 */
	public Tabelle_Versetzung() {
		super("Versetzung", SchemaRevisionen.REV_0);
		setVeraltet(SchemaRevisionen.REV_1);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.klassen");
		setJavaClassName("DTOVersetzung");
		setJavaComment("Tabelle für die Klassen-/Versetzungstabelle");
	}

}
