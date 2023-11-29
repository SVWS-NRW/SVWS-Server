package de.svws_nrw.module.pdf.reptypes.gost.kursplanung;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Die Klasse enthält die Grunddaten eines Kurses für die GOSt-Kursplanung mit einer Liste der Kursschüler.
 */
public class RepGostKursplanungKurs {

	/** ID des Kurses */
	public long id;

	/** Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis. */
	public String gostHalbjahr;

	/** Bezeichnung des Kurses. */
	public @NotNull String bezeichnung;

	/** Kommaseparierte Liste der Lehrkräfte des Kurses. */
	public String lehrkraefte;

	/** Kursart des Kurses. */
	public String kursart;

	/** Anzahl der Schülerinnen und Schüler im Kurs. */
	public int anzahlTeilnehmer;

	/** Anzahl der Schülerinnen und Schüler mit Status extern. */
	public int anzahlExterneTeilnehmer;

	/** Anzahl der Klausurschreiber. */
	public int anzahlKlausurteilnehmer;

	/**
	 * Anzahl der Dummy-Schüler
	 */
	public int anzahlDummy;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist. */
	public int anzahlAB12;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist. */
	public int anzahlAB3;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist. */
	public int anzahlAB4;

	/** Farbe des Faches des Kurses im Web-Client */
	public String farbeClientRGB;

	/** Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses mit ihrer Kursbelegung enthält. */
	public List<RepGostKursplanungKursSchueler> Kursschueler;


	/**
	 * Erzeugt einen neuen Kurs der Kursplanung mit den übergebenen Werten.
	 * @param id						ID des Kurses.
	 * @param gostHalbjahr				Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @param bezeichnung				Bezeichnung des Kurses.
	 * @param lehrkraefte				Kommaseparierte Liste der Lehrkräfte des Kurses.
	 * @param kursart					Kursart des Kurses.
	 * @param anzahlTeilnehmer			Anzahl der Schülerinnen und Schüler im Kurs.
	 * @param anzahlExterneTeilnehmer	Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @param anzahlKlausurteilnehmer	Anzahl der Klausurschreiber.
	 * @param anzahlDummy 				Anzahl der Dummy-Schüler.
	 * @param anzahlAB12				Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @param anzahlAB3					Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @param anzahlAB4					Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @param farbeClientRGB 			Farbe des Faches des Kurses im Web-Client.
	 * @param kursschueler				Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses mit ihrer Kursbelegung enthält.
	 */
	public RepGostKursplanungKurs(final long id,
								  final String gostHalbjahr,
								  final String bezeichnung,
								  final String lehrkraefte,
								  final String kursart,
								  final int anzahlTeilnehmer,
								  final int anzahlExterneTeilnehmer,
								  final int anzahlKlausurteilnehmer,
								  final int anzahlDummy,
								  final int anzahlAB12,
								  final int anzahlAB3,
								  final int anzahlAB4,
								  final String farbeClientRGB,
								  final List<RepGostKursplanungKursSchueler> kursschueler) {

		this.id = id;
		this.gostHalbjahr = gostHalbjahr;
		this.bezeichnung = bezeichnung;
		this.lehrkraefte = lehrkraefte;
		this.kursart = kursart;
		this.anzahlTeilnehmer = anzahlTeilnehmer;
		this.anzahlExterneTeilnehmer = anzahlExterneTeilnehmer;
		this.anzahlKlausurteilnehmer = anzahlKlausurteilnehmer;
		this.anzahlDummy = anzahlDummy;
		this.anzahlAB12 = anzahlAB12;
		this.anzahlAB3 = anzahlAB3;
		this.anzahlAB4 = anzahlAB4;
		this.farbeClientRGB = farbeClientRGB;
		Kursschueler = kursschueler;
	}


	/**
	 * Gibt eine Liste der IDs der Kursschüler zurück, um Überprüfungen durchführen zu können.
	 * @return Liste der IDs der Kursschüler
	 */
	public List<Long> getListeKursschuelerIDs() {
		return Kursschueler.stream().map(s -> s.id).toList();
	}
}
