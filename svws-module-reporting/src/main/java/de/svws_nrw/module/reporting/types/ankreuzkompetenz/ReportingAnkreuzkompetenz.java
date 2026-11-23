package de.svws_nrw.module.reporting.types.ankreuzkompetenz;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Ankreuzkompetenz.
 * <p>
 * Der Text einer Kompetenz kann vor dem ersten Doppelpunkt eine Unterkategorie enthalten, etwa „Soziales Miteinander: hält sich an Regeln“.
 * Die Unterkategorie steht bei jeder ihrer Kompetenzen; ein Eintrag nur aus Unterkategorie und Doppelpunkt ist nicht zulässig. Weitere
 * Doppelpunkte gehören zum Kompetenztext. Ein „#“ im Text steht für einen erzwungenen Zeilenumbruch. Die berechneten Methoden zerlegen den Text danach, damit Vorlagen ihn nicht
 * selbst auswerten müssen.
 */
public class ReportingAnkreuzkompetenz extends ReportingBaseType {

	/** Die Filterkonfiguration für {@link ReportingAnkreuzkompetenz}. */
	public static final ReportingFilterung<ReportingAnkreuzkompetenz> FILTER = ReportingAnkreuzkompetenzFilter.FILTER;

	/** Gibt an, in welchem Abschnitt (1. HJ(1), 2. HJ(2), beide(0)) die Ankreuzkompetenz benutzt wird. */
	protected int abschnitt;

	/** Das Fach, dem die Ankreuzkompetenz zugeordnet ist, sofern istASV den Wert false hat. Andernfalls null. */
	protected ReportingFach fach;

	/** Die Sortierreihenfolge des Faches der Ankreuzkompetenz. */
	protected int fachSortierung;

	/** Der Text der Ankreuzkompetenz. */
	protected String floskelText;

	/** Die ID der Ankreuzkompetenz. */
	protected long id;

	/** Gibt an, ob die Ankreuzkompetenz aktiv ist. */
	protected boolean istAktiv;

	/** Gibt an, falls der Ankreuzkompetenz kein Fach zugeordnet ist, ob es sich um eine Floskel zum Arbeits- und Sozialverhalten handelt. */
	protected boolean istASV;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	protected boolean istSichtbar;

	/** Die Map der Jahrgänge, denen die Ankreuzkompetenz zugeordnet ist (Schlüssel ist die ID des Jahrgangs). */
	protected Map<Long, ReportingJahrgang> jahrgaenge;

	/** Die Schulgliederung, zu der die Ankreuzkompetenz gehört (nur relevant für Berufskolleg). */
	protected String schulgliederung;

