package de.nrw.schule.svws.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Faecher aus einer LuPO-Datenbank 
 * im Access-Format genutzt. 
 */
public class ABPFaecher {

	/** Die ID des Faches */
	public int ID;

	/** Das interne Kürzel des Faches */
	public String FachKrz = null;

	/** Die textuelle Bezeichnung des Faches */
	public String Bezeichnung = null;

	/** Das Statistik-Kürzel des Faches */
	public String StatistikKrz = null;

	/** Die Sortier-Reihenfolge des Faches in der Darstellung */
	public int Sortierung = Integer.MAX_VALUE;

	/** Gibt an, ob es sich um eine Fremdsprache handelt oder nicht */
	public boolean IstSprache = false;

	/** Gibt die Unterrichtssprache des Faches an */
	public String Unterichtssprache = "D";

	/** Gibt an, ob das Fach im 1. Halbjahr der EF unterrichtet wird und wählbar ist */
	public boolean E1 = false;

	/** Gibt an, ob das Fach im 2. Halbjahr der EF unterrichtet wird und wählbar ist */
	public boolean E2 = false;

	/** Gibt an, ob das Fach im 1. Halbjahr der Q-Phase unterrichtet wird und wählbar ist */
	public boolean Q1 = false;

	/** Gibt an, ob das Fach im 2. Halbjahr der Q-Phase unterrichtet wird und wählbar ist */
	public boolean Q2 = false;

	/** Gibt an, ob das Fach im 3. Halbjahr der Q-Phase unterrichtet wird und wählbar ist */
	public boolean Q3 = false;

	/** Gibt an, ob das Fach im 4. Halbjahr der Q-Phase unterrichtet wird und wählbar ist */
	public boolean Q4 = false;

	/** Gibt an, ob das Fach als 3./4. Fach im Abitur gewählt werden kann */
	public boolean Abi_Moegl = false;
	
	/** Gibt an, ob das Fach als 1./2. Fach im Abitur gewählt werden kann */
	public boolean LK_Moegl = false;
	
	/** Gibt an, ob das Fach als neu einsetzen Fremdsprache in der Oberstufe gewählt werden kann */
	public boolean AlsNeueFSInSII = false;
	
	/** Gibt das 1. Leitfach eines Projektkurses oder das Fach eines Vertiefungskurses an. */
	public String Leitfach = null;
	
	/** Gibt das 2. Leitfach eines Projektkurses an. */
	public String Leitfach2 = null;	

	/** Gibt die Anzahl der Wochenstunden für das 1. Halbjahr in der Einführungsphase an. */
	public Integer E1_WStd = null;

	/** Gibt die Anzahl der Wochenstunden für das 2. Halbjahr in der Einführungsphase an. */
	public Integer E2_WStd = null;

	/** Gibt an, ob das Fach in dem 1. Halbjahr der Einführungsphase schriftlich belegt werden muss. */
	public boolean E1_S_M = false;

	/** Gibt an, ob das Fach in dem 2. Halbjahr der Einführungsphase schriftlich belegt werden muss.  */
	public boolean E2_S_M = false;

	/** Gibt die Anzahl der Wochenstunden für die Qualifikationsphase an. */
	public Integer Q_WStd = null;

	/** TODO ???  */
	public String E_ExportKursart = null;

	/** TODO ???  */
	public boolean NurMuendlich = false;
	
