package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import io.github.spannm.jackcess.ColumnBuilder;
import io.github.spannm.jackcess.DataType;
import io.github.spannm.jackcess.Database;
import io.github.spannm.jackcess.IndexBuilder;
import io.github.spannm.jackcess.PropertyMap;
import io.github.spannm.jackcess.Row;
import io.github.spannm.jackcess.Table;
import io.github.spannm.jackcess.TableBuilder;

import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.gost.DTOFaecherNichtMoeglicheKombination;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_NichtMoeglAbiFachKombi
 * aus einer LuPO-Datenbank im Access-Format genutzt.
 */
public final class ABPNichtMoeglAbiFachKombi {

	/** Das Kürzel des 1. Faches der nicht möglichen Kombination */
	public String Fach1_Krz = null;

	/** Das ID des 1. Faches der nicht möglichen Kombination */
	public Integer Fach1_ID = null;

	/** Das Kürzel des 2. Faches der nicht möglichen Kombination */
	public String Fach2_Krz = null;

	/** Das ID des 2. Faches der nicht möglichen Kombination */
	public Integer Fach2_ID = null;

	/** Die Kursart des 1. Faches der nicht möglichen Kombination */
	public String Kursart1 = null;

	/** Die Kursart des 2. Faches der nicht möglichen Kombination */
	public String Kursart2 = null;

	/** Gibt an, über welche Jahrgangsstufen die Kombination nicht geht */
	public String Phase = null;

	/** ??? */
	public String PK = null;

	/** ??? */
	public String Typ = null;


	private static final String fieldFach1_Krz = "Fach1_Krz";
	private static final String fieldFach1_ID = "Fach1_ID";
	private static final String fieldFach2_Krz = "Fach2_Krz";
	private static final String fieldFach2_ID = "Fach2_ID";
	private static final String fieldKursart1 = "Kursart1";
	private static final String fieldKursart2 = "Kursart2";
	private static final String fieldPhase = "Phase";
	private static final String fieldPK = "PK";
	private static final String fieldTyp = "Typ";


