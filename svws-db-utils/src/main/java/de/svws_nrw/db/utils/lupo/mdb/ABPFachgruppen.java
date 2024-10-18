package de.svws_nrw.db.utils.lupo.mdb;

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

import de.svws_nrw.asd.types.fach.Fach;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Fachgruppen aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPFachgruppen {

	private static final String fieldFach = "Fach";
	private static final String fieldBezeichnung = "Bezeichnung";
	private static final String fieldFachgruppeKrz = "FachgruppeKrz";
	private static final String fieldAufgabenfeld = "Aufgabenfeld";
	private static final String fieldSortierung = "Sortierung";


	/** Das Fachkürzel */
	public String FachKuerzel = null;

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
	public static Map<String, ABPFachgruppen> read(final Database db) {
		try {
			final HashMap<String, ABPFachgruppen> zuordnung = new HashMap<>();
			final Table table = db.getTable("ABP_Fachgruppen");
			for (final Row r : table) {
				final ABPFachgruppen fach = new ABPFachgruppen();
				fach.FachKuerzel = r.getString(fieldFach);
				fach.Bezeichnung = r.getString(fieldBezeichnung);
				fach.FachgruppeKrz = r.getString(fieldFachgruppeKrz);
				fach.Aufgabenfeld = (r.getInt(fieldAufgabenfeld) == null) ? 0 : r.getInt(fieldAufgabenfeld);
				fach.Sortierung = (r.getInt(fieldSortierung) == null) ? 0 : r.getInt(fieldSortierung);
				zuordnung.put(fach.FachKuerzel, fach);
			}
			return zuordnung;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyMap();
		}
	}


	/**
	 * Schreibt die angegebenen Fachgruppen in die übergebene Datenbank
	 *
	 * @param db          die zu beschreibende Datenbank
	 * @param zuordnung   die Zuordnung der Fächer zu den Fachgruppen
	 */
	public static void write(final Database db, final Map<String, ABPFachgruppen> zuordnung) {
		try {
			final Table table = new TableBuilder("ABP_Fachgruppen")
					.addColumn(new ColumnBuilder(fieldFach, DataType.TEXT).setLengthInUnits(2))
					.addColumn(new ColumnBuilder(fieldBezeichnung, DataType.TEXT).setLengthInUnits(80))
					.addColumn(new ColumnBuilder(fieldFachgruppeKrz, DataType.TEXT).setLengthInUnits(2))
					.addColumn(new ColumnBuilder(fieldAufgabenfeld, DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
					.addColumn(new ColumnBuilder(fieldSortierung, DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
					.toTable(db);
			for (final ABPFachgruppen fach : zuordnung.values().stream().sorted((fg1, fg2) -> fg1.FachKuerzel.compareToIgnoreCase(fg2.FachKuerzel)).toList()) {
				table.addRow(
						fach.FachKuerzel,
						fach.Bezeichnung,
						fach.FachgruppeKrz,
						fach.Aufgabenfeld,
						fach.Sortierung);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPFachgruppen zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPFachgruppen
	 */
	public static Map<String, ABPFachgruppen> getDefault() {
		return Arrays.stream(Fach.values())
				.filter(f -> f.getFachgruppe(2024) != null)
				.map(f -> {
					final ABPFachgruppen fach = new ABPFachgruppen();
					fach.FachKuerzel = f.daten(2024).schluessel;
					fach.Bezeichnung = f.daten(2024).text;
					fach.FachgruppeKrz = f.getFachgruppe(2024).daten(2024).kuerzel;
					fach.Aufgabenfeld = f.getFachgruppe(2024).daten(2024).nummer;
					return fach;
				}).collect(Collectors.toMap(f -> f.FachKuerzel, f -> f));
	}


	@Override
	public String toString() {
		return "ABPFachgruppen [FachKuerzel=" + FachKuerzel + ", Bezeichnung=" + Bezeichnung + ", FachgruppeKrz=" + FachgruppeKrz
				+ ", Aufgabenfeld=" + Aufgabenfeld + ", Sortierung=" + Sortierung + "]";
	}


}
