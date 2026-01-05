package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Abstrakte Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schülergruppe.
 * Eine Schülergruppe ist eine Gruppe von Schülern mit den sie betreuenden Lehrkräften, aber ohne Unterrichtsbezug, da kein Fach zugeordnet ist.
 * Eine Klasse ist z. B. eine Schülergruppe.
 * Wenn ein Fach und Wochenstunden ergänzt werden, so ergibt sich eine Lerngruppe. Lerngruppen sind Klassen- und Kursunterrichte.
 */
public abstract class ReportingSchuelergruppe extends ReportingBaseType {

	/** Die ID der Schülergruppe (Klassen- oder Kurs-ID). */
	protected long id;

	/** Der Schuljahresabschnitt der Schülergruppe. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Das Kürzel der Schülergruppe. Dies ist das Klassen- oder Kurskürzel. */
	protected String kuerzel;

	/** Die Liste der Lehrer, die die Schülergruppe betreuen (Klassen- oder Kurslehrer). Die erste Lehrkraft wird als Gruppenleitung interpretiert. */
	protected List<ReportingLehrer> lehrer;

	/** Eine Matrix mit Leistungsdaten für die Schüler der Gruppe basierend auf dem Schuljahresabschnitt. */
	protected ReportingSchuelerLeistungsdatenMatrix schuelerLeistungsdatenMatrix = null;

	/** Die Liste der Schüler der Schülergruppe. */
	protected List<ReportingSchueler> schueler;

	/** Eine Liste der Klassen, deren Schüler in dieser Schülergruppe sind. */
	private final List<ReportingKlasse> klassen = new ArrayList<>();

	/** Die Sortierreihenfolge des Listen-Eintrags. */
	private final int sortierung;


	/**
	 * Konstruktor zur Initialisierung einer ReportingSchülergruppe-Instanz.
	 *
	 * @param id Die eindeutige ID der Schülergruppe.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, in dem die Schülergruppe existiert.
	 * @param kuerzel Das Kürzel der Schülergruppe.
	 * @param lehrer Die Liste der Lehrer, die die Schülergruppe betreuen. Die erste Lehrkraft wird als Gruppenleitung interpretiert.
	 * @param schueler Die Liste der Schüler der Schülergruppe.
	 * @param sortierung Die Sortierreihenfolge des Listen-Eintrags.
	 */
	protected ReportingSchuelergruppe(final long id, final ReportingSchuljahresabschnitt schuljahresabschnitt, final String kuerzel,
			final List<ReportingLehrer> lehrer, final List<ReportingSchueler> schueler, final int sortierung) {
		this.id = id;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.kuerzel = ersetzeNullBlankTrim(kuerzel);
		this.lehrer = (lehrer != null) ? new ArrayList<>(lehrer.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.schueler = (schueler != null) ? new ArrayList<>(schueler.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.sortierung = sortierung;
	}


	// ##### Zu implementierende Methoden #####

	/**
	 * Gibt die Jahrgänge der Schülergruppe zurück.
	 *
	 * @return Liste der Jahrgänge.
	 */
	public abstract List<ReportingJahrgang> jahrgaenge();


	// ##### Getter #####

	/**
	 * Die ID der Schülergruppe (Klassen- oder Kurs-ID).
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Der Schuljahresabschnitt der Schülergruppe.

	 *
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Das Kürzel der Schülergruppe.
	 *
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Die Liste der Schüler der Schülergruppe.
	 *
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Die Sortierreihenfolge des Listen-Eintrags.
	 *
	 * @return Inhalt des Feldes Sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Gibt eine Liste aller Lehrkräfte der Schülergruppe zurück, wobei die erste die Leitung ist.
	 * Da diese Methode in den abgeleiteten Klassen eine andere Bezeichnung erhält, sind diese Methoden protected.
	 *
	 * @return Die Liste der Lehrkräfte in der Schülergruppe, beginnend mit der Leitung.
	 */
	protected List<ReportingLehrer> lehrer() {
		return lehrer;
	}

	/**
	 * Der Lehrer, der mit der Leitung der Schülergruppe betraut ist (z. B. Klassen- oder Kursleitung). Dies ist als erster Lehrer in der Liste enthalten.
	 * Da diese Methode in den abgeleiteten Klassen eine andere Bezeichnung erhält, sind diese Methoden protected.
	 *
	 * @return Der erste Lehrer der Lehrerliste.
	 */
	protected ReportingLehrer leitenderLehrer() {
		return lehrer.isEmpty() ? null : lehrer.getFirst();
	}

	/**
	 * Die Liste der zusätzlichen Lehrkräfte der Schülergruppe.
	 * Da diese Methode in den abgeleiteten Klassen eine andere Bezeichnung erhält, sind diese Methoden protected.
	 *
	 * @return Alle Lehrkräfte außer der leitenden Lehrkraft
	 */
	protected List<ReportingLehrer> zusatzLehrer() {
		if (lehrer.size() > 1)
			return lehrer.subList(1, lehrer.size());
		else
			return new ArrayList<>();
	}

	/**
	 * Auflistung der Lehrkräfte der Schülergruppe als kommaseparierte Liste der Kürzel.
	 * Da diese Methode in den abgeleiteten Klassen eine andere Bezeichnung erhält, sind diese Methoden protected.
	 *
	 * @return Kommaseparierte Liste der Lehrerkürzel, beginnend mit der Leitung.
	 */
	protected String auflistungLehrerkuerzel() {
		final List<ReportingLehrer> listeLehrkraefte = lehrer();
		if (listeLehrkraefte.isEmpty())
			return "";
		return listeLehrkraefte.stream().map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));
	}

