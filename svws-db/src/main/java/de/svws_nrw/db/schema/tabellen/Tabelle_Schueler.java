package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.GeschlechtConverter;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;
import de.svws_nrw.db.converter.current.SchuelerStatusConverter;
import de.svws_nrw.db.converter.current.VerkehrssprachenConverter;
import de.svws_nrw.db.converter.current.statkue.SchulgliederungKuerzelConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schueler.
 */
public class Tabelle_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schülerdatensatzes");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte GU_ID */
	public SchemaTabelleSpalte col_GU_ID = add("GU_ID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setNotNull()
		.setJavaComment("GU_ID des Schülerdatensatzes");

	/** Die Definition der Tabellenspalte SrcID */
	public SchemaTabelleSpalte col_SrcID = add("SrcID", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Muss aber noch aus Schild2 und Schild3 entfernt werden");

	/** Die Definition der Tabellenspalte IDext */
	public SchemaTabelleSpalte col_IDext = add("IDext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("externe ID");

	/** Die Definition der Tabellenspalte Status */
	public SchemaTabelleSpalte col_Status = add("Status", SchemaDatentypen.INT, false)
		.setConverter(SchuelerStatusConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Status des Schüler steuert die Einordnung in die Kästen Aktiv Neuaufnahme Abschluss usw.");

	/** Die Definition der Tabellenspalte Name */
	public SchemaTabelleSpalte col_Name = add("Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaName("Nachname")
		.setJavaComment("Name des Schülers PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Vorname */
	public SchemaTabelleSpalte col_Vorname = add("Vorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Vorname des Schülers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt.");

	/** Die Definition der Tabellenspalte Zusatz */
	public SchemaTabelleSpalte col_Zusatz = add("Zusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaName("AlleVornamen")
		.setJavaComment("Alle gültigen Vornamen, wenn mehrere vorhanden sind. Ist nur ein Vorname vorhanden bleibt das Feld leer und Vorname wird genutzt.");

	/** Die Definition der Tabellenspalte Geburtsname */
	public SchemaTabelleSpalte col_Geburtsname = add("Geburtsname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Geburtsname des Schülers");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Straße des Schülers");

	/** Die Definition der Tabellenspalte Strassenname */
	public SchemaTabelleSpalte col_Strassenname = add("Strassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname des Schülers");

	/** Die Definition der Tabellenspalte HausNr */
	public SchemaTabelleSpalte col_HausNr = add("HausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte HausNrZusatz */
	public SchemaTabelleSpalte col_HausNrZusatz = add("HausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden");

	/** Die Definition der Tabellenspalte Ort_ID */
	public SchemaTabelleSpalte col_Ort_ID = add("Ort_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Orts des Schülers");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_3)
		.setJavaComment("DEPRECATED: PLZ des Schülers");

	/** Die Definition der Tabellenspalte OrtAbk */
	public SchemaTabelleSpalte col_OrtAbk = add("OrtAbk", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klartext des Orts des Schülers");

	/** Die Definition der Tabellenspalte Ortsteil_ID */
	public SchemaTabelleSpalte col_Ortsteil_ID = add("Ortsteil_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Ortsteils des Schülers");

	/** Die Definition der Tabellenspalte Telefon */
	public SchemaTabelleSpalte col_Telefon = add("Telefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telefonnummer des Schülers");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("E-Mail-Adresse des Schülers");

	/** Die Definition der Tabellenspalte Fax */
	public SchemaTabelleSpalte col_Fax = add("Fax", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Fax oder Mobilnummer des Schülers");

	/** Die Definition der Tabellenspalte Klassen_ID */
	public SchemaTabelleSpalte col_Klassen_ID = add("Klassen_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: ID der Klasse des Schülers - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte Jahrgang */
	public SchemaTabelleSpalte col_Jahrgang = add("Jahrgang", SchemaDatentypen.SMALLINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("JahrgangSchueler")
		.setJavaComment("DEPRECATED: Schulbesuchsjahre - verschoben nach SchuelerLernabschittsdaten.Schulbesuchsjahre");

	/** Die Definition der Tabellenspalte PruefOrdnung */
	public SchemaTabelleSpalte col_PruefOrdnung = add("PruefOrdnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Prüfungsordnung des Schülers - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte Geburtsdatum */
	public SchemaTabelleSpalte col_Geburtsdatum = add("Geburtsdatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Geburtsdatum des Schülers");

	/** Die Definition der Tabellenspalte Geburtsort */
	public SchemaTabelleSpalte col_Geburtsort = add("Geburtsort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Geburtsort des Schülers");

	/** Die Definition der Tabellenspalte Volljaehrig */
	public SchemaTabelleSpalte col_Volljaehrig = add("Volljaehrig", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Schüler volljährig ist");

	/** Die Definition der Tabellenspalte Geschlecht */
	public SchemaTabelleSpalte col_Geschlecht = add("Geschlecht", SchemaDatentypen.SMALLINT, false)
		.setConverter(GeschlechtConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Geschlecht des Schülers");

	/** Die Definition der Tabellenspalte StaatKrz */
	public SchemaTabelleSpalte col_StaatKrz = add("StaatKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Kürzel der 1. Staatsangehörigkeit");

	/** Die Definition der Tabellenspalte StaatKrz2 */
	public SchemaTabelleSpalte col_StaatKrz2 = add("StaatKrz2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Kürzel der 2. Staatsangehörigkeit");

	/** Die Definition der Tabellenspalte StaatAbk */
	public SchemaTabelleSpalte col_StaatAbk = add("StaatAbk", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klartext der 1. Staatsangehörigkeit des Schülers");

	/** Die Definition der Tabellenspalte Aussiedler */
	public SchemaTabelleSpalte col_Aussiedler = add("Aussiedler", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Gibt an ob der Schüler ausgesiedelt ist (wird nicht mehr erfasst)");

	/** Die Definition der Tabellenspalte Religion_ID */
	public SchemaTabelleSpalte col_Religion_ID = add("Religion_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Religionskatalogeintrags");

	/** Die Definition der Tabellenspalte ReligionAbk */
	public SchemaTabelleSpalte col_ReligionAbk = add("ReligionAbk", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Klartext des Religionseintrags");

	/** Die Definition der Tabellenspalte Religionsabmeldung */
	public SchemaTabelleSpalte col_Religionsabmeldung = add("Religionsabmeldung", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Abmeldetdateum vom Religionsunterricht");

	/** Die Definition der Tabellenspalte Religionsanmeldung */
	public SchemaTabelleSpalte col_Religionsanmeldung = add("Religionsanmeldung", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Anmeldedatum zum Religionsunterricht wenn vorher abgemeldet");

	/** Die Definition der Tabellenspalte Bafoeg */
	public SchemaTabelleSpalte col_Bafoeg = add("Bafoeg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob ein Schüler BAFög bezieht");

	/** Die Definition der Tabellenspalte Schwerbehinderung */
	public SchemaTabelleSpalte col_Schwerbehinderung = add("Schwerbehinderung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Gibt an ob eine Schwerbehinderung vorliegt Ja Nein - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte Sportbefreiung_ID */
	public SchemaTabelleSpalte col_Sportbefreiung_ID = add("Sportbefreiung_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der Sportbefreiung");

	/** Die Definition der Tabellenspalte Fahrschueler_ID */
	public SchemaTabelleSpalte col_Fahrschueler_ID = add("Fahrschueler_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Fahrschülereintras");

	/** Die Definition der Tabellenspalte Haltestelle_ID */
	public SchemaTabelleSpalte col_Haltestelle_ID = add("Haltestelle_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der Haltestelle");

	/** Die Definition der Tabellenspalte HaltestelleAbk */
	public SchemaTabelleSpalte col_HaltestelleAbk = add("HaltestelleAbk", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Text der Haltestelle");

	/** Die Definition der Tabellenspalte ASDSchulform */
	public SchemaTabelleSpalte col_ASDSchulform = add("ASDSchulform", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("Schulgliederung")
		.setConverter(SchulgliederungKuerzelConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: ASD-Kürzel der Schulgliederung - Spalte fehlerhaft benannt! - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: ID des Jahrgangs - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte ASDJahrgang */
	public SchemaTabelleSpalte col_ASDJahrgang = add("ASDJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: ASD-Kürzel des Jahrgangs - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte Fachklasse_ID */
	public SchemaTabelleSpalte col_Fachklasse_ID = add("Fachklasse_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: ID der Fachklasse (BK) - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte SchulpflichtErf */
	public SchemaTabelleSpalte col_SchulpflichtErf = add("SchulpflichtErf", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob die Vollzeitschulpflicht erfüllt ist Ja Nein");

	/** Die Definition der Tabellenspalte Aufnahmedatum */
	public SchemaTabelleSpalte col_Aufnahmedatum = add("Aufnahmedatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Aufnahmedatum");

	/** Die Definition der Tabellenspalte Einschulungsjahr */
	public SchemaTabelleSpalte col_Einschulungsjahr = add("Einschulungsjahr", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Einschulungsjahr");

	/** Die Definition der Tabellenspalte Einschulungsart_ID */
	public SchemaTabelleSpalte col_Einschulungsart_ID = add("Einschulungsart_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der Einschlungsart IT.NRW");

	/** Die Definition der Tabellenspalte LSSchulNr */
	public SchemaTabelleSpalte col_LSSchulNr = add("LSSchulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("letzte Schule Schulnummer");

	/** Die Definition der Tabellenspalte LSSchulformSIM */
	public SchemaTabelleSpalte col_LSSchulformSIM = add("LSSchulformSIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("letzte Schule Schulgliederung");

	/** Die Definition der Tabellenspalte LSJahrgang */
	public SchemaTabelleSpalte col_LSJahrgang = add("LSJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("letzte Schule Jahrgang");

	/** Die Definition der Tabellenspalte LSSchulEntlassDatum */
	public SchemaTabelleSpalte col_LSSchulEntlassDatum = add("LSSchulEntlassDatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("letzte Schule Entlassdatum");

	/** Die Definition der Tabellenspalte LSVersetzung */
	public SchemaTabelleSpalte col_LSVersetzung = add("LSVersetzung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("letzte Schule Versetzungsvermerk");

	/** Die Definition der Tabellenspalte LSFachklKennung */
	public SchemaTabelleSpalte col_LSFachklKennung = add("LSFachklKennung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("letzte Schule Fachklassenkennung");

	/** Die Definition der Tabellenspalte LSFachklSIM */
	public SchemaTabelleSpalte col_LSFachklSIM = add("LSFachklSIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("letzte Schule Fachklassenschlüssel");

	/** Die Definition der Tabellenspalte LSEntlassgrund */
	public SchemaTabelleSpalte col_LSEntlassgrund = add("LSEntlassgrund", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("letzte Schule Entlassgrund");

	/** Die Definition der Tabellenspalte LSEntlassArt */
	public SchemaTabelleSpalte col_LSEntlassArt = add("LSEntlassArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("letzte Schule Entlassart");

	/** Die Definition der Tabellenspalte LSKlassenart */
	public SchemaTabelleSpalte col_LSKlassenart = add("LSKlassenart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("letzte Schule Klassenart");

	/** Die Definition der Tabellenspalte LSRefPaed */
	public SchemaTabelleSpalte col_LSRefPaed = add("LSRefPaed", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("letzte Schule Reformpädagogik");

	/** Die Definition der Tabellenspalte Entlassjahrgang */
	public SchemaTabelleSpalte col_Entlassjahrgang = add("Entlassjahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Entlasung von eigener Schule Jahrgang");

	/** Die Definition der Tabellenspalte Entlassjahrgang_ID */
	public SchemaTabelleSpalte col_Entlassjahrgang_ID = add("Entlassjahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Entlasung von eigener Schule JahrgangsID");

	/** Die Definition der Tabellenspalte Entlassdatum */
	public SchemaTabelleSpalte col_Entlassdatum = add("Entlassdatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Entlassdatum");

	/** Die Definition der Tabellenspalte Entlassgrund */
	public SchemaTabelleSpalte col_Entlassgrund = add("Entlassgrund", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Entlassgrund ");

	/** Die Definition der Tabellenspalte Entlassart */
	public SchemaTabelleSpalte col_Entlassart = add("Entlassart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Entlassart");

	/** Die Definition der Tabellenspalte SchulwechselNr */
	public SchemaTabelleSpalte col_SchulwechselNr = add("SchulwechselNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulnummer aufnehmende Schule");

	/** Die Definition der Tabellenspalte Schulwechseldatum */
	public SchemaTabelleSpalte col_Schulwechseldatum = add("Schulwechseldatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum des Schulwechsels");

	/** Die Definition der Tabellenspalte Geloescht */
	public SchemaTabelleSpalte col_Geloescht = add("Geloescht", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Löschmarkierung Schülerdatensatz");

	/** Die Definition der Tabellenspalte Gesperrt */
	public SchemaTabelleSpalte col_Gesperrt = add("Gesperrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Datensatz gesperrt Ja Nein");

	/** Die Definition der Tabellenspalte ModifiziertAm */
	public SchemaTabelleSpalte col_ModifiziertAm = add("ModifiziertAm", SchemaDatentypen.DATETIME, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("zuletzt geändert Datum");

	/** Die Definition der Tabellenspalte ModifiziertVon */
	public SchemaTabelleSpalte col_ModifiziertVon = add("ModifiziertVon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("zuletzt geändert Benutzer");

	/** Die Definition der Tabellenspalte Markiert */
	public SchemaTabelleSpalte col_Markiert = add("Markiert", SchemaDatentypen.VARCHAR, false).setDatenlaenge(21)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Datensatz ist markiert");

	/** Die Definition der Tabellenspalte FotoVorhanden */
	public SchemaTabelleSpalte col_FotoVorhanden = add("FotoVorhanden", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: nicht mehr genutzt Zustimmung Foto");

	/** Die Definition der Tabellenspalte JVA */
	public SchemaTabelleSpalte col_JVA = add("JVA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Ist Schüler einer Justizvollzugsanstalt");

	/** Die Definition der Tabellenspalte RefPaed */
	public SchemaTabelleSpalte col_RefPaed = add("RefPaed", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Teilnahme an Reformpädagogik - verschoben nach SchuelerLernabschittsdaten");

	/** Die Definition der Tabellenspalte KeineAuskunft */
	public SchemaTabelleSpalte col_KeineAuskunft = add("KeineAuskunft", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Keine Auskunft an Dritte Ja Nein");

	/** Die Definition der Tabellenspalte Beruf */
	public SchemaTabelleSpalte col_Beruf = add("Beruf", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Berufsbezeichnung wenn der Schüler einen hat");

	/** Die Definition der Tabellenspalte AbschlussDatum */
	public SchemaTabelleSpalte col_AbschlussDatum = add("AbschlussDatum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Abschlussdatum");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zum Schüler Memofeld");

	/** Die Definition der Tabellenspalte BeginnBildungsgang */
	public SchemaTabelleSpalte col_BeginnBildungsgang = add("BeginnBildungsgang", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Beginn des Bildungsgangs BK");

	/** Die Definition der Tabellenspalte OrgFormKrz */
	public SchemaTabelleSpalte col_OrgFormKrz = add("OrgFormKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Kürzel der Organisationsform IT.NRW - verschoben nach SchuelerLernabschnittsdaten");

	/** Die Definition der Tabellenspalte Klassenart */
	public SchemaTabelleSpalte col_Klassenart = add("Klassenart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klassenart IT.NRW - verschoben nach SchuelerLernabschittsdaten ");

	/** Die Definition der Tabellenspalte DurchschnittsNote */
	public SchemaTabelleSpalte col_DurchschnittsNote = add("DurchschnittsNote", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaComment("Speichert die Durchschnittsnote mit einer Nachkommastelle (aber als Textfeld) Wird primär am BK benutzt");

	/** Die Definition der Tabellenspalte LSSGL */
	public SchemaTabelleSpalte col_LSSGL = add("LSSGL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("letzte Schule Gliederung");

	/** Die Definition der Tabellenspalte LSSchulform */
	public SchemaTabelleSpalte col_LSSchulform = add("LSSchulform", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("letzte Schule Schulform");

	/** Die Definition der Tabellenspalte KonfDruck */
	public SchemaTabelleSpalte col_KonfDruck = add("KonfDruck", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Konfession aufs Zeugnis für den Druck");

	/** Die Definition der Tabellenspalte DSN_Text */
	public SchemaTabelleSpalte col_DSN_Text = add("DSN_Text", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Speichert die Durchschnittsnote im Klartext (also befriedigend bei einer Durchschnittsnote von z.B. 3.1)");

	/** Die Definition der Tabellenspalte Berufsabschluss */
	public SchemaTabelleSpalte col_Berufsabschluss = add("Berufsabschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Bezeichnung Berufsabschluss");

	/** Die Definition der Tabellenspalte Schwerpunkt_ID */
	public SchemaTabelleSpalte col_Schwerpunkt_ID = add("Schwerpunkt_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: ID des Schwerpunkt (BK und RS) - verschoben nach SchuelerLernabschnittsdaten");

	/** Die Definition der Tabellenspalte LSSGL_SIM */
	public SchemaTabelleSpalte col_LSSGL_SIM = add("LSSGL_SIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Letzte Schule Schulgiederung (wichtig wenn BK)");

	/** Die Definition der Tabellenspalte BerufsschulpflErf */
	public SchemaTabelleSpalte col_BerufsschulpflErf = add("BerufsschulpflErf", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob die Berufsschulpflicht erfüllt ist (Ja/Nein)");

	/** Die Definition der Tabellenspalte StatusNSJ */
	public SchemaTabelleSpalte col_StatusNSJ = add("StatusNSJ", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, welcher Status für das kommende Schuljahr geplant ist (nur BK)");

	/** Die Definition der Tabellenspalte FachklasseNSJ_ID */
	public SchemaTabelleSpalte col_FachklasseNSJ_ID = add("FachklasseNSJ_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Gibt an, welche Fachklasse für das kommende Schuljahr geplant ist (nur BK)");

	/** Die Definition der Tabellenspalte BuchKonto */
	public SchemaTabelleSpalte col_BuchKonto = add("BuchKonto", SchemaDatentypen.FLOAT, false)
		.setJavaComment("DEPRECATED: SchildMedia");

	/** Die Definition der Tabellenspalte VerkehrsspracheFamilie */
	public SchemaTabelleSpalte col_VerkehrsspracheFamilie = add("VerkehrsspracheFamilie", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setDefault("deu")
		.setConverter(VerkehrssprachenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Migrationshintergrund Verkehrssprache in der Familie");

	/** Die Definition der Tabellenspalte JahrZuzug */
	public SchemaTabelleSpalte col_JahrZuzug = add("JahrZuzug", SchemaDatentypen.INT, false)
		.setJavaComment("Migrationshintergrund Zuzugsjahr");

	/** Die Definition der Tabellenspalte DauerKindergartenbesuch */
	public SchemaTabelleSpalte col_DauerKindergartenbesuch = add("DauerKindergartenbesuch", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Dauer des Kindergartenbesuchs");

	/** Die Definition der Tabellenspalte VerpflichtungSprachfoerderkurs */
	public SchemaTabelleSpalte col_VerpflichtungSprachfoerderkurs = add("VerpflichtungSprachfoerderkurs", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein)");

	/** Die Definition der Tabellenspalte TeilnahmeSprachfoerderkurs */
	public SchemaTabelleSpalte col_TeilnahmeSprachfoerderkurs = add("TeilnahmeSprachfoerderkurs", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Teilnahme an einen Sprachförderkurs (Ja/Nein)");

	/** Die Definition der Tabellenspalte SchulbuchgeldBefreit */
	public SchemaTabelleSpalte col_SchulbuchgeldBefreit = add("SchulbuchgeldBefreit", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Vom Schulbuchgeld befreit (Ja/Nein)");

	/** Die Definition der Tabellenspalte Autist */
	public SchemaTabelleSpalte col_Autist = add("Autist", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Gibt an ob Autismuss vorliegt (Ja/Nein) - verschoben nach Tabelle SchuelerLernabschnittsdaten");

	/** Die Definition der Tabellenspalte GeburtslandSchueler */
	public SchemaTabelleSpalte col_GeburtslandSchueler = add("GeburtslandSchueler", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Migrationshintergrund Geburtsland des Schülers");

	/** Die Definition der Tabellenspalte GeburtslandVater */
	public SchemaTabelleSpalte col_GeburtslandVater = add("GeburtslandVater", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Migrationshintergrund Geburtsland des Vaters");

	/** Die Definition der Tabellenspalte GeburtslandMutter */
	public SchemaTabelleSpalte col_GeburtslandMutter = add("GeburtslandMutter", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Migrationshintergrund Geburtsland der Mutter");

	/** Die Definition der Tabellenspalte Uebergangsempfehlung_JG5 */
	public SchemaTabelleSpalte col_Uebergangsempfehlung_JG5 = add("Uebergangsempfehlung_JG5", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Übergangsempfehlung für den Jahrgang 5");

	/** Die Definition der Tabellenspalte ErsteSchulform_SI */
	public SchemaTabelleSpalte col_ErsteSchulform_SI = add("ErsteSchulform_SI", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Erste Schulform in der Sek1");

	/** Die Definition der Tabellenspalte JahrWechsel_SI */
	public SchemaTabelleSpalte col_JahrWechsel_SI = add("JahrWechsel_SI", SchemaDatentypen.INT, false)
		.setJavaComment("Jahr des Wechsels in die Sekundarstufe I");

	/** Die Definition der Tabellenspalte JahrWechsel_SII */
	public SchemaTabelleSpalte col_JahrWechsel_SII = add("JahrWechsel_SII", SchemaDatentypen.INT, false)
		.setJavaComment("Jahr des Wechsels in die Sekundarstufe II");

	/** Die Definition der Tabellenspalte Migrationshintergrund */
	public SchemaTabelleSpalte col_Migrationshintergrund = add("Migrationshintergrund", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Migrationshintergrund vorhanden (Ja/Nein)");

	/** Die Definition der Tabellenspalte ExterneSchulNr */
	public SchemaTabelleSpalte col_ExterneSchulNr = add("ExterneSchulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Schulnummer von externen Koopschüern");

	/** Die Definition der Tabellenspalte Kindergarten_ID */
	public SchemaTabelleSpalte col_Kindergarten_ID = add("Kindergarten_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Kinderkarteneintrags (GS)");

	/** Die Definition der Tabellenspalte LetzterBerufsAbschluss */
	public SchemaTabelleSpalte col_LetzterBerufsAbschluss = add("LetzterBerufsAbschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("erreichter berufsbezogener Abschluss LSSchule");

	/** Die Definition der Tabellenspalte LetzterAllgAbschluss */
	public SchemaTabelleSpalte col_LetzterAllgAbschluss = add("LetzterAllgAbschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("erreichter allgemeinbildender Abschluss LSSchule");

	/** Die Definition der Tabellenspalte Land */
	public SchemaTabelleSpalte col_Land = add("Land", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Land des Wohnsitzes des Schüler (in Grenzgebieten möglich)");

	/** Die Definition der Tabellenspalte Duplikat */
	public SchemaTabelleSpalte col_Duplikat = add("Duplikat", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Datensatz ein Duplikat ist");

	/** Die Definition der Tabellenspalte EinschulungsartASD */
	public SchemaTabelleSpalte col_EinschulungsartASD = add("EinschulungsartASD", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("ASD-Kürzel Einschulungsart IT.NRW");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte BilingualerZweig */
	public SchemaTabelleSpalte col_BilingualerZweig = add("BilingualerZweig", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Sprache des Bilingualen Zweigs, verschoben nach Tabelle SchuelerLernabschnittsdaten");

	/** Die Definition der Tabellenspalte DurchschnittsnoteFHR */
	public SchemaTabelleSpalte col_DurchschnittsnoteFHR = add("DurchschnittsnoteFHR", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaComment("Speichert die Durchschnittsnote der FHR-Prüfung mit einer Nachkommastelle (aber als Textfeld) Wird nur am BK benutzt");

	/** Die Definition der Tabellenspalte DSN_FHR_Text */
	public SchemaTabelleSpalte col_DSN_FHR_Text = add("DSN_FHR_Text", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Speichert die Durchschnittsnote der FHR-Prüfung im Klartext (also befriedigend bei einer Durchschnittsnote von z.B. 3.1) wird nur am BK verwendet");

	/** Die Definition der Tabellenspalte Eigenanteil */
	public SchemaTabelleSpalte col_Eigenanteil = add("Eigenanteil", SchemaDatentypen.FLOAT, false)
		.setJavaComment("TODO DEPRECATED: Eigenanteil Ja/Nein ");

	/** Die Definition der Tabellenspalte StaatAbk2 */
	public SchemaTabelleSpalte col_StaatAbk2 = add("StaatAbk2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Kürzel der 2. Staatsangehörigkeit");

	/** Die Definition der Tabellenspalte BKAZVO */
	public SchemaTabelleSpalte col_BKAZVO = add("BKAZVO", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an ob der Schüler in einem Bildungsgang nach BKAZVO ist (BK)");

	/** Die Definition der Tabellenspalte HatBerufsausbildung */
	public SchemaTabelleSpalte col_HatBerufsausbildung = add("HatBerufsausbildung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Schüler eine Berufsausbildung hat (BK)");

	/** Die Definition der Tabellenspalte Ausweisnummer */
	public SchemaTabelleSpalte col_Ausweisnummer = add("Ausweisnummer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Nummer des Schülerausweises");

	/** Die Definition der Tabellenspalte AOSF */
	public SchemaTabelleSpalte col_AOSF = add("AOSF", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED Gibt an ob der Schüler ein AOSF hat - verschoben nach Tabelle SchuelerLernabschnittsdaten ");

	/** Die Definition der Tabellenspalte EPJahre */
	public SchemaTabelleSpalte col_EPJahre = add("EPJahre", SchemaDatentypen.INT, false)
		.setDefault("2")
		.setJavaComment("Anzahl der Jahre in der Schuleingangssphase");

	/** Die Definition der Tabellenspalte LSBemerkung */
	public SchemaTabelleSpalte col_LSBemerkung = add("LSBemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Bemerkung der zuletzt besuchten Schule");

	/** Die Definition der Tabellenspalte WechselBestaetigt */
	public SchemaTabelleSpalte col_WechselBestaetigt = add("WechselBestaetigt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Wechsel zur aufnehmenden Schule bestätigt");

	/** Die Definition der Tabellenspalte DauerBildungsgang */
	public SchemaTabelleSpalte col_DauerBildungsgang = add("DauerBildungsgang", SchemaDatentypen.INT, false)
		.setJavaComment("Dauer des Bildungsgangs am BK");

	/** Die Definition der Tabellenspalte AnmeldeDatum */
	public SchemaTabelleSpalte col_AnmeldeDatum = add("AnmeldeDatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Anmeldedatum des Schülers");

	/** Die Definition der Tabellenspalte MeisterBafoeg */
	public SchemaTabelleSpalte col_MeisterBafoeg = add("MeisterBafoeg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob ein Schüler MeisterBafög bezieht BK");

	/** Die Definition der Tabellenspalte OnlineAnmeldung */
	public SchemaTabelleSpalte col_OnlineAnmeldung = add("OnlineAnmeldung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Schüler hat sich Online angemeldet (Ja/Nein)");

	/** Die Definition der Tabellenspalte Dokumentenverzeichnis */
	public SchemaTabelleSpalte col_Dokumentenverzeichnis = add("Dokumentenverzeichnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Pfad zum Dokumentenverzeichnis");

	/** Die Definition der Tabellenspalte Berufsqualifikation */
	public SchemaTabelleSpalte col_Berufsqualifikation = add("Berufsqualifikation", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Karteireiter Schulbesuch Berufsausbildung vorhanden (Ja/Nein)");

	/** Die Definition der Tabellenspalte ZieldifferentesLernen */
	public SchemaTabelleSpalte col_ZieldifferentesLernen = add("ZieldifferentesLernen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Gibt an ob der Schüler zieldifferent unterrichtet wird - verschoben nach Tabelle SchuelerLernabschnittsdaten");

	/** Die Definition der Tabellenspalte ZusatzNachname */
	public SchemaTabelleSpalte col_ZusatzNachname = add("ZusatzNachname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setVeraltet(SchemaRevisionen.REV_2)
		.setJavaComment("Gibt ggf. den Zusatz zum Nachnamen an.");

	/** Die Definition der Tabellenspalte EndeEingliederung */
	public SchemaTabelleSpalte col_EndeEingliederung = add("EndeEingliederung", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Ende der Eingliederung bei zugezogenen Schülern (Flüchtlingen)");

	/** Die Definition der Tabellenspalte SchulEmail */
	public SchemaTabelleSpalte col_SchulEmail = add("SchulEmail", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("schulische E-Mail-Adresse des Schülers");

	/** Die Definition der Tabellenspalte EndeAnschlussfoerderung */
	public SchemaTabelleSpalte col_EndeAnschlussfoerderung = add("EndeAnschlussfoerderung", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Ende der Anschlussförderung bei zugezogenen Schülern (Flüchtlingen)");

	/** Die Definition der Tabellenspalte MasernImpfnachweis */
	public SchemaTabelleSpalte col_MasernImpfnachweis = add("MasernImpfnachweis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an, ob ein Nachweis über die Masern-Impfung erbracht wurde");

	/** Die Definition der Tabellenspalte Lernstandsbericht */
	public SchemaTabelleSpalte col_Lernstandsbericht = add("Lernstandsbericht", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob ein Schüler Sprachförderung in Deutsch (DAZ) erhält und somit Lernstandsberichte statt Zeugnisse");

	/** Die Definition der Tabellenspalte SprachfoerderungVon */
	public SchemaTabelleSpalte col_SprachfoerderungVon = add("SprachfoerderungVon", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum des Beginns der Sprachförderung");

	/** Die Definition der Tabellenspalte SprachfoerderungBis */
	public SchemaTabelleSpalte col_SprachfoerderungBis = add("SprachfoerderungBis", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum des Endes der Sprachförderung");

	/** Die Definition der Tabellenspalte EntlassungBemerkung */
	public SchemaTabelleSpalte col_EntlassungBemerkung = add("EntlassungBemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Bemerkung bei Entlassung von der eigenen Schule");

	/** Die Definition der Tabellenspalte CredentialID */
	public SchemaTabelleSpalte col_CredentialID = add("CredentialID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des Credential-Eintrags");

	/** Die Definition der Tabellenspalte AktSchuljahr */
	public SchemaTabelleSpalte col_AktSchuljahr = add("AktSchuljahr", SchemaDatentypen.SMALLINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Aktuelles Schuljahr des Schülers");

	/** Die Definition der Tabellenspalte AktAbschnitt */
	public SchemaTabelleSpalte col_AktAbschnitt = add("AktAbschnitt", SchemaDatentypen.SMALLINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Aktueller Abschnitt des Schülers");

	/** Die Definition der Tabellenspalte Klasse */
	public SchemaTabelleSpalte col_Klasse = add("Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klartext Klasse des Schülers");

	/** Die Definition der Tabellenspalte NeuZugewandert */
	public SchemaTabelleSpalte col_NeuZugewandert = add("NeuZugewandert", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob der Schueler neu zugewandert ist (1) oder nicht (0). Wurde in der Ukraine Krise im Migrationshintergrund geschaffen.");


	/** Die Definition des Fremdschlüssels Schueler_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Schuljahreabschnitt_FK = addForeignKey(
			"Schueler_Schuljahreabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels Schueler_Credentials_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Credentials_FK = addForeignKey(
			"Schueler_Credentials_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_CredentialID, Schema.tab_Credentials.col_ID)
		);

	/** Die Definition des Fremdschlüssels Schueler_Einschulungsart_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Einschulungsart_FK = addForeignKey(
			"Schueler_Einschulungsart_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Einschulungsart_ID, Schema.tab_K_EinschulungsArt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Entlassjahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Entlassjahrgang_FK = addForeignKey(
			"Schueler_Entlassjahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Entlassjahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Fachklasse_Naechstes_Schuljahr_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Fachklasse_Naechstes_Schuljahr_FK = addForeignKey(
			"Schueler_Fachklasse_Naechstes_Schuljahr_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_FachklasseNSJ_ID, Schema.tab_EigeneSchule_Fachklassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Fahrschueler_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Fahrschueler_FK = addForeignKey(
			"Schueler_Fahrschueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Fahrschueler_ID, Schema.tab_K_FahrschuelerArt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Haltestelle_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Haltestelle_FK = addForeignKey(
			"Schueler_Haltestelle_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Haltestelle_ID, Schema.tab_K_Haltestelle.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Kindergarten_ID_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Kindergarten_ID_FK = addForeignKey(
			"Schueler_Kindergarten_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Kindergarten_ID, Schema.tab_K_Kindergarten.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Ort_PLZ_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Ort_PLZ_FK = addForeignKey(
			"Schueler_Ort_PLZ_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Ort_ID, Schema.tab_K_Ort.col_ID)
		);

	/** Die Definition des Fremdschlüssels Schueler_Ortsteil_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Ortsteil_FK = addForeignKey(
			"Schueler_Ortsteil_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Ortsteil_ID, Schema.tab_K_Ortsteil.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Religion_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Religion_FK = addForeignKey(
			"Schueler_Religion_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Religion_ID, Schema.tab_K_Religion.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Sportbefreiung_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Sportbefreiung_FK = addForeignKey(
			"Schueler_Sportbefreiung_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Sportbefreiung_ID, Schema.tab_K_Sportbefreiung.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Status_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Status_FK = addForeignKey(
			"Schueler_Status_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Status, Schema.tab_SchuelerStatus.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Statkue_Nationalitaeten_1_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Statkue_Nationalitaeten_1_FK = addForeignKey(
			"Schueler_Statkue_Nationalitaeten_1_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_StaatKrz, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Statkue_Nationalitaeten_2_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Statkue_Nationalitaeten_2_FK = addForeignKey(
			"Schueler_Statkue_Nationalitaeten_2_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_StaatKrz2, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Statkue_Nationalitaeten_3_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Statkue_Nationalitaeten_3_FK = addForeignKey(
			"Schueler_Statkue_Nationalitaeten_3_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_GeburtslandSchueler, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Statkue_Nationalitaeten_4_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Statkue_Nationalitaeten_4_FK = addForeignKey(
			"Schueler_Statkue_Nationalitaeten_4_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_GeburtslandVater, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Schueler_Statkue_Nationalitaeten_5_FK */
	public SchemaTabelleFremdschluessel fk_Schueler_Statkue_Nationalitaeten_5_FK = addForeignKey(
			"Schueler_Statkue_Nationalitaeten_5_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_GeburtslandMutter, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Unique-Index Schueler_UC1 */
	public SchemaTabelleUniqueIndex unique_Schueler_UC1 = addUniqueIndex("Schueler_UC1",
		col_GU_ID
	);


	/** Die Definition des Non-Unique-Index Schueler_IDX_Geloescht_Status_Entlassjahrgang_ID */
	public SchemaTabelleIndex index_Schueler_IDX_Geloescht_Status_Entlassjahrgang_ID = addIndex("Schueler_IDX_Geloescht_Status_Entlassjahrgang_ID",
		col_Geloescht,
		col_Status,
		col_Entlassjahrgang_ID
	).setRevision(SchemaRevisionen.REV_12);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schueler.
	 */
	public Tabelle_Schueler() {
		super("Schueler", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchueler");
		setJavaComment("Tabelle mit den Grundlegenden Schülerdaten");
	}

}
