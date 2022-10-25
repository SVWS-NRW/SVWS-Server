package de.nrw.schule.svws.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
public class ABPKursarten {

	/** Gibt die Art des Kurses an z.B. GKM oder GKS oder LK */
	@JsonProperty public String Kursart = null;

	/** Gibt die Art des Kurses im Klartext an. */
	@JsonProperty public String Klartext = null;

	/** Gibt an, ob der Kurs im ersten Halbjahr der EF belegt wurde. */
	@JsonProperty public boolean E1 = false;

	/** Gibt an, ob der Kurs im zweiten Halbjahr der EF belegt wurde. */
	@JsonProperty public boolean E2 = false;
	
	/** Gibt an, ob der Kurs im ersten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty public boolean Q1 = false;

	/** Gibt an, ob der Kurs im zweiten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty public boolean Q2 = false;

	/** Gibt an, ob der Kurs im dritten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty public boolean Q3 = false;

	/** Gibt an, ob der Kurs im vierten Halbjahr der Q-Phase belegt wurde. */
	@JsonProperty public boolean Q4 = false;
	
	/** Die Sortierung der Kursart */
	@JsonProperty public int Sortierung = 32000;


	/**
	 * Liest alle Einträge der Tabelle "ABP_Kursarten" aus der LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Liste der Kursarten aus der LuPO-Datei
	 */
	public static List<ABPKursarten> read(Database db) {
		try {
			List<ABPKursarten> liste = new Vector<>();
			Table table = db.getTable("ABP_Kursarten");
			for (Row r : table) {
				ABPKursarten zuordnung = new ABPKursarten();
				zuordnung.Kursart = r.getString("Kursart");
				zuordnung.Klartext = r.getString("Klartext");
				zuordnung.E1 = "J".equals(r.getString("E1"));
				zuordnung.E2 = "J".equals(r.getString("E2"));
				zuordnung.Q1 = "J".equals(r.getString("Q1"));
				zuordnung.Q2 = "J".equals(r.getString("Q2"));
				zuordnung.Q3 = "J".equals(r.getString("Q3"));
				zuordnung.Q4 = "J".equals(r.getString("Q4"));
				zuordnung.Sortierung = (r.getInt("Sortierung") == null) ? 32000 : r.getInt("Sortierung");
				liste.add(zuordnung);
			}
			return liste;
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Kursarten in die übergebene Datenbank
	 * 
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Kursarten
	 */
	public static void write(Database db, List<ABPKursarten> list) {
		try {
			Table table = new TableBuilder("ABP_Kursarten")
				.addColumn(new ColumnBuilder("Kursart", DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder("Klartext", DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder("E1", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("E2", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("Q1", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("Q2", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("Q3", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("Q4", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("Sortierung", DataType.LONG))
			    .toTable(db);
			for (ABPKursarten zuordnung: list) {
				table.addRow(
					zuordnung.Kursart,
					zuordnung.Klartext,
					zuordnung.E1 ? "J" : "N",
					zuordnung.E2 ? "J" : "N",
					zuordnung.Q1 ? "J" : "N",
					zuordnung.Q2 ? "J" : "N",
					zuordnung.Q3 ? "J" : "N",
					zuordnung.Q4 ? "J" : "N",
					zuordnung.Sortierung
				);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPKursarten zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPKursarten
	 */
	public static List<ABPKursarten> getDefault() {
	    ABPKursarten gkm =  new ABPKursarten();
	    gkm.Kursart = "GKM";
	    gkm.Klartext = "Grundkurs mündlich";
	    gkm.E1 = true;
	    gkm.E2 = true;
	    gkm.Q1 = true;
        gkm.Q2 = true;
        gkm.Q3 = true;
        gkm.Q4 = true;
        gkm.Sortierung = 1;
        ABPKursarten gks =  new ABPKursarten();
        gks.Kursart = "GKS";
        gks.Klartext = "Grundkurs schriftlich";
        gks.E1 = true;
        gks.E2 = true;
        gks.Q1 = true;
        gks.Q2 = true;
        gks.Q3 = true;
        gks.Q4 = true;
        gks.Sortierung = 2;
        ABPKursarten lk =  new ABPKursarten();
        lk.Kursart = "LK";
        lk.Klartext = "Leistungskurs";
        lk.E1 = false;
        lk.E2 = false;
        lk.Q1 = true;
        lk.Q2 = true;
        lk.Q3 = true;
        lk.Q4 = true;
        lk.Sortierung = 3;
        Vector<ABPKursarten> result = new Vector<>();
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
