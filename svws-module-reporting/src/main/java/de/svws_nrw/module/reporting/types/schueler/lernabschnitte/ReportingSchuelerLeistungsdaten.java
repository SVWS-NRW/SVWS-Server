package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt.
 */
public class ReportingSchuelerLeistungsdaten extends ReportingBaseType {

	/** Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4). */
	protected Integer abifach;

	/** Gibt an, on das Fach auf dem Zeugnis erscheint oder nicht. */
	protected boolean aufZeugnis = true;

	/** Das Fach, auf welches sich die Leistungsdaten beziehen. */
	protected ReportingFach fach;

	/** Die zugehörige Fachlehrkraft. */
	protected ReportingLehrer fachlehrer;

	/** Die Gesamt-Anzahl der Fehlstunden für dieses Fach. */
	protected int fehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden für dieses Fach. */
	protected int fehlstundenUnentschuldigt;

	/** Gibt an, ob es sich um eine Leistung handelt, welche über das "Holen von abgeschlossenen Fächern" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben, aus welchem die Daten geholt wurden. */
	protected String geholtJahrgangAbgeschlossen;

	/** Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg). */
	protected int gewichtungAllgemeinbildend;

	/** Die ID des Leistungsdatensatzes in der Datenbank. */
	protected long id;

	/** Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht. */
	protected boolean istEpochal;

	/** Gibt an, ob die Leistung gemahnt wurde oder nicht. */
	protected boolean istGemahnt;

	/** Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren EF. */
	protected boolean istZP10oderZKEF;

	/** Der Kurs, auf welchen sich die Leistungsdaten beziehen, oder null bei Klassenunterricht. */
	protected ReportingKurs kurs;

	/** Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt. */
	protected String kursart;

	/** Der Lernabschnitt des Schülers, zu dem diese Leistungsdaten gehören. */
	protected ReportingSchuelerLernabschnitt lernabschnitt;

	/** Das Datum, wann die Leistung gemahnt wurde oder null. */
	protected String mahndatum;

	/** Das Kürzel der erteilten Note - es können auch Pseudonoten eingetragen werden (z.B. AT). */
	protected Note note;

	/** Die Berufsabschlussnote am Berufskolleg. */
	protected Note noteBerufsabschluss;

	/** Das Kürzel der erteilten Quartalsnote - es können auch Pseudonoten eingetragen werden (z.B. AT). */
	protected Note noteQuartal;

	/** Die Schulnummer einer externen Schule, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL. */
	protected Integer schulnummerExtern;

	/** Der Text für die fachbezogene Lernentwicklung des Schülers */
	protected String textFachbezogeneLernentwicklung;

	/** Die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang) */
	protected String umfangLernstandsbericht;

	/** Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID. */
	protected Map<Long, Double> wochenstundenLehrer;

	/** Die dem Schüler angerechneten Wochenstunden des Faches. */
	protected int wochenstundenSchueler;

