package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_SchuelerFaecherBasisSicherung aus einer 
 * LuPO-Datenbank im Access-Format genutzt. 
 */
public class ABPSchuelerFaecherBasisSicherung {

	/** Eine laufende ID der Zuordnung von Fächern zum Schüler */
	public int ID = -1;
	
	/** Die LuPO-Schüler-ID */
	public int Schueler_ID = -1;
	
	/** Die ID ds zugeordneten Faches */
	public int Fach_ID = -1;
	
	/** Das Kürzel des zugeordneten Faches */
	public String FachKrz;
	
	/** Gibt an, ab welchem Jahrgang die Fremdsprache belegt wurde. */
	public String FS_BeginnJg = null; 
	
	/** Gibt an, als wievielte Fremdsprache eine Fremdsprache gewählt wurde. */
	public String Sprachenfolge = null;

	/** Gibt die Kursart des gewählten Faches in der EF im 1. Halbjahr an */
	public String Kursart_E1 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der EF im 1. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_E1 = null;

	/** Gibt die Kursart des gewählten Faches in der EF im 2. Halbjahr an */
	public String Kursart_E2 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der EF im 2. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_E2 = null;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 1. Halbjahr an */
	public String Kursart_Q1 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 1. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q1 = null;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 2. Halbjahr an */
	public String Kursart_Q2 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 2. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q2 = null;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 3. Halbjahr an */
	public String Kursart_Q3 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 3. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q3 = null;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 4. Halbjahr an */
	public String Kursart_Q4 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 4. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q4 = null;
	
	/** Gibt an, ob das Fach als 1., 2., 3., 4. Abiturfach oder nicht als Abiturfach gewählt wurde. */
	public Integer AbiturFach = null;
	
	/** Die Sortierung des Faches in der Wahl */
	public int Sortierung = 32000;

	/** Die Fachgruppe des Faches */
	public String Fachgruppe = null;