	/** Die Sortierreihenfolge der Ankreuzkompetenz. */
	protected int sortierung;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abschnitt Gibt an, in welchem Abschnitt (1. HJ(1), 2. HJ(2), beide(3)) die Ankreuzkompetenz benutzt wird.
	 * @param fach Das Fach, dem die Ankreuzkompetenz zugeordnet ist, sofern istASV den Wert false hat. Andernfalls null.
	 * @param fachSortierung Die Sortierreihenfolge des Faches der Ankreuzkompetenz.
	 * @param floskelText Der Text der Ankreuzkompetenz.
	 * @param id Die ID der Ankreuzkompetenz.
	 * @param istAktiv Gibt an, ob die Ankreuzkompetenz aktiv ist.
	 * @param istASV Gibt an, falls der Ankreuzkompetenz kein Fach zugeordnet ist, ob es sich um eine Floskel zum Arbeits- und Sozialverhalten handelt.
	 * @param istSichtbar Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @param jahrgaenge Die Map der Jahrgänge, denen die Ankreuzkompetenz zugeordnet ist (Schlüssel ist die ID des Jahrgangs).
	 * @param schulgliederung Die Schulgliederung, zu der die Ankreuzkompetenz gehört (nur relevant für Berufskolleg).
	 * @param sortierung Die Sortierreihenfolge der Ankreuzkompetenz.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingAnkreuzkompetenz(final int abschnitt, final ReportingFach fach, final int fachSortierung, final String floskelText,
			final long id, final boolean istAktiv, final boolean istASV, final boolean istSichtbar, final Map<Long, ReportingJahrgang> jahrgaenge,
			final String schulgliederung, final int sortierung) {
		this.abschnitt = abschnitt;
		this.fach = fach;
		this.fachSortierung = fachSortierung;
		this.floskelText = ersetzeNullBlankTrim(floskelText);
		this.id = id;
		this.istAktiv = istAktiv;
		this.istASV = istASV;
		this.istSichtbar = istSichtbar;
		this.jahrgaenge = (jahrgaenge != null) ? new HashMap<>(jahrgaenge) : new HashMap<>();
		this.schulgliederung = ersetzeNullBlankTrim(schulgliederung);
		this.sortierung = sortierung;
	}


	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof final ReportingAnkreuzkompetenz other)) {
			return false;
		}
		return (id == other.id);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Unterkategorie der Kompetenz: der Text vor dem ersten Doppelpunkt. Ein „#“ wird dabei zu einem Leerzeichen.
	 *
	 * @return Die Unterkategorie; nie {@code null}, ein leerer String, wenn der Text keinen Doppelpunkt enthält.
	 */
	public String unterkategorie() {
		final int doppelpunkt = floskelText.indexOf(':');
		if (doppelpunkt < 0) {
			return "";
		}
		return String.join(" ", zeilen(floskelText.substring(0, doppelpunkt)));
	}

	/**
	 * Der Text der Kompetenz ohne Unterkategorie, in einer Zeile. Ein „#“ wird dabei zu einem Leerzeichen.
	 *
	 * @return Der Text nach dem ersten Doppelpunkt oder der ganze Text, wenn er keinen Doppelpunkt enthält; nie {@code null}.
	 */
	public String kompetenzText() {
		return String.join(" ", kompetenzZeilen());
	}

	/**
	 * Der Text der Kompetenz ohne Unterkategorie, aufgeteilt an jedem „#“ in einzelne Zeilen. Leere Zeilen entfallen.
	 *
	 * @return Die Zeilen des Kompetenztextes; nie {@code null}, eine leere Liste, wenn kein Text vorhanden ist.
	 */
	public List<String> kompetenzZeilen() {
		final int doppelpunkt = floskelText.indexOf(':');
		return zeilen((doppelpunkt < 0) ? floskelText : floskelText.substring(doppelpunkt + 1));
	}

	private static List<String> zeilen(final String text) {
		return Arrays.stream(text.split("#")).map(String::trim).filter(zeile -> !zeile.isEmpty()).toList();
	}


	// ##### Getter #####

	/**
	 * Gibt an, in welchem Abschnitt (1. HJ(1), 2. HJ(2), beide(3)) die Ankreuzkompetenz benutzt wird.
	 *
	 * @return Inhalt des Feldes abschnitt
	 */
	public int abschnitt() {
		return abschnitt;
	}

	/**
	 * Das Fach, dem die Ankreuzkompetenz zugeordnet ist, sofern istASV den Wert false hat. Andernfalls null.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die Sortierreihenfolge des Faches der Ankreuzkompetenz.
	 *
	 * @return Inhalt des Feldes fachSortierung
	 */
	public int fachSortierung() {
		return fachSortierung;
	}

	/**
	 * Der Text der Ankreuzkompetenz.
	 *
	 * @return Inhalt des Feldes floskelText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String floskelText() {
		return floskelText;
	}

	/**
	 * Die ID der Ankreuzkompetenz.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob die Ankreuzkompetenz aktiv ist.
	 *
	 * @return Inhalt des Feldes istAktiv
	 */
	public boolean istAktiv() {
		return istAktiv;
	}

	/**
	 * Gibt an, falls der Ankreuzkompetenz kein Fach zugeordnet ist, ob es sich um eine Floskel zum Arbeits- und Sozialverhalten handelt.
	 *
	 * @return Inhalt des Feldes istASV
	 */
	public boolean istASV() {
		return istASV;
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
	 * Die Map der Jahrgänge, denen die Ankreuzkompetenz zugeordnet ist (Schlüssel ist die ID des Jahrgangs).
	 *
	 * @return Inhalt des Feldes jahrgaenge; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingJahrgang> jahrgaenge() {
		return jahrgaenge;
	}

	/**
	 * Die Schulgliederung, zu der die Ankreuzkompetenz gehört (nur relevant für Berufskolleg).
	 *
	 * @return Inhalt des Feldes schulgliederung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String schulgliederung() {
		return schulgliederung;
	}

	/**
	 * Die Sortierreihenfolge der Ankreuzkompetenz.
	 *
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

}
