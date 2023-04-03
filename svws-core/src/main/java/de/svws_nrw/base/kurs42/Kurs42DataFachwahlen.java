package de.svws_nrw.base.kurs42;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.types.kurse.ZulaessigeKursart;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code Fachwahlen.txt} eines Kurs42-Textdatei-Exportes. In dieser
 * Datei findet man die Fachwahlen der SuS. Pro Schüler und Fachwahl existiert eine Zeile. Ein Schüler-Objekt ist in der
 * Kurs42-Datenstruktur eindeutig durch die vier Attribute {@link #Name}, {@link #Vorname}, {@link #Geschlecht} und
 * {@link #GebDat} spezifiziert. Das gewählte Fach ist eindeutig durch die zwei Attribute {@link #Fachkrz} und
 * {@link #Kursart} spezifiziert.
 *
 * @author Benjamin A. Bartsch, Thomas Bachran
 */
@JsonPropertyOrder({ "Name", "Vorname", "GebDat", "Geschlecht", "Fachkrz", "Kursart", "Note", "Kl1", "Kl2", "KLG",
	"SoMi1", "SoMi2", "SomiG", "Fehl1", "Fehl2", "uFehl1", "uFehl2", "FehlStd", "UFehlStd", "Facharbeit",
	"Facharbeitsthema", "Kurs", "Mahnung" })
public class Kurs42DataFachwahlen {

	/** Der Nachname des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public String Name;

	/** Der Vorname des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public String Vorname;

	/** Das Geburtsdatum des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public String GebDat;

	/** Das Geschlecht des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public int Geschlecht;

	/** Das Kürzel des Faches (z.B. 'D') der Fachwahl des Schülers. Eine von zwei Spalten, um die Fachart eindeutig zu identifizieren. */
	public String Fachkrz;

	/** Das Kürzel der Kursart (z.B. 'LK') der Fachwahl des Schülers. Eine von zwei Spalten, um die Fachart
	 * eindeutig zu identifizieren. (siehe auf {@link ZulaessigeKursart}) */
	public String Kursart;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public String Note;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int Kl1;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int Kl2;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int KLG;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int SoMi1;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int SoMi2;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int SomiG;

	/** Diese Spalte wird ignoriert, da Fehlstunden beim Import nicht berücksichtigt werden. */
	public int Fehl1;

	/** Diese Spalte wird ignoriert, da Fehlstunden beim Import nicht berücksichtigt werden. */
	public int Fehl2;

	/** Diese Spalte wird ignoriert, da Fehlstunden beim Import nicht berücksichtigt werden. */
	public int uFehl1;

	/** Diese Spalte wird ignoriert, da Fehlstunden beim Import nicht berücksichtigt werden. */
	public int uFehl2;

	/** Diese Spalte wird ignoriert, da Fehlstunden beim Import nicht berücksichtigt werden. */
	public int FehlStd;

	/** Diese Spalte wird ignoriert, da Fehlstunden beim Import nicht berücksichtigt werden. */
	public int UFehlStd;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int Facharbeit;

	/** Diese Spalte wird ignoriert, da Leistungsdaten beim Import nicht berücksichtigt werden. */
	public String Facharbeitsthema;

	/** Der Name des Kurses, dem der Schüler mit dieser Fachwahl zugeordnet wurde. */
	public String Kurs;

	/** Gibt an, ob der Schüler gemahnt werden soll oder nicht. Diese Spalte wird ignoriert, da
	 * Leistungsdaten beim Import nicht berücksichtigt werden. */
	public int Mahnung;

}