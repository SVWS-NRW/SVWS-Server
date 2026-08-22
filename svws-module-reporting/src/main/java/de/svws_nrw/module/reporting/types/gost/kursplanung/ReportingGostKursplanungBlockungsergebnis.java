package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis.</p>
 * <p>Sie enthält die Daten zu einem Blockungsergebnis, d. h. zu den Anzahlen, den Schienen und Kursen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungBlockungsergebnis extends ReportingBaseType {

	/** Die Filterkonfiguration für {@link ReportingGostKursplanungBlockungsergebnis}. */
	public static final ReportingFilterung<ReportingGostKursplanungBlockungsergebnis> FILTER = ReportingGostKursplanungBlockungsergebnisFilter.FILTER;

	/** Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet */
	protected int abiturjahr;

	/** Anzahl der Dummy-Schüler im Ergebnis */
	protected int anzahlDummy;

	/** Anzahl der externen Schüler im Ergebnis */
	protected int anzahlExterne;

	/** Maximale Anzahl an Kursen pro Schiene über alle Schienen */
	protected int anzahlMaxKurseProSchiene;

	/** Anzahl der Schienen */
	protected int anzahlSchienen;

	/** Anzahl der Schüler im Ergebnis */
	protected int anzahlSchueler;

	/** Bezeichnung des Blockungsergebnisses */
	protected String bezeichnung;

	/** Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID */
	protected Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik;

	/** Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe der Hauptliste enthalten sind. */
	protected final Predicate<ReportingSchueler> filterSchueler;

	/** Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind. */
	protected final Predicate<ReportingGostKursplanungKurs> filterKurse;

	/** Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses */
	protected GostHalbjahr gostHalbjahr;

	/** ID des Blockungsergebnisses */
	protected long id;

	/** Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet. */
	protected List<ReportingGostKursplanungKurs> kurse;

	/** Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet. */
	protected List<ReportingGostKursplanungSchiene> schienen;

	/** Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet. */
	protected List<ReportingSchueler> schueler;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahr               Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet
	 * @param anzahlDummy              Anzahl der Dummy-Schüler im Ergebnis
	 * @param anzahlExterne            Anzahl der externen Schüler im Ergebnis
	 * @param anzahlMaxKurseProSchiene Maximale Anzahl an Kursen pro Schiene über alle Schienen
	 * @param anzahlSchienen           Anzahl der Schienen
	 * @param anzahlSchueler           Anzahl der Schüler im Ergebnis
	 * @param bezeichnung              Bezeichnung des Blockungsergebnisses
	 * @param fachwahlstatistik        Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID
	 * @param gostHalbjahr             Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses
	 * @param id                       ID des Blockungsergebnisses
	 * @param kurse                    Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet.
	 * @param schienen                 Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet.
	 * @param schueler                 Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet.
	 * @param filterSchueler           Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe der Hauptliste enthalten sind.
	 * @param filterKurse              Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingGostKursplanungBlockungsergebnis(final int abiturjahr, final int anzahlDummy, final int anzahlExterne, final int anzahlMaxKurseProSchiene,
			final int anzahlSchienen, final int anzahlSchueler, final String bezeichnung,
			final Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik, final GostHalbjahr gostHalbjahr, final long id,
			final List<ReportingGostKursplanungKurs> kurse, final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingGostKursplanungKurs> filterKurse) {
		this.abiturjahr = abiturjahr;
		this.anzahlDummy = anzahlDummy;
		this.anzahlExterne = anzahlExterne;
		this.anzahlMaxKurseProSchiene = anzahlMaxKurseProSchiene;
		this.anzahlSchienen = anzahlSchienen;
		this.anzahlSchueler = anzahlSchueler;
		this.bezeichnung = ersetzeNullBlankTrim(bezeichnung);
		this.fachwahlstatistik = (fachwahlstatistik != null) ? new HashMap<>(fachwahlstatistik) : new HashMap<>();
		this.gostHalbjahr = gostHalbjahr;
		this.id = id;
		this.kurse = (kurse != null) ? new ArrayList<>(kurse.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.schienen = (schienen != null) ? new ArrayList<>(schienen.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.schueler = (schueler != null) ? new ArrayList<>(schueler.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();

		this.filterSchueler = (filterSchueler == null) ? s -> true : filterSchueler;
		this.filterKurse = (filterKurse == null) ? k -> true : filterKurse;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Gibt den Kurs zur übergebenen ID zurück, sofern er nicht durch den Filter ausgeschlossen ist.
	 *
	 * @param  id 	Die ID des Kurses
	 *
	 * @return 		Der Kurs zur ID oder null, wenn nicht vorhanden bzw. herausgefiltert.
	 */
	public ReportingGostKursplanungKurs kurs(final long id) {
		return (id < 0) ? null : this.kurse.stream().filter(filterKurse).filter(k -> id == k.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt den Schüler zur übergebenen ID zurück, sofern er nicht durch den Filter ausgeschlossen ist.
	 *
	 * @param  id 	Die ID des Schülers
	 *
	 * @return 		Der Schüler zur ID oder null, wenn nicht vorhanden bzw. herausgefiltert.
	 */
	public ReportingSchueler schueler(final long id) {
		return (id < 0) ? null : this.schueler.stream().filter(filterSchueler).filter(s -> id == s.id()).findFirst().orElse(null);
	}


	// ##### Getter #####

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet
	 *
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Anzahl der Dummy-Schüler im Ergebnis
	 *
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der externen Schüler im Ergebnis
	 *
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Maximale Anzahl an Kursen über alle Schienen
	 *
	 * @return Inhalt des Feldes anzahlMaxKurseProSchiene
	 */
	public int anzahlMaxKurseProSchiene() {
		return anzahlMaxKurseProSchiene;
	}

	/**
	 * Anzahl der Schienen
	 *
	 * @return Inhalt des Feldes anzahlSchienen
	 */
	public int anzahlSchienen() {
		return anzahlSchienen;
	}

	/**
	 * Anzahl der Schüler im Ergebnis, wie sie die Blockungsdaten führen. Diese Zahl ist von Filterung und Ladefehlern unberührt und deshalb die Quelle für
	 * das Feld {@code angefordert} des Hinweis-Headers; die Liste aus {@link #schueler()} taugt dafür nicht, denn sie ist bereits verkürzt.
	 *
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID
	 *
	 * @return Inhalt des Feldes fachwahlstatistik; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik() {
		return fachwahlstatistik;
	}

	/**
	 * Bezeichnung des Blockungsergebnisses
	 *
	 * @return Inhalt des Feldes bezeichnung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses
	 *
	 * @return Inhalt des Feldes gostHalbjahr; kann {@code null} sein, wenn kein Halbjahr zugeordnet ist.
	 */
	public GostHalbjahr gostHalbjahr() {
		return gostHalbjahr;
	}

	/**
	 * ID des Blockungsergebnisses
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet. Es werden nur Kurse zurückgegeben,
	 * die das konfigurierte Filter-Prädikat erfüllen.
	 *
	 * @return Liste der Kurse
	 */
	public List<ReportingGostKursplanungKurs> kurse() {
		return this.kurse.stream().filter(filterKurse).toList();
	}

	/**
	 * Gibt die Anzahl aller Kurse des Blockungsergebnisses zurück - vor der Filterung der Ausgabe. Aus ihr entsteht das Feld {@code angefordert} des
	 * Hinweis-Headers der Kurs-Sichtweise.
	 *
	 * @return Die Anzahl aller Kurse des Blockungsergebnisses.
	 */
	public int anzahlKurseVorhanden() {
		return this.kurse.size();
	}

	/**
	 * Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet.
	 *
	 * @return Inhalt des Feldes schienen; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingGostKursplanungSchiene> schienen() {
		return schienen.stream().toList();
	}

	/**
	 * Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet. Es werden nur Schüler
	 * zurückgegeben, die das konfigurierte Filter-Prädikat erfüllen. Für die Zahl der ursprünglich angeforderten Schüler ist deshalb
	 * {@link #anzahlSchueler()} zu verwenden.
	 *
	 * @return Liste der Schüler
	 */
	public List<ReportingSchueler> schueler() {
		return this.schueler.stream().filter(filterSchueler).toList();
	}



	/**
	 * Setzt die Liste der Kurse als gefilterte Defensivkopie. Wird vom Repository erst NACH dem vollständigen Aufbau
	 * der Kurs-Proxys aufgerufen, da diese eine Rückreferenz auf dieses Blockungsergebnis benötigen und daher erst
	 * nach dessen Konstruktion erzeugt werden können.
	 *
	 * @param kurse Die vollständig aufgebaute Liste der Kurse.
	 */
	public void setKurse(final List<ReportingGostKursplanungKurs> kurse) {
		this.kurse = (kurse != null) ? new ArrayList<>(kurse.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
	}

	/**
	 * Setzt die Liste der Schienen als gefilterte Defensivkopie (analog zu {@link #setKurse}).
	 *
	 * @param schienen Die vollständig aufgebaute Liste der Schienen.
	 */
	public void setSchienen(final List<ReportingGostKursplanungSchiene> schienen) {
		this.schienen = (schienen != null) ? new ArrayList<>(schienen.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
	}

}