	/**
	 * Gibt eine kommaseparierte Auflistung der Schülernamen zurück, wobei die Namen im Format "Vorname Nachname" dargestellt sind.
	 *
	 * @return Eine kommaseparierte Zeichenkette mit den Namen der Schüler im Format "Vorname Nachname". Wenn die Schülerliste leer ist, wird ein leerer
	 *         String zurückgegeben.
	 */
	public String auflistungSchuelerVornameName() {
		if (schueler.isEmpty())
			return "";
		return schueler.stream().map(ReportingSchueler::vornameNachname).collect(Collectors.joining(", "));
	}

	/**
	 * Gibt eine kommaseparierte Auflistung der Schülernamen zurück, wobei die Namen im Format "Vornamen Nachname" dargestellt sind.
	 *
	 * @return Eine kommaseparierte Zeichenkette mit den Namen der Schüler im Format "Vornamen Nachname". Wenn die Schülerliste leer ist, wird ein leerer
	 *         String zurückgegeben.
	 */
	public String auflistungSchuelerVornamenName() {
		if (schueler.isEmpty())
			return "";
		return schueler.stream().map(ReportingSchueler::vornamenNachname).collect(Collectors.joining(", "));
	}

	/**
	 * Gibt eine separierte Auflistung der Schülernamen zurück, wobei die Namen im Format "Nachname, Vorname" dargestellt sind.
	 *
	 * @return Eine separierte Zeichenkette mit den Namen der Schüler im Format "Nachname, Vorname". Wenn die Schülerliste leer ist, wird ein leerer
	 *         String zurückgegeben.
	 */
	public String auflistungSchuelerNameVorname() {
		if (schueler.isEmpty())
			return "";
		return schueler.stream().map(ReportingSchueler::nachnameVorname).collect(Collectors.joining("; "));
	}

	/**
	 * Gibt eine kommaseparierte Auflistung der Schülernamen zurück, wobei die Namen im Format "Nachname, Vornamen" dargestellt sind.
	 *
	 * @return Eine separierte Zeichenkette mit den Namen der Schüler im Format "Nachname, Vornamen". Wenn die Schülerliste leer ist, wird ein leerer
	 *         String zurückgegeben.
	 */
	public String auflistungSchuelerNameVornamen() {
		if (schueler.isEmpty())
			return "";
		return schueler.stream().map(ReportingSchueler::nachnameVornamen).collect(Collectors.joining("; "));
	}


