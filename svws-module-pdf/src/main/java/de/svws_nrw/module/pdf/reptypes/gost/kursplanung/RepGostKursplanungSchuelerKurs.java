package de.svws_nrw.module.pdf.reptypes.gost.kursplanung;

import java.util.List;

/**
 * Die Klasse enthält die Grunddaten eines Kurses für einen Schüler für die GOSt-Kursplanung.
 */
public class RepGostKursplanungSchuelerKurs extends RepGostKursplanungKurs {

	/** Gibt an, ob das Fach des Kurses vom Schüler schriftlich belegt ist. */
	public boolean istSchriftlich;

	/** Gibt an, welches Abiturfach das Fach des Kurses beim Schüler ist. */
	public String abiturfach;


	/**
	 * Erzeugt einen neuen Kurs der Kursplanung mit den übergebenen Werten für den Schüler.
	 *
	 * @param id                      	ID des Kurses.
	 * @param gostHalbjahr            	Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @param bezeichnung             	Bezeichnung des Kurses.
	 * @param lehrkraefte             	Kommaseparierte Liste der Lehrkräfte des Kurses.
	 * @param kursart                 	Kursart des Kurses.
	 * @param anzahlTeilnehmer        	Anzahl der Schülerinnen und Schüler im Kurs.
	 * @param anzahlExterneTeilnehmer 	Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @param anzahlKlausurteilnehmer 	Anzahl der Klausurschreiber.
	 * @param anzahlDummy 				Anzahl der Dummy-Schüler.
	 * @param anzahlAB12              	Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @param anzahlAB3               	Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @param anzahlAB4               	Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @param farbeClientRGB 		  	Farbe des Faches des Kurses im Web-Client.
	 * @param kursschueler            	Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses mit ihrer Kursbelegung enthält.
	 *
	 * @param istSchriftlich		  	Belegung des Faches des Kurses durch den Schüler ist schriftlich?
	 * @param abiturfach 			  	Abiturfacheintrag des Schülers in diesem Kurs
	 */
	public RepGostKursplanungSchuelerKurs(final long id,
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
										  final List<RepGostKursplanungKursSchueler> kursschueler,
										  final boolean istSchriftlich,
										  final String abiturfach) {
		super(id, gostHalbjahr, bezeichnung, lehrkraefte, kursart, anzahlTeilnehmer, anzahlExterneTeilnehmer, anzahlKlausurteilnehmer, anzahlDummy, anzahlAB12, anzahlAB3, anzahlAB4, farbeClientRGB, kursschueler);
		this.istSchriftlich = istSchriftlich;
		this.abiturfach = abiturfach;
	}

	/**
	 * Erzeugt einen neuen Kurs der Kursplanung mit den übergebenen Werten für den Schüler.
	 *
	 * @param kurs					Kurs des Schülers
	 * @param istSchriftlich		Belegung des Faches des Kurses durch den Schüler ist schriftlich?
	 * @param abiturfach 			Abiturfacheintrag des Schülers in diesem Kurs
	 */
	public RepGostKursplanungSchuelerKurs(final RepGostKursplanungKurs kurs,
										  final boolean istSchriftlich,
										  final String abiturfach) {
		super(kurs.id, kurs.gostHalbjahr, kurs.bezeichnung, kurs.lehrkraefte, kurs.kursart, kurs.anzahlTeilnehmer, kurs.anzahlExterneTeilnehmer, kurs.anzahlKlausurteilnehmer, kurs.anzahlDummy, kurs.anzahlAB12, kurs.anzahlAB3, kurs.anzahlAB4, kurs.farbeClientRGB, kurs.Kursschueler);
		this.istSchriftlich = istSchriftlich;
		this.abiturfach = abiturfach;
	}
}