	/**
	 * Liest alle Einträge der Tabelle "ABP_Faecher" aus der LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Map der Fächer aus der LuPO-Datei
	 */
	public static Map<String, ABPFaecher> read(Database db) {
		try {
			HashMap<String, ABPFaecher> map = new HashMap<>();
			Table table = db.getTable("ABP_Faecher");
			for (Row r : table) {
				ABPFaecher fach = new ABPFaecher(); 
				fach.ID = r.getInt("ID");
				fach.FachKrz = r.getString("FachKrz");
				fach.Bezeichnung = r.getString("Bezeichnung");
				fach.StatistikKrz = r.getString("StatistikKrz");
				fach.Sortierung = (r.getInt("Sortierung") == null) ? Integer.MAX_VALUE : r.getInt("Sortierung");
				fach.IstSprache = "J".equals(r.getString("IstSprache"));
				fach.Unterichtssprache = r.getString("Unterichtssprache") == null ? "D" : r.getString("Unterichtssprache");
				fach.E1 = "J".equals(r.getString("E1"));
				fach.E2 = "J".equals(r.getString("E2"));
				fach.Q1 = "J".equals(r.getString("Q1"));
				fach.Q2 = "J".equals(r.getString("Q2"));
				fach.Q3 = "J".equals(r.getString("Q3"));
				fach.Q4 = "J".equals(r.getString("Q4"));
				fach.Abi_Moegl = "J".equals(r.getString("Abi_Moegl"));
				fach.LK_Moegl = "J".equals(r.getString("LK_Moegl"));
				fach.AlsNeueFSInSII = "J".equals(r.getString("AlsNeueFSInSII"));
				fach.Leitfach = r.getString("Leitfach");
				fach.Leitfach2 = r.getString("Leitfach2");
				fach.E1_WStd = r.getInt("E1_WStd");
				fach.E2_WStd = r.getInt("E2_WStd");
				fach.E1_S_M = "J".equals(r.getString("E1_S_M"));
				fach.E2_S_M = "J".equals(r.getString("E2_S_M"));
				fach.Q_WStd = r.getInt("Q_WStd");
				fach.E_ExportKursart = r.getString("E_ExportKursart");
				fach.NurMuendlich = "J".equals(r.getString("NurMuendlich"));
				map.put(fach.FachKrz, fach);
			}
			return map;
		} catch (IOException e) {
			return Collections.emptyMap();
		}
	}
	
	
	/**
	 * Schreibt die angegebenen Fächer in die übergebene Datenbank
	 * 
	 * @param db     die zu beschreibende Datenbank
	 * @param map    die zu schreibenden Fächer
	 */
	public static void write(Database db, Map<String, ABPFaecher> map) {
		try {
			Table table = new TableBuilder("ABP_Faecher")
			     .addColumn(new ColumnBuilder("ID", DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
			     .addColumn(new ColumnBuilder("FachKrz", DataType.TEXT).setLengthInUnits(20))
			     .addColumn(new ColumnBuilder("Bezeichnung", DataType.TEXT).setLengthInUnits(80))
			     .addColumn(new ColumnBuilder("StatistikKrz", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("Sortierung", DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "32000"))
			     .addColumn(new ColumnBuilder("IstSprache", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'N'"))
			     .addColumn(new ColumnBuilder("Unterichtssprache", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'D'"))
			     .addColumn(new ColumnBuilder("E1", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("E2", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("Q1", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("Q2", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("Q3", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("Q4", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("Abi_Moegl", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("LK_Moegl", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder("AlsNeueFSInSII", DataType.TEXT).setLengthInUnits(1))
			     .addColumn(new ColumnBuilder("Leitfach", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("Leitfach2", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("E1_WStd", DataType.LONG))
			     .addColumn(new ColumnBuilder("E2_WStd", DataType.LONG))
			     .addColumn(new ColumnBuilder("E1_S_M", DataType.TEXT).setLengthInUnits(1))
			     .addColumn(new ColumnBuilder("E2_S_M", DataType.TEXT).setLengthInUnits(1))
			     .addColumn(new ColumnBuilder("Q_WStd", DataType.LONG))
			     .addColumn(new ColumnBuilder("E_ExportKursart", DataType.TEXT).setLengthInUnits(5))
			     .addColumn(new ColumnBuilder("NurMuendlich", DataType.TEXT).setLengthInUnits(1))
			     .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns("FachKrz").setPrimaryKey())
			     .toTable(db);
			map.values().stream().sorted((f1, f2) -> Integer.compare(f1.ID, f2.ID)).forEach(fach -> {
				try {
					table.addRow(
						fach.ID,
						fach.FachKrz,
						fach.Bezeichnung,
						fach.StatistikKrz,
						fach.Sortierung,
						fach.IstSprache ? "J" : "N",
						fach.Unterichtssprache == null ? "D" : fach.Unterichtssprache,
						fach.E1 ? "J" : "N",
						fach.E2 ? "J" : "N",
						fach.Q1 ? "J" : "N",
						fach.Q2 ? "J" : "N",
						fach.Q3 ? "J" : "N",
						fach.Q4 ? "J" : "N",
						fach.Abi_Moegl ? "J" : "N",
						fach.LK_Moegl ? "J" : "N",
						fach.AlsNeueFSInSII ? "J" : "N",
						fach.Leitfach,
						fach.Leitfach2,
						fach.E1_WStd,
						fach.E2_WStd,
						fach.E1_S_M ? "J" : "N",
						fach.E2_S_M ? "J" : "N",
						fach.Q_WStd,
						fach.E_ExportKursart,
						fach.NurMuendlich ? "J" : "N"
					);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPFaecher zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPFaecher
	 */
	public static Map<String, ABPFaecher> getDefault() {
		HashMap<String, ABPFaecher> faecher = new HashMap<>();
		return faecher;
	}


	/**
	 * Erstellt die Einträge für die Tabelle ABP_Faecher aus dem DTO
	 * der SVWS-Server-Datenbank.
	 * 
	 * @param fachgruppen   die Facherguppen für LuPO, wird ggf. zum Filtern von Fächern aus dem SVWS-Server verwendet 
	 * @param faecher       die SVWS-Server-DTOs für die Fächer
	 * @param faecherMap    die SVWS-Server-DTOs für die Fächer, jeweils ihrer ID zugeordnet
	 * 
	 * @return die Map der Einträge für die Tabelle ABP_Faecher, als Schlüssel wird das Fachkürzel vwerdnet
	 */
	public static Map<String, ABPFaecher> get(Map<String, ABPFachgruppen> fachgruppen, List<DTOFach> faecher, Map<Long, DTOFach> faecherMap) {
		HashMap<String, ABPFaecher> lupoFaecher = new HashMap<>();
		if (faecher == null)
			return lupoFaecher;
		
		// Filtere alle Fächer, für die keine Fachgruppe definiert ist
		List<DTOFach> faecherGefiltert = faecher.stream()
			.filter(fach -> ((fach.Sichtbar == null) || fach.Sichtbar) && (fachgruppen.get(fach.StatistikFach.daten.kuerzelASD) != null))
			.collect(Collectors.toList());
		for (int i = 0; i < faecherGefiltert.size(); i++) {
			DTOFach fach = faecherGefiltert.get(i);
			ABPFaecher lupofach = new ABPFaecher();
			lupofach.ID = i+1;
			lupofach.FachKrz = fach.Kuerzel;
			lupofach.Bezeichnung = fach.Bezeichnung;
			lupofach.StatistikKrz = fach.StatistikFach.daten.kuerzelASD;
			lupofach.Sortierung = fach.SortierungSekII;
			lupofach.IstSprache = fach.IstFremdsprache && (!"PX".equalsIgnoreCase(fach.StatistikFach.daten.kuerzelASD)) && (!"VX".equalsIgnoreCase(fach.StatistikFach.daten.kuerzelASD)); 
			lupofach.Unterichtssprache = fach.Unterichtssprache == null ? "D" : fach.Unterichtssprache;
			lupofach.E1 = fach.IstMoeglichEF1;
			lupofach.E2 = fach.IstMoeglichEF2;
			lupofach.Q1 = fach.IstMoeglichQ11;
			lupofach.Q2 = fach.IstMoeglichQ12;
			lupofach.Q3 = fach.IstMoeglichQ21;
			lupofach.Q4 = fach.IstMoeglichQ22;
			lupofach.Abi_Moegl = fach.IstMoeglichAbiGK;
			lupofach.LK_Moegl = fach.IstMoeglichAbiLK;
			lupofach.AlsNeueFSInSII = fach.IstMoeglichAlsNeueFremdspracheInSekII;
			DTOFach lf = faecherMap.get(fach.ProjektKursLeitfach1_ID);
			lupofach.Leitfach = lf == null ? null : lf.StatistikFach.daten.kuerzelASD;
			DTOFach lf2 = faecherMap.get(fach.ProjektKursLeitfach2_ID);
			lupofach.Leitfach2 = lf2 == null ? null : lf2.StatistikFach.daten.kuerzelASD;
			lupofach.E1_WStd = fach.WochenstundenEF1;
			lupofach.E2_WStd = fach.WochenstundenEF2;
			lupofach.E1_S_M = fach.MussSchriftlichEF1;
			lupofach.E2_S_M = fach.MussSchriftlichEF2;
			lupofach.Q_WStd = fach.WochenstundenQualifikationsphase;
			lupofach.E_ExportKursart = null;
			lupofach.NurMuendlich = false;
			lupoFaecher.put(lupofach.FachKrz, lupofach);
		}
		return lupoFaecher;
	}
	
	@Override
	public String toString() {
		return "ABPFaecher [ID=" + ID + ", FachKrz=" + FachKrz + ", Bezeichnung=" + Bezeichnung + ", StatistikKrz="
				+ StatistikKrz + ", Sortierung=" + Sortierung + ", IstSprache=" + IstSprache + ", Unterichtssprache="
				+ Unterichtssprache + ", E1=" + E1 + ", E2=" + E2 + ", Q1=" + Q1 + ", Q2=" + Q2 + ", Q3=" + Q3 + ", Q4="
				+ Q4 + ", Abi_Moegl=" + Abi_Moegl + ", LK_Moegl=" + LK_Moegl + ", AlsNeueFSInSII=" + AlsNeueFSInSII
				+ ", Leitfach=" + Leitfach + ", Leitfach2=" + Leitfach2 + "]";
	}

}
