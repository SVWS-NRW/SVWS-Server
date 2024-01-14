package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Faecher aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPFaecher {

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

	/** Deprecated: Wird in der Laufbahnplanung nicht genutzt */
	public String E_ExportKursart = null;

	/** Deprecated: Wird in der Laufbahnplanung nicht genutzt */
	public boolean NurMuendlich = false;


    private static final String fieldID = "ID";
    private static final String fieldFachKrz = "FachKrz";
    private static final String fieldBezeichnung = "Bezeichnung";
    private static final String fieldStatistikKrz = "StatistikKrz";
    private static final String fieldSortierung = "Sortierung";
    private static final String fieldIstSprache = "IstSprache";
	private static final String fieldUnterichtssprache = "Unterichtssprache";
	private static final String fieldE1 = "E1";
	private static final String fieldE2 = "E2";
	private static final String fieldQ1 = "Q1";
	private static final String fieldQ2 = "Q2";
	private static final String fieldQ3 = "Q3";
	private static final String fieldQ4 = "Q4";
	private static final String fieldAbi_Moegl = "Abi_Moegl";
	private static final String fieldLK_Moegl = "LK_Moegl";
	private static final String fieldAlsNeueFSInSII = "AlsNeueFSInSII";
	private static final String fieldLeitfach = "Leitfach";
	private static final String fieldLeitfach2 = "Leitfach2";
	private static final String fieldE1_WStd = "E1_WStd";
	private static final String fieldE2_WStd = "E2_WStd";
	private static final String fieldE1_S_M = "E1_S_M";
	private static final String fieldE2_S_M = "E2_S_M";
	private static final String fieldQ_WStd = "Q_WStd";
	private static final String fieldE_ExportKursart = "E_ExportKursart";
	private static final String fieldNurMuendlich = "NurMuendlich";


	/**
	 * Liest alle Einträge der Tabelle "ABP_Faecher" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Map der Fächer aus der LuPO-Datei
	 */
	public static Map<String, ABPFaecher> read(final Database db) {
		try {
			final HashMap<String, ABPFaecher> map = new HashMap<>();
			final Table table = db.getTable("ABP_Faecher");
			for (final Row r : table) {
				final ABPFaecher fach = new ABPFaecher();
				fach.ID = r.getInt(fieldID);
				fach.FachKrz = r.getString(fieldFachKrz);
				fach.Bezeichnung = r.getString(fieldBezeichnung);
				fach.StatistikKrz = r.getString(fieldStatistikKrz);
				fach.Sortierung = (r.getInt(fieldSortierung) == null) ? Integer.MAX_VALUE : r.getInt(fieldSortierung);
				fach.IstSprache = "J".equals(r.getString(fieldIstSprache));
				fach.Unterichtssprache = r.getString(fieldUnterichtssprache) == null ? "D" : r.getString(fieldUnterichtssprache);
				fach.E1 = "J".equals(r.getString(fieldE1));
				fach.E2 = "J".equals(r.getString(fieldE2));
				fach.Q1 = "J".equals(r.getString(fieldQ1));
				fach.Q2 = "J".equals(r.getString(fieldQ2));
				fach.Q3 = "J".equals(r.getString(fieldQ3));
				fach.Q4 = "J".equals(r.getString(fieldQ4));
				fach.Abi_Moegl = "J".equals(r.getString(fieldAbi_Moegl));
				fach.LK_Moegl = "J".equals(r.getString(fieldLK_Moegl));
				fach.AlsNeueFSInSII = "J".equals(r.getString(fieldAlsNeueFSInSII));
				fach.Leitfach = r.getString(fieldLeitfach);
				fach.Leitfach2 = r.getString(fieldLeitfach2);
				fach.E1_WStd = r.getInt(fieldE1_WStd);
				fach.E2_WStd = r.getInt(fieldE2_WStd);
				fach.E1_S_M = "J".equals(r.getString(fieldE1_S_M));
				fach.E2_S_M = "J".equals(r.getString(fieldE2_S_M));
				fach.Q_WStd = r.getInt(fieldQ_WStd);
				fach.E_ExportKursart = r.getString(fieldE_ExportKursart);
				fach.NurMuendlich = "J".equals(r.getString(fieldNurMuendlich));
				map.put(fach.FachKrz, fach);
			}
			return map;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyMap();
		}
	}


	private static String toStringJN(final boolean value) {
		return value ? "J" : "N";
	}


	/**
	 * Schreibt die angegebenen Fächer in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param map    die zu schreibenden Fächer
	 */
	public static void write(final Database db, final Map<String, ABPFaecher> map) {
		try {
			final Table table = new TableBuilder("ABP_Faecher")
			     .addColumn(new ColumnBuilder(fieldID, DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
			     .addColumn(new ColumnBuilder(fieldFachKrz, DataType.TEXT).setLengthInUnits(20))
			     .addColumn(new ColumnBuilder(fieldBezeichnung, DataType.TEXT).setLengthInUnits(80))
			     .addColumn(new ColumnBuilder(fieldStatistikKrz, DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder(fieldSortierung, DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "32000"))
			     .addColumn(new ColumnBuilder(fieldIstSprache, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'N'"))
			     .addColumn(new ColumnBuilder(fieldUnterichtssprache, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'D'"))
			     .addColumn(new ColumnBuilder(fieldE1, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldE2, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldQ1, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldQ2, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldQ3, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldQ4, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldAbi_Moegl, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldLK_Moegl, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
			     .addColumn(new ColumnBuilder(fieldAlsNeueFSInSII, DataType.TEXT).setLengthInUnits(1))
			     .addColumn(new ColumnBuilder(fieldLeitfach, DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder(fieldLeitfach2, DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder(fieldE1_WStd, DataType.LONG))
			     .addColumn(new ColumnBuilder(fieldE2_WStd, DataType.LONG))
			     .addColumn(new ColumnBuilder(fieldE1_S_M, DataType.TEXT).setLengthInUnits(1))
			     .addColumn(new ColumnBuilder(fieldE2_S_M, DataType.TEXT).setLengthInUnits(1))
			     .addColumn(new ColumnBuilder(fieldQ_WStd, DataType.LONG))
			     .addColumn(new ColumnBuilder(fieldE_ExportKursart, DataType.TEXT).setLengthInUnits(5))
			     .addColumn(new ColumnBuilder(fieldNurMuendlich, DataType.TEXT).setLengthInUnits(1))
			     .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns(fieldFachKrz).setPrimaryKey())
			     .toTable(db);
			map.values().stream().sorted((f1, f2) -> Integer.compare(f1.ID, f2.ID)).forEach(fach -> {
				try {
					table.addRow(
						fach.ID,
						fach.FachKrz,
						fach.Bezeichnung,
						fach.StatistikKrz,
						fach.Sortierung,
						toStringJN(fach.IstSprache),
						fach.Unterichtssprache == null ? "D" : fach.Unterichtssprache,
						toStringJN(fach.E1),
						toStringJN(fach.E2),
						toStringJN(fach.Q1),
						toStringJN(fach.Q2),
						toStringJN(fach.Q3),
						toStringJN(fach.Q4),
						toStringJN(fach.Abi_Moegl),
						toStringJN(fach.LK_Moegl),
						toStringJN(fach.AlsNeueFSInSII),
						fach.Leitfach,
						fach.Leitfach2,
						fach.E1_WStd,
						fach.E2_WStd,
						toStringJN(fach.E1_S_M),
						toStringJN(fach.E2_S_M),
						fach.Q_WStd,
						fach.E_ExportKursart,
						toStringJN(fach.NurMuendlich)
					);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			});
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPFaecher zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPFaecher
	 */
	public static Map<String, ABPFaecher> getDefault() {
		return new HashMap<>();
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
	public static Map<String, ABPFaecher> get(final Map<String, ABPFachgruppen> fachgruppen, final List<DTOFach> faecher, final Map<Long, DTOFach> faecherMap) {
		final HashMap<String, ABPFaecher> lupoFaecher = new HashMap<>();
		if (faecher == null)
			return lupoFaecher;

		// Filtere alle Fächer, für die keine Fachgruppe definiert ist
		final List<DTOFach> faecherGefiltert = faecher.stream()
			.filter(fach -> ((fach.Sichtbar == null) || fach.Sichtbar) && (fachgruppen.get(fach.StatistikFach.daten.kuerzelASD) != null))
			.toList();
		for (int i = 0; i < faecherGefiltert.size(); i++) {
			final DTOFach fach = faecherGefiltert.get(i);
			final ABPFaecher lupofach = new ABPFaecher();
			lupofach.ID = i + 1;
			lupofach.FachKrz = fach.Kuerzel;
			lupofach.Bezeichnung = fach.Bezeichnung;
			lupofach.StatistikKrz = fach.StatistikFach.daten.kuerzelASD;
			lupofach.Sortierung = fach.SortierungAllg;
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
			final DTOFach lf = faecherMap.get(fach.ProjektKursLeitfach1_ID);
			lupofach.Leitfach = lf == null ? null : lf.StatistikFach.daten.kuerzelASD;
			final DTOFach lf2 = faecherMap.get(fach.ProjektKursLeitfach2_ID);
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
