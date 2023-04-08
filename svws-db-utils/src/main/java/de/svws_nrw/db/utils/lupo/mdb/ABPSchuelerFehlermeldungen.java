package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
public final class ABPSchuelerFehlermeldungen {

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
	public static List<ABPSchuelerFehlermeldungen> read(final Database db) {
		try {
			final List<ABPSchuelerFehlermeldungen> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_SchuelerFehlermeldungen");
			for (final Row r : table) {
				final ABPSchuelerFehlermeldungen fehler = new ABPSchuelerFehlermeldungen();
				fehler.Schueler_ID = r.getInt("Schueler_ID");
				fehler.Fehlercode = r.getString("Fehlercode");
				fehler.Fehlertext = r.getString("Fehlertext");
				fehler.Fehlergruppe = r.getString("Fehlergruppe");
				fehler.Sortierung = r.getInt("Sortierung");
				liste.add(fehler);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Schüler in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler
	 */
	public static void write(final Database db, final List<ABPSchuelerFehlermeldungen> list) {
		try {
			final Table table = new TableBuilder("ABP_SchuelerFehlermeldungen")
			     .addColumn(new ColumnBuilder("Schueler_ID", DataType.LONG))
			     .addColumn(new ColumnBuilder("Fehlercode", DataType.TEXT).setLengthInUnits(20))
			     .addColumn(new ColumnBuilder("Fehlertext", DataType.MEMO).setLengthInUnits(16777216))
			     .addColumn(new ColumnBuilder("Fehlergruppe", DataType.TEXT).setLengthInUnits(2))
			     .addColumn(new ColumnBuilder("Sortierung", DataType.LONG))
			     .toTable(db);
			if (list == null)
				return;
			for (final ABPSchuelerFehlermeldungen schueler: list) {
				table.addRow(
					schueler.Schueler_ID,
					schueler.Fehlercode,
					schueler.Fehlertext,
					schueler.Fehlergruppe,
					schueler.Sortierung
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuelerFehlermeldungen zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPSchuelerFehlermeldungen
	 */
	public static List<ABPSchuelerFehlermeldungen> getDefault() {
		final List<ABPSchuelerFehlermeldungen> fehlermeldungen = new ArrayList<>();
		return fehlermeldungen;
	}


	@Override
	public String toString() {
		return "ABPSchuelerFehlermeldungen [Schueler_ID=" + Schueler_ID + ", Fehlercode=" + Fehlercode + ", Fehlertext="
				+ Fehlertext + ", Fehlergruppe=" + Fehlergruppe + ", Sortierung=" + Sortierung + "]";
	}

}
