package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Kurs.
 * Ein Kurs ist eine spezielle Lerngruppe und erweitert ReportingLerngruppe.
 */
public class ReportingKurs extends ReportingLerngruppe {

	/** Eine Map, die die Leistungsdaten zur ID des Schülers speichert. */
	private final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten = new HashMap<>();

	/** Eine Map mit den individuellen Kursarten der Schüler zur ID des Schüler. */
	private final Map<Long, String> mapSchuelerIndividuelleKursarten = new HashMap<>();

	/** Eine Map mit den individuellen Kursarten der Schüler und deren Anzahl im Kurs. */
	private final Map<String, Integer> mapStatistikIndividuelleKursarten = new HashMap<>();


	/** Ggf. die Zeugnisbezeichnung des Kurses. */
	protected String bezeichnungZeugnis;

	/** Gibt an, ob der Kurs zu einem epochalen Unterricht gehört. */
	protected boolean istEpochalunterricht;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	protected boolean istSichtbar;

	/** Die Jahrgänge, denen der Kurs zugeordnet ist. */
	protected List<ReportingJahrgang> jahrgaenge;

	/** Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird. */
	protected String kursartAllg;

	/** Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde */
	protected List<Integer> schienen;

	/** Die IDs der Schüler des Kurses. */
	protected List<Long> idsSchueler;

