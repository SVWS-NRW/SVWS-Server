package de.svws_nrw.core.types.bk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die Aufgabenfelder im Beruflichen Gymnasium entsprechend der Auflistung aus der
 * APO-BK Anlage D § 4 Abs. (4)
 */
public enum BKGymAufgabenfeld {

	/** Aufgabenfeld sprachlich literarisch künstlerisch */
	SPRACHLICH_LITERARISCH_KUENSTLERISCH("I",
			"Deutsch", "Englisch", "Französisch", "Griechisch", "Italienisch", "Kunst", "Literatur", "Musik",
			"Latein", "Niederländisch", "Russisch", "Spanisch"),

	/** Aufgabenfeld gesellschaftswissenschaftlich */
	GESELLSCHAFTSWISSENSCHAFTLICH("II",
			"Arbeits- und Betriebslehre", "Außenhandel", "Betriebsorganisation", "Betriebswirtschaftslehre",
			"Betriebswirtschaftslehre mit Rechnungswesen", "Betriebswirtschaftslehre mit Rechnungswesen und Controlling",
			"Betriebswirtschaftslehre mit Rechnungswesen/Wirtschaftsrecht", "Didaktik und Methodik", "Erdkunde",
			"Erziehungswissenschaften", "Geschichte", "Gesellschaftslehre mit Geschichte", "Business Communication",
			"Marketing", "Organisationslehre", "Philosophie", "Politik/Geschichte", "Psychologie", "Rechtskunde",
			"Recht und Verwaltung", "Sozialpädagogik", "Soziologie", "Spezielle Betriebswirtschaftslehre", "Global Studies",
			"Volkswirtschaftslehre", "Volks- und Betriebswirtschaftslehre", "Wirtschaftsgeografie", "Wirtschaftslehre",
			"Wirtschaftslehre des Haushalts", "Wirtschaftsrecht"),

	/** Aufgabenfeld mathematisch naturwissenschaftlich */
	MATHEMATISCH_NATURWISSENSCHAFTLICH("III",
			"Angewandte Informatik", "Anwendungsentwicklung", "Bautechnik", "Bauplanungstechnik,", "Betriebsinformatik",
			"Biochemie", "Biologie", "Biologietechnik", "Chemie", "Chemietechnik", "Datentechnik", "Datenverarbeitungstechnik",
			"Elektrotechnik", "Energietechnik", "Ernährungslehre", "Ernährung", "Gestaltungstechnik", "Gesundheit", "Grafik-Design",
			"Haushaltstechnik", "Holztechnik", "Informatik", "Ingenieurwissenschaften", "Konstruktions- und Fertigungstechnik",
			"Maschinenbautechnik", "Maschinentechnik", "Mathematik", "Nachrichtentechnik", "Physik", "Physikalische Chemie",
			"Physiktechnik", "Softwareentwicklung", "Technische Informatik", "Technische Kommunikation", "Technisches Zeichnen",
			"Textil- und Bekleidungstechnik", "Umweltschutztechnik", "Umwelttechnik", "Werkstofftechnik", "Wirtschaftsinformatik",
			"Wirtschaftsinformatik/Organisationslehre"),

	/** Fächer ohne Aufgabenfeld */
	OHNE_AUFGABENFELD("0", "Religionslehre", "Sport", "Sport/Gesundheitsförderung"),

	/** Naturwissenschaften */
	NATURWISSENSCHAFTEN("NW", "Biologie", "Chemie", "Physik");


	/** Eine Map, welche dem zulässigen Fach sein Aufgabenfeld zuordnet. */
	private static final @NotNull Map<String, BKGymAufgabenfeld> _mapAufgabenfeldByFach = new HashMap<>();

	/** Eine Map, welche dem Kuerzel sein Aufgabenfeld zuordnet. */
	private static final @NotNull Map<String, BKGymAufgabenfeld> _mapAufgabenfeldByKuerzel = new HashMap<>();

	/** Das Kürzel für das Aufgabenfeld */
	private final @NotNull String kuerzel;

	/** Eine Liste der Fächer dieses Aufgabenfeldes */
	private final @NotNull ArrayList<String> fachbezeichnungen = new ArrayList<>();


	/**
	 * Erstellt einen neues Aufgabenfeld mit den übergebenen Fächern
	 *
	 * @param kuerzel             Kürzel des Aufgabenfeldes
	 * @param fachbezeichnungen   die Fächer des Aufgabenfeldes
	 */
	BKGymAufgabenfeld(final @NotNull String kuerzel, final @NotNull String... fachbezeichnungen) {
		this.kuerzel = kuerzel;
		for (final String fach : fachbezeichnungen)
			this.fachbezeichnungen.add(fach);
	}


	/**
	 * Initialisiert die Map von den Fächern auf das zugehörige Aufgabenfeld, wenn es noch nicht geschehen ist.
	 */
	private static void initMapAufgabenfeldByFach() {
		if (_mapAufgabenfeldByFach.size() == 0)
			for (final @NotNull BKGymAufgabenfeld feld : BKGymAufgabenfeld.values())
				for (final String fachbezeichnung : feld.fachbezeichnungen)
					_mapAufgabenfeldByFach.put(fachbezeichnung, feld);
	}


	/**
	 * Initialisiert die Map von den Kürzeln auf das zugehörige Aufgabenfeld, wenn es noch nicht geschehen ist.
	 */
	private static void initMapAufgabenfeldByKuerzel() {
		if (_mapAufgabenfeldByKuerzel.size() == 0)
			for (final @NotNull BKGymAufgabenfeld feld : BKGymAufgabenfeld.values())
				_mapAufgabenfeldByKuerzel.put(feld.kuerzel, feld);
	}


	/**
	 * Gibt die Liste der Fächer des Aufgabenfeldes zurück.
	 *
	 * @return die Liste der Fächer des Aufgabenfeldes
	 */
	public @NotNull List<String> getFaecher() {
		return this.fachbezeichnungen;
	}


	/**
	 * Prüft, ob das Fach mit der übergebenen Fachbezeichnung zu diesem Aufgabenfeld gehört.
	 *
	 * @param fachbezeichnung   die Fachbezeichnung
	 *
	 * @return true, falls das Fach zu dem Aufgabenfeld gehört, sonst false
	 */
	public boolean hatFachbezeichnung(final String fachbezeichnung) {
		initMapAufgabenfeldByFach();
		if (fachbezeichnung == null)
			return false;
		return _mapAufgabenfeldByFach.get(fachbezeichnung) == this;
	}


	/**
	 * Liefert zu einem Kürzel das zugehörige Aufgabenfeld oder null
	 *
	 * @param kuerzel   das Kürzel eines Aufgabenfeldes
	 *
	 * @return das Aufgabenfeld oder null
	 */
	public static BKGymAufgabenfeld getAufgabenfeldFromKuerzel(final @NotNull String kuerzel) {
		initMapAufgabenfeldByKuerzel();
		return _mapAufgabenfeldByKuerzel.get(kuerzel);
	}

}
