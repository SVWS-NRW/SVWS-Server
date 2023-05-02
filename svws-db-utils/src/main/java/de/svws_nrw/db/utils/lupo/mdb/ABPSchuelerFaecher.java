package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostLeistungenFachbelegung;
import de.svws_nrw.core.data.gost.GostLeistungenFachwahl;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_SchuelerFaecher aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPSchuelerFaecher {

	/** Eine laufende ID der Zuordnung von Fächern zum Schüler */
	public int ID = -1;

	/** Die LuPO-Schüler-ID */
	public int Schueler_ID = -1;

	/** Die ID ds zugeordneten Faches */
	public int Fach_ID = -1;

	/** Das Kürzel des zugeordneten Faches */
	public String FachKrz;

	/** Gibt an, ab welchem Jahrgang die Fremdsprache belegt wurde. */
	public String FS_BeginnJg = null;

	/** Gibt an, als wievielte Fremdsprache eine Fremdsprache gewählt wurde. */
	public String Sprachenfolge = null;

	/** Gibt die Kursart des gewählten Faches in der EF im 1. Halbjahr an */
	public String Kursart_E1 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der EF im 1. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_E1 = null;

	/** Gibt an, ob die Wahl des Faches in der EF im 1. Halbjahr in Konflikt zu einer anderen Wahl steht. */
	public boolean Konflikt_E1 = false;

	/** Gibt die Kursart des gewählten Faches in der EF im 2. Halbjahr an */
	public String Kursart_E2 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der EF im 2. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_E2 = null;

	/** Gibt an, ob die Wahl des Faches in der EF im 2. Halbjahr in Konflikt zu einer anderen Wahl steht. */
	public boolean Konflikt_E2 = false;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 1. Halbjahr an */
	public String Kursart_Q1 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 1. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q1 = null;

	/** Gibt an, ob die Wahl des Faches in der Q-Phase im 1. Halbjahr in Konflikt zu einer anderen Wahl steht. */
	public boolean Konflikt_Q1 = false;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 2. Halbjahr an */
	public String Kursart_Q2 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 2. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q2 = null;

	/** Gibt an, ob die Wahl des Faches in der Q-Phase im 2. Halbjahr in Konflikt zu einer anderen Wahl steht. */
	public boolean Konflikt_Q2 = false;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 3. Halbjahr an */
	public String Kursart_Q3 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 3. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q3 = null;

	/** Gibt an, ob die Wahl des Faches in der Q-Phase im 3. Halbjahr in Konflikt zu einer anderen Wahl steht. */
	public boolean Konflikt_Q3 = false;

	/** Gibt die Kursart des gewählten Faches in der Q-Phase im 4. Halbjahr an */
	public String Kursart_Q4 = null;

	/** Gibt die Notenpunkte des gewählten Faches in der Q-Phase im 4. Halbjahr an, falls bereits Noten vorliegenen */
	public String Punkte_Q4 = null;

	/** Gibt an, ob die Wahl des Faches in der Q-Phase im 4. Halbjahr in Konflikt zu einer anderen Wahl steht. */
	public boolean Konflikt_Q4 = false;

	/** Gibt an, ob das Fach als 1., 2., 3., 4. Abiturfach oder nicht als Abiturfach gewählt wurde. */
	public Integer AbiturFach = null;

	/** Gibt an, ob es Konflikte bei der Wahl des Abuturfaches gibt. */
	public boolean Konflikt_AF = false;

	/** Bemerkungen zu dieser Fachwahl */
	public String Bemerkungen = null;

	/** Die Sortierung des Faches in der Wahl */
	public int Sortierung = 32000;

	/** Die Fachgruppe des Faches */
	public String Fachgruppe = null;

	/** Das Aufgabenfeld des Faches */
	public int Aufgabenfeld = -1;

	/** Gibt an, ob das Fach im 1. Halbjahr der EF vom Benutzer geändert werden darf */
	public String Aendern_E1 = null;

	/** Gibt an, ob das Fach im 2. Halbjahr der EF vom Benutzer geändert werden darf */
	public String Aendern_E2 = null;

	/** Gibt an, ob das Fach im 1. Halbjahr der Q-Phase vom Benutzer geändert werden darf */
	public String Aendern_Q1 = null;

	/** Gibt an, ob das Fach im 2. Halbjahr der Q-Phase vom Benutzer geändert werden darf */
	public String Aendern_Q2 = null;

	/** Gibt an, ob das Fach im 3. Halbjahr der Q-Phase vom Benutzer geändert werden darf */
	public String Aendern_Q3 = null;

	/** Gibt an, ob das Fach im 4. Halbjahr der Q-Phase vom Benutzer geändert werden darf */
	public String Aendern_Q4 = null;

	/** Gibt an, ob das Fach im 1. Halbjahr der Q-Phase für die Wertung in Block I des Abiturs markiert wurde. */
	public String Markiert_Q1 = null;

	/** Gibt an, ob das Fach im 2. Halbjahr der Q-Phase für die Wertung in Block I des Abiturs markiert wurde. */
	public String Markiert_Q2 = null;

	/** Gibt an, ob das Fach im 3. Halbjahr der Q-Phase für die Wertung in Block I des Abiturs markiert wurde. */
	public String Markiert_Q3 = null;

	/** Gibt an, ob das Fach im 4. Halbjahr der Q-Phase für die Wertung in Block I des Abiturs markiert wurde. */
	public String Markiert_Q4 = null;

	/** Gibt das Ergebnis in der Abiturprüfung an. */
	public Integer AbiPruefErgebnis = null;

	/** Gibt an, ob eine Mündliche Pflichtprüfung stattfinden muss. */
	public String MdlPflichtPruefung = null;

	/** Gibt das Ergebnis einer mündlichen Prüfung an. */
	public Integer MdlPruefErgebnis = null;


	private static final String fieldID = "ID";
	private static final String fieldSchueler_ID = "Schueler_ID";
	private static final String fieldFach_ID = "Fach_ID";
	private static final String fieldFachKrz = "FachKrz";
	private static final String fieldFS_BeginnJg = "FS_BeginnJg";
	private static final String fieldSprachenfolge = "Sprachenfolge";
	private static final String fieldKursart_E1 = "Kursart_E1";
	private static final String fieldPunkte_E1 = "Punkte_E1";
	private static final String fieldKonflikt_E1 = "Konflikt_E1";
	private static final String fieldKursart_E2 = "Kursart_E2";
	private static final String fieldPunkte_E2 = "Punkte_E2";
	private static final String fieldKonflikt_E2 = "Konflikt_E2";
	private static final String fieldKursart_Q1 = "Kursart_Q1";
	private static final String fieldPunkte_Q1 = "Punkte_Q1";
	private static final String fieldKonflikt_Q1 = "Konflikt_Q1";
	private static final String fieldKursart_Q2 = "Kursart_Q2";
	private static final String fieldPunkte_Q2 = "Punkte_Q2";
	private static final String fieldKonflikt_Q2 = "Konflikt_Q2";
	private static final String fieldKursart_Q3 = "Kursart_Q3";
	private static final String fieldPunkte_Q3 = "Punkte_Q3";
	private static final String fieldKonflikt_Q3 = "Konflikt_Q3";
	private static final String fieldKursart_Q4 = "Kursart_Q4";
	private static final String fieldPunkte_Q4 = "Punkte_Q4";
	private static final String fieldKonflikt_Q4 = "Konflikt_Q4";
	private static final String fieldAbiturFach = "AbiturFach";
	private static final String fieldKonflikt_AF = "Konflikt_AF";
	private static final String fieldBemerkungen = "Bemerkungen";
	private static final String fieldSortierung = "Sortierung";
	private static final String fieldFachgruppe = "Fachgruppe";
	private static final String fieldAufgabenfeld = "Aufgabenfeld";
	private static final String fieldAendern_E1 = "Aendern_E1";
	private static final String fieldAendern_E2 = "Aendern_E2";
	private static final String fieldAendern_Q1 = "Aendern_Q1";
	private static final String fieldAendern_Q2 = "Aendern_Q2";
	private static final String fieldAendern_Q3 = "Aendern_Q3";
	private static final String fieldAendern_Q4 = "Aendern_Q4";
	private static final String fieldMarkiert_Q1 = "Markiert_Q1";
	private static final String fieldMarkiert_Q2 = "Markiert_Q2";
	private static final String fieldMarkiert_Q3 = "Markiert_Q3";
	private static final String fieldMarkiert_Q4 = "Markiert_Q4";
	private static final String fieldAbiPruefErgebnis = "AbiPruefErgebnis";
	private static final String fieldMdlPflichtPruefung = "MdlPflichtPruefung";
	private static final String fieldMdlPruefErgebnis = "MdlPruefErgebnis";


	/**
	 * Liest alle Einträge der Tabelle "ABP_SchuelerFaecher" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der Fächerzuordnungen der Schüler aus der LuPO-Datei
	 */
	public static List<ABPSchuelerFaecher> read(final Database db) {
		try {
			final List<ABPSchuelerFaecher> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_SchuelerFaecher");
			for (final Row r : table) {
				final ABPSchuelerFaecher zuordnung = new ABPSchuelerFaecher();
				zuordnung.ID = r.getInt(fieldID);
				zuordnung.Schueler_ID = r.getInt(fieldSchueler_ID);
				zuordnung.Fach_ID = r.getInt(fieldFach_ID);
				zuordnung.FachKrz = r.getString(fieldFachKrz);
				zuordnung.FS_BeginnJg = r.getString(fieldFS_BeginnJg);
				zuordnung.Sprachenfolge = r.getString(fieldSprachenfolge);
				zuordnung.Kursart_E1 = r.getString(fieldKursart_E1);
				zuordnung.Punkte_E1 = r.getString(fieldPunkte_E1);
				zuordnung.Konflikt_E1 = "J".equals(r.getString(fieldKonflikt_E1));
				zuordnung.Kursart_E2 = r.getString(fieldKursart_E2);
				zuordnung.Punkte_E2 = r.getString(fieldPunkte_E2);
				zuordnung.Konflikt_E2 = "J".equals(r.getString(fieldKonflikt_E2));
				zuordnung.Kursart_Q1 = r.getString(fieldKursart_Q1);
				zuordnung.Punkte_Q1 = r.getString(fieldPunkte_Q1);
				zuordnung.Konflikt_Q1 = "J".equals(r.getString(fieldKonflikt_Q1));
				zuordnung.Kursart_Q2 = r.getString(fieldKursart_Q2);
				zuordnung.Punkte_Q2 = r.getString(fieldPunkte_Q2);
				zuordnung.Konflikt_Q2 = "J".equals(r.getString(fieldKonflikt_Q2));
				zuordnung.Kursart_Q3 = r.getString(fieldKursart_Q3);
				zuordnung.Punkte_Q3 = r.getString(fieldPunkte_Q3);
				zuordnung.Konflikt_Q3 = "J".equals(r.getString(fieldKonflikt_Q3));
				zuordnung.Kursart_Q4 = r.getString(fieldKursart_Q4);
				zuordnung.Punkte_Q4 = r.getString(fieldPunkte_Q4);
				zuordnung.Konflikt_Q4 = "J".equals(r.getString(fieldKonflikt_Q4));
				zuordnung.AbiturFach = r.getInt(fieldAbiturFach);
				zuordnung.Konflikt_AF = "J".equals(r.getString(fieldKonflikt_AF));
				zuordnung.Bemerkungen = r.getString(fieldBemerkungen);
				zuordnung.Sortierung = (r.getInt(fieldSortierung) == null) ? 32000 : r.getInt(fieldSortierung);
				zuordnung.Fachgruppe = r.getString(fieldFachgruppe);
				zuordnung.Aufgabenfeld = (r.getInt(fieldAufgabenfeld) == null) ? -1 : r.getInt(fieldAufgabenfeld);
				zuordnung.Aendern_E1 = r.getString(fieldAendern_E1);
				zuordnung.Aendern_E2 = r.getString(fieldAendern_E2);
				zuordnung.Aendern_Q1 = r.getString(fieldAendern_Q1);
				zuordnung.Aendern_Q2 = r.getString(fieldAendern_Q2);
				zuordnung.Aendern_Q3 = r.getString(fieldAendern_Q3);
				zuordnung.Aendern_Q4 = r.getString(fieldAendern_Q4);
				zuordnung.Markiert_Q1 = r.getString(fieldMarkiert_Q1);
				zuordnung.Markiert_Q2 = r.getString(fieldMarkiert_Q2);
				zuordnung.Markiert_Q3 = r.getString(fieldMarkiert_Q3);
				zuordnung.Markiert_Q4 = r.getString(fieldMarkiert_Q4);
				zuordnung.AbiPruefErgebnis = r.getInt(fieldAbiPruefErgebnis);
				zuordnung.MdlPflichtPruefung = r.getString(fieldMdlPflichtPruefung);
				zuordnung.MdlPruefErgebnis = r.getInt(fieldMdlPruefErgebnis);
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Schüler-Fächer-Zuordnung in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Schüler-Fächer-Zuordnung
	 */
	public static void write(final Database db, final List<ABPSchuelerFaecher> list) {
		try {
			final Table table = new TableBuilder("ABP_SchuelerFaecher")
				.addColumn(new ColumnBuilder(fieldID, DataType.LONG).putProperty(PropertyMap.REQUIRED_PROP, DataType.BOOLEAN, true))
				.addColumn(new ColumnBuilder(fieldSchueler_ID, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldFach_ID, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldFachKrz, DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder(fieldFS_BeginnJg, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldSprachenfolge, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKursart_E1, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldPunkte_E1, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldKonflikt_E1, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKursart_E2, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldPunkte_E2, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldKonflikt_E2, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKursart_Q1, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldPunkte_Q1, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldKonflikt_Q1, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKursart_Q2, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldPunkte_Q2, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldKonflikt_Q2, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKursart_Q3, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldPunkte_Q3, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldKonflikt_Q3, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKursart_Q4, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldPunkte_Q4, DataType.TEXT).setLengthInUnits(2))
				.addColumn(new ColumnBuilder(fieldKonflikt_Q4, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAbiturFach, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldKonflikt_AF, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldBemerkungen, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldSortierung, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldFachgruppe, DataType.TEXT).setLengthInUnits(5))
				.addColumn(new ColumnBuilder(fieldAufgabenfeld, DataType.LONG).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "0"))
				.addColumn(new ColumnBuilder(fieldAendern_E1, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAendern_E2, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAendern_Q1, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAendern_Q2, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAendern_Q3, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAendern_Q4, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldMarkiert_Q1, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldMarkiert_Q2, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldMarkiert_Q3, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldMarkiert_Q4, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAbiPruefErgebnis, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldMdlPflichtPruefung, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldMdlPruefErgebnis, DataType.LONG))
			    .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns(fieldID).setPrimaryKey())
			    .toTable(db);
			if (list == null)
				return;
			for (final ABPSchuelerFaecher zuordnung: list) {
				table.addRow(
					zuordnung.ID,
					zuordnung.Schueler_ID,
					zuordnung.Fach_ID,
					zuordnung.FachKrz,
					zuordnung.FS_BeginnJg,
					zuordnung.Sprachenfolge,
					zuordnung.Kursart_E1,
					zuordnung.Punkte_E1,
					zuordnung.Konflikt_E1 ? "J" : "N",
					zuordnung.Kursart_E2,
					zuordnung.Punkte_E2,
					zuordnung.Konflikt_E2 ? "J" : "N",
					zuordnung.Kursart_Q1,
					zuordnung.Punkte_Q1,
					zuordnung.Konflikt_Q1 ? "J" : "N",
					zuordnung.Kursart_Q2,
					zuordnung.Punkte_Q2,
					zuordnung.Konflikt_Q2 ? "J" : "N",
					zuordnung.Kursart_Q3,
					zuordnung.Punkte_Q3,
					zuordnung.Konflikt_Q3 ? "J" : "N",
					zuordnung.Kursart_Q4,
					zuordnung.Punkte_Q4,
					zuordnung.Konflikt_Q4 ? "J" : "N",
					zuordnung.AbiturFach,
					zuordnung.Konflikt_AF ? "J" : "N",
					zuordnung.Bemerkungen,
					zuordnung.Sortierung,
					zuordnung.Fachgruppe,
					zuordnung.Aufgabenfeld,
					zuordnung.Aendern_E1,
					zuordnung.Aendern_E2,
					zuordnung.Aendern_Q1,
					zuordnung.Aendern_Q2,
					zuordnung.Aendern_Q3,
					zuordnung.Aendern_Q4,
					zuordnung.Markiert_Q1,
					zuordnung.Markiert_Q2,
					zuordnung.Markiert_Q3,
					zuordnung.Markiert_Q4,
					zuordnung.AbiPruefErgebnis,
					zuordnung.MdlPflichtPruefung,
					zuordnung.MdlPruefErgebnis
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuelerFaecher zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPSchuelerFaecher
	 */
	public static List<ABPSchuelerFaecher> getDefault() {
		return new ArrayList<>();
	}


	/**
	 * Gibt den zu der angegebenen Belegung gehörigen String für die Kursart zurück
	 *
	 * @param belegung   die Belegung
	 *
	 * @return der in der LuPO-MDB verwendete String für die Kursart
	 */
	private static String getKursart(final GostLeistungenFachbelegung belegung) {
		return switch (GostKursart.fromKuerzel(belegung.kursartKuerzel).kuerzel) {
			case "GK" -> belegung.istSchriftlich ? "S" : "M";
			case "LK" -> "LK";
			case "PJK" -> "M";
			case "VTF" -> "M";
			case "ZK" -> "ZK";
			default -> "M";
		};
	}


	/**
	 * Erstellt die Einträge für die Tabelle ABPSchuelerFaecher aus den DTOs
	 * der SVWS-Server-Datenbank.
	 *
	 * @param faecher            die für die LuPO-Datei definierten Faecher
	 * @param fachgruppen        die Zuordnung der Fächer zu den einzelnen Fachgruppen
	 * @param schuelerListe      die SVWS-Server-DTOs für die Schüler
	 * @param schuelerLupoInfo   die LuPO-Information zu dem Schüler, die in der
	 *                           SVWS-Datenbank hinterlegt sind.
	 * @param gostInfo           die Leistungen des Schülers in der gymnasialen Oberstufe
	 *
	 * @return die Liste der Einträge für die Tabelle ABPSchuelerFaecher
	 */
	public static List<ABPSchuelerFaecher> get(final Map<String, ABPFaecher> faecher, final Map<String, ABPFachgruppen> fachgruppen,
			                                   final List<DTOSchueler> schuelerListe, final Map<Long, DTOGostSchueler> schuelerLupoInfo,
			                                   final Map<Long, GostLeistungen> gostInfo) {
		final List<ABPSchuelerFaecher> liste = new ArrayList<>();
		if (schuelerListe == null)
			return liste;
		int j = 1;   // TODO mit Maximum+1 aus DTOGostSchuelerFachbelegungen anfangen...
		for (int sid = 0; sid < schuelerListe.size(); sid++) {
			final DTOSchueler schueler = schuelerListe.get(sid);
			// TODO DTOGostSchuelerFachbelegungen lupoSchuelerFacher.get(schueler.getID());
			final GostLeistungen gostLeistungen = gostInfo.get(schueler.ID);
			for (final GostLeistungenFachwahl fachwahl : gostLeistungen.faecher) {
				final ABPSchuelerFaecher eintrag = new ABPSchuelerFaecher();
				eintrag.ID = j;
				eintrag.Schueler_ID = sid + 1;
				eintrag.FachKrz = fachwahl.fach.kuerzelAnzeige;
				final ABPFaecher fach = faecher.get(eintrag.FachKrz);
				eintrag.Fach_ID = fach.ID;
				if (fach.IstSprache) {
					final Sprachbelegung belegung = SprachendatenUtils.getSprachbelegung(gostLeistungen.sprachendaten, fach.StatistikKrz.toUpperCase().substring(0, 1));
					if (belegung != null) {
						eintrag.FS_BeginnJg = "" + belegung.belegungVonJahrgang;
						eintrag.Sprachenfolge = "" + belegung.reihenfolge;
					}
				}
				eintrag.Sortierung = fach.Sortierung;
				final ABPFachgruppen fachgruppe = fachgruppen.get(fach.StatistikKrz);
				if (fachgruppe == null)   // Eine Fachgruppe muss definiert sein, damit das Fach übernommen wird
					continue;
				eintrag.Fachgruppe = fachgruppe.FachgruppeKrz;
				eintrag.Aufgabenfeld = fachgruppe.Aufgabenfeld;
				eintrag.AbiturFach = GostAbiturFach.fromID(fachwahl.abiturfach) == null ? null : fachwahl.abiturfach;
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					GostLeistungenFachbelegung belegung = null;
					for (final GostLeistungenFachbelegung tmpBelegung : fachwahl.belegungen) {
						if (!tmpBelegung.abschnittGewertet)
							continue;
						if (halbjahr == GostHalbjahr.fromKuerzel(tmpBelegung.halbjahrKuerzel)) {
							belegung = tmpBelegung;
							break;
						}
					}
					if (belegung != null) {
						final String note = Note.fromKuerzel(belegung.notenKuerzel).istNote() ? "" + Note.fromKuerzel(belegung.notenKuerzel).notenpunkte : Note.fromKuerzel(belegung.notenKuerzel).kuerzel;
						if (halbjahr == GostHalbjahr.EF1) {
							eintrag.Kursart_E1 = getKursart(belegung);
							eintrag.Punkte_E1 = note;
						} else if (halbjahr == GostHalbjahr.EF2) {
							eintrag.Kursart_E2 = getKursart(belegung);
							eintrag.Punkte_E2 = note;
						} else if (halbjahr == GostHalbjahr.Q11) {
							eintrag.Kursart_Q1 = getKursart(belegung);
							eintrag.Punkte_Q1 = note;
						} else if (halbjahr == GostHalbjahr.Q12) {
							eintrag.Kursart_Q2 = getKursart(belegung);
							eintrag.Punkte_Q2 = note;
						} else if (halbjahr == GostHalbjahr.Q21) {
							eintrag.Kursart_Q3 = getKursart(belegung);
							eintrag.Punkte_Q3 = note;
						} else if (halbjahr == GostHalbjahr.Q22) {
							eintrag.Kursart_Q4 = getKursart(belegung);
							eintrag.Punkte_Q4 = note;
						}
					}
				}
				// TODO Abiturdaten: eintrag.AbiPruefErgebnis;
				// TODO Abiturdaten: eintrag.MdlPflichtPruefung;
				// TODO Abiturdaten: eintrag.MdlPruefErgebnis;
				liste.add(eintrag);
				j++;
			}
			// Füge ggf. Fächer der Sprachenfolge hinzu, die nicht in den Leistungsdaten vorhanden sind, deren Belegung aber relevant ist -> Prüfung zweite Fremdsprache
// TODO Hinzufügen von Einträgen für alle Sprachfächer aus dem Methoden-Parameter faecher (LUPO-Export)
/*			for (Sprachbelegung belegung: gostLeistungen.getSprachenNichtFortgefuehrt()) {
				ABPSchuelerFaecher eintrag = new ABPSchuelerFaecher();
				eintrag.ID = j;
				eintrag.Schueler_ID = sid+1;
				ABPFaecher fach = faecher.get(belegung.fachKuerzel);
				if (fach == null)
					fach = faecher.get(belegung.sprache);
				eintrag.FachKrz = fach.StatistikKrz;
				eintrag.Fach_ID = fach.ID;
				if (fach.IstSprache) {
					eintrag.FS_BeginnJg = "" + belegung.belegungVonJahrgang;
					if (belegung.istSprachpruefungSI) {
						eintrag.Sprachenfolge = "P";
					} else if (belegung.istSprachnachweisSI) {
						eintrag.Sprachenfolge = "N";
					} else {
						eintrag.Sprachenfolge = "" + belegung.reihenfolge;
					}
				}
				eintrag.Sortierung = fach.Sortierung;
				ABPFachgruppen fachgruppe = fachgruppen.get(fach.StatistikKrz);
				if (fachgruppe == null)   // Eine Fachgruppe muss definiert sein, damit das Fach übernommen wird
					continue;
				eintrag.Fachgruppe = fachgruppe.FachgruppeKrz;
				eintrag.Aufgabenfeld = fachgruppe.Aufgabenfeld;
				liste.add(eintrag);
				j++;
			}*/
		}
		return liste;
	}


	@Override
	public String toString() {
		return "ABPSchuelerFaecher [ID=" + ID + ", Schueler_ID=" + Schueler_ID + ", Fach_ID=" + Fach_ID + ", FachKrz="
				+ FachKrz + ", FS_BeginnJg=" + FS_BeginnJg + ", Sprachenfolge=" + Sprachenfolge + ", Kursart_E1="
				+ Kursart_E1 + ", Punkte_E1=" + Punkte_E1 + ", Konflikt_E1=" + Konflikt_E1 + ", Kursart_E2="
				+ Kursart_E2 + ", Punkte_E2=" + Punkte_E2 + ", Konflikt_E2=" + Konflikt_E2 + ", Kursart_Q1="
				+ Kursart_Q1 + ", Punkte_Q1=" + Punkte_Q1 + ", Konflikt_Q1=" + Konflikt_Q1 + ", Kursart_Q2="
				+ Kursart_Q2 + ", Punkte_Q2=" + Punkte_Q2 + ", Konflikt_Q2=" + Konflikt_Q2 + ", Kursart_Q3="
				+ Kursart_Q3 + ", Punkte_Q3=" + Punkte_Q3 + ", Konflikt_Q3=" + Konflikt_Q3 + ", Kursart_Q4="
				+ Kursart_Q4 + ", Punkte_Q4=" + Punkte_Q4 + ", Konflikt_Q4=" + Konflikt_Q4 + ", AbiturFach="
				+ AbiturFach + ", Konflikt_AF=" + Konflikt_AF + ", Bemerkungen=" + Bemerkungen + ", Sortierung="
				+ Sortierung + ", Fachgruppe=" + Fachgruppe + ", Aufgabenfeld=" + Aufgabenfeld + ", Aendern_E1="
				+ Aendern_E1 + ", Aendern_E2=" + Aendern_E2 + ", Aendern_Q1=" + Aendern_Q1 + ", Aendern_Q2="
				+ Aendern_Q2 + ", Aendern_Q3=" + Aendern_Q3 + ", Aendern_Q4=" + Aendern_Q4 + ", Markiert_Q1="
				+ Markiert_Q1 + ", Markiert_Q2=" + Markiert_Q2 + ", Markiert_Q3=" + Markiert_Q3 + ", Markiert_Q4="
				+ Markiert_Q4 + ", AbiPruefErgebnis=" + AbiPruefErgebnis + ", MdlPflichtPruefung=" + MdlPflichtPruefung
				+ ", MdlPruefErgebnis=" + MdlPruefErgebnis + "]";
	}

}
