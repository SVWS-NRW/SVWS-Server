package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Arten von berufsbildenden Schulabschlüssen.
 */
public enum SchulabschlussBerufsbildend {

	/** Es liegt kein Abschluss vor */
	OA(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(0, "OA", "Ohne Abschluss", "0", null, null)
	}),
	
	/** Abschluss der Ausbildungsvorbereitung */
	VORB(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(10, "VORB", "Abschluss der Ausbildungsvorbereitung", "1", null, null)
	}),	
	
	/** Versetzungszeugnis */
	VERS(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(11, "VERS", "Versetzungszeugnis", "1", null, null)
	}),	

	/** Abschlusszeugnis in Aufbaubildungsgängen */
	AUFB(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(12, "AUFB", "Abschlusszeugnis in Aufbaubildungsgängen", "1", null, null)
	}),	
	
	/** Abschluss der Berufschulvorbereitung */
	BV(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(13, "BV", "Abschluss der Berufschulvorbereitung", "1", null, null)
	}),

	/** Vorpraktikum */
	VP(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(14, "VP", "Vorpraktikum", "1", null, null)
	}),

	/** Vorpraktikum */
	BP(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(15, "BP", "Berufspraktikum", "1", null, null)
	}),

	/** Abschluss der Berufschulgrundjahres */
	BG(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(20, "BG", "Abschluss der Berufschulgrundjahres", "2", null, null)
	}),
	
	/** Berufschulabschluss */
	BS(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(30, "BS", "Berufschulabschluss", "3", null, null)
	}),
	
	/** Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten */
	BK(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(40, "BK", "Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten", "4", null, null)
	}),
	
	/** Berufsabschluss */
	BAB(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(50, "BAB", "Berufsabschluss", "5", null, null)
	}),

	/** Fachschulabschluss (berufliche Weiterbildung) */
	BW(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(60, "BW", "Fachschulabschluss (berufliche Weiterbildung)", "6", null, null)
	}),
		
	/** Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten */
	VBK(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(80, "VBK", "Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten", "8", null, null)
	}),

	/** Pseudoabschluss: Schulwechsler, die im selben Bildungsgang verbleiben */
	WECHSEL(new SchulabschlussBerufsbildendKatalogEintrag[] {
		new SchulabschlussBerufsbildendKatalogEintrag(90, "WECHSEL", "Schulwechsler, die im selben Bildungsgang verbleiben", "9", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Abschlussart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull SchulabschlussBerufsbildendKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Abschlussarten */
	public final @NotNull SchulabschlussBerufsbildendKatalogEintrag@NotNull[] historie;	

	/** Eine HashMap mit den Abschlussarten, welche ihren Kürzeln zugeordnet werden */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull SchulabschlussBerufsbildend> _mapByKuerzel = new HashMap<>();

	/** Eine HashMap mit den Abschlussarten, welche ihren Statistik-Kürzeln zugeordnet werden */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull SchulabschlussBerufsbildend> _mapByKuerzelStatistik = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Abschlussart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Abschlussarten, welches ein Array von {@link SchulabschlussBerufsbildendKatalogEintrag} ist  
	 */
	private SchulabschlussBerufsbildend(@NotNull SchulabschlussBerufsbildendKatalogEintrag@NotNull[] historie) {
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
	private static @NotNull HashMap<@NotNull String, @NotNull SchulabschlussBerufsbildend> getMapByKuerzel() {
		if (_mapByKuerzel.size() == 0) {
			for (SchulabschlussBerufsbildend s : SchulabschlussBerufsbildend.values()) {
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
	public static SchulabschlussBerufsbildend getByKuerzel(String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}


	/**
	 * Gibt eine Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull SchulabschlussBerufsbildend> getMapByKuerzelStatistik() {
		if (_mapByKuerzelStatistik.size() == 0) {
			for (SchulabschlussBerufsbildend s : SchulabschlussBerufsbildend.values()) {
				if (s.daten != null)
					_mapByKuerzelStatistik.put(s.daten.kuerzelStatistik, s);
			}
		}
		return _mapByKuerzelStatistik;
	}


	/**
	 * Gibt die Abschlussart für das angegebene Statistik-Kürzel zurück.
	 * 
	 * @param kuerzel   das Statistik-Kürzel der Abschlussart
	 * 
	 * @return die Abschlussart oder null, falls das Statistik-Kürzel ungültig ist
	 */
	public static SchulabschlussBerufsbildend getByKuerzelStatistik(String kuerzel) {
		return getMapByKuerzelStatistik().get(kuerzel);
	}


}
