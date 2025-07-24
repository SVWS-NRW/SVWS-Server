package de.svws_nrw.module.reporting.types.gost.abitur;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegung.
 */
public class ReportingGostAbiturFachbelegung extends ReportingBaseType {

	/** Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null) */
	protected Integer abiturFach;

	/** Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung */
	protected Double block1NotenpunkteDurchschnitt;

	/** Die Punktsumme im Block I des Abiturs für die Fachbelegung */
	protected Integer block1PunktSumme;

	/** Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr). */
	protected Boolean block2MuendlichePruefungAbweichung;

	/** Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	protected Boolean block2MuendlichePruefungBestehen;

	/** Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	protected Boolean block2MuendlichePruefungFreiwillig;

	/** Die Note der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt. */
	protected Note block2MuendlichePruefungNote;

	/** Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	protected Integer block2MuendlichePruefungReihenfolge;

	/** Die Lehrkraft, welche Prüfer im Rahmen der Abiturprüfung ist. */
	protected ReportingLehrer block2Pruefer;

	/** Die Note in der Abiturprüfung, sofern dies die Belegung eines Abiturfaches ist. */
	protected Note block2PruefungNote;

	/** Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist. */
	protected Integer block2Punkte;

	/** Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist. */
	protected Integer block2PunkteZwischenstand;

	/** Das Fach der gymnasialen Oberstufe, welches belegt wurde. */
	protected ReportingFach fach;

	/** Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs */
	protected ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen;

	/** Die letzte Kursart der gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde */
	protected String letzteKursart;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
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
	public ReportingGostAbiturFachbelegung(final Integer abiturFach, final Double block1NotenpunkteDurchschnitt, final Integer block1PunktSumme,
			final Boolean block2MuendlichePruefungAbweichung, final Boolean block2MuendlichePruefungBestehen, final Boolean block2MuendlichePruefungFreiwillig,
			final Note block2MuendlichePruefungNote, final Integer block2MuendlichePruefungReihenfolge, final ReportingLehrer block2Pruefer,
			final Note block2PruefungNote, final Integer block2Punkte, final Integer block2PunkteZwischenstand, final ReportingFach fach,
			final ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen, final String letzteKursart) {
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
			this.halbjahresbelegungen = new ReportingGostAbiturFachbelegungHalbjahr[6];
	}



	/**
	 * Vergleicht zwei ReportingGostAbiturFachbelegungen hinsichtlich ihrer Fachsortierung in der GOSt.
	 *
	 * @param gostAbiFachbelegung1 Erste ReportingGostAbiturFachbelegung
	 * @param gostAbiFachbelegung2 Zweite ReportingGostAbiturFachbelegung
	 *
	 * @return int-Wert des Vergleiches gemäß {@link Comparable#compareTo(Object)}
	 */
	public static int compareToGost(final ReportingGostAbiturFachbelegung gostAbiFachbelegung1, final ReportingGostAbiturFachbelegung gostAbiFachbelegung2) {
		return ReportingFach.compareToGost(gostAbiFachbelegung1.fach(), gostAbiFachbelegung2.fach());
	}



	// ##### Berechnete Methoden #####

	/**
	 * Gibt die Fachbezeichnung für ein Zeugnis zurück. Optional kann dabei eine Kennzeichnung für Leistungskurse (LK) ausgegeben werden.
	 *
	 * @param mitLKBezeichnung Gibt an, ob die Fachbezeichnung mit dem Zusatz "(LK/eA)" bei den LKs ausgegeben werden soll.
	 *                         Wenn true, wird der Zusatz hinzugefügt. Andernfalls wird nur die Fachbezeichnung zurückgegeben.
	 *
	 * @return Die Fachbezeichnung des Faches, ggf. erweitert um den Zusatz "(LK/eA)".
	 */
	public String zeugnisFachbezeichnung(final boolean mitLKBezeichnung) {
		if (mitLKBezeichnung && (abiturFach != null) && (abiturFach <= 2))
			return (fach.bezeichnungZeugnis() + " (LK/eA)");
		else
			return this.fach.bezeichnungZeugnis();
	}

