package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.statkue.ZulaessigesFachKuerzelASDConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Faecher.
 */
public class Tabelle_EigeneSchule_Faecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eindeutige ID zur Kennzeichnung des Fächer-Datensatzes");

	/** Die Definition der Tabellenspalte FachKrz */
	public SchemaTabelleSpalte col_FachKrz = add("FachKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("Kuerzel")
		.setJavaComment("Ein eindeutiges Kürzel zur Identifikation des Fach");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Die Bezeichnung des Faches");

	/** Die Definition der Tabellenspalte ZeugnisBez */
	public SchemaTabelleSpalte col_ZeugnisBez = add("ZeugnisBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaName("BezeichnungZeugnis")
		.setJavaComment("Die Bezeichnung des Faches auf einem Zeugnis");

	/** Die Definition der Tabellenspalte UeZeugnisBez */
	public SchemaTabelleSpalte col_UeZeugnisBez = add("UeZeugnisBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaName("BezeichnungUeberweisungsZeugnis")
		.setJavaComment("Die Bezeichnung des Faches auf einem Überweisungszeugnis");

	/** Die Definition der Tabellenspalte Zeugnisdatenquelle_ID */
	public SchemaTabelleSpalte col_Zeugnisdatenquelle_ID = add("Zeugnisdatenquelle_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Feld zum Festlegen der ID für die Zeugnisdatenquelle in Schild");

	/** Die Definition der Tabellenspalte Fachgruppe_ID */
	public SchemaTabelleSpalte col_Fachgruppe_ID = add("Fachgruppe_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Feld zum Festlegen der ID für die Zeugnisdatenquelle in Schild, wird während der Migration umbenannt.");

	/** Die Definition der Tabellenspalte StatistikKrz */
	public SchemaTabelleSpalte col_StatistikKrz = add("StatistikKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("StatistikFach")
		.setConverter(ZulaessigesFachKuerzelASDConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Das Statistik-Kürzel des Faches");

	/** Die Definition der Tabellenspalte BasisFach */
	public SchemaTabelleSpalte col_BasisFach = add("BasisFach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("IstOberstufenFach")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an, ob das Fach in der Oberstufe unterrichtet wird");

	/** Die Definition der Tabellenspalte IstSprache */
	public SchemaTabelleSpalte col_IstSprache = add("IstSprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("IstFremdsprache")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an, ob das Fach eine Fremdsprache ist");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaName("SortierungAllg")
		.setJavaComment("Gibt eine Sortierung für die Fächer vor");

	/** Die Definition der Tabellenspalte SortierungS2 */
	public SchemaTabelleSpalte col_SortierungS2 = add("SortierungS2", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaName("SortierungSekII")
		.setJavaComment("Gibt die spezielle Sortierung der Fächer für die Sekundarstufe II vor");

	/** Die Definition der Tabellenspalte NachprErlaubt */
	public SchemaTabelleSpalte col_NachprErlaubt = add("NachprErlaubt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstNachpruefungErlaubt")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob in dem Fach eine Nachprüfung erlaubt ist.");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob der Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht. ");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob Änderungen am Datensatz erlaubt sind.");

	/** Die Definition der Tabellenspalte Gewichtung */
	public SchemaTabelleSpalte col_Gewichtung = add("Gewichtung", SchemaDatentypen.SMALLINT, false)
		.setDefault("1")
		.setJavaComment("Gibt die Gewichtung des Faches im allgemeinbildenden Bereich am BK an, wenn keine gliederungsbezogenen Gewichtungen vergeben sind");

	/** Die Definition der Tabellenspalte Unterichtssprache */
	public SchemaTabelleSpalte col_Unterichtssprache = add("Unterichtssprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("D")
		.setJavaComment("Einstelliges Kürzel der Unterrichtssprache (E, F, …)");

	/** Die Definition der Tabellenspalte IstSchriftlich */
	public SchemaTabelleSpalte col_IstSchriftlich = add("IstSchriftlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("IstSchriftlichZK")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("gibt an, ob das Fach ein schritliches Fach für ZK ist oder nicht");

	/** Die Definition der Tabellenspalte IstSchriftlichBA */
	public SchemaTabelleSpalte col_IstSchriftlichBA = add("IstSchriftlichBA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("gibt an, ob ein Fach am BK für den beruflichen Abschluss schriftlich gewertet wird");

	/** Die Definition der Tabellenspalte AufZeugnis */
	public SchemaTabelleSpalte col_AufZeugnis = add("AufZeugnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob das Fach auf dem Zeugnis erscheinen soll");

	/** Die Definition der Tabellenspalte Lernfelder */
	public SchemaTabelleSpalte col_Lernfelder = add("Lernfelder", SchemaDatentypen.TEXT, false)
		.setJavaComment("speichert die Lernfeld-Texte zu den Fachklassen beim Unterrichtsfach am BK, ist von den Textfelder im Menü Fachklassen abgelöst worden, sollte nicht mehr genutzt werden");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte LK_Moegl */
	public SchemaTabelleSpalte col_LK_Moegl = add("LK_Moegl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichAbiLK")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach als Leistungskurs belegt werden kann.");

	/** Die Definition der Tabellenspalte Abi_Moegl */
	public SchemaTabelleSpalte col_Abi_Moegl = add("Abi_Moegl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichAbiGK")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach als drittes oder viertes Abiturfach belegt werden kann.");

	/** Die Definition der Tabellenspalte E1 */
	public SchemaTabelleSpalte col_E1 = add("E1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichEF1")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach im 1. Halbjahr der Einführungsphase belegt werden kann,");

	/** Die Definition der Tabellenspalte E2 */
	public SchemaTabelleSpalte col_E2 = add("E2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichEF2")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach im 2. Halbjahr der Einführungsphase belegt werden kann,");

	/** Die Definition der Tabellenspalte Q1 */
	public SchemaTabelleSpalte col_Q1 = add("Q1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichQ11")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach im 1. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann,");

	/** Die Definition der Tabellenspalte Q2 */
	public SchemaTabelleSpalte col_Q2 = add("Q2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichQ12")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach im 2. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann,");

	/** Die Definition der Tabellenspalte Q3 */
	public SchemaTabelleSpalte col_Q3 = add("Q3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichQ21")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach im 1. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann,");

	/** Die Definition der Tabellenspalte Q4 */
	public SchemaTabelleSpalte col_Q4 = add("Q4", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("IstMoeglichQ22")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("gibt an, ob das Fach im 2. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann,");

	/** Die Definition der Tabellenspalte AlsNeueFSInSII */
	public SchemaTabelleSpalte col_AlsNeueFSInSII = add("AlsNeueFSInSII", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("IstMoeglichAlsNeueFremdspracheInSekII")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("gibt an, ob das Fach als neue Fremdsprache in der Sekundarstufe zählt.");

	/** Die Definition der Tabellenspalte Leitfach_ID */
	public SchemaTabelleSpalte col_Leitfach_ID = add("Leitfach_ID", SchemaDatentypen.BIGINT, false)
		.setJavaName("ProjektKursLeitfach1_ID")
		.setJavaComment("bei Projektkursfächern: Die ID des Leitfaches");

	/** Die Definition der Tabellenspalte Leitfach2_ID */
	public SchemaTabelleSpalte col_Leitfach2_ID = add("Leitfach2_ID", SchemaDatentypen.BIGINT, false)
		.setJavaName("ProjektKursLeitfach2_ID")
		.setJavaComment("bei Projektkursfächern: Ggf. die ID des zweiten Leitfaches");

	/** Die Definition der Tabellenspalte E1_WZE */
	public SchemaTabelleSpalte col_E1_WZE = add("E1_WZE", SchemaDatentypen.INT, false)
		.setJavaName("WochenstundenEF1")
		.setJavaComment("Beim WBK: Gibt die Anzahl der Wochenstunden für das 1. Halbjahr in der Einführungsphase an.");

	/** Die Definition der Tabellenspalte E2_WZE */
	public SchemaTabelleSpalte col_E2_WZE = add("E2_WZE", SchemaDatentypen.INT, false)
		.setJavaName("WochenstundenEF2")
		.setJavaComment("Beim WBK: Gibt die Anzahl der Wochenstunden für das 2. Halbjahr in der Einführungsphase an.");

	/** Die Definition der Tabellenspalte Q_WZE */
	public SchemaTabelleSpalte col_Q_WZE = add("Q_WZE", SchemaDatentypen.INT, false)
		.setJavaName("WochenstundenQualifikationsphase")
		.setJavaComment("Beim WBK: Gibt die Anzahl der Wochenstunden für die Qualifikationsphase an.");

	/** Die Definition der Tabellenspalte E1_S */
	public SchemaTabelleSpalte col_E1_S = add("E1_S", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("MussSchriftlichEF1")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Beim WBK: Gibt an, ob das Fach in dem 1. Halbjahr der Einführungsphase schriftlich belegt werden muss");

	/** Die Definition der Tabellenspalte E2_S */
	public SchemaTabelleSpalte col_E2_S = add("E2_S", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("MussSchriftlichEF2")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Beim WBK: Gibt an, ob das Fach in dem 2. Halbjahr der Einführungsphase schriftlich belegt werden muss");

	/** Die Definition der Tabellenspalte NurMuendlich */
	public SchemaTabelleSpalte col_NurMuendlich = add("NurMuendlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("MussMuendlich")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Beim WBK: Gibt an, ob das Fach nur muendlich belegt werden darf");

	/** Die Definition der Tabellenspalte Aufgabenfeld */
	public SchemaTabelleSpalte col_Aufgabenfeld = add("Aufgabenfeld", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Das Aufgabenfeld, zu welchem das Fach gehört");

	/** Die Definition der Tabellenspalte AbgeschlFaecherHolen */
	public SchemaTabelleSpalte col_AbgeschlFaecherHolen = add("AbgeschlFaecherHolen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob das Fach bei der Operation „Abgeschlossene Fächer holen“ berücksichtigt werden soll.");

	/** Die Definition der Tabellenspalte GewichtungFHR */
	public SchemaTabelleSpalte col_GewichtungFHR = add("GewichtungFHR", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt eine Gewichtung des Faches bei der Berechnung der FHR an.");

	/** Die Definition der Tabellenspalte MaxBemZeichen */
	public SchemaTabelleSpalte col_MaxBemZeichen = add("MaxBemZeichen", SchemaDatentypen.INT, false)
		.setJavaComment("steuert die max. Anzahl von Zeichen in der Memo-Feldern der Fachbezogenen Bemerkungen (insbes. Grundschulzeugnisse)");


	/** Die Definition des Fremdschlüssels EigeneSchule_Faecher_Zeugnisdatenquellen_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Faecher_Zeugnisdatenquellen_FK = addForeignKey(
			"EigeneSchule_Faecher_Zeugnisdatenquellen_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Zeugnisdatenquelle_ID, Schema.tab_Fachgruppen.col_SchildFgID)
		);


	/** Die Definition des Unique-Index EigeneSchule_Faecher_UC1 */
	public SchemaTabelleUniqueIndex unique_EigeneSchule_Faecher_UC1 = addUniqueIndex("EigeneSchule_Faecher_UC1", 
			col_FachKrz
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Faecher.
	 */
	public Tabelle_EigeneSchule_Faecher() {
		super("EigeneSchule_Faecher", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.faecher");
		setJavaClassName("DTOFach");
		setJavaComment("Tabelle der vorhandenen Unterrichtsfächer und deren Bezeichnungen/ Einstellungen");
	}

}
