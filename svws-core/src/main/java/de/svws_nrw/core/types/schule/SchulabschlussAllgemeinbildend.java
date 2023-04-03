package de.svws_nrw.core.types.schule;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Arten von allgemeinbildenden Schulabschlüssen.
 */
public enum SchulabschlussAllgemeinbildend {

	/** Es liegt kein Abschluss vor */
	OA(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(0, "OA", "Ohne Abschluss", "A", null, null)
	}),

	/** Hauptschulabschluss nach Klasse 9 (ohne Berechtigung zum Besuch der Klasse 10 Typ B) */
	HA9A(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(1, "HA9A", "Hauptschulabschluss nach Klasse 9 (ohne Berechtigung zum Besuch der Klasse 10 Typ B)", "B", null, null)
	}),

	/** Hauptschulabschluss nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B) */
	HA9(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(2, "HA9", "Hauptschulabschluss nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B)", "C", null, null)
	}),

	/** Hauptschulabschluss nach Klasse 9 (ggf. mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs am Berufskolleg bei internationalen Förderklassen) - siehe BK-Bildungsgang A12 */
	HA9_FOE(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(3, "HA9_FOE", "Hauptschulabschluss nach Klasse 9 (ggf. mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs am Berufskolleg bei internationalen Förderklassen)", "S", null, null)
	}),

	/** Hauptschulabschluss nach Klasse 9 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe */
	HA9_Q(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(4, "HA9_Q", "Hauptschulabschluss nach Klasse 9 (mit der Berechtigung zum Besuch der Gymnasialen Oberstufe)", "O", null, null)
	}),

	/** Hauptschulabschluss nach Klasse 10 */
	HA10(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(5, "HA10", "Hauptschulabschluss nach Klasse 10", "D", null, null)
	}),

	/** Hauptschulabschluss nach Klasse 10 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe */
	HA10_Q(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(6, "HA10_Q", "Hauptschulabschluss nach Klasse 10 (mit der Berechtigung zum Besuch der Gymnasialen Oberstufe)", "U", null, null)
	}),

	/** Der Mittlere Schulabschluss bzw. Fachoberschulreife */
	MSA(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(10, "MSA", "Mittlerer Schulabschluss", "F", null, null)
	}),

	/** Der Mittlere Schulabschluss mit der Berechtigung zum Besuch Gymnasialen Oberstufe */
	MSA_Q(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(11, "MSA_Q", "Mittlerer Schulabschluss (mit der Berechtigung zum Besuch Gymnasialen Oberstufe)", "G", null, null)
	}),

	/** Der Mittlere Schulabschluss mit der Berechtigung zum Besuch der Qualifikationsphase der Gymnasialen Oberstufe */
	MSA_Q1(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(12, "MSA_Q1", "Mittlerer Schulabschluss mit der Berechtigung zum Besuch der Qualifikationsphase Gymnasialen Oberstufe", "I", null, null)
	}),

	/** Versetzung in die Klasse 11 der Fachoberschule (BK) */
	VS_11(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(13, "VS_11", "Versetzung in die Klasse 11 der Fachoberschule (BK)", "P", null, null)
	}),

	/** Fachhochschulreife (nur schulischer Teil) */
	FHR_S(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(20, "FHR_S", "Fachhochschulreife (nur schulischer Teil)", "H", null, null)
	}),

	/** Fachhochschulreife */
	FHR(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(21, "FHR", "Fachhochschulreife", "J", null, null)
	}),

	/** fachgebundene Hochschulreife (BK) */
	FGHR(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(22, "FGHR", "fachgebundene Hochschulreife (BK)", "Q", null, null)
	}),

	/** Abitur / Allgemeine Hochschulreife */
	ABITUR(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(30, "ABITUR", "Abitur / Allgemeine Hochschulreife", "K", null, null)
	}),

	/** Förderschule (Förderschwerpunkt geistige Entwicklung) */
	FOEG(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(40, "FOEG", "Förderschule (Förderschwerpunkt geistige Entwicklung)", "M", null, null)
	}),

	/** Förderschule (Förderschwerpunkt Lernen) */
	FOEL(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(41, "FOEL", "Förderschule (Förderschwerpunkt Lernen)", "V", null, null)
	}),

	/** Waldorfschule */
	WALD(new SchulabschlussAllgemeinbildendKatalogEintrag[] {
		new SchulabschlussAllgemeinbildendKatalogEintrag(50, "WALD", "Zeugnis der Waldorfschule", "W", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Abschlussart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull SchulabschlussAllgemeinbildendKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Abschlussarten */
	public final @NotNull SchulabschlussAllgemeinbildendKatalogEintrag@NotNull[] historie;

	/** Eine HashMap mit den Abschlussarten, welche ihren Kürzeln zugeordnet werden */
	private static final @NotNull HashMap<@NotNull String, @NotNull SchulabschlussAllgemeinbildend> _mapByKuerzel = new HashMap<>();

	/** Eine HashMap mit den Abschlussarten, welche ihren Statistik-Kürzeln zugeordnet werden */
	private static final @NotNull HashMap<@NotNull String, @NotNull SchulabschlussAllgemeinbildend> _mapByKuerzelStatistik = new HashMap<>();


	/**
	 * Erzeugt eine neue Abschlussart in der Aufzählung.
	 *
	 * @param historie   die Historie der Abschlussarten, welches ein Array von {@link SchulabschlussAllgemeinbildendKatalogEintrag} ist
	 */
	SchulabschlussAllgemeinbildend(final @NotNull SchulabschlussAllgemeinbildendKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull SchulabschlussAllgemeinbildend> getMapByKuerzel() {
		if (_mapByKuerzel.size() == 0) {
			for (final SchulabschlussAllgemeinbildend s : SchulabschlussAllgemeinbildend.values()) {
				if (s.daten != null)
					_mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapByKuerzel;
	}


	/**
	 * Gibt die Abschlussart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Abschlussart
	 *
	 * @return die Abschlussart oder null, falls das Kürzel ungültig ist
	 */
	public static SchulabschlussAllgemeinbildend getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}


	/**
	 * Gibt eine Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull SchulabschlussAllgemeinbildend> getMapByKuerzelStatistik() {
		if (_mapByKuerzelStatistik.size() == 0) {
			for (final SchulabschlussAllgemeinbildend s : SchulabschlussAllgemeinbildend.values()) {
				if (s.daten != null)
					_mapByKuerzelStatistik.put(s.daten.kuerzelStatistik, s);
			}
		}
		return _mapByKuerzelStatistik;
	}


	/**
	 * Gibt die Abschlussart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Statistik-Kürzel der Abschlussart
	 *
	 * @return die Abschlussart oder null, falls das Statistik-Kürzel ungültig ist
	 */
	public static SchulabschlussAllgemeinbildend getByKuerzelStatistik(final String kuerzel) {
		return getMapByKuerzelStatistik().get(kuerzel);
	}


	/**
	 * Prüft, ob dieser Abschluss dem im String-Parameter str übergebenen
	 * Abschluss entspricht.
	 *
	 * @param str   der Name des Abschlusses für den Vergleich als String
	 *
	 * @return true, falls beide Abschlüsse übereinstimmen und ansonsten false
	 */
	public boolean is(final String str) {
		if (str == null)
			return false;
		try {
			final SchulabschlussAllgemeinbildend other = SchulabschlussAllgemeinbildend.valueOf(str);
			return this.equals(other);
		} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
			return false;
		}
	}

}