	/**
	 * Gibt die Klassen der in der Schülergruppe enthaltenen Schüler zurück.
	 *
	 * @return Liste mit den Klassen der Schüler.
	 */
	public List<ReportingKlasse> klassen() {
		final List<ReportingSchueler> reportingSchueler = this.schueler();
		if (klassen.isEmpty() && (reportingSchueler != null) && !reportingSchueler.isEmpty()) {
			final List<ReportingKlasse> result = new ArrayList<>();
			for (final ReportingSchueler s : reportingSchueler) {
				final ReportingSchuelerLernabschnitt lernabschnitt = s.aktiverLernabschnittInSchuljahresabschnitt(this.schuljahresabschnitt);
				if ((lernabschnitt != null) && (lernabschnitt.klasse() != null))
					result.add(lernabschnitt.klasse());
			}
			if (!result.isEmpty()) {
				this.klassen.addAll(result.stream().distinct().sorted(Comparator.comparing(ReportingKlasse::kuerzel)).toList());
			}
		}
		return this.klassen;
	}

	/**
	 * Auflistung der Klassen des Kurses als kommaseparierte Liste der Kürzel.
	 *
	 * @return Kommaseparierte Liste der Klassen der Schüler des Kurses
	 */
	public String auflistungKlassen() {
		final List<ReportingKlasse> listeKlassen = this.klassen();
		if (listeKlassen.isEmpty())
			return "";
		return listeKlassen.stream().filter(Objects::nonNull).map(ReportingKlasse::kuerzel).collect(Collectors.joining(","));
	}

	/**
	 * Auflistung der Jahrgänge der Schülergruppe als kommaseparierte Liste der Kürzel.
	 *
	 * @return Kommaseparierte Liste der Jahrgänge der Schülergruppe
	 */
	public String auflistungJahrgaenge() {
		final List<ReportingJahrgang> listeJahrgaenge = this.jahrgaenge();
		if (listeJahrgaenge.isEmpty())
			return "";
		return listeJahrgaenge.stream().filter(Objects::nonNull).map(ReportingJahrgang::kuerzel).collect(Collectors.joining(","));
	}


	/**
	 * Erstellt eine Leistungsdaten-Matrix für die Schüler dieser Gruppe basierend auf dem Schuljahresabschnitt der Gruppe.
	 * Es werden die Standardsortierungen für Fächer und Schüler verwendet und alle Fächer einbezogen.
	 *
	 * @return Die Leistungsdaten-Matrix für diese Schülergruppe.
	 */
	public ReportingSchuelerLeistungsdatenMatrix schuelerLeistungsdatenMatrix() {
		if (this.schuelerLeistungsdatenMatrix == null)
			this.schuelerLeistungsdatenMatrix = new ReportingSchuelerLeistungsdatenMatrix(this.schueler(), this.schuljahresabschnitt(), null, null, null);
		return schuelerLeistungsdatenMatrix;
	}


	/**
	 * Erstellt aus den Geschlechtern der Schüler eine Statistik in der Form (m/w/d).
	 *
	 * @return Statistik in der Form (m/w/d).
	 */
	public String statistikGeschlechter() {
		if ((schueler == null) || schueler.isEmpty())
			return "(0/0/0)";
		final long anzahlM = schueler.stream().filter(s -> "m".equalsIgnoreCase(s.geschlecht().kuerzel)).count();
		final long anzahlW = schueler.stream().filter(s -> "w".equalsIgnoreCase(s.geschlecht().kuerzel)).count();
		final long anzahlD = schueler.stream().filter(s -> "d".equalsIgnoreCase(s.geschlecht().kuerzel)).count();
		return String.format("(%d/%d/%d)", anzahlM, anzahlW, anzahlD);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Berechnet den Hash-Code basierend auf der eindeutigen ID der Schülergruppe.
	 *
	 * @return Den berechneten Hash-Code als Ganzzahl.
	 */
	@Override
	public int hashCode() {
		int result = 1;
		result = (31 * result) + Long.hashCode(id);
		result = (31 * result) + getClass().hashCode();
		return result;
	}

	/**
	 * Überprüft, ob das übergebene Objekt gleich diesem Objekt ist. Die Gleichheit wird anhand der ID und des Klassentyps überprüft.
	 *
	 * @param obj Das Objekt, mit dem dieses Objekt verglichen werden soll.
	 *
	 * @return Ergibt true, wenn die Objekte gleich sind, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingSchuelergruppe other))
			return false;

		// Vergleich von ID und Klassentyp
		return (id == other.id) && (this.getClass() == other.getClass());
	}
}