	/**
	 * Liefert die Notenpunkte eines bestimmten Halbjahrs für das Zeugnis zurück oder 'teilg.' bei der Note E3.
	 * Berücksichtigt dabei, ob es sich um ein Abiturzeugnis handelt und ob der Kurs im Block 1 gewertet wird.
	 *
	 * @param gostHalbjahr        das gymnasiale Oberstufenhalbjahr (2 bis 5), für das die Notenpunkte abgerufen werden sollen
	 * @param schuljahr           das Schuljahr, auf das sich die Notenpunkte beziehen
	 * @param ausgabeAbiturzeugnis gibt an, ob die Notenpunkte für ein Abiturzeugnis ausgegeben werden
	 * @return die Notenpunkte des angegebenen Halbjahres im zweistelligen Format, falls der Kurs
	 *         gewertet wird, oder '--', falls keine gültige Belegung vorliegt oder der Kurs
	 *         nicht auf dem Zeugnis erscheint
	 */
	private String zeugnisNotenpunkte(final int gostHalbjahr, final int schuljahr, final boolean ausgabeAbiturzeugnis) {
		if ((gostHalbjahr < 2) || (gostHalbjahr > 5)
				|| (this.halbjahresbelegungen()[gostHalbjahr] == null)
				|| (this.halbjahresbelegungen()[gostHalbjahr].note() == null))
			return "--";

		if (ausgabeAbiturzeugnis && !this.halbjahresbelegungen()[gostHalbjahr].block1kursAufZeugnis())
			return "--";

		if (this.halbjahresbelegungen()[gostHalbjahr].note() == Note.E3_TEILGENOMMEN)
			return "teilg.";

		if (this.halbjahresbelegungen()[gostHalbjahr].note().getNotenpunkteZweistellig(schuljahr) == null)
			return "--";

		if (ausgabeAbiturzeugnis && !this.halbjahresbelegungen()[gostHalbjahr].block1gewertet())
			return '(' + this.halbjahresbelegungen()[gostHalbjahr].note().getNotenpunkteZweistellig(schuljahr) + ')';

		return this.halbjahresbelegungen()[gostHalbjahr].note().getNotenpunkteZweistellig(schuljahr);
	}

	/**
	 * Gibt die Zeugnisnotenpunkte für die Q11 aus oder 'teilg.' bei der Note E3, z. B. bei einem Vertiefungskurs.
	 *
	 * @param schuljahr Das Schuljahr, für das die Notenpunkte ausgegeben werden sollen.
	 * @param ausgabeAbiturzeugnis Ein Wahrheitswert, der angibt, ob die Notenpunkte in einem Format für das Abiturzeugnis ausgegeben werden sollen.
	 * @return die Notenpunkte im zweistelligen Format, falls der Kurs gewertet wird, oder '--', falls keine gültige Belegung vorliegt oder der Kurs nicht
	 * auf dem Zeugnis erscheint oder 'teilg.' bei Note E3.
	 */
	public String zeugnisNotenpunkteQ11(final int schuljahr, final boolean ausgabeAbiturzeugnis) {
		return zeugnisNotenpunkte(2, schuljahr, ausgabeAbiturzeugnis);
	}

	/**
	 * Gibt die Zeugnisnotenpunkte für die Q12 aus oder 'teilg.' bei der Note E3, z. B. bei einem Vertiefungskurs.
	 *
	 * @param schuljahr Das Schuljahr, für das die Notenpunkte ausgegeben werden sollen.
	 * @param ausgabeAbiturzeugnis Ein Wahrheitswert, der angibt, ob die Notenpunkte in einem Format für das Abiturzeugnis ausgegeben werden sollen.
	 * @return die Notenpunkte im zweistelligen Format, falls der Kurs gewertet wird, oder '--', falls keine gültige Belegung vorliegt oder der Kurs nicht
	 * auf dem Zeugnis erscheint oder 'teilg.' bei Note E3.
	 */
	public String zeugnisNotenpunkteQ12(final int schuljahr, final boolean ausgabeAbiturzeugnis) {
		return zeugnisNotenpunkte(3, schuljahr, ausgabeAbiturzeugnis);
	}

	/**
	 * Gibt die Zeugnisnotenpunkte für die Q21 aus oder 'teilg.' bei der Note E3, z. B. bei einem Vertiefungskurs.
	 *
	 * @param schuljahr Das Schuljahr, für das die Notenpunkte ausgegeben werden sollen.
	 * @param ausgabeAbiturzeugnis Ein Wahrheitswert, der angibt, ob die Notenpunkte in einem Format für das Abiturzeugnis ausgegeben werden sollen.
	 * @return die Notenpunkte im zweistelligen Format, falls der Kurs gewertet wird, oder '--', falls keine gültige Belegung vorliegt oder der Kurs nicht
	 * auf dem Zeugnis erscheint oder 'teilg.' bei Note E3.
	 */
	public String zeugnisNotenpunkteQ21(final int schuljahr, final boolean ausgabeAbiturzeugnis) {
		return zeugnisNotenpunkte(4, schuljahr, ausgabeAbiturzeugnis);
	}

