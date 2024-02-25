package de.svws_nrw.module.reporting.types.gost.abitur;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegung.</p>
 * <p>Sie enthält die Daten zur Belegung eines Faches durch einen Schüler im Rahmen des Abiturs der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 *  einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostAbiturFachbelegung {

	/** Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null) */
	private Integer abiturFach;

	/** Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung */
	private Double block1NotenpunkteDurchschnitt;

	/** Die Punktsumme im Block I des Abiturs für die Fachbelegung */
	private Integer block1PunktSumme;

	/** Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr). */
	private Boolean block2MuendlichePruefungAbweichung;

	/** Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	private Boolean block2MuendlichePruefungBestehen;

	/** Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	private Boolean block2MuendlichePruefungFreiwillig;

	/** Die Note der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt. */
	private Note block2MuendlichePruefungNote;

	/** Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	private Integer block2MuendlichePruefungReihenfolge;

	/** Die Lehrkraft, welche Prüfer im Rahmen der Abiturprüfung ist. */
	private ReportingLehrer block2Pruefer;

	/** Die Note in der Abiturprüfung, sofern dies die Belegung eines Abiturfaches ist. */
	private Note block2PruefungNote;

	/** Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist. */
	private Integer block2Punkte;

	/** Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist. */
	private Integer block2PunkteZwischenstand;

	/** Das Fach der gymnasialen Oberstufe, welches belegt wurde. */
	private ReportingFach fach;

	/** Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs */
	private ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen;

	/** Die letzte Kursart der gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde */
	private String letzteKursart;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abiturFach Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null)
	 * @param block1NotenpunkteDurchschnitt Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung
	 * @param block1PunktSumme Die Punktsumme im Block I des Abiturs für die Fachbelegung
	 * @param block2MuendlichePruefungAbweichung Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr).
	 * @param block2MuendlichePruefungBestehen Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 * @param block2MuendlichePruefungFreiwillig Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 * @param block2MuendlichePruefungNote Die Note der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt.
	 * @param block2MuendlichePruefungReihenfolge Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 * @param block2Pruefer Die Lehrer-ID des Prüfers im Rahmen der Abiturprüfung.
	 * @param block2PruefungNote Die Note in der Abiturprüfung, sofern dies die Belegung eines Abiturfaches ist.
	 * @param block2Punkte Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 * @param block2PunkteZwischenstand Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 * @param fach Das Fach der gymnasialen Oberstufe, welches belegt wurde.
	 * @param halbjahresbelegungen Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs
	 * @param letzteKursart Die letzte Kursart der gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde
	 */
	public ReportingGostAbiturFachbelegung(final Integer abiturFach, final Double block1NotenpunkteDurchschnitt, final Integer block1PunktSumme, final Boolean block2MuendlichePruefungAbweichung, final Boolean block2MuendlichePruefungBestehen, final Boolean block2MuendlichePruefungFreiwillig, final Note block2MuendlichePruefungNote, final Integer block2MuendlichePruefungReihenfolge, final ReportingLehrer block2Pruefer, final Note block2PruefungNote, final Integer block2Punkte, final Integer block2PunkteZwischenstand, final ReportingFach fach, final ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen, final String letzteKursart) {
		this.abiturFach = abiturFach;
		this.block1NotenpunkteDurchschnitt = block1NotenpunkteDurchschnitt;
		this.block1PunktSumme = block1PunktSumme;
		this.block2MuendlichePruefungAbweichung = block2MuendlichePruefungAbweichung;
		this.block2MuendlichePruefungBestehen = block2MuendlichePruefungBestehen;
		this.block2MuendlichePruefungFreiwillig = block2MuendlichePruefungFreiwillig;
		this.block2MuendlichePruefungNote = block2MuendlichePruefungNote;
		this.block2MuendlichePruefungReihenfolge = block2MuendlichePruefungReihenfolge;
		this.block2Pruefer = block2Pruefer;
		this.block2PruefungNote = block2PruefungNote;
		this.block2Punkte = block2Punkte;
		this.block2PunkteZwischenstand = block2PunkteZwischenstand;
		this.fach = fach;
		this.halbjahresbelegungen = halbjahresbelegungen;
		this.letzteKursart = letzteKursart;

		if (halbjahresbelegungen == null)
			setHalbjahresbelegungen(new ReportingGostAbiturFachbelegungHalbjahr[6]);
	}



	/**
	 * Vergleicht zwei ReportingGostAbiturFachbelegungen hinsichtlich ihrer Fachsortierung in der GOSt.
	 * @param gostAbiFachbelegung1 Erste ReportingGostAbiturFachbelegung
	 * @param gostAbiFachbelegung2 Zweite ReportingGostAbiturFachbelegung
	 * @return int-Wert des Vergleiches gemäß {@link Comparable#compareTo(Object)}
	 */
	public static int compareToGost(final ReportingGostAbiturFachbelegung gostAbiFachbelegung1, final ReportingGostAbiturFachbelegung gostAbiFachbelegung2) {
		return ReportingFach.compareToGost(gostAbiFachbelegung1.fach(), gostAbiFachbelegung2.fach());
	}



	// ##### Getter und Setter #####

	/**
	 * Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null)
	 * @return Inhalt des Feldes abiturFach
	 */
	public Integer abiturFach() {
		return abiturFach;
	}

	/**
	 * Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null) wird gesetzt.
	 * @param abiturFach Neuer Wert für das Feld abiturFach
	 */
	public void setAbiturFach(final Integer abiturFach) {
		this.abiturFach = abiturFach;
	}

	/**
	 * Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung
	 * @return Inhalt des Feldes block1NotenpunkteDurchschnitt
	 */
	public Double block1NotenpunkteDurchschnitt() {
		return block1NotenpunkteDurchschnitt;
	}

	/**
	 * Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung wird gesetzt.
	 * @param block1NotenpunkteDurchschnitt Neuer Wert für das Feld block1NotenpunkteDurchschnitt
	 */
	public void setBlock1NotenpunkteDurchschnitt(final Double block1NotenpunkteDurchschnitt) {
		this.block1NotenpunkteDurchschnitt = block1NotenpunkteDurchschnitt;
	}

	/**
	 * Die Punktsumme im Block I des Abiturs für die Fachbelegung
	 * @return Inhalt des Feldes block1PunktSumme
	 */
	public Integer block1PunktSumme() {
		return block1PunktSumme;
	}

	/**
	 * Die Punktsumme im Block I des Abiturs für die Fachbelegung wird gesetzt.
	 * @param block1PunktSumme Neuer Wert für das Feld block1PunktSumme
	 */
	public void setBlock1PunktSumme(final Integer block1PunktSumme) {
		this.block1PunktSumme = block1PunktSumme;
	}

	/**
	 * Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr).
	 * @return Inhalt des Feldes block2MuendlichePruefungAbweichung
	 */
	public Boolean block2MuendlichePruefungAbweichung() {
		return block2MuendlichePruefungAbweichung;
	}

	/**
	 * Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr) wird gesetzt.
	 * @param block2MuendlichePruefungAbweichung Neuer Wert für das Feld block2MuendlichePruefungAbweichung
	 */
	public void setBlock2MuendlichePruefungAbweichung(final Boolean block2MuendlichePruefungAbweichung) {
		this.block2MuendlichePruefungAbweichung = block2MuendlichePruefungAbweichung;
	}

	/**
	 * Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 * @return Inhalt des Feldes block2MuendlichePruefungBestehen
	 */
	public Boolean block2MuendlichePruefungBestehen() {
		return block2MuendlichePruefungBestehen;
	}

	/**
	 * Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist wird gesetzt.
	 * @param block2MuendlichePruefungBestehen Neuer Wert für das Feld block2MuendlichePruefungBestehen
	 */
	public void setBlock2MuendlichePruefungBestehen(final Boolean block2MuendlichePruefungBestehen) {
		this.block2MuendlichePruefungBestehen = block2MuendlichePruefungBestehen;
	}

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 * @return Inhalt des Feldes block2MuendlichePruefungFreiwillig
	 */
	public Boolean block2MuendlichePruefungFreiwillig() {
		return block2MuendlichePruefungFreiwillig;
	}

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist wird gesetzt.
	 * @param block2MuendlichePruefungFreiwillig Neuer Wert für das Feld block2MuendlichePruefungFreiwillig
	 */
	public void setBlock2MuendlichePruefungFreiwillig(final Boolean block2MuendlichePruefungFreiwillig) {
		this.block2MuendlichePruefungFreiwillig = block2MuendlichePruefungFreiwillig;
	}

	/**
	 * Das Notenkürzel der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt.
	 * @return Inhalt des Feldes block2MuendlichePruefungNote
	 */
	public Note block2MuendlichePruefungNote() {
		return block2MuendlichePruefungNote;
	}

	/**
	 * Die Note der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt wird gesetzt.
	 * @param block2MuendlichePruefungNote Neuer Wert für das Feld block2MuendlichePruefungNote
	 */
	public void setBlock2MuendlichePruefungNote(final Note block2MuendlichePruefungNote) {
		this.block2MuendlichePruefungNote = block2MuendlichePruefungNote;
	}

	/**
	 * Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 * @return Inhalt des Feldes block2MuendlichePruefungReihenfolge
	 */
	public Integer block2MuendlichePruefungReihenfolge() {
		return block2MuendlichePruefungReihenfolge;
	}

	/**
	 * Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist wird gesetzt.
	 * @param block2MuendlichePruefungReihenfolge Neuer Wert für das Feld block2MuendlichePruefungReihenfolge
	 */
	public void setBlock2MuendlichePruefungReihenfolge(final Integer block2MuendlichePruefungReihenfolge) {
		this.block2MuendlichePruefungReihenfolge = block2MuendlichePruefungReihenfolge;
	}

	/**
	 * Die Lehrkraft, welche Prüfer im Rahmen der Abiturprüfung ist.
	 * @return Inhalt des Feldes block2Pruefer
	 */
	public ReportingLehrer block2Pruefer() {
		return block2Pruefer;
	}

	/**
	 * Die Lehrkraft, welche Prüfer im Rahmen der Abiturprüfung ist, wird gesetzt.
	 * @param block2Pruefer Neuer Wert für das Feld block2Pruefer
	 */
	public void setBlock2Pruefer(final ReportingLehrer block2Pruefer) {
		this.block2Pruefer = block2Pruefer;
	}

	/**
	 * Die Note in der Abiturprüfung, sofern dies die Belegung eines Abiturfaches ist.
	 * @return Inhalt des Feldes block2PruefungNote
	 */
	public Note block2PruefungNote() {
		return block2PruefungNote;
	}

	/**
	 * Die Note in der Abiturprüfung, sofern dies die Belegung eines Abiturfaches ist, wird gesetzt.
	 * @param block2PruefungNote Neuer Wert für das Feld block2PruefungNote
	 */
	public void setBlock2PruefungNote(final Note block2PruefungNote) {
		this.block2PruefungNote = block2PruefungNote;
	}

	/**
	 * Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 * @return Inhalt des Feldes block2Punkte
	 */
	public Integer block2Punkte() {
		return block2Punkte;
	}

	/**
	 * Die erreichten Punkte im Abitur nach einer evtl mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist wird gesetzt.
	 * @param block2Punkte Neuer Wert für das Feld block2Punkte
	 */
	public void setBlock2Punkte(final Integer block2Punkte) {
		this.block2Punkte = block2Punkte;
	}

	/**
	 * Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 * @return Inhalt des Feldes block2PunkteZwischenstand
	 */
	public Integer block2PunkteZwischenstand() {
		return block2PunkteZwischenstand;
	}

	/**
	 * Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist wird gesetzt.
	 * @param block2PunkteZwischenstand Neuer Wert für das Feld block2PunkteZwischenstand
	 */
	public void setBlock2PunkteZwischenstand(final Integer block2PunkteZwischenstand) {
		this.block2PunkteZwischenstand = block2PunkteZwischenstand;
	}

	/**
	 * Das Fach der gymnasialen Oberstufe, welches belegt wurde.
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Das Fach der gymnasialen Oberstufe, welches belegt wurde, wird gesetzt.
	 * @param fach Neuer Wert für das Feld fach
	 */
	public void setFach(final ReportingFach fach) {
		this.fach = fach;
	}

	/**
	 * Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs
	 * @return Inhalt des Feldes halbjahresbelegungen
	 */
	public final ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen() {
		return halbjahresbelegungen;
	}

	/**
	 * Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs wird gesetzt.
	 * @param halbjahresbelegungen Neuer Wert für das Feld halbjahresbelegungen
	 */
	public void setHalbjahresbelegungen(final ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen) {
		this.halbjahresbelegungen = halbjahresbelegungen;
	}

	/**
	 * Die letzte Kursart der gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde
	 * @return Inhalt des Feldes letzteKursart
	 */
	public String letzteKursart() {
		return letzteKursart;
	}

	/**
	 * Die letzte Kursart der gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde, wird gesetzt.
	 * @param letzteKursart Neuer Wert für das Feld letzteKursart
	 */
	public void setLetzteKursart(final String letzteKursart) {
		this.letzteKursart = letzteKursart;
	}



}
