package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromNotenpunkte;
import de.nrw.schule.svws.db.converter.current.gost.GOStBesondereLernleistungConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerAbitur.
 */
public class Tabelle_SchuelerAbitur extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine eindeutige ID für den Abiturdatensatz");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Schülers – verweist auf den Schüler");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte FA_Fach */
	public SchemaTabelleSpalte col_FA_Fach = add("FA_Fach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(130)
		.setJavaName("FacharbeitFach")
		.setJavaComment("BK: Facharbeit – Fach");

	/** Die Definition der Tabellenspalte FA_Punkte */
	public SchemaTabelleSpalte col_FA_Punkte = add("FA_Punkte", SchemaDatentypen.INT, false)
		.setJavaName("FacharbeitNotenpunkte")
		.setJavaComment("BK: Facharbeit – Notenpunkte der Facharbeit");

	/** Die Definition der Tabellenspalte FehlStd */
	public SchemaTabelleSpalte col_FehlStd = add("FehlStd", SchemaDatentypen.INT, false)
		.setJavaName("FehlstundenSumme")
		.setJavaComment("Die Summe der Fehlstunden – sofern berechnet");

	/** Die Definition der Tabellenspalte uFehlStd */
	public SchemaTabelleSpalte col_uFehlStd = add("uFehlStd", SchemaDatentypen.INT, false)
		.setJavaName("FehlstundenSummeUnentschuldigt")
		.setJavaComment("Die Summe der unentschuldigten Fehlstunden – sofern berechnet");

	/** Die Definition der Tabellenspalte Latinum */
	public SchemaTabelleSpalte col_Latinum = add("Latinum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("hatLatinum")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Sprachqualifikation: Latinum");

	/** Die Definition der Tabellenspalte KlLatinum */
	public SchemaTabelleSpalte col_KlLatinum = add("KlLatinum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("hatKleinesLatinum")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Sprachqualifikation: Kleines Latinum");

	/** Die Definition der Tabellenspalte Graecum */
	public SchemaTabelleSpalte col_Graecum = add("Graecum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("hatGraecum")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Sprachqualifikation: Graecum");

	/** Die Definition der Tabellenspalte Hebraicum */
	public SchemaTabelleSpalte col_Hebraicum = add("Hebraicum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("hatHebraicum")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Sprachqualifikation: Hebraicum");

	/** Die Definition der Tabellenspalte FranzBilingual */
	public SchemaTabelleSpalte col_FranzBilingual = add("FranzBilingual", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED, nicht mehr genutzt");

	/** Die Definition der Tabellenspalte BesondereLernleistung */
	public SchemaTabelleSpalte col_BesondereLernleistung = add("BesondereLernleistung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED, nur für alte APO, markiert, ob eine besondere Lernleistung vorliegt");

	/** Die Definition der Tabellenspalte AnzRelLK */
	public SchemaTabelleSpalte col_AnzRelLK = add("AnzRelLK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("-")
		.setJavaComment("DEPRECATED, nur alte APO für altes G9");

	/** Die Definition der Tabellenspalte AnzRelGK */
	public SchemaTabelleSpalte col_AnzRelGK = add("AnzRelGK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("-")
		.setJavaComment("DEPRECATED, nur alte APO für altes G9");

	/** Die Definition der Tabellenspalte AnzRelOK */
	public SchemaTabelleSpalte col_AnzRelOK = add("AnzRelOK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("-")
		.setJavaComment("DEPRECATED, nur alte APO für altes G9");

	/** Die Definition der Tabellenspalte AnzDefLK */
	public SchemaTabelleSpalte col_AnzDefLK = add("AnzDefLK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("-")
		.setJavaComment("DEPRECATED, nur alte APO für altes G9");

	/** Die Definition der Tabellenspalte AnzDefGK */
	public SchemaTabelleSpalte col_AnzDefGK = add("AnzDefGK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("-")
		.setJavaComment("DEPRECATED, nur alte APO für altes G9");

	/** Die Definition der Tabellenspalte Thema_PJK */
	public SchemaTabelleSpalte col_Thema_PJK = add("Thema_PJK", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaName("ProjektkursThema")
		.setJavaComment("Projektkurs: Das Thema des Projektkurses");

	/** Die Definition der Tabellenspalte FS2_SekI_Manuell */
	public SchemaTabelleSpalte col_FS2_SekI_Manuell = add("FS2_SekI_Manuell", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("FremdspracheSekIManuellGeprueft")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("true, falls die zweite Fremsprache in der Sekundarstufe 1 manuell geprüft wurde und vom Algorithmus als gegeben angesehen werden kann.");

	/** Die Definition der Tabellenspalte Kurse_I */
	public SchemaTabelleSpalte col_Kurse_I = add("Kurse_I", SchemaDatentypen.INT, false)
		.setJavaName("BlockI_AnzahlKurseEingebracht")
		.setJavaComment("Block I: Die Anzahl der eingebrachten Kurse aus der Qualifikationsphase");

	/** Die Definition der Tabellenspalte Defizite_I */
	public SchemaTabelleSpalte col_Defizite_I = add("Defizite_I", SchemaDatentypen.INT, false)
		.setJavaName("BlockI_AnzahlDefiziteEingebracht")
		.setJavaComment("Block I: Die Gesamtanzahl der eingebrachten Defizite aus der Qualifikationsphase");

	/** Die Definition der Tabellenspalte LK_Defizite_I */
	public SchemaTabelleSpalte col_LK_Defizite_I = add("LK_Defizite_I", SchemaDatentypen.INT, false)
		.setJavaName("BlockI_AnzahlDefiziteLK")
		.setJavaComment("Block I: Die Anzahl der Defizite im LK-Bereich aus der Qualifikationsphase");

	/** Die Definition der Tabellenspalte AnzahlKurse_0 */
	public SchemaTabelleSpalte col_AnzahlKurse_0 = add("AnzahlKurse_0", SchemaDatentypen.INT, false)
		.setJavaName("BlockI_AnzahlDefizite0Punkte")
		.setJavaComment("Block I: Die Anzahl der Defizite mit 0 Punkten");

	/** Die Definition der Tabellenspalte Punktsumme_I */
	public SchemaTabelleSpalte col_Punktsumme_I = add("Punktsumme_I", SchemaDatentypen.INT, false)
		.setJavaName("BlockI_PunktsummeNormiert")
		.setJavaComment("Block I: Die normierte Punktesumme aus der Qualifikationsphase");

	/** Die Definition der Tabellenspalte Durchschnitt_I */
	public SchemaTabelleSpalte col_Durchschnitt_I = add("Durchschnitt_I", SchemaDatentypen.FLOAT, false)
		.setJavaName("BlockI_NotenpunktdurchschnittEingebrachterKurse")
		.setJavaComment("Block I: Der Notenpunktdurchschnitt aus den eingebrachten Kurse der Qualifikationsphase");

	/** Die Definition der Tabellenspalte SummeGK */
	public SchemaTabelleSpalte col_SummeGK = add("SummeGK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("BlockI_SummeNotenpunkteGK")
		.setJavaComment("Block I: Die Summe der erreichten Notepunkte aus dem Grundkurs-Bereich");

	/** Die Definition der Tabellenspalte SummeLK */
	public SchemaTabelleSpalte col_SummeLK = add("SummeLK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("BlockI_SummeNotenpunkteLK")
		.setJavaComment("Block I: Die Summe der erreichten Notepunkte aus dem Leistungskurs-Bereich");

	/** Die Definition der Tabellenspalte SummenOK */
	public SchemaTabelleSpalte col_SummenOK = add("SummenOK", SchemaDatentypen.SMALLINT, false)
		.setJavaName("-")
		.setJavaComment("DEPRECATED: Punktsummen OK für alte APO für alres G9 (1: , NULL: )");

	/** Die Definition der Tabellenspalte Zugelassen */
	public SchemaTabelleSpalte col_Zugelassen = add("Zugelassen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("BlockI_HatZulassung")
		.setJavaComment("Block I: Gibt an, ob der Schüler zu den Abiturprüfungen zugelassen ist, d.h. die Bedingungen für Block I erfüllt hat (+: Ja, -: Nein, R: Freiwilliger Rücktritt)");

	/** Die Definition der Tabellenspalte BLL_Art */
	public SchemaTabelleSpalte col_BLL_Art = add("BLL_Art", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("K")
		.setJavaName("BesondereLernleistungArt")
		.setConverter(GOStBesondereLernleistungConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Besondere Lernleistung: Die Art der besonderen Lernleistung(K: Keine, E: Externe, P: in einem Projektkurs)");

	/** Die Definition der Tabellenspalte BLL_Punkte */
	public SchemaTabelleSpalte col_BLL_Punkte = add("BLL_Punkte", SchemaDatentypen.INT, false)
		.setJavaName("BesondereLernleistungNotenpunkte")
		.setConverter(NoteConverterFromNotenpunkte.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Besondere Lernleistung: Die Notenpunkte, welche bei der besonderen Lernleistung erreicht wurden – einfach gewichtet");

	/** Die Definition der Tabellenspalte Thema_BLL */
	public SchemaTabelleSpalte col_Thema_BLL = add("Thema_BLL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaName("BesondereLernleistungThema")
		.setJavaComment("Besondere Lernleistung: Das Thema der besonderen Lernleistung");

	/** Die Definition der Tabellenspalte Punktsumme_II */
	public SchemaTabelleSpalte col_Punktsumme_II = add("Punktsumme_II", SchemaDatentypen.INT, false)
		.setJavaName("Pruefung_Punktsumme")
		.setJavaComment("Block II: Die Punktsumme aus den Abiturprüfungen, NULL falls noch nicht berechnet");

	/** Die Definition der Tabellenspalte Defizite_II */
	public SchemaTabelleSpalte col_Defizite_II = add("Defizite_II", SchemaDatentypen.INT, false)
		.setJavaName("Pruefung_AnzahlDefizite")
		.setJavaComment("Block II: Die Anzahl der Defizite bei den Abiturprüfungen, NULL falls noch nicht berechnet");

	/** Die Definition der Tabellenspalte LK_Defizite_II */
	public SchemaTabelleSpalte col_LK_Defizite_II = add("LK_Defizite_II", SchemaDatentypen.INT, false)
		.setJavaName("Pruefung_AnzahlDefiziteLK")
		.setJavaComment("Block II: Die Anzahl der LK-Defizite bei den Abiturprüfungen, NULL falls noch nicht berechnet");

	/** Die Definition der Tabellenspalte PruefungBestanden */
	public SchemaTabelleSpalte col_PruefungBestanden = add("PruefungBestanden", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Pruefung_hatBestanden")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gesamt: Gibt an, ob die Abiturprüfung bestanden wurde");

	/** Die Definition der Tabellenspalte Note */
	public SchemaTabelleSpalte col_Note = add("Note", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaName("AbiturNote")
		.setJavaComment("Gesamt: Die Abschlussnote des Abiturs");

	/** Die Definition der Tabellenspalte GesamtPunktzahl */
	public SchemaTabelleSpalte col_GesamtPunktzahl = add("GesamtPunktzahl", SchemaDatentypen.SMALLINT, false)
		.setJavaName("AbiturGesamtPunktzahl")
		.setJavaComment("Gesamt: Die Gesamtpunktzahl aus beiden Blöcken (Qualifikationsphase und Abiturprüfung)");

	/** Die Definition der Tabellenspalte Notensprung */
	public SchemaTabelleSpalte col_Notensprung = add("Notensprung", SchemaDatentypen.SMALLINT, false)
		.setJavaName("VerbesserungAbPunktzahl")
		.setJavaComment("Gesamt: Die Gesamtpunktzahl, ab der eine höhere Notenstufe erreicht würde");

	/** Die Definition der Tabellenspalte FehlendePunktzahl */
	public SchemaTabelleSpalte col_FehlendePunktzahl = add("FehlendePunktzahl", SchemaDatentypen.SMALLINT, false)
		.setJavaName("VerbesserungBenoetigePunkte")
		.setJavaComment("Gesamt: Die Anzahl an Punkten, die zur nächsthöheren Note fehlt");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("Pruefung_Schuljahr")
		.setJavaComment("Das Schuljahr der Abiturprüfung");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("Pruefung_SchuljahresAbschnitt")
		.setJavaComment("Der Schuljahres-Abschnitt der Abiturprüfung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Nummer der Schule, zu der dieser Datensatz gehört (falls mehrere Schulen mit dem gleichen Schema arbeiten)");


	/** Die Definition des Fremdschlüssels SchuelerAbitur_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAbitur_Schuljahreabschnitt_FK = addForeignKey(
			"SchuelerAbitur_Schuljahreabschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerAbitur_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAbitur_Schueler_FK = addForeignKey(
			"SchuelerAbitur_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/** Die Definition des Unique-Index SchuelerAbitur_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerAbitur_UC1 = addUniqueIndex("SchuelerAbitur_UC1", 
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerAbitur.
	 */
	public Tabelle_SchuelerAbitur() {
		super("SchuelerAbitur", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler.abitur");
		setJavaClassName("DTOSchuelerAbitur");
		setJavaComment("Abiturdaten zum Schüler");
	}

}
