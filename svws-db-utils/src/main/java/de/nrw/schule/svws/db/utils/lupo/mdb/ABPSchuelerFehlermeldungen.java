package de.nrw.schule.svws.db.utils.lupo.mdb;

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
 * Diese Klasse wird für den Import der Tabelle ABP_SchuelerFehlermeldungen aus 
 * einer LuPO-Datenbank im Access-Format genutzt. 
 */
public class ABPSchuelerFehlermeldungen {

	/** Die ID des Schülers in der LuPO-DB */
	public int Schueler_ID = -1;
	
	/** Der Fehlercode */
	public String Fehlercode = null;
	
	/** Der ausführliche Fehlertext */
	public String Fehlertext = null;
	
	/** 
	 * Die Gruppe, zu der der Fehlercode gehört: 
	 *   BV: Belegungsverpflichtung;  
	 *   KV: Klausurverpflichtung; 
	 *   KA: Kursanzahl; 
	 *   AB: Abitur; 
	 *   SP: Spezielle Meldungen 
	 */
	public String Fehlergruppe = null;
	
	/** Die Sortierung der Fehlermeldung */
	public Integer Sortierung = 32000;



	/**
	 * Liest alle Einträge der Tabelle "ABP_Schueler" aus der LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Liste der Schüler aus der LuPO-Datei
	 */
	public static List<ABPSchuelerFehlermeldungen> read(Database db) {
		try {
			List<ABPSchuelerFehlermeldungen> liste = new Vector<>();
			Table table = db.getTable("ABP_SchuelerFehlermeldungen");
			for (Row r : table) {
				ABPSchuelerFehlermeldungen fehler = new ABPSchuelerFehlermeldungen(); 
				fehler.Schueler_ID = r.getInt("Schueler_ID");
				fehler.Fehlercode = r.getString("Fehlercode");
				fehler.Fehlertext = r.getString("Fehlertext");
				fehler.Fehlergruppe = r.getString("Fehlergruppe");
				fehler.Sortierung = r.getInt("Sortierung");
				liste.add(fehler);
			}
			return liste;
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Schüler in die übergebene Datenbank
	 * 
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler
	 */
	public static void write(Database db, List<ABPSchuelerFehlermeldungen> list) {
		try {
			Table table = new TableBuilder("ABP_SchuelerFehlermeldungen")
			     .addColumn(new ColumnBuilder("Schueler_ID", DataType.LONG))
			     .addColumn(new ColumnBuilder("Fehlercode", DataType.TEXT).setLengthInUnits(20))
			     .addColumn(new ColumnBuilder("Fehlertext", DataType.MEMO).setLengthInUnits(16777216))
			     .addColumn(new ColumnBuilder("Fehlergruppe", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("Sortierung", DataType.LONG))
			     .toTable(db);
			if (list == null)
				return;
			for (ABPSchuelerFehlermeldungen schueler: list) {
				table.addRow(
					schueler.Schueler_ID,
					schueler.Fehlercode,
					schueler.Fehlertext,
					schueler.Fehlergruppe,
					schueler.Sortierung
				);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuelerFehlermeldungen zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPSchuelerFehlermeldungen
	 */
	public static List<ABPSchuelerFehlermeldungen> getDefault() {
		List<ABPSchuelerFehlermeldungen> fehlermeldungen = new Vector<>();
		return fehlermeldungen;
	}


	@Override
	public String toString() {
		return "ABPSchuelerFehlermeldungen [Schueler_ID=" + Schueler_ID + ", Fehlercode=" + Fehlercode + ", Fehlertext="
				+ Fehlertext + ", Fehlergruppe=" + Fehlergruppe + ", Sortierung=" + Sortierung + "]";
	}

}
