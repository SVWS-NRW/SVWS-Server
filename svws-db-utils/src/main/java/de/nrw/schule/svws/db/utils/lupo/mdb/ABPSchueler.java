package de.nrw.schule.svws.db.utils.lupo.mdb;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.nrw.schule.svws.core.data.gost.GostLeistungen;
import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.core.utils.schueler.SprachendatenUtils;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostSchueler;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Schueler aus einer LuPO-Datenbank 
 * im Access-Format genutzt. 
 */
public class ABPSchueler {

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

	/** TODO: Klären */
	public String PruefPhase = null;

	/** TODO: Klären */
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



	/**
	 * Liest alle Einträge der Tabelle "ABP_Schueler" aus der LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Liste der Schüler aus der LuPO-Datei
	 */
	public static List<ABPSchueler> read(Database db) {
		try {
			List<ABPSchueler> liste = new Vector<>();
			Table table = db.getTable("ABP_Schueler");
			for (Row r : table) {
				ABPSchueler schueler = new ABPSchueler(); 
				schueler.ID = r.getInt("ID");
				schueler.Schild_ID = r.getInt("Schild_ID");
				schueler.GU_ID = r.getString("GU_ID");
				schueler.Name = r.getString("Name");
				schueler.Vorname = r.getString("Vorname");
				schueler.Geburtsdatum = r.getLocalDateTime("Geburtsdatum");
				schueler.geschlecht = (r.getByte("Geschlecht") == null) ? Geschlecht.X.id : Geschlecht.fromValue(r.getByte("Geschlecht") == null ? null : (int)r.getByte("Geschlecht")).id;
				schueler.DatumBeratung = r.getLocalDateTime("DatumBeratung");
				schueler.DatumRuecklauf = r.getLocalDateTime("DatumRuecklauf");
				schueler.Klasse = r.getString("Klasse");
				schueler.SPP = "J".equals(r.getString("SPP"));
				schueler.Bilingual = r.getString("Bilingual");
				schueler.Latein = "J".equals(r.getString("Latein"));
				schueler.Sportattest = r.getString("Sportattest");
				schueler.Kommentar = r.getString("Kommentar");
				schueler.PruefOrdnung = r.getString("PruefOrdnung");
				schueler.Email = r.getString("Email");
				schueler.Beratungslehrer = r.getString("Beratungslehrer");
				schueler.AnzK_E1 = r.getInt("AnzK_E1");
				schueler.AnzK_E2 = r.getInt("AnzK_E1");
				schueler.AnzK_Q1 = r.getInt("AnzK_Q2");
				schueler.AnzK_Q2 = r.getInt("AnzK_Q2");
				schueler.AnzK_Q3 = r.getInt("AnzK_Q3");
				schueler.AnzK_Q4 = r.getInt("AnzK_Q4");
				schueler.AnzS_E1 = r.getInt("AnzS_E1");
				schueler.AnzS_E2 = r.getInt("AnzS_E2");
				schueler.AnzS_Q1 = r.getInt("AnzS_Q1");
				schueler.AnzS_Q2 = r.getInt("AnzS_Q2");
				schueler.AnzS_Q3 = r.getInt("AnzS_Q3");
				schueler.AnzS_Q4 = r.getInt("AnzS_Q4");
				schueler.AnzS_Summe = r.getString("AnzS_Summe");
				schueler.AnzK_Summe = r.getString("AnzK_Summe");
				schueler.PruefPhase = r.getString("PruefPhase");
				schueler.Zeitstempel = r.getLocalDateTime("Zeitstempel");
				schueler.Gliederung = r.getString("Gliederung");
				schueler.Konfession = r.getString("Konfession");
				schueler.Einsprachler_S1 = r.getString("Einsprachler_S1") == null ? null : "J".equals(r.getString("Einsprachler_S1"));
				schueler.BLL_Art = r.getString("BLL_Art");
				schueler.Zulassung = r.getString("Zulassung") == null ? null : "J".equals(r.getString("Zulassung"));
				schueler.BLL_Punkte = r.getInt("BLL_Punkte");
				schueler.FS2_SekI_manuell = r.getString("FS2_SekI_manuell") == null ? null : "J".equals(r.getString("FS2_SekI_manuell"));
				liste.add(schueler);
			}
			return liste;
		} catch (@SuppressWarnings("unused") IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Schüler in die übergebene Datenbank
	 * 
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler
	 */
	public static void write(Database db, List<ABPSchueler> list) {
		try {
			Table table = new TableBuilder("ABP_Schueler")
			     .addColumn(new ColumnBuilder("ID", DataType.LONG))
			     .addColumn(new ColumnBuilder("Schild_ID", DataType.LONG))
			     .addColumn(new ColumnBuilder("GU_ID", DataType.TEXT).setLengthInUnits(40))
			     .addColumn(new ColumnBuilder("Name", DataType.TEXT).setLengthInUnits(50))
			     .addColumn(new ColumnBuilder("Vorname", DataType.TEXT).setLengthInUnits(50))
			     .addColumn(new ColumnBuilder("Geburtsdatum", DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder("Geschlecht", DataType.INT).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "4"))
				 .addColumn(new ColumnBuilder("DatumBeratung", DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder("DatumRuecklauf", DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder("Klasse", DataType.TEXT).setLengthInUnits(15))
				 .addColumn(new ColumnBuilder("SPP", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("Bilingual", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("Latein", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("Sportattest", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("Kommentar", DataType.MEMO).setLengthInUnits(16777216))
				 .addColumn(new ColumnBuilder("PruefOrdnung", DataType.TEXT).setLengthInUnits(20))
				 .addColumn(new ColumnBuilder("Email", DataType.TEXT).setLengthInUnits(100))
				 .addColumn(new ColumnBuilder("Beratungslehrer", DataType.TEXT).setLengthInUnits(50))
				 .addColumn(new ColumnBuilder("AnzK_E1", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzK_E2", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzK_Q1", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzK_Q2", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzK_Q3", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzK_Q4", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_E1", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_E2", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_Q1", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_Q2", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_Q3", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_Q4", DataType.LONG))
				 .addColumn(new ColumnBuilder("AnzS_Summe", DataType.TEXT).setLengthInUnits(5))
				 .addColumn(new ColumnBuilder("AnzK_Summe", DataType.TEXT).setLengthInUnits(5))
				 .addColumn(new ColumnBuilder("PruefPhase", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("Zeitstempel", DataType.SHORT_DATE_TIME))
				 .addColumn(new ColumnBuilder("Gliederung", DataType.TEXT).setLengthInUnits(3))
				 .addColumn(new ColumnBuilder("Konfession", DataType.TEXT).setLengthInUnits(2))
				 .addColumn(new ColumnBuilder("Einsprachler_S1", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("BLL_Art", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("Zulassung", DataType.TEXT).setLengthInUnits(1))
				 .addColumn(new ColumnBuilder("BLL_Punkte", DataType.LONG))
				 .addColumn(new ColumnBuilder("FS2_SekI_manuell", DataType.TEXT).setLengthInUnits(1))
			     .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns("ID").setPrimaryKey())
			     .toTable(db);
			for (ABPSchueler schueler: list) {
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
					schueler.SPP ? "J" : "N",
					schueler.Bilingual,
					schueler.Latein ? "J" : "N",
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
					schueler.Einsprachler_S1 == null ? null : (schueler.Einsprachler_S1 ? "J" : "N"),
					schueler.BLL_Art,
					schueler.Zulassung == null ? null : (schueler.Zulassung ? "J" : "N"),
					schueler.BLL_Punkte,
					schueler.FS2_SekI_manuell == null ? null : (schueler.FS2_SekI_manuell ? "J" : "N")
				);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchueler zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPSchueler
	 */
	public static List<ABPSchueler> getDefault() {
		List<ABPSchueler> schuelerliste = new Vector<>();
		return schuelerliste;
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
	public static List<ABPSchueler> get(List<DTOSchueler> schuelerListe, Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte, Map<Long, DTOKlassen> mapKlassen, Map<Long, DTOLehrer> mapLehrer, Map<Long, DTOGostSchueler> schuelerLupoInfo, Map<Long, GostLeistungen> gostInfo) {
		List<ABPSchueler> liste = new Vector<>();
		if (schuelerListe == null)
			return liste;
		for (int i = 0; i < schuelerListe.size(); i++) {
			DTOSchueler schueler = schuelerListe.get(i);
			DTOSchuelerLernabschnittsdaten aktAbschnitt = mapAktAbschnitte.get(schueler.ID);
			DTOGostSchueler lupoSchueler = schuelerLupoInfo.get(schueler.ID);
			GostLeistungen gostLeistungen = gostInfo.get(schueler.ID);
			ABPSchueler eintrag = new ABPSchueler();
			eintrag.ID = i+1;
			eintrag.Schild_ID = schueler.ID.intValue();
			eintrag.GU_ID = schueler.GU_ID;
			eintrag.Name = schueler.Nachname;
			eintrag.Vorname = schueler.Vorname;
			eintrag.Geburtsdatum = LocalDate.parse(schueler.Geburtsdatum).atStartOfDay();
			eintrag.geschlecht = schueler.Geschlecht.id;
			DTOKlassen klasse = mapKlassen.get(aktAbschnitt.Klassen_ID);
			eintrag.Klasse = (klasse == null) ? null : klasse.Klasse;
			eintrag.PruefOrdnung = aktAbschnitt.PruefOrdnung;
			eintrag.Email = schueler.Email;
			if (lupoSchueler != null) {
				eintrag.DatumBeratung = (lupoSchueler.DatumBeratung == null) ? null : LocalDateTime.parse(lupoSchueler.DatumBeratung);
				eintrag.DatumRuecklauf = (lupoSchueler.DatumRuecklauf == null) ? null : LocalDateTime.parse(lupoSchueler.DatumRuecklauf);
				eintrag.SPP = false; // TODO Bestimme über: SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten()), Problem SprachDatenManager nuss zuvor geladen werden...
				eintrag.Sportattest = lupoSchueler.HatSportattest == null ? null : lupoSchueler.HatSportattest ? "J" : "N";
				eintrag.Kommentar = lupoSchueler.Kommentar;
				DTOLehrer beratungslehrer = mapLehrer.get(lupoSchueler.Beratungslehrer_ID);
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
					eintrag.Einsprachler_S1 = !(SprachendatenUtils.hatZweiSprachenMitMin4JahrenDauerEndeSekI(gostLeistungen.sprachendaten) || SprachendatenUtils.hatSpracheMit2JahrenDauerEndeSekI(gostLeistungen.sprachendaten));
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
