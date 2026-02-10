package de.svws_nrw.module.reporting.types.lerngruppen;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Abstrakte Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lerngruppe.
 * Eine Lerngruppe stellt einen Klassen- oder Kursunterricht dar und enthält die unterrichtenden Fachlehrer und das Fach.
 * Sie erweitert die ReportingSchuelergruppe und nutzt deren Lehrer- und Schülerverwaltung.
 */
public abstract class ReportingLerngruppe extends ReportingSchuelergruppe {

	/** Das Fach, in dem der Unterricht stattfindet. */
	protected ReportingFach fach;

	/** Die Map, die die Wochenstunden der Fachlehrer zu deren ID speichert. */
	protected Map<Long, Double> wochenstundenFachlehrer;

	/** Die Wochenstunden der Schüler. */
	protected int wochenstundenSchueler;

	/** Speichert zwischen, ob der Kursunterricht Schüler mit fachbezogenen Bemerkungen hat. 1 bedeutet mind. 1 Schüler, 0 bedeutet kein Schüler. */
	private int minAnzahlSchuelerMitFachbezogenerBemerkung = -1;


	/**
	 * Konstruktor zur Initialisierung einer ReportingLerngruppe-Instanz mit den übergebenen Parametern.
	 *
	 * @param id Die eindeutige ID der Lerngruppe (Klassen- oder Kurs-ID).
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, in dem die Lerngruppe existiert.
	 * @param kuerzel Das Kürzel der Lerngruppe.
	 * @param fach Das Fach der Lerngruppe.
	 * @param fachlehrer Die Fachlehrer der Lerngruppe. Der erste Lehrer der Liste ist für die Bewertung im Sinne der Leistungsdaten zuständig.
	 * @param wochenstundenFachlehrer Eine Map, die die Wochenstunden der Lehrkräfte zur jeweiligen Lehrer-ID enthält.
	 * @param schueler Die Liste der Schüler der Lerngruppe.
	 * @param wochenstundenSchueler Die Anzahl der Wochenstunden der Schüler in der Lerngruppe.
	 * @param sortierung Die Sortierreihenfolge des Listen-Eintrags.
	 */
	protected ReportingLerngruppe(final long id, final ReportingSchuljahresabschnitt schuljahresabschnitt, final String kuerzel,
			final @NotNull ReportingFach fach, final List<ReportingLehrer> fachlehrer, final Map<Long, Double> wochenstundenFachlehrer,
			final List<ReportingSchueler> schueler, final int wochenstundenSchueler, final int sortierung) {
		super(id, schuljahresabschnitt, kuerzel, fachlehrer, schueler, sortierung);
		this.fach = fach;
		this.wochenstundenFachlehrer = (wochenstundenFachlehrer != null) ? wochenstundenFachlehrer : new HashMap<>();
		this.wochenstundenSchueler = wochenstundenSchueler;

		// Validiere die Daten der Lehrer und ihrer Wochenstunden. Passe sie ggf. an, sodass sie konsistent sind und der bewertende Fachlehrer gesetzt ist.
		validiereLehrerWochenstunden();
	}