	/**
	 * Liest alle Einträge der Tabelle "ABP_Lehrer" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der LuPO-MDB-Benutzer aus der LuPO-Datei
	 */
	public static List<ABPNichtMoeglAbiFachKombi> read(final Database db) {
		try {
			final List<ABPNichtMoeglAbiFachKombi> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_NichtMoeglAbiFachKombi");
			for (final Row r : table) {
				final ABPNichtMoeglAbiFachKombi zuordnung = new ABPNichtMoeglAbiFachKombi();
				zuordnung.Fach1_Krz = r.getString(fieldFach1_Krz);
				zuordnung.Fach1_ID = r.getInt(fieldFach1_ID);
				zuordnung.Fach2_Krz = r.getString(fieldFach2_Krz);
				zuordnung.Fach2_ID = r.getInt(fieldFach2_ID);
				zuordnung.Kursart1 = r.getString(fieldKursart1);
				zuordnung.Kursart2 = r.getString(fieldKursart2);
				zuordnung.Phase = r.getString(fieldPhase);
				zuordnung.PK = r.getString(fieldPK);
				zuordnung.Typ = r.getString(fieldTyp);
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen LuPO-MDB-Benutzer in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden LuPO-MDB-Benutzer
	 */
	public static void write(final Database db, final List<ABPNichtMoeglAbiFachKombi> list) {
		try {
			final Table table = new TableBuilder("ABP_NichtMoeglAbiFachKombi")
					.addColumn(new ColumnBuilder(fieldFach1_Krz, DataType.TEXT).withLengthInUnits(20))
					.addColumn(new ColumnBuilder(fieldFach1_ID, DataType.LONG))
					.addColumn(new ColumnBuilder(fieldFach2_Krz, DataType.TEXT).withLengthInUnits(20))
					.addColumn(new ColumnBuilder(fieldFach2_ID, DataType.LONG))
					.addColumn(new ColumnBuilder(fieldKursart1, DataType.TEXT).withLengthInUnits(5))
					.addColumn(new ColumnBuilder(fieldKursart2, DataType.TEXT).withLengthInUnits(5))
					.addColumn(new ColumnBuilder(fieldPhase, DataType.TEXT).withLengthInUnits(10).withProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT,
							"'-'"))
					.addColumn(new ColumnBuilder(fieldPK, DataType.TEXT).withLengthInUnits(30))
					.addColumn(new ColumnBuilder(fieldTyp, DataType.TEXT).withLengthInUnits(1).withProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'-'"))
					.addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).withColumns(fieldPK).withPrimaryKey())
					.toTable(db);
			for (final ABPNichtMoeglAbiFachKombi zuordnung : list) {
				table.addRow(
						zuordnung.Fach1_Krz,
						zuordnung.Fach1_ID,
						zuordnung.Fach2_Krz,
						zuordnung.Fach2_ID,
						zuordnung.Kursart1,
						zuordnung.Kursart2,
						zuordnung.Phase,
						zuordnung.PK,
						zuordnung.Typ);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Erstellt die Einträge für die Tabelle ABP_NichtMoeglAbiFachKombi aus dem DTO
	 * der SVWS-Server-Datenbank.
	 *
	 * @param nichtMoeglicheKombinationen   die SVWS-Server-DTOs für die nicht möglichen Kombinationen
	 * @param faecher       die SVWS-Server-DTOs für die Fächer als Liste
	 * @param faecherMap    die SVWS-Server-DTOs für die Fächer, jeweils ihrer ID zugeordnet
	 *
	 * @return die Liste der Einträge für die Tabelle ABP_NichtMoeglAbiFachKombi
	 */
	public static List<ABPNichtMoeglAbiFachKombi> get(final List<DTOFaecherNichtMoeglicheKombination> nichtMoeglicheKombinationen, final List<DTOFach> faecher,
			final Map<Long, DTOFach> faecherMap) {
		final List<ABPNichtMoeglAbiFachKombi> liste = new ArrayList<>();
		if (nichtMoeglicheKombinationen == null)
			return liste;
		for (int i = 0; i < nichtMoeglicheKombinationen.size(); i++) {
			final DTOFaecherNichtMoeglicheKombination nichtMoeglicheKombination = nichtMoeglicheKombinationen.get(i);
			final ABPNichtMoeglAbiFachKombi eintrag = new ABPNichtMoeglAbiFachKombi();
			final DTOFach fach1 = faecherMap.get(nichtMoeglicheKombination.Fach1_ID);
			final DTOFach fach2 = faecherMap.get(nichtMoeglicheKombination.Fach2_ID);
			eintrag.Fach1_Krz = (fach1 == null) ? null : fach1.Kuerzel;
			eintrag.Fach1_ID = (fach1 == null) ? null : faecher.indexOf(fach1);
			eintrag.Fach2_Krz = (fach2 == null) ? null : fach2.Kuerzel;
			eintrag.Fach2_ID = (fach2 == null) ? null : faecher.indexOf(fach2);
			eintrag.Kursart1 = nichtMoeglicheKombination.Kursart1;
			eintrag.Kursart2 = nichtMoeglicheKombination.Kursart2;
			eintrag.Phase = nichtMoeglicheKombination.Phase;
			eintrag.PK = nichtMoeglicheKombination.PK;
			eintrag.Typ = nichtMoeglicheKombination.Typ;
			liste.add(eintrag);
		}
		return liste;
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPNichtMoeglAbiFachKombi zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPNichtMoeglAbiFachKombi
	 */
	public static List<ABPNichtMoeglAbiFachKombi> getDefault() {
		return new ArrayList<>();
	}


	@Override
	public String toString() {
		return "ABPNichtMoeglAbiFachKombi [Fach1_Krz=" + Fach1_Krz + ", Fach1_ID=" + Fach1_ID + ", Fach2_Krz="
				+ Fach2_Krz + ", Fach2_ID=" + Fach2_ID + ", Kursart1=" + Kursart1 + ", Kursart2=" + Kursart2
				+ ", Phase=" + Phase + ", PK=" + PK + ", Typ=" + Typ + "]";
	}

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ABPNichtMoeglAbiFachKombi() {
		// leer
	}
}
