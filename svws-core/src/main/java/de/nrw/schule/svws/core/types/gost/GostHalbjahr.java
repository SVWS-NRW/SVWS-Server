package de.nrw.schule.svws.core.types.gost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Ennummeration für 
 * die Halbjahre der gymnasialen Oberstufe zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum GostHalbjahr implements Comparable<GostHalbjahr> {

	/** Einführungsphase 1. Halbjahr = EF1 */
	EF1(0, "EF", 1, "EF.1", "E1", "Einführungsphase 1. Halbjahr"),

	/** Einführungsphase 2. Halbjahr = EF2 */
	EF2(1, "EF", 2, "EF.2", "E2", "Einführungsphase 2. Halbjahr"),

	/** Qualifikationsphase 1. Jahr, 1. Halbjahr = Q1.1 */
	Q11(2, "Q1", 1, "Q1.1", "Q1", "Qualifikationsphase 1. Jahr, 1. Halbjahr"),

	/** Qualifikationsphase 1. Jahr, 2. Halbjahr = Q1.2 */
	Q12(3, "Q1", 2, "Q1.2", "Q2", "Qualifikationsphase 1. Jahr, 2. Halbjahr"),

	/** Qualifikationsphase 2. Jahr, 1. Halbjahr = Q2.1 */
	Q21(4, "Q2", 1, "Q2.1", "Q3", "Qualifikationsphase 2. Jahr, 1. Halbjahr"),

	/** Qualifikationsphase 2. Jahr, 2. Halbjahr = Q2.2 */
	Q22(5, "Q2", 2, "Q2.2", "Q4", "Qualifikationsphase 2. Jahr, 2. Halbjahr");


	/** Eine Zuordnung der Halbjahre zu der ID, welche die Reihenfolge der Halbjahre angibt. */
	private final static @NotNull HashMap<@NotNull Integer, @NotNull GostHalbjahr> _mapID = new HashMap<>();
	
	/** Eine Zuordnung der Halbjahre zu dem Kürzel. */
	private final static @NotNull HashMap<@NotNull String, @NotNull GostHalbjahr> _mapKuerzel = new HashMap<>();

	/** Eine Zuordnung der Halbjahre zu dem alten Kürzel. */
	private final static @NotNull HashMap<@NotNull String, @NotNull GostHalbjahr> _mapKuerzelAlt = new HashMap<>();
	
	
	/** Die maximale Anzahl an Halbjahren in der gymnasialen Oberstufe */
	public static final int maxHalbjahre = 6;


	/** Eine ID für das Halbjahr, welches die Reihenfolge der Halbjahre wiederspiegelt und als Index für Arrays verwendet werden kann. */
	public final int id;
	
	/** Das Jahrgangskürzel des Halbjahres */
	public final @NotNull String jahrgang;
	
	/** Die Nummer des Halbjahres */
	public final int halbjahr;
	
	/** Das eindeutige Kürzel für das Halbjahr der gymnasialen Oberstufe */
	public final @NotNull String kuerzel;
	
	/** Ein eindeutiges Kürzel, welche in alten Tabellen (z.B. LuPO) verwendet wurde. */
	public final @NotNull String kuerzelAlt;
	
	/** Eine textuelle Beschreibung für das Halbjahr der gymnasialen Oberstufe */
	public final @NotNull String beschreibung; 
	

	/**
	 * Erzeugt ein neues Halbjahr der Gymnasialen Oberstufe für diese Aufzählung.
	 * 
	 * @param id             die ID für das Halbjahr, welches die Reihenfolge der Halbjahre wiederspiegelt
	 * @param jahrgang       das Jahrgangskürzel des Halbjahres
	 * @param halbjahr       die Nummer des Halbjahres
	 * @param kuerzel        das eindeutige Kürzel für das Halbjahr der gymnasialen Oberstufe
	 * @param kuerzelAlt     ein eindeutiges Kürzel, welche in alten Tabellen verwendet wurde.
	 * @param beschreibung   die textuelle Beschreibung für das Halbjahr der gymnasialen Oberstufe
	 */
	private GostHalbjahr(final int id, final @NotNull String jahrgang, final int halbjahr, 
			             final @NotNull String kuerzel, final @NotNull String kuerzelAlt, 
			             final @NotNull String beschreibung) {
		this.id = id;
		this.jahrgang = jahrgang;
		this.halbjahr = halbjahr;
		this.kuerzel = kuerzel;
		this.kuerzelAlt = kuerzelAlt;
		this.beschreibung = beschreibung;
	}


	/**
	 * Gibt eine Map von den IDs auf das Gost-Halbjahr zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs auf das Gost-Halbjahr
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull GostHalbjahr> getMapByID() {
		if (_mapID.size() == 0)
			for (@NotNull GostHalbjahr h : GostHalbjahr.values())
				_mapID.put(h.id, h);
		return _mapID;
	}


	/**
	 * Gibt eine Map von den Kürzeln auf das Gost-Halbjahr zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf das Gost-Halbjahr
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull GostHalbjahr> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (@NotNull GostHalbjahr h : GostHalbjahr.values())
				_mapKuerzel.put(h.kuerzel, h);
		return _mapKuerzel;
	}


	/**
	 * Gibt eine Map von den alten Kürzeln auf das Gost-Halbjahr zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den alten Kürzeln auf das Gost-Halbjahr
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull GostHalbjahr> getMapByKuerzelAlt() {
		if (_mapKuerzelAlt.size() == 0)
			for (@NotNull GostHalbjahr h : GostHalbjahr.values())
				_mapKuerzelAlt.put(h.kuerzelAlt, h);
		return _mapKuerzelAlt;
	}


    /**
     * Gibt das nachfolgende Halbjahr zurück. 
     * 
     * @return das nachfolgende Halbjahr oder null, wenn es keines mehr gibt
     */
	@JsonIgnore
	public GostHalbjahr next() {
		return getMapByID().get(this.id + 1);
	}
	
	
    /**
     * Gibt das vorherige Halbjahr zurück. 
     * 
     * @return das vorherige Halbjahr oder null, wenn es keines mehr gibt
     */
	@JsonIgnore
	public GostHalbjahr previous() {
		return getMapByID().get(this.id - 1);
	}
	
	
    /**
     * Gibt alle Halbjahre der Einführungsphase zurück.
     * 
     * @return ein Array mit allen Halbjahren der Einführungsphase der gymnasialen Oberstufe
     */
	public static @NotNull GostHalbjahr@NotNull [] getEinfuehrungsphase() {
		@NotNull GostHalbjahr@NotNull [] ef = { GostHalbjahr.EF1, GostHalbjahr.EF2 };
		return ef;
	}	

	
    /**
     * Gibt alle Halbjahre der Qualifikationsphase zurück.
     * 
     * @return ein Array mit allen Halbjahren der Qualifikationsphase der gymnasialen Oberstufe
     */
	public static @NotNull GostHalbjahr@NotNull [] getQualifikationsphase() {
		@NotNull GostHalbjahr@NotNull [] q = { GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22 };
		return q;
	}	
	
	
    /**
     * Gibt alle Halbjahre des übergebenen Jahrgangs zurück.
     * 
     * @param jahrgang     der Jahrgang
     * 
     * @return ein Array mit den Halbjahren des Jahrgangs
     */
	public static @NotNull GostHalbjahr@NotNull[] getHalbjahreFromJahrgang(@NotNull String jahrgang) {
		switch (jahrgang) {
			case "EF":
				@NotNull GostHalbjahr@NotNull[] ef = { GostHalbjahr.EF1, GostHalbjahr.EF2 };
				return ef;
			case "Q1":
				@NotNull GostHalbjahr@NotNull[] q1 = { GostHalbjahr.Q11, GostHalbjahr.Q12 };
				return q1;
			case "Q2":
				@NotNull GostHalbjahr@NotNull[] q2 = { GostHalbjahr.Q21, GostHalbjahr.Q22 };
				return q2;
			default:
				throw new IllegalArgumentException("Der angegebene Jahrgang ist kein gültiger Jahrgang der gymnasialen Oberstufe"); 
		}
	}


    /**
     * Gibt das Halbjahr zurück, welches die übergebene ID hat.
     * 
     * @param id   die ID des Halbjahres
     * 
     * @return das Halbjahr oder null, falls die ID nicht gültig ist
     */
	public static GostHalbjahr fromID(Integer id) {
		if (id == null)
			return null;
		switch (id) {
			case 0: return GostHalbjahr.EF1;
			case 1: return GostHalbjahr.EF2;
			case 2: return GostHalbjahr.Q11;
			case 3: return GostHalbjahr.Q12;
			case 4: return GostHalbjahr.Q21;
			case 5: return GostHalbjahr.Q22;
			default: return null;
		}
	}


    /**
     * Gibt das Halbjahr zurück, welches die übergebene ID hat. <br>
     * Wirft eine Exception, falls die ID keinem Halbjahr zugeordnet werden kann.
     * 
     * @param pGostHalbjahID   Die ID des Halbjahres.
     * 
     * @return Das Halbjahr oder eine Exception, falls die ID nicht gültig ist
     * @throws NullPointerException Falls die ID keinem Halbjahr zugeordnet werden kann.
     */
    public static @NotNull GostHalbjahr fromIDorException(int pGostHalbjahID) throws NullPointerException {
    	GostHalbjahr halbjahr = GostHalbjahr.fromID(pGostHalbjahID);
		if (halbjahr == null)
			throw new NullPointerException("GostHalbjahr nicht gefunden!");    	
		return halbjahr;
	}


	/**
     * Gibt das Halbjahr zurück, welches das übergebene Kürzel hat.
     * 
     * @param kuerzel   das Kürzel
     * 
     * @return das Halbjahr oder null, falls das Kürzel nicht gültig ist
     */
	public static GostHalbjahr fromKuerzel(String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}


    /**
     * Gibt das Halbjahr zurück, welches das übergebene alte Kürzel hat.
     * 
     * @param kuerzelAlt   das alte Kürzel
     * 
     * @return das Halbjahr oder null, falls das Kürzel nicht gültig ist
     */
	public static GostHalbjahr fromKuerzelAlt(String kuerzelAlt) {
		return getMapByKuerzelAlt().get(kuerzelAlt);
	}


    /**
     * Gibt das Halbjahr zurück, welches zu dem übergebenen Jahrgang und Halbjahr gehört.
     * 
     * @param jahrgang     der Jahrgang
     * @param halbjahr     die Nummer des Halbjahres
     * 
     * @return das Halbjahr oder null, falls es kein gültiges Halbjahr mit den Angaben gibt.
     */
	public static GostHalbjahr fromJahrgangUndHalbjahr(String jahrgang, int halbjahr) {
		if ((halbjahr != 1) && (halbjahr != 2))
			return null;
		switch (jahrgang) {
			case "EF": return (halbjahr == 1) ? EF1 : EF2;
			case "Q1": return (halbjahr == 1) ? Q11 : Q12;
			case "Q2": return (halbjahr == 1) ? Q21 : Q22;		
			default: return null;
		}
	}

	
	/**
	 * Ermittelt das Halbjahr der gymnasialen Oberstufe anhand des angegegebenen Abiturjahres und
	 * dem aktuellen Schuljahr und Halbjahr
	 * 
	 * @param abiturjahr   das Abiturjahr
	 * @param schuljahr    das aktuelle Schuljahr
	 * @param halbjahr     das aktuelle Halbjahr
	 * 
	 * @return das Halbjahr der gymnasialen Oberstufe oder null
	 */
	public static GostHalbjahr fromAbiturjahrSchuljahrUndHalbjahr(int abiturjahr, int schuljahr, int halbjahr) {
		// Bestimme die ID des Halbjahres und prüfe, ob das Ergebnis im gültigen Bereich liegt
		int id = ((schuljahr + 3 - abiturjahr) * 2) + halbjahr - 1;
		return GostHalbjahr.fromID(id);
	}
	
	
	/**
	 * Bestimmt das Halbjahr der gymnasialen Oberstufe anhand des angegegebenen Abiturjahres und
	 * dem aktuellen Schuljahr und Halbjahr und gibt anhand dessen das Halbjahr der gymnasialen 
	 * Oberstufe zurück, welches als nächstes geplant wird.
	 * 
	 * @param abiturjahr   das Abiturjahr
	 * @param schuljahr    das aktuelle Schuljahr
	 * @param halbjahr     das aktuelle Halbjahr
	 * 
	 * @return das nächste Halbjahr der gymnasialen Oberstufe zur Planung oder null, wenn der 
	 *         Jahrgang in der Q2.2 ist oder das Abitur bereits abgeschlossen ist.
	 */
	public static GostHalbjahr getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(int abiturjahr, int schuljahr, int halbjahr) {
		// Bestimme die ID des Halbjahres und prüfe, ob das Ergebnis im gültigen Bereich liegt
		int id = ((schuljahr + 3 - abiturjahr) * 2) + halbjahr;
		if (id < 0)
			id = 0;
		return GostHalbjahr.fromID(id);
	}


	/**
	 * Bestimmt das Schuljahr für dieses Halbjahr der Gymnasialen Oberstufe anhand des 
	 * übergebenen Abiturjahres.
	 * 
	 * @param abiturjahr   das Abiturjahr
	 * 
	 * @return das Schuljahr
	 */
	public int getSchuljahrFromAbiturjahr(int abiturjahr) {
		return abiturjahr - 3 + (id / 2);
	}


    /**
     * Gibt zurück, ob es Einführungsphase ist.
     * 
     * @return  Einführungsphase, true oder false
     */
	@JsonIgnore
	public boolean istEinfuehrungsphase() {
		return "EF".equals(jahrgang);
	}
	
	
    /**
     * Gibt zurück, ob es Qualifikationsphase ist.
     * 
     * @return  Qualifikationsphase, true oder false
     */
	@JsonIgnore
	public boolean istQualifikationsphase() {
		return !istEinfuehrungsphase();
	}


    /**
     * Prüft anhand der übergebenen Halbjahre, ob es sich um die beiden Halbjahre
     * der Einführungsphase handelt.
     * 
     * @param halbjahre    die Halbjahre
     * 
     * @return true, wenn es sich um die beiden Halbjahre der Einführungsphase handelt
     *         und ansonsten false
     */
	public static boolean pruefeEinfuehrungsphase(GostHalbjahr... halbjahre) {
		if ((halbjahre == null) || (halbjahre.length != 2))
			return false;
		return ((halbjahre[0] == GostHalbjahr.EF1) && (halbjahre[0] == GostHalbjahr.EF2)) || 
				((halbjahre[0] == GostHalbjahr.EF2) && (halbjahre[0] == GostHalbjahr.EF1));
	}


    /**
     * Prüft anhand der übergebenen Halbjahre, ob es sich um die vier Halbjahre
     * der Qualifikationsphase handelt.
     * 
     * @param halbjahre    die Halbjahre
     * 
     * @return true, wenn es sich um die vier Halbjahre der Qualifikationsphase 
     *         handelt und ansonsten false
     */
	public static boolean pruefeQualifikationsphase(@NotNull GostHalbjahr... halbjahre) {
		if ((halbjahre == null) || (halbjahre.length != 4))
			return false;
		@NotNull List<@NotNull GostHalbjahr> list = Arrays.asList(halbjahre);
		return (list.contains(GostHalbjahr.Q11) && list.contains(GostHalbjahr.Q12) && list.contains(GostHalbjahr.Q21) && list.contains(GostHalbjahr.Q22));
	}

}
