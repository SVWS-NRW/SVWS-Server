package de.nrw.schule.svws.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Fachgruppen aus einer LuPO-Datenbank 
 * im Access-Format genutzt. 
 */
public class ABPFachgruppen {

	/** Das Fachkürzel */
	public String Fach = null;
	
	/** Die textuelle Bezeichnung des Faches */
	public String Bezeichnung = null;

	/** 
	 * Das Fachgruppenkürzel der zugehörigen Fachgruppe:
	 *   FS: Fremdsprachen
	 *   AL: Arbeitslehre
	 *   D: Deutsch
	 *   GS: Gesellschaftswissenschaften
	 *   M: Mathematik
	 *   ME: musische Ersatzfächer
	 *   MS: musische Fächer
	 *   NW: klass. Naturwiss.
	 *   PL: Politik
	 *   RE: Religion
	 *   SP: Sport
	 *   WN: Weitere Naturwiss.
	 */
	public String FachgruppeKrz = null;

	/** Das Aufgabenfeld dem das Fach zugeordnet ist. */
	public int Aufgabenfeld = 0;

	/** Die Sortier-Reihenfolge des Faches in der Darstellung */
	public int Sortierung = 0;

	
	/**
	 * Liest alle Einträge der Tabelle "ABP_Fachgruppen" aus der LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Map mit den ABPFachgruppen aus der LuPO-Datei
	 */
	public static Map<String, ABPFachgruppen> read(Database db) {
		try {
			HashMap<String, ABPFachgruppen> zuordnung = new HashMap<>();
			Table table = db.getTable("ABP_Fachgruppen");
			for (Row r : table) {
				ABPFachgruppen fach = new ABPFachgruppen();
				fach.Fach = r.getString("Fach");
				fach.Bezeichnung = r.getString("Bezeichnung");
				fach.FachgruppeKrz = r.getString("FachgruppeKrz");
				fach.Aufgabenfeld = (r.getInt("Aufgabenfeld") == null) ? 0 : r.getInt("Aufgabenfeld");
				fach.Sortierung = (r.getInt("Sortierung") == null) ? 0 : r.getInt("Sortierung");
				zuordnung.put(fach.Fach, fach);
			}
			return zuordnung;
		} catch (IOException e) {
			return Collections.emptyMap();
		}
	}
	
	
	/**
	 * Schreibt die angegebenen Fachgruppen in die übergebene Datenbank
	 * 
	 * @param db          die zu beschreibende Datenbank
	 * @param zuordnung   die Zuordnung der Fächer zu den Fachgruppen
	 */
	public static void write(Database db, Map<String, ABPFachgruppen> zuordnung) {
		try {
			Table table = new TableBuilder("ABP_Fachgruppen")
			     .addColumn(new ColumnBuilder("Fach", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("Bezeichnung", DataType.TEXT).setLengthInUnits(80))
			     .addColumn(new ColumnBuilder("FachgruppeKrz", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("Aufgabenfeld", DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
			     .addColumn(new ColumnBuilder("Sortierung", DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
			     .toTable(db);
			for (ABPFachgruppen fach: zuordnung.values().stream().sorted((fg1, fg2) -> fg1.Fach.compareToIgnoreCase(fg2.Fach)).collect(Collectors.toList())) {
				table.addRow(
					fach.Fach,	
					fach.Bezeichnung,
					fach.FachgruppeKrz,
					fach.Aufgabenfeld,
					fach.Sortierung
				);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPFachgruppen zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPFachgruppen
	 */
	public static Map<String, ABPFachgruppen> getDefault() {
		return Arrays.stream(ZulaessigesFach.values())
				.filter(f -> f.getFachgruppe() != null)
				.map(f -> {
					ABPFachgruppen fach = new ABPFachgruppen();
					fach.Fach = f.daten.kuerzelASD;
					fach.Bezeichnung = f.daten.bezeichnung;
					fach.FachgruppeKrz = f.getFachgruppe().daten.kuerzel;
					fach.Aufgabenfeld = f.getFachgruppe().daten.nummer;
					return fach;
				}).collect(Collectors.toMap(f -> f.Fach, f -> f));
	}


	@Override
	public String toString() {
		return "ABPFachgruppen [Fach=" + Fach + ", Bezeichnung=" + Bezeichnung + ", FachgruppeKrz=" + FachgruppeKrz
				+ ", Aufgabenfeld=" + Aufgabenfeld + ", Sortierung=" + Sortierung + "]";
	}


}
