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
 * Diese Klasse wird für den Import der Tabelle ABP_Version aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPVersion {

	/** Die Version der LuPO-MDB als Datum in der Form yyyyMMdd */
	public String Version = "20210307"; // versionDateFormatter.format(LocalDate.now());


	/**
	 * Liest alle Einträge der Tabelle "ABP_Version" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der Schüler aus der LuPO-Datei
	 */
	public static List<ABPVersion> read(final Database db) {
		try {
			final List<ABPVersion> liste = new Vector<>();
			final Table table = db.getTable("ABP_Version");
			for (final Row r : table) {
				final ABPVersion version = new ABPVersion();
				version.Version = r.getString("Version");
				liste.add(version);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Versionen in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Versionen
	 */
	public static void write(final Database db, final List<ABPVersion> list) {
		try {
			final Table table = new TableBuilder("ABP_Version")
			     .addColumn(new ColumnBuilder("Version", DataType.TEXT).setLengthInUnits(8))
			     .toTable(db);
			for (final ABPVersion version: list) {
				table.addRow(
					version.Version
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPVersion zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPVersion
	 */
	public static List<ABPVersion> getDefault() {
		final List<ABPVersion> versionen = new Vector<>();
		versionen.add(new ABPVersion());
		return versionen;
	}


	@Override
	public String toString() {
		return "ABPVersion [Version=" + Version + "]";
	}

}
