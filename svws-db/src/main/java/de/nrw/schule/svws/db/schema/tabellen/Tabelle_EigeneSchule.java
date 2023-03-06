package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.StringToIntegerConverter;
import de.nrw.schule.svws.db.converter.current.statkue.SchulformKuerzelConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule.
 */
public class Tabelle_EigeneSchule extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes der eigenen Schule");

	/** Die Definition der Tabellenspalte SchulformNr */
	public SchemaTabelleSpalte col_SchulformNr = add("SchulformNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulformnummer der eigenen Schule (Statkue IT.NRW)");

	/** Die Definition der Tabellenspalte SchulformKrz */
	public SchemaTabelleSpalte col_SchulformKrz = add("SchulformKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaName("Schulform")
		.setConverter(SchulformKuerzelConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Schulformkürzel der eigenen Schule (Statkue IT.NRW)");

	/** Die Definition der Tabellenspalte SchulformBez */
	public SchemaTabelleSpalte col_SchulformBez = add("SchulformBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Bezeichnender Text der Schulform");

	/** Die Definition der Tabellenspalte SchultraegerArt */
	public SchemaTabelleSpalte col_SchultraegerArt = add("SchultraegerArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Art des Schulträgers");

	/** Die Definition der Tabellenspalte SchultraegerNr */
	public SchemaTabelleSpalte col_SchultraegerNr = add("SchultraegerNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulträgernummer aus dem Katalog der Schulver IT.NRW");

	/** Die Definition der Tabellenspalte SchulNr */
	public SchemaTabelleSpalte col_SchulNr = add("SchulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setConverter(StringToIntegerConverter.class)
		.setJavaComment("Eindeutige Schulnummer der eigenen Schule aus der Schulver IT.NRW");

	/** Die Definition der Tabellenspalte Bezeichnung1 */
	public SchemaTabelleSpalte col_Bezeichnung1 = add("Bezeichnung1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("1. Text für die Bezeichnung der Schule");

	/** Die Definition der Tabellenspalte Bezeichnung2 */
	public SchemaTabelleSpalte col_Bezeichnung2 = add("Bezeichnung2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("2. Text für die Bezeichnung der Schule");

	/** Die Definition der Tabellenspalte Bezeichnung3 */
	public SchemaTabelleSpalte col_Bezeichnung3 = add("Bezeichnung3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("3. Text für die Bezeichnung der Schule");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Straße der eigenen Schule");

	/** Die Definition der Tabellenspalte Strassenname */
	public SchemaTabelleSpalte col_Strassenname = add("Strassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname der Schule");

	/** Die Definition der Tabellenspalte HausNr */
	public SchemaTabelleSpalte col_HausNr = add("HausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte HausNrZusatz */
	public SchemaTabelleSpalte col_HausNrZusatz = add("HausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Hausnummerzusatz wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("PLZ der eigenen Schule");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Ortsangabe der eigenen Schule");

	/** Die Definition der Tabellenspalte Telefon */
	public SchemaTabelleSpalte col_Telefon = add("Telefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telefonnummer der eigenen Schule");

	/** Die Definition der Tabellenspalte Fax */
	public SchemaTabelleSpalte col_Fax = add("Fax", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Faxnummer der eigenen Schule");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Email-Adresse der eigenen Schule");

	/** Die Definition der Tabellenspalte Ganztags */
	public SchemaTabelleSpalte col_Ganztags = add("Ganztags", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob die Schule Ganztagsbetrieb hat");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte AnzahlAbschnitte */
	public SchemaTabelleSpalte col_AnzahlAbschnitte = add("AnzahlAbschnitte", SchemaDatentypen.INT, false)
		.setDefault("2")
		.setJavaComment("Anzahl der verfügbaren Abschnitte (meist Halbjahre oder Quartale)");

	/** Die Definition der Tabellenspalte AbschnittBez */
	public SchemaTabelleSpalte col_AbschnittBez = add("AbschnittBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setDefault("Halbjahr")
		.setJavaComment("Bezeichnung der Abschnitte");

	/** Die Definition der Tabellenspalte BezAbschnitt1 */
	public SchemaTabelleSpalte col_BezAbschnitt1 = add("BezAbschnitt1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(12)
		.setDefault("1. Hj")
		.setJavaComment("Bezeichnung des ersten Abschnitts");

	/** Die Definition der Tabellenspalte BezAbschnitt2 */
	public SchemaTabelleSpalte col_BezAbschnitt2 = add("BezAbschnitt2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(12)
		.setDefault("2. Hj")
		.setJavaComment("Bezeichnung des zweiten Abschnitts");

	/** Die Definition der Tabellenspalte BezAbschnitt3 */
	public SchemaTabelleSpalte col_BezAbschnitt3 = add("BezAbschnitt3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(12)
		.setJavaComment("Bezeichnung des dritten Abschnitts");

	/** Die Definition der Tabellenspalte BezAbschnitt4 */
	public SchemaTabelleSpalte col_BezAbschnitt4 = add("BezAbschnitt4", SchemaDatentypen.VARCHAR, false).setDatenlaenge(12)
		.setJavaComment("Bezeichnung des vierten Abschnitts");

	/** Die Definition der Tabellenspalte Fremdsprachen */
	public SchemaTabelleSpalte col_Fremdsprachen = add("Fremdsprachen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Welche Fremdsprachen werden unterrichtet");

	/** Die Definition der Tabellenspalte JVAZeigen */
	public SchemaTabelleSpalte col_JVAZeigen = add("JVAZeigen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Schule unterrichtet in der Justizvollzugsanstalt");

	/** Die Definition der Tabellenspalte RefPaedagogikZeigen */
	public SchemaTabelleSpalte col_RefPaedagogikZeigen = add("RefPaedagogikZeigen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Schule hat Reformpädagogischen-Zweig");

	/** Die Definition der Tabellenspalte AnzJGS_Jahr */
	public SchemaTabelleSpalte col_AnzJGS_Jahr = add("AnzJGS_Jahr", SchemaDatentypen.SMALLINT, false)
		.setDefault("1")
		.setJavaComment("Anzahl der Jahrgangstufen pro Schuljahr (Semesterbetrieb WBK)");

	/** Die Definition der Tabellenspalte IstHauptsitz */
	public SchemaTabelleSpalte col_IstHauptsitz = add("IstHauptsitz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob die Datenbank am Hauptsitzder Schule ist (Dependancebetrieb)");

	/** Die Definition der Tabellenspalte NotenGesperrt */
	public SchemaTabelleSpalte col_NotenGesperrt = add("NotenGesperrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Sperrt die Noteneingabe");

	/** Die Definition der Tabellenspalte ZurueckgestelltAnzahl */
	public SchemaTabelleSpalte col_ZurueckgestelltAnzahl = add("ZurueckgestelltAnzahl", SchemaDatentypen.INT, false)
		.setJavaComment("Anzahl de Zurückgestellten Kinder in der Grundschule");

	/** Die Definition der Tabellenspalte ZurueckgestelltWeibl */
	public SchemaTabelleSpalte col_ZurueckgestelltWeibl = add("ZurueckgestelltWeibl", SchemaDatentypen.INT, false)
		.setJavaComment("Anzahl de Zurückgestellten weiblichen Kinder in der Grundschule");

	/** Die Definition der Tabellenspalte ZurueckgestelltAuslaender */
	public SchemaTabelleSpalte col_ZurueckgestelltAuslaender = add("ZurueckgestelltAuslaender", SchemaDatentypen.INT, false)
		.setJavaComment("Anzahl de Zurückgestellten ausländischen Kinder in der Grundschule");

	/** Die Definition der Tabellenspalte ZurueckgestelltAuslaenderWeibl */
	public SchemaTabelleSpalte col_ZurueckgestelltAuslaenderWeibl = add("ZurueckgestelltAuslaenderWeibl", SchemaDatentypen.INT, false)
		.setJavaComment("Anzahl de Zurückgestellten ausländischen weiblichen Kinder in der Grundschule");

	/** Die Definition der Tabellenspalte ZurueckgestelltAussiedler */
	public SchemaTabelleSpalte col_ZurueckgestelltAussiedler = add("ZurueckgestelltAussiedler", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Anzahl de Zurückgestellten ausgesiedelten Kinder in der Grundschule");

	/** Die Definition der Tabellenspalte ZurueckgestelltAussiedlerWeibl */
	public SchemaTabelleSpalte col_ZurueckgestelltAussiedlerWeibl = add("ZurueckgestelltAussiedlerWeibl", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Anzahl de Zurückgestellten ausgesiedelten weiblichen Kinder in der Grundschule");

	/** Die Definition der Tabellenspalte TeamTeaching */
	public SchemaTabelleSpalte col_TeamTeaching = add("TeamTeaching", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Aktiviert das Teamteaching");

	/** Die Definition der Tabellenspalte AbiGruppenprozess */
	public SchemaTabelleSpalte col_AbiGruppenprozess = add("AbiGruppenprozess", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sperrt oder erlaubt die Gruppenprozesse für das Abitur");

	/** Die Definition der Tabellenspalte DauerUnterrichtseinheit */
	public SchemaTabelleSpalte col_DauerUnterrichtseinheit = add("DauerUnterrichtseinheit", SchemaDatentypen.INT, false)
		.setJavaComment("Dauer der Unterrichtseinheit (bei NULL = 45 Minuten)");

	/** Die Definition der Tabellenspalte Gruppen8Bis1 */
	public SchemaTabelleSpalte col_Gruppen8Bis1 = add("Gruppen8Bis1", SchemaDatentypen.INT, false)
		.setJavaComment("Schule hat Betreuung 8 bis 13");

	/** Die Definition der Tabellenspalte Gruppen13Plus */
	public SchemaTabelleSpalte col_Gruppen13Plus = add("Gruppen13Plus", SchemaDatentypen.INT, false)
		.setJavaComment("Gruppen in der 13Plus-Betreuung");

	/** Die Definition der Tabellenspalte InternatsplaetzeM */
	public SchemaTabelleSpalte col_InternatsplaetzeM = add("InternatsplaetzeM", SchemaDatentypen.INT, false)
		.setJavaComment("Internatsplätze männlilch");

	/** Die Definition der Tabellenspalte InternatsplaetzeW */
	public SchemaTabelleSpalte col_InternatsplaetzeW = add("InternatsplaetzeW", SchemaDatentypen.INT, false)
		.setJavaComment("Internatsplätze weiblich");

	/** Die Definition der Tabellenspalte InternatsplaetzeNeutral */
	public SchemaTabelleSpalte col_InternatsplaetzeNeutral = add("InternatsplaetzeNeutral", SchemaDatentypen.INT, false)
		.setJavaComment("Internatsplätze divers ohne Angabe");

	/** Die Definition der Tabellenspalte SchulLogo */
	public SchemaTabelleSpalte col_SchulLogo = add("SchulLogo", SchemaDatentypen.LONGBLOB, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schullogo als Bild im Binärformat");

	/** Die Definition der Tabellenspalte SchulLogoBase64 */
	public SchemaTabelleSpalte col_SchulLogoBase64 = add("SchulLogoBase64", SchemaDatentypen.TEXT, false)
		.setJavaComment("Schullogo als Bild im Base64-Format");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte SchulleiterName */
	public SchemaTabelleSpalte col_SchulleiterName = add("SchulleiterName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Nachname der Schulleitung");

	/** Die Definition der Tabellenspalte SchulleiterVorname */
	public SchemaTabelleSpalte col_SchulleiterVorname = add("SchulleiterVorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Vorname der Schulleitung");

	/** Die Definition der Tabellenspalte SchulleiterAmtsbez */
	public SchemaTabelleSpalte col_SchulleiterAmtsbez = add("SchulleiterAmtsbez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Amtsbezeichnung der Schulleitung");

	/** Die Definition der Tabellenspalte SchulleiterGeschlecht */
	public SchemaTabelleSpalte col_SchulleiterGeschlecht = add("SchulleiterGeschlecht", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Geschlecht der Schulleitung");

	/** Die Definition der Tabellenspalte StvSchulleiterName */
	public SchemaTabelleSpalte col_StvSchulleiterName = add("StvSchulleiterName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Name der stellvertretenden Schulleitung");

	/** Die Definition der Tabellenspalte StvSchulleiterVorname */
	public SchemaTabelleSpalte col_StvSchulleiterVorname = add("StvSchulleiterVorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Vorname der stellvertretenden Schulleitung");

	/** Die Definition der Tabellenspalte StvSchulleiterAmtsbez */
	public SchemaTabelleSpalte col_StvSchulleiterAmtsbez = add("StvSchulleiterAmtsbez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Amtsbezeichnung der stellvertretenden Schulleitung");

	/** Die Definition der Tabellenspalte StvSchulleiterGeschlecht */
	public SchemaTabelleSpalte col_StvSchulleiterGeschlecht = add("StvSchulleiterGeschlecht", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Geschlecht der stellvertretenden Schulleitung");

	/** Die Definition der Tabellenspalte Einstellungen */
	public SchemaTabelleSpalte col_Einstellungen = add("Einstellungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("DEPRECATED: Schild2 - Einstellungen zur Schule im INI-Format (kann in einem Texteditor gelesen werden). Wird in Schild 3 ausgelagert.");

	/** Die Definition der Tabellenspalte WebAdresse */
	public SchemaTabelleSpalte col_WebAdresse = add("WebAdresse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Enthält die Homepage-Adresse URL der Schule");

	/** Die Definition der Tabellenspalte Land */
	public SchemaTabelleSpalte col_Land = add("Land", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Land der Schule ??? Wird wahrscheinlich für Bundeswehr verwendet?");

	/** Die Definition der Tabellenspalte Schuljahr */
	public SchemaTabelleSpalte col_Schuljahr = add("Schuljahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Aktuelle Schuljahr der Datenbank");

	/** Die Definition der Tabellenspalte SchuljahrAbschnitt */
	public SchemaTabelleSpalte col_SchuljahrAbschnitt = add("SchuljahrAbschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Aktueller Abschnitt der Datenbank");


	/** Die Definition des Fremdschlüssels EigeneSchule_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Schuljahreabschnitt_FK = addForeignKey(
			"EigeneSchule_Schuljahreabschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule.
	 */
	public Tabelle_EigeneSchule() {
		super("EigeneSchule", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOEigeneSchule");
		setJavaComment("Tabelle in der die Daten der Schule verwaltet werden, diese Tabelle darf in einer Einzelinstallation nur eine Zeile haben");
	}

}