	/** Das Aufgabenfeld des Faches */
	public int Aufgabenfeld = -1;

	
	/**
	 * Liest alle Einträge der Tabelle "ABP_SchuelerFaecherBasisSicherung" aus der 
	 * LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Liste der Fächerzuordnungen der Schüler aus der LuPO-Datei
	 */
	public static List<ABPSchuelerFaecherBasisSicherung> read(Database db) {
		try {
			List<ABPSchuelerFaecherBasisSicherung> liste = new Vector<>();
			Table table = db.getTable("ABP_SchuelerFaecherBasisSicherung");
			for (Row r : table) {
				ABPSchuelerFaecherBasisSicherung zuordnung = new ABPSchuelerFaecherBasisSicherung();
				zuordnung.ID = r.getInt("ID");
				zuordnung.Schueler_ID = r.getInt("Schueler_ID");
				zuordnung.Fach_ID = r.getInt("Fach_ID");
				zuordnung.FachKrz = r.getString("FachKrz");
				zuordnung.FS_BeginnJg = r.getString("FS_BeginnJg"); 
				zuordnung.Sprachenfolge = r.getString("Sprachenfolge");
				zuordnung.Kursart_E1 = r.getString("Kursart_E1");
				zuordnung.Punkte_E1 = r.getString("Punkte_E1");
				zuordnung.Kursart_E2 = r.getString("Kursart_E2");
				zuordnung.Punkte_E2 = r.getString("Punkte_E2");
				zuordnung.Kursart_Q1 = r.getString("Kursart_Q1");
				zuordnung.Punkte_Q1 = r.getString("Punkte_Q1");
				zuordnung.Kursart_Q2 = r.getString("Kursart_Q2");
				zuordnung.Punkte_Q2 = r.getString("Punkte_Q2");
				zuordnung.Kursart_Q3 = r.getString("Kursart_Q3");
				zuordnung.Punkte_Q3 = r.getString("Punkte_Q3");
				zuordnung.Kursart_Q4 = r.getString("Kursart_Q4");
				zuordnung.Punkte_Q4 = r.getString("Punkte_Q4");
				zuordnung.AbiturFach = r.getInt("AbiturFach");
				zuordnung.Sortierung = (r.getInt("Sortierung") == null) ? 32000 : r.getInt("Sortierung");
				zuordnung.Fachgruppe = r.getString("Fachgruppe");
				zuordnung.Aufgabenfeld = (r.getInt("Aufgabenfeld") == null) ? -1 : r.getInt("Aufgabenfeld");
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Schüler-Fächer-Zuordnungen in die übergebene Datenbank.
	 * 
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler-Fächer-Zuordnung
	 */
	public static void write(Database db, List<ABPSchuelerFaecherBasisSicherung> list) {
		try {
			Table table = new TableBuilder("ABP_SchuelerFaecherBasisSicherung")
				.addColumn(new ColumnBuilder("ID", DataType.LONG))
				.addColumn(new ColumnBuilder("Schueler_ID", DataType.LONG))
				.addColumn(new ColumnBuilder("Fach_ID", DataType.LONG))
				.addColumn(new ColumnBuilder("FachKrz", DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder("FS_BeginnJg", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("Sprachenfolge", DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder("Kursart_E1", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Punkte_E1", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("Kursart_E2", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Punkte_E2", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("Kursart_Q1", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Punkte_Q1", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("Kursart_Q2", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Punkte_Q2", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("Kursart_Q3", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Punkte_Q3", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("Kursart_Q4", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Punkte_Q4", DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder("AbiturFach", DataType.LONG))
				.addColumn(new ColumnBuilder("Sortierung", DataType.LONG))
				.addColumn(new ColumnBuilder("Fachgruppe", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Aufgabenfeld", DataType.LONG))
			    .toTable(db);
			if (list == null)
				return;
			for (ABPSchuelerFaecherBasisSicherung zuordnung: list) {
				table.addRow(
					zuordnung.ID,
					zuordnung.Schueler_ID,
					zuordnung.Fach_ID,
					zuordnung.FachKrz,
					zuordnung.FS_BeginnJg, 
					zuordnung.Sprachenfolge,
					zuordnung.Kursart_E1,
					zuordnung.Punkte_E1,
					zuordnung.Kursart_E2,
					zuordnung.Punkte_E2,
					zuordnung.Kursart_Q1,
					zuordnung.Punkte_Q1,
					zuordnung.Kursart_Q2,
					zuordnung.Punkte_Q2,
					zuordnung.Kursart_Q3,
					zuordnung.Punkte_Q3,
					zuordnung.Kursart_Q4,
					zuordnung.Punkte_Q4,
					zuordnung.AbiturFach,
					zuordnung.Sortierung,
					zuordnung.Fachgruppe,
					zuordnung.Aufgabenfeld
				);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuelerFaecherBasisSicherung zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPSchuelerFaecherBasisSicherung
	 */
	public static List<ABPSchuelerFaecherBasisSicherung> getDefault() {
		List<ABPSchuelerFaecherBasisSicherung> faecherBasisSicherung = new Vector<>();
		return faecherBasisSicherung;
	}


	@Override
	public String toString() {
		return "ABPSchuelerFaecherBasisSicherung [ID=" + ID + ", Schueler_ID=" + Schueler_ID + ", Fach_ID=" + Fach_ID
				+ ", FachKrz=" + FachKrz + ", FS_BeginnJg=" + FS_BeginnJg + ", Sprachenfolge=" + Sprachenfolge
				+ ", Kursart_E1=" + Kursart_E1 + ", Punkte_E1=" + Punkte_E1 + ", Kursart_E2=" + Kursart_E2
				+ ", Punkte_E2=" + Punkte_E2 + ", Kursart_Q1=" + Kursart_Q1 + ", Punkte_Q1=" + Punkte_Q1
				+ ", Kursart_Q2=" + Kursart_Q2 + ", Punkte_Q2=" + Punkte_Q2 + ", Kursart_Q3=" + Kursart_Q3
				+ ", Punkte_Q3=" + Punkte_Q3 + ", Kursart_Q4=" + Kursart_Q4 + ", Punkte_Q4=" + Punkte_Q4
				+ ", AbiturFach=" + AbiturFach + ", Sortierung=" + Sortierung + ", Fachgruppe=" + Fachgruppe
				+ ", Aufgabenfeld=" + Aufgabenfeld + "]";
	}

}