	/**
	 * Konstruktor zur Initialisierung einer ReportingLerngruppe-Instanz aus einer Klasse mit zusätzlichen Parametern.
	 *
	 * @param klasse Die Klasse, aus der die Lerngruppe erstellt werden soll.
	 * @param fachlehrer Die Fachlehrer der Lerngruppe. Der erste Lehrer der Liste ist für die Bewertung im Sinne der Leistungsdaten zuständig.
	 * @param wochenstundenFachlehrer Eine Map, die die Wochenstunden der Lehrkräfte zur jeweiligen Lehrer-ID enthält.
	 * @param schueler Die Liste der Schüler, die der Lerngruppe zugeordnet werden sollen. Ist die Liste null/empty, so werden die Schüler der Klasse gestezt.
	 * @param wochenstundenSchueler Die Anzahl der Wochenstunden der Schüler in der Lerngruppe.
	 * @param fach Das Fach der Lerngruppe.
	 */
	protected ReportingLerngruppe(final @NotNull ReportingKlasse klasse, final @NotNull ReportingFach fach, final List<ReportingLehrer> fachlehrer,
			final Map<Long, Double> wochenstundenFachlehrer, final List<ReportingSchueler> schueler, final int wochenstundenSchueler) {
		super(klasse.id(), klasse.schuljahresabschnitt(), fach.kuerzel() + "-" + klasse.kuerzel(), fachlehrer,
				((schueler != null) && !schueler.isEmpty()) ? schueler : klasse.schueler(), klasse.sortierung());
		this.fach = fach;
		this.wochenstundenFachlehrer = (wochenstundenFachlehrer != null) ? wochenstundenFachlehrer : new HashMap<>();
		this.wochenstundenSchueler = wochenstundenSchueler;

		// Validiere die Daten der Lehrer und ihrer Wochenstunden. Passe sie ggf. an, sodass sie konsistent sind und der bewertende Fachlehrer gesetzt ist.
		validiereLehrerWochenstunden();
	}

	/**
	 * Validiert die Lehrkräfte und ihre zugehörigen Wochenstunden für die Lerngruppe und passt die Daten gegebenenfalls an. Diese Methode überprüft und
	 * aktualisiert die Zuordnung zwischen den Fachlehrkräften und den in der Map gespeicherten Wochenstunden. Sie ergänzt fehlende Einträge in der Map mit
	 * einem Standardwert von 0, entfernt überflüssige Einträge aus der Map und bestimmt den Fachlehrer anhand der übergeordneten Daten, falls dieser
	 * zunächst nicht angegeben ist.
	 */
	private void validiereLehrerWochenstunden() {
		// Entferne null-Schlüssel aus der Map der Wochenstunden.
		wochenstundenFachlehrer.keySet().removeIf(Objects::isNull);
		// Sammle alle IDs der übergebenen Fachlehrer, beginnend mit dem bewertenden Lehrer.
		final List<Long> idsLehrer = new ArrayList<>();
		if (!lehrer.isEmpty())
			idsLehrer.addAll(lehrer.stream().filter(Objects::nonNull).map(ReportingLehrer::id).distinct().toList());
		// Prüfe, ob zu allen Fachlehrern Wochenstunden angegeben sind, ansonsten ergänze sie mit 0 in der Map.
		// Fachlehrer, die nur in der Map gefunden werden, werden entfernt.
		idsLehrer.forEach(idLehrer -> {
			if (!wochenstundenFachlehrer.containsKey(idLehrer))
				wochenstundenFachlehrer.put(idLehrer, 0.0);
		});
		wochenstundenFachlehrer.keySet().removeIf(idLehrer -> !idsLehrer.contains(idLehrer));
	}


	/**
	 * Das Fach der Lerngruppe.
	 *
	 * @return Inhalt des Feldes Fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Gibt die Fachlehrer der Lerngruppe zurück. Die Liste enthält alle Fachlehrer dieser Lerngruppe, wobei der erste Lehrer
	 * in der Liste für die Bewertung im Sinne der Leistungsdaten zuständig ist.
	 *
	 * @return Die Liste der Fachlehrer der Lerngruppe.
	 */
	public List<ReportingLehrer> fachlehrer() {
		return lehrer;
	}

	/**
	 * Gibt eine Auflistung der Fachlehrer der Lerngruppe als kommaseparierte Liste der Kürzel zurück.
	 * Die Auflistung beginnt mit dem Lehrer, der für die Leitung verantwortlich ist.
	 *
	 * @return Eine kommaseparierte Liste der Kürzel der Fachlehrer.
	 */
	public String auflistungFachlehrerkuerzel() {
		return auflistungLehrerkuerzel();
	}