	/** Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet. */
	protected Integer schulnummer;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id Die ID des Kurses.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt des Kurses.
	 * @param kuerzel Das Kürzel des Kurses.
	 * @param fach Das Fach, das dem Kurs zugeordnet ist.
	 * @param kurslehrer Die Liste der Kurslehrkräfte. Der erste Lehrer der Liste ist für die Bewertung in den Leistungsdaten zuständig (Kursleitung).
	 * @param wochenstundenKurslehrer Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID.
	 * @param schueler Die Schüler des Kurses.
	 * @param sortierung Die Sortierreihenfolge des Listen-Eintrags.
	 * @param wochenstunden Die Wochenstunden des Kurses für die Schüler.
	 * @param bezeichnungZeugnis Ggf. die Zeugnisbezeichnung des Kurses.
	 * @param istEpochalunterricht Gibt an, ob der Kurs zu einem epochalen Unterricht gehört.
	 * @param istSichtbar Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @param jahrgaenge Die Jahrgänge, denen der Kurs zugeordnet ist.
	 * @param kursartAllg Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird.
	 * @param schienen Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde.
	 * @param idsSchueler Die Schüler des Kurses als Liste ihrer IDs.
	 * @param schulnummer Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet.
	 */
	public ReportingKurs(final long id, final ReportingSchuljahresabschnitt schuljahresabschnitt, final String kuerzel, final ReportingFach fach,
			final List<ReportingLehrer> kurslehrer, final Map<Long, Double> wochenstundenKurslehrer, final List<ReportingSchueler> schueler,
			final int sortierung, final int wochenstunden, final String bezeichnungZeugnis, final boolean istEpochalunterricht, final boolean istSichtbar,
			final List<ReportingJahrgang> jahrgaenge, final String kursartAllg, final List<Integer> schienen, final List<Long> idsSchueler,
			final Integer schulnummer) {
		super(id, schuljahresabschnitt, kuerzel, fach, kurslehrer, wochenstundenKurslehrer, schueler, wochenstunden, sortierung);
		this.bezeichnungZeugnis = bezeichnungZeugnis;
		this.istEpochalunterricht = istEpochalunterricht;
		this.istSichtbar = istSichtbar;
		this.jahrgaenge = jahrgaenge;
		this.kursartAllg = kursartAllg;
		this.schienen = schienen;
		this.idsSchueler = (idsSchueler != null) ? new ArrayList<>(idsSchueler.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.schulnummer = schulnummer;
	}


	/**
	 * Gibt die Jahrgänge zurück, denen der Kurs zugeordnet ist.
	 *
	 * @return Liste der Jahrgänge des Kurses.
	 */
	@Override
	public List<ReportingJahrgang> jahrgaenge() {
		return jahrgaenge;
	}


	/**
	 * Liefert die Leistungsdaten eines Schülers anhand der übergebenen Schüler-ID zurück.
	 *
	 * @param idSchueler Die eindeutige ID des Schülers, dessen Leistungsdaten abgefragt werden sollen. Wenn null übergeben wird, wird null zurückgegeben.
	 *
	 * @return Die Leistungsdaten des Schülers als {@link ReportingSchuelerLeistungsdaten} oder null wenn keine Daten vorhanden sind oder null übergeben wurde.
	 */
	@Override
	public ReportingSchuelerLeistungsdaten leistungsdatenBySchueler(final Long idSchueler) {
		if (this.mapSchuelerLeistungsdaten.isEmpty()) {
			this.schueler().forEach(s -> this.mapSchuelerLeistungsdaten.computeIfAbsent(s.id(),
					id -> s.aktiverLernabschnittInSchuljahresabschnitt(this.schuljahresabschnitt())
							.leistungsdatenZurIdKurs(this.id())));
		}

		if ((idSchueler == null) || mapSchuelerLeistungsdaten.isEmpty())
			return null;
		return this.mapSchuelerLeistungsdaten.get(idSchueler);
	}

	/**
	 * Gibt die individuelle Kursart für den angegebenen Schüler anhand seiner ID zurück.
	 * Falls keine spezifische Kursart für den Schüler vorhanden ist, wird ein leerer String zurückgegeben.
	 * Die Methode initialisiert oder aktualisiert zuerst die interne Map, bevor die Kursart abgerufen wird.
	 *
	 * @param idSchueler Die ID des Schülers, für den die individuelle Kursart ermittelt werden soll.
	 *
	 * @return Die individuelle Kursart des Schülers als String. Gibt einen leeren String zurück,
	 *         falls keine spezifische Kursart hinterlegt ist oder der idSchueler null ist
	 */
	public String schuelerIndividuelleKursart(final Long idSchueler) {
		if (idSchueler == null)
			return "";
		this.erstelleMapIndividuelleKursartenProSchueler();
		return this.mapSchuelerIndividuelleKursarten.getOrDefault(idSchueler, "");
	}

	/**
	 * Befüllt die Hashmap der individuellen Kursarten pro Schüler-ID.
	 * Ist für einen Schüler keine individuelle Kursart vorhanden, wird ein leerer String gespeichert.
	 * Die Map wird nur einmal befüllt und anschließend zwischengespeichert.
	 */
	private void erstelleMapIndividuelleKursartenProSchueler() {
		// Wenn die HashMap bereits gefüllt wurde, muss sie nicht noch einmal erstellt werden.
		if (!this.mapSchuelerIndividuelleKursarten.isEmpty())
			return;

		// Wenn keine Schüler im Kurs sind, breche die Berechnung ebenfalls ab.
		final List<ReportingSchueler> kursSchueler = this.schueler();
		if ((kursSchueler == null) || kursSchueler.isEmpty())
			return;

		for (final ReportingSchueler reportingSchueler : kursSchueler) {
			final Long idSchueler = reportingSchueler.id();
			String individuelleKursart = "";

			// Hole den aktiven Lernabschnitt und die Leistungsdaten des Schülers aus dem Schuljahresabschnitt dieses Kurses.
			final ReportingSchuelerLernabschnitt lernabschnitt = reportingSchueler.aktiverLernabschnittInSchuljahresabschnitt(this.schuljahresabschnitt);
			if (lernabschnitt == null) {
				this.mapSchuelerIndividuelleKursarten.put(idSchueler, individuelleKursart);
				continue;
			}
			final ReportingSchuelerLeistungsdaten leistungsdaten = lernabschnitt.leistungsdatenZurIdKurs(this.id);
			if (leistungsdaten != null)
				individuelleKursart = (leistungsdaten.kursart() == null) ? "" : leistungsdaten.kursart().trim();

			this.mapSchuelerIndividuelleKursarten.put(idSchueler, individuelleKursart);
		}
	}

	/**
	 * Erstellt eine Statistik der Kursarten und gibt diese als formatierte Zeichenkette zurück. Die Statistik zeigt die Anzahl der Kurse pro Kursart,
	 * getrennt durch Kommas.
	 *
	 * @return Eine formatierte Zeichenkette, die die Statistik der Kursarten darstellt, z. B. "Kursart1: Anzahl, Kursart2: Anzahl".
	 */
	public String statistikIndividuelleKursarten() {
		this.erstelleMapStatistikKursarten();
		final StringBuilder stringBuilder = new StringBuilder();
		// Sortiere die Einträge alphabetisch nach Kursart
		this.mapStatistikIndividuelleKursarten.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
				.forEach(kursart -> {
					if (!stringBuilder.isEmpty())
						stringBuilder.append(", ");
					stringBuilder.append(kursart.getKey()).append(": ").append(kursart.getValue());
				});
		return stringBuilder.toString();
	}

	/**
	 * Befüllt die HashMap mit der Statistik zu den Kursarten auf Basis der individuellen Kursarten der Schüler des Kurses aus den Leistungsdaten des
	 * Lernabschnitts dieses Kurses. Die HashMap wird nur einmal berechnet und anschließend zwischengespeichert.
	 */
	private void erstelleMapStatistikKursarten() {
		// Wenn die HashMap bereits gefüllt wurde, muss nicht noch einmal die Statistik erstellt werden.
		if (!this.mapStatistikIndividuelleKursarten.isEmpty())
			return;

		// Stelle sicher, dass die individuellen Kursarten pro Schüler ermittelt wurden
		this.erstelleMapIndividuelleKursartenProSchueler();

		// Wenn keine Schüler im Kurs sind, breche die Berechnung ebenfalls ab.
		final List<ReportingSchueler> kursSchueler = this.schueler();
		if ((kursSchueler == null) || kursSchueler.isEmpty())
			return;

		// Zähle die nicht-leeren Kursarten aus der vorbereiteten Map
		for (final ReportingSchueler reportingSchueler : kursSchueler) {
			if (reportingSchueler == null)
				continue;
			final String individuelleKursart = this.mapSchuelerIndividuelleKursarten.getOrDefault(reportingSchueler.id(), "");
			if ((individuelleKursart != null) && !individuelleKursart.isEmpty())
				this.mapStatistikIndividuelleKursarten.merge(individuelleKursart, 1, Integer::sum);
		}
	}


	// ##### Getter #####

	/**
	 * Ggf. die Zeugnisbezeichnung des Kurses.
	 *
	 * @return Inhalt des Feldes bezeichnungZeugnis
	 */
	public String bezeichnungZeugnis() {
		return bezeichnungZeugnis;
	}

	/**
	 * Gibt an, ob der Kurs zu einem epochalen Unterricht gehört.
	 *
	 * @return Inhalt des Feldes istEpochalunterricht
	 */
	public boolean istEpochalunterricht() {
		return istEpochalunterricht;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 *
	 * @return Inhalt des Feldes istSichtbar
	 */
	public boolean istSichtbar() {
		return istSichtbar;
	}

	/**
	 * Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird.
	 *
	 * @return Inhalt des Feldes kursartAllg
	 */
	public String kursartAllg() {
		return kursartAllg;
	}

	/**
	 * Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde.
	 *
	 * @return Inhalt des Feldes schienen
	 */
	public List<Integer> schienen() {
		return schienen;
	}

	/**
	 * Die Schüler des Kurses als Liste ihrer IDs.
	 *
	 * @return Inhalt des Feldes idsSchueler
	 */
	public List<Long> idsSchueler() {
		return idsSchueler;
	}

	/**
	 * Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet.
	 *
	 * @return Inhalt des Feldes schulnummer
	 */
	public Integer schulnummer() {
		return schulnummer;
	}

	/**
	 * Gibt eine Liste aller Lehrkräfte des Kurses aus, wobei die erste die Kursleitung ist.
	 *
	 * @return Die Liste der Lehrkräfte im Kurs, beginnend mit der Kursleitung.
	 */
	public List<ReportingLehrer> kurslehrer() {
		return lehrer();
	}

	/**
	 * Die Lehrkraft, die für die Bewertung in den Leistungsdaten zuständig ist (Kursleitung).
	 *
	 * @return Der bewertende Kurslehrer.
	 */
	public ReportingLehrer kursleitung() {
		return leitenderLehrer();
	}

	/**
	 * Die Lehrkräfte, die den Kurs neben der Kursleitung unterrichten.
	 *
	 * @return Weitere Lehrer neben der bewertenden Lehrkraft.
	 */
	public List<ReportingLehrer> zusatzKurslehrer() {
		return zusatzLehrer();
	}

	/**
	 * Auflistung der Lehrkräfte des Kurses als kommaseparierte Liste der Kürzel.
	 *
	 * @return Kommaseparierte Liste der Lehrkräfte, beginnend mit der Kursleitung.
	 */
	public String auflistungKurslehrerkuerzel() {
		return auflistungLehrerkuerzel();
	}


	/**
	 * Die Wochenstunden des Kurses für die Schüler.
	 *
	 * @return Inhalt des Feldes Wochenstunden
	 */
	public int wochenstunden() {
		return wochenstundenSchueler;
	}

	/**
	 * Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID.
	 * Delegiert an die Basisklasse ReportingLerngruppe.
	 *
	 * @return Inhalt des Feldes wochenstundenLehrkraefte
	 */
	public Map<Long, Double> wochenstundenLehrkraefte() {
		return wochenstundenFachlehrer;
	}

	/**
	 * Gibt die Wochenstunden zur ID einer Lehrkraft zurück.
	 * Delegiert an die Basisklasse ReportingLerngruppe.
	 *
	 * @param id Die ID der Lehrkraft.
	 *
	 * @return Die Wochenstunden der Lehrkraft in diesem Kurs.
	 */
	public double wochenstundenLehrerZurID(final Long id) {
		if (id == null)
			return 0.0;
		return wochenstundenFachlehrer(id);
	}

}