	/**
	 * Gibt die Zeugnisnotenpunkte für die Q22 aus oder 'teilg.' bei der Note E3, z. B. bei einem Vertiefungskurs.
	 *
	 * @param schuljahr Das Schuljahr, für das die Notenpunkte ausgegeben werden sollen.
	 * @param ausgabeAbiturzeugnis Ein Wahrheitswert, der angibt, ob die Notenpunkte in einem Format für das Abiturzeugnis ausgegeben werden sollen.
	 * @return die Notenpunkte im zweistelligen Format, falls der Kurs gewertet wird, oder '--', falls keine gültige Belegung vorliegt oder der Kurs nicht
	 * auf dem Zeugnis erscheint oder 'teilg.' bei Note E3.
	 */
	public String zeugnisNotenpunkteQ22(final int schuljahr, final boolean ausgabeAbiturzeugnis) {
		return zeugnisNotenpunkte(5, schuljahr, ausgabeAbiturzeugnis);
	}



	// ##### Getter #####

	/**
	 * Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null)
	 *
	 * @return Inhalt des Feldes abiturFach
	 */
	public Integer abiturFach() {
		return abiturFach;
	}

	/**
	 * Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung
	 *
	 * @return Inhalt des Feldes block1NotenpunkteDurchschnitt
	 */
	public Double block1NotenpunkteDurchschnitt() {
		return block1NotenpunkteDurchschnitt;
	}

	/**
	 * Die Punktsumme im Block I des Abiturs für die Fachbelegung
	 *
	 * @return Inhalt des Feldes block1PunktSumme
	 */
	public Integer block1PunktSumme() {
		return block1PunktSumme;
	}

	/**
	 * Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr).
	 *
	 * @return Inhalt des Feldes block2MuendlichePruefungAbweichung
	 */
	public Boolean block2MuendlichePruefungAbweichung() {
		return block2MuendlichePruefungAbweichung;
	}

	/**
	 * Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 *
	 * @return Inhalt des Feldes block2MuendlichePruefungBestehen
	 */
	public Boolean block2MuendlichePruefungBestehen() {
		return block2MuendlichePruefungBestehen;
	}

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 *
	 * @return Inhalt des Feldes block2MuendlichePruefungFreiwillig
	 */
	public Boolean block2MuendlichePruefungFreiwillig() {
		return block2MuendlichePruefungFreiwillig;
	}

	/**
	 * Das Notenkürzel der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt.
	 *
	 * @return Inhalt des Feldes block2MuendlichePruefungNote
	 */
	public Note block2MuendlichePruefungNote() {
		return block2MuendlichePruefungNote;
	}

	/**
	 * Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 *
	 * @return Inhalt des Feldes block2MuendlichePruefungReihenfolge
	 */
	public Integer block2MuendlichePruefungReihenfolge() {
		return block2MuendlichePruefungReihenfolge;
	}

	/**
	 * Die Lehrkraft, welche Prüfer im Rahmen der Abiturprüfung ist.
	 *
	 * @return Inhalt des Feldes block2Pruefer
	 */
	public ReportingLehrer block2Pruefer() {
		return block2Pruefer;
	}

	/**
	 * Die Note in der Abiturprüfung, sofern dies die Belegung eines Abiturfaches ist.
	 *
	 * @return Inhalt des Feldes block2PruefungNote
	 */
	public Note block2PruefungNote() {
		return block2PruefungNote;
	}

	/**
	 * Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 *
	 * @return Inhalt des Feldes block2Punkte
	 */
	public Integer block2Punkte() {
		return block2Punkte;
	}

	/**
	 * Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 *
	 * @return Inhalt des Feldes block2PunkteZwischenstand
	 */
	public Integer block2PunkteZwischenstand() {
		return block2PunkteZwischenstand;
	}

	/**
	 * Das Fach der gymnasialen Oberstufe, welches belegt wurde.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs
	 *
	 * @return Inhalt des Feldes halbjahresbelegungen
	 */
	public final ReportingGostAbiturFachbelegungHalbjahr[] halbjahresbelegungen() {
		return halbjahresbelegungen;
	}

	/**
	 * Die letzte Kursart der gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde
	 *
	 * @return Inhalt des Feldes letzteKursart
	 */
	public String letzteKursart() {
		return letzteKursart;
	}



}
