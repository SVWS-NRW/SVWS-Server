package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_SchuelerSprachenfolge aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPSchuelerSprachenfolge {

	/** Die ID des Schülers in der SVWS-Datenbank */
	public int Schueler_ID = -1;

	/** Das interne Kürzel des Faches */
	public String FachKrz = null;

	/** Der Jahrgang, ab dem die Sprache belegt wurde. */
	public Short JahrgangVon = null;

	/** Der Jahrgang, bis zu dem die Sprache belegt wurde. */
	public Short JahrgangBis = null;

	/** Die Sprachreihenfolge (erste, zweite, ... Fremdsprache */
	public String Reihenfolge = null;

	/** Der Abschnitt im Schuljahr, ab dem die Sprache belegt wurde. */
	public Short AbschnittVon = null;

	/** Der Abschnitt im Schuljahr, bis zu dem die Sprache belegt wurde. */
	public Short AbschnittBis = null;

	/** Das Statistik-Kürzel des Faches */
	public String StatistikKrz = null;


	private static final String fieldSchueler_ID = "Schueler_ID";
	private static final String fieldFachKrz = "FachKrz";
	private static final String fieldJahrgangVon = "JahrgangVon";
	private static final String fieldJahrgangBis = "JahrgangBis";
	private static final String fieldReihenfolge = "Reihenfolge";
	private static final String fieldAbschnittVon = "AbschnittVon";
	private static final String fieldAbschnittBis = "AbschnittBis";
	private static final String fieldStatistikKrz = "StatistikKrz";


	/**
	 * Liest alle Einträge der Tabelle "ABP_SchuelerSprachenfolge" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der Fächerzuordnungen der Schüler aus der LuPO-Datei
	 */
	public static List<ABPSchuelerSprachenfolge> read(final Database db) {
		try {
			final List<ABPSchuelerSprachenfolge> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_SchuelerSprachenfolge");
			for (final Row r : table) {
				final ABPSchuelerSprachenfolge zuordnung = new ABPSchuelerSprachenfolge();
				zuordnung.Schueler_ID = r.getInt(fieldSchueler_ID);
				zuordnung.FachKrz = r.getString(fieldFachKrz);
				zuordnung.JahrgangVon = r.getShort(fieldJahrgangVon);
				zuordnung.JahrgangBis = r.getShort(fieldJahrgangBis);
				zuordnung.Reihenfolge = r.getString(fieldReihenfolge);
				zuordnung.AbschnittVon = r.getShort(fieldAbschnittVon);
				zuordnung.AbschnittBis = r.getShort(fieldAbschnittBis);
				zuordnung.StatistikKrz = r.getString(fieldStatistikKrz);
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Schüler-Sprachenfolge in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler-Sprachenfolge
	 */
	public static void write(final Database db, final List<ABPSchuelerSprachenfolge> list) {
		try {
			final Table table = new TableBuilder("ABP_SchuelerSprachenfolge")
				.addColumn(new ColumnBuilder(fieldSchueler_ID, DataType.LONG).putProperty(PropertyMap.REQUIRED_PROP, DataType.BOOLEAN, true))
				.addColumn(new ColumnBuilder(fieldFachKrz, DataType.TEXT).setLengthInUnits(20).putProperty(PropertyMap.REQUIRED_PROP, DataType.BOOLEAN, true))
				.addColumn(new ColumnBuilder(fieldJahrgangVon, DataType.INT))
				.addColumn(new ColumnBuilder(fieldJahrgangBis, DataType.INT))
				.addColumn(new ColumnBuilder(fieldReihenfolge, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAbschnittVon, DataType.INT))
				.addColumn(new ColumnBuilder(fieldAbschnittBis, DataType.INT))
				.addColumn(new ColumnBuilder(fieldStatistikKrz, DataType.TEXT).setLengthInUnits(2))
			    .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns(fieldSchueler_ID, fieldFachKrz).setPrimaryKey())
			    .toTable(db);
			if (list == null)
				return;
			for (final ABPSchuelerSprachenfolge zuordnung: list) {
				table.addRow(
					zuordnung.Schueler_ID,
					zuordnung.FachKrz,
					zuordnung.JahrgangVon,
					zuordnung.JahrgangBis,
					zuordnung.Reihenfolge,
					zuordnung.AbschnittVon,
					zuordnung.AbschnittBis,
					zuordnung.StatistikKrz
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuelerSprachenfolge zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPSchuelerSprachenfolge
	 */
	public static List<ABPSchuelerSprachenfolge> getDefault() {
		return new ArrayList<>();
	}


	@Override
	public String toString() {
		return "ABPSchuelerSprachenfolge [Schueler_ID=" + Schueler_ID + ", FachKrz=" + FachKrz + ", JahrgangVon="
				+ JahrgangVon + ", JahrgangBis=" + JahrgangBis + ", Reihenfolge=" + Reihenfolge + ", AbschnittVon="
				+ AbschnittVon + ", AbschnittBis=" + AbschnittBis + ", StatistikKrz=" + StatistikKrz + "]";
	}

}