	/** Die Lehrkräfte, die das Fach neben der Fachlehrkraft unterrichten. */
	protected List<ReportingLehrer> zusatzLehrer;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abifach Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4).
	 * @param aufZeugnis Gibt an, on das Fach auf dem Zeugnis erscheint oder nicht.
	 * @param fach Das Fach, auf welches sich die Leistungsdaten beziehen.
	 * @param fachlehrer Die zugehörige Fachlehrkraft.
	 * @param fehlstundenGesamt Die Gesamt-Anzahl der Fehlstunden für dieses Fach.
	 * @param fehlstundenUnentschuldigt Die Anzahl der unentschuldigten Fehlstunden für dieses Fach.
	 * @param geholtJahrgangAbgeschlossen Gibt an, ob es sich um eine Leistung handelt, welche über das "Holen von abgeschlossenen Fächern" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben, aus welchem die Daten geholt wurden.
	 * @param gewichtungAllgemeinbildend Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg).
	 * @param id Die ID des Leistungsdatensatzes in der Datenbank.
	 * @param istEpochal Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht.
	 * @param istGemahnt Gibt an, ob die Leistung gemahnt wurde oder nicht.
	 * @param istZP10oderZKEF Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren EF.
	 * @param kurs Der Kurs, auf welchen sich die Leistungsdaten beziehen, oder null bei Klassenunterricht.
	 * @param kursart Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt.
	 * @param lernabschnitt Der Lernabschnitt des Schülers, zu dem diese Leistungsdaten gehören.
	 * @param mahndatum Das Datum, wann die Leistung gemahnt wurde oder null.
	 * @param note Das Kürzel der erteilten Note - es können auch Pseudonoten eingetragen werden (z.B. AT).
	 * @param noteBerufsabschluss Die Berufsabschlussnote am Berufskolleg.
	 * @param noteQuartal Das Kürzel der erteilten Quartalsnote - es können auch Pseudonoten eingetragen werden (z.B. AT).
	 * @param schulnummerExtern Die Schulnummer einer externen Schule, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL.
	 * @param textFachbezogeneLernentwicklung Der Text für die fachbezogene Lernentwicklung des Schülers.
	 * @param umfangLernstandsbericht Die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang)
	 * @param wochenstundenLehrer Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID.
	 * @param wochenstundenSchueler Die dem Schüler angerechneten Wochenstunden des Faches.
	 * @param zusatzLehrer Die Lehrkräfte, die das Fach neben der Fachlehrkraft unterrichten.
	 */
	public ReportingSchuelerLeistungsdaten(final Integer abifach, final boolean aufZeugnis, final ReportingFach fach, final ReportingLehrer fachlehrer,
			final int fehlstundenGesamt, final int fehlstundenUnentschuldigt, final String geholtJahrgangAbgeschlossen, final int gewichtungAllgemeinbildend,
			final long id, final boolean istEpochal, final boolean istGemahnt, final boolean istZP10oderZKEF, final ReportingKurs kurs, final String kursart,
			final ReportingSchuelerLernabschnitt lernabschnitt, final String mahndatum, final Note note, final Note noteBerufsabschluss, final Note noteQuartal,
			final Integer schulnummerExtern, final String textFachbezogeneLernentwicklung, final String umfangLernstandsbericht,
			final Map<Long, Double> wochenstundenLehrer, final int wochenstundenSchueler, final List<ReportingLehrer> zusatzLehrer) {
		this.abifach = abifach;
		this.aufZeugnis = aufZeugnis;
		this.fach = fach;
		this.fachlehrer = fachlehrer;
		this.fehlstundenGesamt = fehlstundenGesamt;
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
		this.geholtJahrgangAbgeschlossen = geholtJahrgangAbgeschlossen;
		this.gewichtungAllgemeinbildend = gewichtungAllgemeinbildend;
		this.id = id;
		this.istEpochal = istEpochal;
		this.istGemahnt = istGemahnt;
		this.istZP10oderZKEF = istZP10oderZKEF;
		this.kurs = kurs;
		this.kursart = kursart;
		this.lernabschnitt = lernabschnitt;
		this.mahndatum = mahndatum;
		this.note = note;
		this.noteBerufsabschluss = noteBerufsabschluss;
		this.noteQuartal = noteQuartal;
		this.schulnummerExtern = schulnummerExtern;
		this.textFachbezogeneLernentwicklung = textFachbezogeneLernentwicklung;
		this.umfangLernstandsbericht = umfangLernstandsbericht;
		this.wochenstundenLehrer = wochenstundenLehrer;
		this.wochenstundenSchueler = wochenstundenSchueler;
		this.zusatzLehrer = zusatzLehrer;
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingSchuelerLeistungsdaten other))
			return false;
		return (id == other.id);
	}


	// ##### Berechnete Methoden #####
	/**
	 * Gibt eine Liste aller Lehrkräfte des Faches aus, wobei die erste die Fachlehrkraft ist.
	 *
	 * @return		Die Liste der Lehrkräfte im Faches, beginnend mit der Fachlehrkraft.
	 */
	public List<ReportingLehrer> lehrer() {
		final List<ReportingLehrer> listeLehrkraefte = new ArrayList<>();
		if (fachlehrer != null)
			listeLehrkraefte.add(fachlehrer);
		if ((zusatzLehrer != null) && !zusatzLehrer.isEmpty())
			listeLehrkraefte.addAll(zusatzLehrer);
		return listeLehrkraefte;
	}

	/**
	 * Auflistung der Lehrkräfte des Faches als kommaseparierte Liste der Kürzel.
	 *
	 * @return		Kommaseparierte Liste der Lehrkräfte, beginnend mit der Fachlehrkraft.
	 */
	public String auflistungLehrerkuerzel() {
		if (lehrer().isEmpty())
			return "";
		return this.lehrer().stream().map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));
	}

	/**
	 * Gibt die Wochenstunden zur ID einer Lehrkraft zurück.
	 *
	 * @param id	Die ID der Lehrkraft.
	 *
	 * @return		Die Wochenstunden der Lehrkraft in diesem Kurs.
	 */
	public double wochenstundenLehrerZurID(final Long id) {
		if ((id == null) || !wochenstundenLehrer.containsKey(id))
			return 0;
		return wochenstundenLehrer.get(id);
	}



	// ##### Getter #####

	/**
	 * Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4).
	 *
	 * @return Inhalt des Feldes abifach
	 */
	public Integer abifach() {
		return abifach;
	}

	/**
	 * Gibt an, on das Fach auf dem Zeugnis erscheint oder nicht.
	 *
	 * @return Inhalt des Feldes aufZeugnis
	 */
	public boolean aufZeugnis() {
		return aufZeugnis;
	}

	/**
	 * Das Fach, auf welches sich die Leistungsdaten beziehen.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die zugehörige Fachlehrkraft.
	 *
	 * @return Inhalt des Feldes fachlehrer
	 */
	public ReportingLehrer fachlehrer() {
		return fachlehrer;
	}

	/**
	 * Die Gesamt-Anzahl der Fehlstunden für dieses Fach.
	 *
	 * @return Inhalt des Feldes fehlstundenGesamt
	 */
	public int fehlstundenGesamt() {
		return fehlstundenGesamt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden für dieses Fach.
	 *
	 * @return Inhalt des Feldes fehlstundenUnentschuldigt
	 */
	public int fehlstundenUnentschuldigt() {
		return fehlstundenUnentschuldigt;
	}

	/**
	 * Gibt an, ob es sich um eine Leistung handelt, welche über das "Holen von abgeschlossenen Fächern" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben, aus welchem die Daten geholt wurden.
	 *
	 * @return Inhalt des Feldes geholtJahrgangAbgeschlossen
	 */
	public String geholtJahrgangAbgeschlossen() {
		return geholtJahrgangAbgeschlossen;
	}

	/**
	 * Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg).
	 *
	 * @return Inhalt des Feldes gewichtungAllgemeinbildend
	 */
	public int gewichtungAllgemeinbildend() {
		return gewichtungAllgemeinbildend;
	}

	/**
	 * Die ID des Leistungsdatensatzes in der Datenbank.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht.
	 *
	 * @return Inhalt des Feldes istEpochal
	 */
	public boolean istEpochal() {
		return istEpochal;
	}

	/**
	 * Gibt an, ob die Leistung gemahnt wurde oder nicht.
	 *
	 * @return Inhalt des Feldes istGemahnt
	 */
	public boolean istGemahnt() {
		return istGemahnt;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren EF.
	 *
	 * @return Inhalt des Feldes istZP10oderZKEF
	 */
	public boolean istZP10oderZKEF() {
		return istZP10oderZKEF;
	}

	/**
	 * Der Kurs, auf welchen sich die Leistungsdaten beziehen, oder null bei Klassenunterricht.
	 *
	 * @return Inhalt des Feldes kurs
	 */
	public ReportingKurs kurs() {
		return kurs;
	}

	/**
	 * Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt.
	 *
	 * @return Inhalt des Feldes kursart
	 */
	public String kursart() {
		return kursart;
	}

	/**
	 * Der Lernabschnitt des Schülers, zu dem diese Leistungsdaten gehören.
	 *
	 * @return Inhalt des Feldes lernabschnitt
	 */
	public ReportingSchuelerLernabschnitt lernabschnitt() {
		return lernabschnitt;
	}

	/**
	 * Das Datum, wann die Leistung gemahnt wurde oder null.
	 *
	 * @return Inhalt des Feldes mahndatum
	 */
	public String mahndatum() {
		return mahndatum;
	}

	/**
	 * Die erteilte Note.
	 *
	 * @return Inhalt des Feldes note
	 */
	public Note note() {
		return note;
	}

	/**
	 * Das Kürzel der erteilten Note wie zum Beispiel '3+'
	 *
	 * @return Das Kürzel der Note.
	 */
	public String noteKuerzel() {
		final String result = (this.note == null) ? null : this.note.getNoteKuerzel(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Die Punkte (zweistellig) der erteilten Note wie zum Beispiel '09'
	 *
	 * @return Die Punkte der Note.
	 */
	public String notePunkte() {
		final String result = (this.note == null) ? null : this.note.getNotenpunkteZweistellig(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Der Text der erteilten Note mit Tendenz.
	 *
	 * @return Der Text der Note.
	 */
	public String noteText() {
		final String result = (this.note == null) ? null : this.note.getNoteText(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Der Text der erteilten Note als ganze Note, z. B. für Zeugnisse.
	 *
	 * @return Der Text der ganzen Note
	 */
	public String noteTextZeugnis() {
		final String result = (this.note == null) ? null : this.note.getNoteTextZeugnis(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Die Berufsabschlussnote am Berufskolleg.
	 *
	 * @return Inhalt des Feldes noteBerufsabschluss
	 */
	public Note noteBerufsabschluss() {
		return noteBerufsabschluss;
	}

	/**
	 * Das Kürzel der Berufsabschlussnote wie zum Beispiel '3+'
	 *
	 * @return Das Kürzel der Note.
	 */
	public String noteBerufsabschlussKuerzel() {
		final String result =
				(this.noteBerufsabschluss == null) ? null : this.noteBerufsabschluss.getNoteKuerzel(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Die Punkte (zweistellig) der erteilten Berufsabschlussnote wie zum Beispiel '09'
	 *
	 * @return Die Punkte der Note.
	 */
	public String noteBerufsabschlussPunkte() {
		final String result = (this.noteBerufsabschluss == null) ? null
				: this.noteBerufsabschluss.getNotenpunkteZweistellig(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Der Text der Berufsabschlussnote mit Tendenz.
	 *
	 * @return Der Text der Note.
	 */
	public String noteBerufsabschlussText() {
		final String result =
				(this.noteBerufsabschluss == null) ? null : this.noteBerufsabschluss.getNoteText(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Der Text der Berufsabschlussnote als ganze Note, z. B. für Zeugnisse.
	 *
	 * @return Der Text der ganzen Note
	 */
	public String noteBerufsabschlussTextZeugnis() {
		final String result = (this.noteBerufsabschluss == null) ? null
				: this.noteBerufsabschluss.getNoteTextZeugnis(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Das Kürzel der erteilten Quartalsnote - es können auch Pseudonoten eingetragen werden (z.B. AT).
	 *
	 * @return Inhalt des Feldes noteQuartal
	 */
	public Note noteQuartal() {
		return noteQuartal;
	}

	/**
	 * Das Kürzel der Quartalsnote wie zum Beispiel '3+'
	 *
	 * @return Das Kürzel der Note.
	 */
	public String noteQuartalKuerzel() {
		final String result = (this.noteQuartal == null) ? null : this.noteQuartal.getNoteKuerzel(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Die Punkte (zweistellig) der erteilten Quartalsnote wie zum Beispiel '09'
	 *
	 * @return Die Punkte der Note.
	 */
	public String noteQuartalPunkte() {
		final String result =
				(this.noteQuartal == null) ? null : this.noteQuartal.getNotenpunkteZweistellig(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Der Text der Quartalsnote mit Tendenz.
	 *
	 * @return Der Text der Note.
	 */
	public String noteQuartalText() {
		final String result = (this.noteQuartal == null) ? null : this.noteQuartal.getNoteText(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Der Text der Quartalsnote als ganze Note, z. B. für Zeugnisse.
	 *
	 * @return Der Text der ganzen Note
	 */
	public String noteQuartalTextZeugnis() {
		final String result = (this.noteQuartal == null) ? null : this.noteQuartal.getNoteTextZeugnis(this.lernabschnitt().schuljahresabschnitt().schuljahr());
		return (result != null) ? result : "";
	}

	/**
	 * Die Schulnummer einer externen Schule, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL.
	 *
	 * @return Inhalt des Feldes schulnummerExtern
	 */
	public Integer schulnummerExtern() {
		return schulnummerExtern;
	}

	/**
	 * Der Text für die fachbezogene Lernentwicklung des Schülers
	 *
	 * @return Inhalt des Feldes textFachbezogeneLernentwicklung
	 */
	public String textFachbezogeneLernentwicklung() {
		return textFachbezogeneLernentwicklung;
	}

	/**
	 * Die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang)
	 *
	 * @return Inhalt des Feldes umfangLernstandsbericht
	 */
	public String umfangLernstandsbericht() {
		return umfangLernstandsbericht;
	}

	/**
	 * Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID.
	 *
	 * @return Inhalt des Feldes wochenstundenLehrkraefte
	 */
	public Map<Long, Double> wochenstundenLehrer() {
		return wochenstundenLehrer;
	}

	/**
	 * Die dem Schüler angerechneten Wochenstunden des Faches.
	 *
	 * @return Inhalt des Feldes wochenstundenSchueler
	 */
	public int wochenstundenSchueler() {
		return wochenstundenSchueler;
	}

	/**
	 * Die Lehrkräfte, die das Fach neben der Fachlehrkraft unterrichten.
	 *
	 * @return Inhalt des Feldes zusatzLehrkraefte
	 */
	public List<ReportingLehrer> zusatzLehrer() {
		return zusatzLehrer;
	}

	/**
	 * Liefert die zugewiesene Kursart für dieses Fach.
	 *
	 * @return Die Kursart der Zuweisung oder einen leeren String.
	 */
	public String zuweisungKursart() {
		if ((this.fach() == null) || (this.lernabschnitt() == null))
			return "";

		return this.lernabschnitt().zuweisungen().stream()
				.filter(z -> ((z.fach() != null) && (z.fach().id() == this.fach().id())))
				.map(ReportingSchuelerZuweisung::kursart)
				.findFirst()
				.orElse("");
	}
}
