package de.svws_nrw.module.pdf.drucktypes;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse enthält die Grunddaten eines Kurses für die GOSt-Kursplanung mit einer Liste der Kursschüler.
 */
public class DruckGostKursplanungKurs {

	/** ID des Kurses */
	public long id = -1;

	/** Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis. */
	public String gostHalbjahr = "";

	/** Bezeichnung des Kurses. */
	public @NotNull String Bezeichnung = "";

	/** Kommaseparierte Liste der Lehrkräfte des Kurses. */
	public String lehrkraefte = "";

	/** Kursart des Kurses. */
	public String kursart = "";

	/** Anzahl der Schülerinnen und Schüler im Kurs. */
	public int anzahlTeilnehmer = -1;

	/** Anzahl der Schülerinnen und Schüler mit Status extern. */
	public int anzahlExterneTeilnehmer = -1;

	/** Anzahl der Klausurschreiber. */
	public int anzahlKlausurteilnehmer = -1;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist. */
	public int anzahlAB12 = -1;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist. */
	public int anzahlAB3 = -1;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist. */
	public int anzahlAB4 = -1;

	/** Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses mit ihrer Kursbelegung enthält. */
	public List<DruckGostKursplanungKursSchueler> Kursschueler = new ArrayList<>();
}
