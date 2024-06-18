package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Kursarten aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPKursarten {

	/** Gibt die Art des Kurses an z.B. GKM oder GKS oder LK */
	@JsonProperty
	public String Kursart = null;

	/** Gibt die Art des Kurses im Klartext an. */
	@JsonProperty
	public String Klartext = null;

	/** Gibt an, ob der Kurs im ersten Halbjahr der EF belegt wurde. */
	@JsonProperty
	public boolean E1 = false;

	/** Gibt an, ob der Kurs im zweiten Halbjahr der EF belegt wurde. */
	@JsonProperty
	public boolean E2 = false;

	/** Gibt an, ob der Kurs im ersten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty
	public boolean Q1 = false;

	/** Gibt an, ob der Kurs im zweiten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty
	public boolean Q2 = false;

	/** Gibt an, ob der Kurs im dritten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty
	public boolean Q3 = false;

	/** Gibt an, ob der Kurs im vierten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty
	public boolean Q4 = false;

	/** Die Sortierung der Kursart */
	@JsonProperty
	public int Sortierung = 32000;


	private static final String fieldKursart = "Kursart";
	private static final String fieldKlartext = "Klartext";
	private static final String fieldE1 = "E1";
	private static final String fieldE2 = "E2";
	private static final String fieldQ1 = "Q1";
	private static final String fieldQ2 = "Q2";
	private static final String fieldQ3 = "Q3";
	private static final String fieldQ4 = "Q4";
	private static final String fieldSortierung = "Sortierung";


	/**
	 * Liest alle Einträge der Tabelle "ABP_Kursarten" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der Kursarten aus der LuPO-Datei
	 */
	public static List<ABPKursarten> read(final Database db) {
		try {
			final List<ABPKursarten> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_Kursarten");
			for (final Row r : table) {
				final ABPKursarten zuordnung = new ABPKursarten();
				zuordnung.Kursart = r.getString(fieldKursart);
				zuordnung.Klartext = r.getString(fieldKlartext);
				zuordnung.E1 = "J".equals(r.getString(fieldE1));
				zuordnung.E2 = "J".equals(r.getString(fieldE2));
				zuordnung.Q1 = "J".equals(r.getString(fieldQ1));
				zuordnung.Q2 = "J".equals(r.getString(fieldQ2));
				zuordnung.Q3 = "J".equals(r.getString(fieldQ3));
				zuordnung.Q4 = "J".equals(r.getString(fieldQ4));
				zuordnung.Sortierung = (r.getInt(fieldSortierung) == null) ? 32000 : r.getInt(fieldSortierung);
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Kursarten in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Kursarten
	 */
	public static void write(final Database db, final List<ABPKursarten> list) {
		try {
			final Table table = new TableBuilder("ABP_Kursarten")
					.addColumn(new ColumnBuilder(fieldKursart, DataType.TEXT).setLengthInUnits(5))
					.addColumn(new ColumnBuilder(fieldKlartext, DataType.TEXT).setLengthInUnits(50))
					.addColumn(new ColumnBuilder(fieldE1, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
					.addColumn(new ColumnBuilder(fieldE2, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
					.addColumn(new ColumnBuilder(fieldQ1, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
					.addColumn(new ColumnBuilder(fieldQ2, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
					.addColumn(new ColumnBuilder(fieldQ3, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
					.addColumn(new ColumnBuilder("Q4", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
					.addColumn(new ColumnBuilder(fieldSortierung, DataType.LONG))
					.toTable(db);
			for (final ABPKursarten zuordnung : list) {
				table.addRow(
						zuordnung.Kursart,
						zuordnung.Klartext,
						zuordnung.E1 ? "J" : "N",
						zuordnung.E2 ? "J" : "N",
						zuordnung.Q1 ? "J" : "N",
						zuordnung.Q2 ? "J" : "N",
						zuordnung.Q3 ? "J" : "N",
						zuordnung.Q4 ? "J" : "N",
						zuordnung.Sortierung);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPKursarten zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPKursarten
	 */
	public static List<ABPKursarten> getDefault() {
		final ABPKursarten gkm = new ABPKursarten();
		gkm.Kursart = "GKM";
		gkm.Klartext = "Grundkurs mündlich";
		gkm.E1 = true;
		gkm.E2 = true;
		gkm.Q1 = true;
		gkm.Q2 = true;
		gkm.Q3 = true;
		gkm.Q4 = true;
		gkm.Sortierung = 1;
		final ABPKursarten gks = new ABPKursarten();
		gks.Kursart = "GKS";
		gks.Klartext = "Grundkurs schriftlich";
		gks.E1 = true;
		gks.E2 = true;
		gks.Q1 = true;
		gks.Q2 = true;
		gks.Q3 = true;
		gks.Q4 = true;
		gks.Sortierung = 2;
		final ABPKursarten lk = new ABPKursarten();
		lk.Kursart = "LK";
		lk.Klartext = "Leistungskurs";
		lk.E1 = false;
		lk.E2 = false;
		lk.Q1 = true;
		lk.Q2 = true;
		lk.Q3 = true;
		lk.Q4 = true;
		lk.Sortierung = 3;
		final ArrayList<ABPKursarten> result = new ArrayList<>();
		result.add(gkm);
		result.add(gks);
		result.add(lk);
		return result;
	}


	@Override
	public String toString() {
		return "ABPKursarten [Kursart=" + Kursart + ", Klartext=" + Klartext + ", E1=" + E1 + ", E2=" + E2 + ", Q1="
				+ Q1 + ", Q2=" + Q2 + ", Q3=" + Q3 + ", Q4=" + Q4 + ", Sortierung=" + Sortierung + "]";
	}


}