	/**
	 * Gibt die Wochenstunden der Lehrkräfte der Lerngruppe zurück. Der Schlüssel der Map repräsentiert die ID einer Lehrkraft, und der zugehörige Wert gibt
	 * die Anzahl der Wochenstunden an, die dieser Lehrkraft zugeordnet sind.
	 *
	 * @return Eine Map, die die Wochenstunden der Lehrkräfte zur Lehrer-ID enthält.
	 */
	public Map<Long, Double> wochenstundenFachlehrer() {
		return wochenstundenFachlehrer;
	}

	/**
	 * Gibt die Anzahl der Wochenstunden für eine spezifische Lehrkraft in der Lerngruppe zurück.
	 * Wenn keine Stunden für die übergebene Lehrkraft-ID hinterlegt sind, wird 0 zurückgegeben.
	 *
	 * @param idLehrer Die eindeutige ID der Lehrkraft, für die die Wochenstunden ermittelt werden sollen.
	 *
	 * @return Die Anzahl der Wochenstunden der Lehrkraft. Ist keine Zuordnung vorhanden, wird 0 zurückgegeben.
	 */
	public double wochenstundenFachlehrer(final long idLehrer) {
		if (wochenstundenFachlehrer == null)
			return 0.0;
		return wochenstundenFachlehrer.getOrDefault(idLehrer, 0.0);
	}

	/**
	 * Gibt die Anzahl der Wochenstunden der Schüler in der Lerngruppe zurück.
	 *
	 * @return Die Anzahl der Wochenstunden der Schüler.
	 */
	public int wochenstundenSchueler() {
		return wochenstundenSchueler;
	}

	/**
	 * Liefert die Leistungsdaten eines Schülers anhand der übergebenen Schüler-ID zurück.
	 * Muss von den konkreten Unterklassen implementiert werden, da die Datenhaltung unterschiedlich sein kann.
	 *
	 * @param idSchueler Die eindeutige ID des Schülers.
	 * @return Die Leistungsdaten des Schülers oder null.
	 */
	public abstract ReportingSchuelerLeistungsdaten leistungsdatenBySchueler(Long idSchueler);

	/**
	 * Prüft, ob es fachbezogene Bemerkungen für mindestens einen Schüler in dieser Lerngruppe gibt.
	 *
	 * @return True, wenn mindestens ein Schüler eine fachbezogene Bemerkung hat; false, wenn keine solche Bemerkung vorhanden ist.
	 */
	public boolean hatSchuelerMitFachbezogenerBemerkung() {
		if (minAnzahlSchuelerMitFachbezogenerBemerkung == -1) {
			minAnzahlSchuelerMitFachbezogenerBemerkung = (this.schueler().stream()
					.anyMatch(s -> {
						final ReportingSchuelerLeistungsdaten leistungsdaten = this.leistungsdatenBySchueler(s.id());
						return (leistungsdaten != null) && !leistungsdaten.textFachbezogeneLernentwicklung().isEmpty();
					})
					? 1 : 0);
		}
		return minAnzahlSchuelerMitFachbezogenerBemerkung == 1;
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Berechnet den Hash-Code basierend auf der Fach-ID, der Schülergruppen-ID und dem istKurs-Flag.
	 *
	 * @return Den berechneten Hash-Code als Ganzzahl.
	 */
	@Override
	public int hashCode() {
		int result = 1;
		result = (31 * result) + ((fach == null) ? 0 : Long.hashCode(fach.id()));
		result = (31 * result) + Long.hashCode(id);
		result = (31 * result) + getClass().hashCode();
		return result;
	}

	/**
	 * Überprüft, ob das übergebene Objekt gleich diesem Objekt ist. Die Gleichheit wird anhand der Fach-ID, der Schülergruppen-ID und des Klassentyps
	 * überprüft.
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
		if (!(obj instanceof final ReportingLerngruppe other))
			return false;

		// Vergleich von Fach
		if (fach == null) {
			if (other.fach != null)
				return false;
		} else if (other.fach == null) {
			return false;
		} else if (fach.id() != other.fach.id()) {
			return false;
		}

		// Vergleich von ID und Klassentyp
		return (id == other.id) && (this.getClass() == other.getClass());
	}

}
