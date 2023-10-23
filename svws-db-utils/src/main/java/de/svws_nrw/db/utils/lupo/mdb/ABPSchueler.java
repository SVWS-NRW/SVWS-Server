package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Schueler aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPSchueler {

	/** Die ID des Schülers in der LuPO-DB */
	public int ID = -1;

	/** Die ID des Schülers in der SVWS-Datenbank */
	public Integer Schild_ID = null;

	/** Die GU_ID des Schülers in der SVWS-Datenbank */
	public String GU_ID = null;

	/** Der Nachname des Schülers */
	public String Name = null;

	/** Der Vorname des Schülers */
	public String Vorname = null;

	/** Das Geburtsdatum des Schülers */
	public LocalDateTime Geburtsdatum = null;

	/** Das Geschlecht des Schülers */
	public int geschlecht = Geschlecht.X.id;

	/** Das letzte Beratungsdatum */
	public LocalDateTime DatumBeratung = null;

	/** Das Datum des Rücklaufs der Beratungsdaten durch den Schüler */
	public LocalDateTime DatumRuecklauf = null;

	/** Die Klasse des Schülers */
	public String Klasse = null;

	/** Gibt an, ob eine Muttersprachliche Prüfung am Ende der EF beabsichtigt ist bzw. bestanden ist */
	public boolean SPP = false;

	/** Das Fachkürzel des bilingualen Sprachfaches */
	public String Bilingual = null;

	/** Gibt an, ob Latein belegt wurde */
	public boolean Latein = false;

	/** Gibt an, ob ein Sprtattest vorliegt oder nicht */
	public String Sportattest = null;

	/** Ein Kommentar in LuPO zu dem Schüler */
	public String Kommentar = null;

	/** Die Prüfungsordnund, die dem Schüler zugeordnet ist */
	public String PruefOrdnung = null;

	/** Die Email-Adresse des Schülers */
	public String Email = null;

	/** Der Beratungslehrer, der die letzte Beratung durchgeführt hat. */
	public String Beratungslehrer = null;

	/** Die Anzahl der Kurse in der EF 1. Halbjahr */
	public Integer AnzK_E1 = null;

	/** Die Anzahl der Kurse in der EF 2. Halbjahr */
	public Integer AnzK_E2 = null;

	/** Die Anzahl der Kurse in der Q-Phase 1. Halbjahr */
	public Integer AnzK_Q1 = null;

	/** Die Anzahl der Kurse in der Q-Phase 2. Halbjahr */
	public Integer AnzK_Q2 = null;

	/** Die Anzahl der Kurse in der Q-Phase 3. Halbjahr */
	public Integer AnzK_Q3 = null;

	/** Die Anzahl der Kurse in der Q-Phase 4. Halbjahr */
	public Integer AnzK_Q4 = null;

	/** Die Anzahl der Wochenstunden in der EF 1. Halbjahr */
	public Integer AnzS_E1 = null;

	/** Die Anzahl der Wochenstunden in der EF 2. Halbjahr */
	public Integer AnzS_E2 = null;

	/** Die Anzahl der Wochenstunden in der Q-Phase 1. Halbjahr */
	public Integer AnzS_Q1 = null;

	/** Die Anzahl der Wochenstunden in der Q-Phase 2. Halbjahr */
	public Integer AnzS_Q2 = null;

	/** Die Anzahl der Wochenstunden in der Q-Phase 3. Halbjahr */
	public Integer AnzS_Q3 = null;

	/** Die Anzahl der Wochenstunden in der Q-Phase 4. Halbjahr */
	public Integer AnzS_Q4 = null;

	/** Die Anzahl der Wochenstunden in der Summe */
	public String AnzS_Summe = null;

	/** Die Anzahl der Kurse in der Summe */
	public String AnzK_Summe = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht mehr genutzt */
	public String PruefPhase = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht mehr genutzt */
	public LocalDateTime Zeitstempel = null;

	/** Die Gliederung */
	public String Gliederung = null;

	/** Die Konfession des Schülers. */
	public String Konfession = null;

	/** Gibt an, ob der Schüler in der Sekundarstufe 1 nur eine Sprache belegt hatte */
	public Boolean Einsprachler_S1 = null;

	/** Gibt die Art einer Besonderen Lernleistung an, sofern diese vorhanden ist */
	public String BLL_Art = null;

	/** Gibt an, ob der Schüler die Zulassung erreicht hat oder (noch) nicht. */
	public Boolean Zulassung = null;

	/** Gibt die Punkte an, die bei der besonderen Lernleistung erreicht wurden. */
	public Integer BLL_Punkte = null;

	/** Gibt an, ob die zweite Fremdsprache in der Sekundarstufe I manuell überprüft wurde */
	public Boolean FS2_SekI_manuell = null;


    private static final String fieldID = "ID";
    private static final String fieldSchild_ID = "Schild_ID";
    private static final String fieldGU_ID = "GU_ID";
    private static final String fieldName = "Name";
    private static final String fieldVorname = "Vorname";
    private static final String fieldGeburtsdatum = "Geburtsdatum";
    private static final String fieldGeschlecht = "Geschlecht";
    private static final String fieldDatumBeratung = "DatumBeratung";
    private static final String fieldDatumRuecklauf = "DatumRuecklauf";
    private static final String fieldKlasse = "Klasse";
    private static final String fieldSPP = "SPP";
    private static final String fieldBilingual = "Bilingual";
    private static final String fieldLatein = "Latein";
    private static final String fieldSportattest = "Sportattest";
    private static final String fieldKommentar = "Kommentar";
    private static final String fieldPruefOrdnung = "PruefOrdnung";
    private static final String fieldEmail = "Email";
    private static final String fieldBeratungslehrer = "Beratungslehrer";
    private static final String fieldAnzK_E1 = "AnzK_E1";
    private static final String fieldAnzK_E2 = "AnzK_E2";
    private static final String fieldAnzK_Q1 = "AnzK_Q1";
    private static final String fieldAnzK_Q2 = "AnzK_Q2";
    private static final String fieldAnzK_Q3 = "AnzK_Q3";
    private static final String fieldAnzK_Q4 = "AnzK_Q4";
    private static final String fieldAnzS_E1 = "AnzS_E1";
    private static final String fieldAnzS_E2 = "AnzS_E2";
    private static final String fieldAnzS_Q1 = "AnzS_Q1";
    private static final String fieldAnzS_Q2 = "AnzS_Q2";
    private static final String fieldAnzS_Q3 = "AnzS_Q3";
    private static final String fieldAnzS_Q4 = "AnzS_Q4";
    private static final String fieldAnzS_Summe = "AnzS_Summe";
    private static final String fieldAnzK_Summe = "AnzK_Summe";
    private static final String fieldPruefPhase = "PruefPhase";
    private static final String fieldZeitstempel = "Zeitstempel";
    private static final String fieldGliederung = "Gliederung";
    private static final String fieldKonfession = "Konfession";
    private static final String fieldEinsprachler_S1 = "Einsprachler_S1";
    private static final String fieldBLL_Art = "BLL_Art";
    private static final String fieldZulassung = "Zulassung";
    private static final String fieldBLL_Punkte = "BLL_Punkte";
    private static final String fieldFS2_SekI_manuell = "FS2_SekI_manuell";


	/**
	 * Liest alle Einträge der Tabelle "ABP_Schueler" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der Schüler aus der LuPO-Datei
	 */
	public static List<ABPSchueler> read(final Database db) {
		try {
			final List<ABPSchueler> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_Schueler");
			for (final Row r : table) {
				final ABPSchueler schueler = new ABPSchueler();
				schueler.ID = r.getInt(fieldID);
				schueler.Schild_ID = r.getInt(fieldSchild_ID);
				schueler.GU_ID = r.getString(fieldGU_ID);
				schueler.Name = r.getString(fieldName);
				schueler.Vorname = r.getString(fieldVorname);
				schueler.Geburtsdatum = r.getLocalDateTime(fieldGeburtsdatum);
				if (r.getByte(fieldGeschlecht) == null)
					schueler.geschlecht = Geschlecht.X.id;
				else
					schueler.geschlecht = Geschlecht.fromValue((int) r.getByte(fieldGeschlecht)).id;
				schueler.DatumBeratung = r.getLocalDateTime(fieldDatumBeratung);
				schueler.DatumRuecklauf = r.getLocalDateTime(fieldDatumRuecklauf);
				schueler.Klasse = r.getString(fieldKlasse);
				schueler.SPP = "J".equals(r.getString(fieldSPP));
				schueler.Bilingual = r.getString(fieldBilingual);
				schueler.Latein = "J".equals(r.getString(fieldLatein));
				schueler.Sportattest = r.getString(fieldSportattest);
				schueler.Kommentar = r.getString(fieldKommentar);
				schueler.PruefOrdnung = r.getString(fieldPruefOrdnung);
				schueler.Email = r.getString(fieldEmail);
				schueler.Beratungslehrer = r.getString(fieldBeratungslehrer);
				schueler.AnzK_E1 = r.getInt(fieldAnzK_E1);
				schueler.AnzK_E2 = r.getInt(fieldAnzK_E2);
				schueler.AnzK_Q1 = r.getInt(fieldAnzK_Q1);
				schueler.AnzK_Q2 = r.getInt(fieldAnzK_Q2);
				schueler.AnzK_Q3 = r.getInt(fieldAnzK_Q3);
				schueler.AnzK_Q4 = r.getInt(fieldAnzK_Q4);
				schueler.AnzS_E1 = r.getInt(fieldAnzS_E1);
				schueler.AnzS_E2 = r.getInt(fieldAnzS_E2);
				schueler.AnzS_Q1 = r.getInt(fieldAnzS_Q1);
				schueler.AnzS_Q2 = r.getInt(fieldAnzS_Q2);
				schueler.AnzS_Q3 = r.getInt(fieldAnzS_Q3);
				schueler.AnzS_Q4 = r.getInt(fieldAnzS_Q4);
				schueler.AnzS_Summe = r.getString(fieldAnzS_Summe);
				schueler.AnzK_Summe = r.getString(fieldAnzK_Summe);
				schueler.PruefPhase = r.getString(fieldPruefPhase);
				schueler.Zeitstempel = r.getLocalDateTime(fieldZeitstempel);
				schueler.Gliederung = r.getString(fieldGliederung);
				schueler.Konfession = r.getString(fieldKonfession);
				schueler.Einsprachler_S1 = r.getString(fieldEinsprachler_S1) == null ? null : "J".equals(r.getString(fieldEinsprachler_S1));
				schueler.BLL_Art = r.getString(fieldBLL_Art);
				schueler.Zulassung = r.getString(fieldZulassung) == null ? null : "J".equals(r.getString(fieldZulassung));
				schueler.BLL_Punkte = r.getInt(fieldBLL_Punkte);
				schueler.FS2_SekI_manuell = r.getString(fieldFS2_SekI_manuell) == null ? null : "J".equals(r.getString(fieldFS2_SekI_manuell));
				liste.add(schueler);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	private static String toStringJN(final boolean value) {
		return value ? "J" : "N";
	}


	/**
	 * Schreibt die angegebenen Schüler in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler
	 */
	public static void write(final Database db, final List<ABPSchueler> list) {
		try {
			final Table table = new TableBuilder("ABP_Schueler")
			     .addColumn(new ColumnBuilder(fieldID, DataType.LONG))
			     .addColumn(new ColumnBuilder(fieldSchild_ID, DataType.LONG))
			     .addColumn(new ColumnBuilder(fieldGU_ID, DataType.TEXT).setLengthInUnits(40))
			     .addColumn(new ColumnBuilder(fieldName, DataType.TEXT).setLengthInUnits(50))
			     .addColumn(new ColumnBuilder(fieldVorname, DataType.TEXT).setLengthInUnits(50))
			     .addColumn(new ColumnBuilder(fieldGeburtsdatum, DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder(fieldGeschlecht, DataType.INT).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "4"))
				 .addColumn(new ColumnBuilder(fieldDatumBeratung, DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder(fieldDatumRuecklauf, DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder(fieldKlasse, DataType.TEXT).setLengthInUnits(15))
				 .addColumn(new ColumnBuilder(fieldSPP, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldBilingual, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldLatein, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldSportattest, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldKommentar, DataType.MEMO).setLengthInUnits(16777216))
				 .addColumn(new ColumnBuilder(fieldPruefOrdnung, DataType.TEXT).setLengthInUnits(20))
				 .addColumn(new ColumnBuilder(fieldEmail, DataType.TEXT).setLengthInUnits(100))
				 .addColumn(new ColumnBuilder(fieldBeratungslehrer, DataType.TEXT).setLengthInUnits(50))
				 .addColumn(new ColumnBuilder(fieldAnzK_E1, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzK_E2, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzK_Q1, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzK_Q2, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzK_Q3, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzK_Q4, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_E1, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_E2, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_Q1, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_Q2, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_Q3, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_Q4, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldAnzS_Summe, DataType.TEXT).setLengthInUnits(5))
				 .addColumn(new ColumnBuilder(fieldAnzK_Summe, DataType.TEXT).setLengthInUnits(5))
				 .addColumn(new ColumnBuilder(fieldPruefPhase, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldZeitstempel, DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder(fieldGliederung, DataType.TEXT).setLengthInUnits(3))
				 .addColumn(new ColumnBuilder(fieldKonfession, DataType.TEXT).setLengthInUnits(2))
				 .addColumn(new ColumnBuilder(fieldEinsprachler_S1, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldBLL_Art, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldZulassung, DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder(fieldBLL_Punkte, DataType.LONG))
				 .addColumn(new ColumnBuilder(fieldFS2_SekI_manuell, DataType.TEXT).setLengthInUnits(1))
			     .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns(fieldID).setPrimaryKey())
			     .toTable(db);
			for (final ABPSchueler schueler: list) {
				table.addRow(
					schueler.ID,
					schueler.Schild_ID,
					schueler.GU_ID,
					schueler.Name,
					schueler.Vorname,
					schueler.Geburtsdatum,
					schueler.geschlecht,
					schueler.DatumBeratung,
					schueler.DatumRuecklauf,
					schueler.Klasse,
					toStringJN(schueler.SPP),
					schueler.Bilingual,
					toStringJN(schueler.Latein),
					schueler.Sportattest,
					schueler.Kommentar,
					schueler.PruefOrdnung,
					schueler.Email,
					schueler.Beratungslehrer,
					schueler.AnzK_E1,
					schueler.AnzK_E2,
					schueler.AnzK_Q1,
					schueler.AnzK_Q2,
					schueler.AnzK_Q3,
					schueler.AnzK_Q4,
					schueler.AnzS_E1,
					schueler.AnzS_E2,
					schueler.AnzS_Q1,
					schueler.AnzS_Q2,
					schueler.AnzS_Q3,
					schueler.AnzS_Q4,
					schueler.AnzS_Summe,
					schueler.AnzK_Summe,
					schueler.PruefPhase,
					schueler.Zeitstempel,
					schueler.Gliederung,
					schueler.Konfession,
					schueler.Einsprachler_S1 == null ? null : toStringJN(schueler.Einsprachler_S1),
					schueler.BLL_Art,
					schueler.Zulassung == null ? null : toStringJN(schueler.Zulassung),
					schueler.BLL_Punkte,
					schueler.FS2_SekI_manuell == null ? null : toStringJN(schueler.FS2_SekI_manuell)
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchueler zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPSchueler
	 */
	public static List<ABPSchueler> getDefault() {
		return new ArrayList<>();
	}


	/**
	 * Erstellt die Einträge für die Tabelle ABP_Schueler aus den DTOs
	 * der SVWS-Server-Datenbank.
	 *
	 * @param schuelerListe      die SVWS-Server-DTOs für die Schüler
	 * @param mapAktAbschnitte   die SVWS-Server-DTOs für die aktuellen Lernabschnitte des Schülers
	 * @param mapKlassen         die SVWS-Server-DTOs für die Klassen
	 * @param mapLehrer          die SVWS-Server-DTOs für die Lehrer
	 * @param schuelerLupoInfo   die LuPO-Information zu dem Schüler, die in der
	 *                           SVWS-Datenbank hinterlegt sind.
	 * @param gostInfo           die Leistungen des Schülers in der gymnasialen Oberstufe
	 *
	 * @return die Liste der Einträge für die Tabelle ABP_Schueler
	 */
	public static List<ABPSchueler> get(final List<DTOSchueler> schuelerListe, final Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte, final Map<Long, DTOKlassen> mapKlassen, final Map<Long, DTOLehrer> mapLehrer, final Map<Long, DTOGostSchueler> schuelerLupoInfo, final Map<Long, GostLeistungen> gostInfo) {
		final List<ABPSchueler> liste = new ArrayList<>();
		if (schuelerListe == null)
			return liste;
		for (int i = 0; i < schuelerListe.size(); i++) {
			final DTOSchueler schueler = schuelerListe.get(i);
			final DTOSchuelerLernabschnittsdaten aktAbschnitt = mapAktAbschnitte.get(schueler.ID);
			final DTOGostSchueler lupoSchueler = schuelerLupoInfo.get(schueler.ID);
			final GostLeistungen gostLeistungen = gostInfo.get(schueler.ID);
			final ABPSchueler eintrag = new ABPSchueler();
			eintrag.ID = i + 1;
			eintrag.Schild_ID = (int) schueler.ID;
			eintrag.GU_ID = schueler.GU_ID;
			eintrag.Name = schueler.Nachname;
			eintrag.Vorname = schueler.Vorname;
			eintrag.Geburtsdatum = LocalDate.parse(schueler.Geburtsdatum).atStartOfDay();
			eintrag.geschlecht = schueler.Geschlecht.id;
			final DTOKlassen klasse = mapKlassen.get(aktAbschnitt.Klassen_ID);
			eintrag.Klasse = (klasse == null) ? null : klasse.Klasse;
			eintrag.PruefOrdnung = aktAbschnitt.PruefOrdnung;
			eintrag.Email = schueler.Email;
			if (lupoSchueler != null) {
				eintrag.DatumBeratung = (lupoSchueler.DatumBeratung == null) ? null : LocalDateTime.parse(lupoSchueler.DatumBeratung);
				eintrag.DatumRuecklauf = (lupoSchueler.DatumRuecklauf == null) ? null : LocalDateTime.parse(lupoSchueler.DatumRuecklauf);
				eintrag.SPP = false; // TODO Bestimme über: SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten()), Problem SprachDatenManager nuss zuvor geladen werden...
				eintrag.Sportattest = lupoSchueler.HatSportattest == null ? null : toStringJN(lupoSchueler.HatSportattest);
				eintrag.Kommentar = lupoSchueler.Kommentar;
				final DTOLehrer beratungslehrer = mapLehrer.get(lupoSchueler.Beratungslehrer_ID);
				eintrag.Beratungslehrer = beratungslehrer == null ? null : (beratungslehrer.Nachname + ", " + beratungslehrer.Vorname);
				eintrag.PruefPhase = lupoSchueler.PruefPhase;
				eintrag.BLL_Art = lupoSchueler.BesondereLernleistung_Art;
				eintrag.BLL_Punkte = lupoSchueler.BesondereLernleistung_Punkte;
				eintrag.FS2_SekI_manuell = false;
				eintrag.Gliederung = "***";   // TODO from SchülerAbschnittsdaten
			}
			eintrag.Konfession = null; // TODO from schueler.Religion_ID;
			if (gostLeistungen != null) {
				eintrag.Bilingual = gostLeistungen.bilingualeSprache;
				if (gostLeistungen.sprachendaten != null) {
					eintrag.Latein = SprachendatenUtils.hatSprachbelegungInSekI(gostLeistungen.sprachendaten, "L");
					eintrag.Einsprachler_S1 = !(SprachendatenUtils.hatZweiSprachenAb5Bis7MitMin4JahrenDauerEndeSekI(gostLeistungen.sprachendaten) || SprachendatenUtils.hatEineSpracheAb8MitMin2JahrenDauerEndeSekI(gostLeistungen.sprachendaten));
				}
			}
			liste.add(eintrag);
		}
		return liste;
	}


	@Override
	public String toString() {
		return "ABPSchueler [ID=" + ID + ", Schild_ID=" + Schild_ID + ", GU_ID=" + GU_ID + ", Name=" + Name
				+ ", Vorname=" + Vorname + ", Geburtsdatum=" + Geburtsdatum + ", Geschlecht=" + geschlecht
				+ ", DatumBeratung=" + DatumBeratung + ", DatumRuecklauf=" + DatumRuecklauf + ", Klasse=" + Klasse
				+ ", SPP=" + SPP + ", Bilingual=" + Bilingual + ", Latein=" + Latein + ", Sportattest=" + Sportattest
				+ ", Kommentar=" + Kommentar + ", PruefOrdnung=" + PruefOrdnung + ", Email=" + Email
				+ ", Beratungslehrer=" + Beratungslehrer + ", AnzK_E1=" + AnzK_E1 + ", AnzK_E2=" + AnzK_E2
				+ ", AnzK_Q1=" + AnzK_Q1 + ", AnzK_Q2=" + AnzK_Q2 + ", AnzK_Q3=" + AnzK_Q3 + ", AnzK_Q4=" + AnzK_Q4
				+ ", AnzS_E1=" + AnzS_E1 + ", AnzS_E2=" + AnzS_E2 + ", AnzS_Q1=" + AnzS_Q1 + ", AnzS_Q2=" + AnzS_Q2
				+ ", AnzS_Q3=" + AnzS_Q3 + ", AnzS_Q4=" + AnzS_Q4 + ", AnzS_Summe=" + AnzS_Summe + ", AnzK_Summe="
				+ AnzK_Summe + ", PruefPhase=" + PruefPhase + ", Zeitstempel=" + Zeitstempel + ", Gliederung="
				+ Gliederung + ", Konfession=" + Konfession + ", Einsprachler_S1=" + Einsprachler_S1 + ", BLL_Art="
				+ BLL_Art + ", Zulassung=" + Zulassung + ", BLL_Punkte=" + BLL_Punkte + ", FS2_SekI_manuell="
				+ FS2_SekI_manuell + "]";
	}

}
