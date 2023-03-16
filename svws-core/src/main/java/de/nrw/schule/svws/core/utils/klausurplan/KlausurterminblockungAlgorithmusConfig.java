package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.List;

/**
 * Konfiguration des Algorithmus. Die GUI muss diese Daten definieren und dem Algorithmus beim 
 * Aufruf der Methode {@link KlausurblockungSchienenAlgorithmus#berechne(List, long)} übergeben. 
 * 
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungAlgorithmusConfig {

	/**
	 * Dieser LK-GK-Modus blockt beide Kursarten gemischt.
	 */
	public static final int LK_GK_MODUS_BEIDE = 0;
	
	/**
	 * Dieser LK-GK-Modus blockt nur die Kursart LK.
	 */
	public static final int LK_GK_MODUS_NUR_LK = 1;
	
	/**
	 * Dieser LK-GK-Modus blockt nur die Kursart GK.
	 */
	public static final int LK_GK_MODUS_NUR_GK = 2;
	
	/**
	 * Dieser LK-GK-Modus blockt zuerst die Kursart LK, danach die Kursart GK. 
	 */
	public static final int LK_GK_MODUS_GETRENNT = 3;
	
	/**
	 * Der normale Algorithmus minimiert die Anzahl der Termine.
	 */
	public static final int ALGORITHMUS_NORMAL = 1;

	/**
	 * Dieser Algorithmus forciert, das pro Termin nur die selben Fächer sind (LK+GK).
	 * Im zweiten Schritt werden die Termine versucht zu minimieren.
	 */
	public static final int ALGORITHMUS_FAECHERWEISE = 2;

	/**
	 * Dieser Algorithmus forciert, das pro Termin nur die selben Kurs-Schienen.
	 * Im zweiten Schritt werden die Termine versucht zu minimieren.
	 */
	public static final int ALGORITHMUS_SCHIENENWEISE = 3;

	private long max_time_millis;
	private int algorithmus;
	private int lk_gk_modus;
	private boolean regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;

	/**
	 * Der Konstruktor definiert Standardwerte.
	 */
	public KlausurterminblockungAlgorithmusConfig() {
		set_max_time_millis(1000L);
		set_lk_gk_modus(LK_GK_MODUS_BEIDE);
		set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(false);
		set_algorithmus(ALGORITHMUS_NORMAL);
	}

	/**
	 * Liefert die maximale Blockungszeit.
	 * @return die maximale Blockungszeit.
	 */
	public long get_max_time_millis() {
		return max_time_millis;
	}

	/**
	 * Setzt die maximale Blockungszeit.
	 * @param pMax_time_millis die maximale Blockungszeit.
	 */
	public void set_max_time_millis(long pMax_time_millis) {
		max_time_millis = pMax_time_millis;
	}

	/**
	 * Liefert den selektierten Algorithmus.
	 * @return den selektierten Algorithmus.
	 */
	public int get_algorithmus() {
		return algorithmus;
	}

	/**
	 * Setzt den zu verwendenden Algorithmus.
	 * @param pAlgorithmus den zu verwendenden Algorithmus.
	 */
	public void set_algorithmus(int pAlgorithmus) {
		algorithmus = pAlgorithmus;
	}

	/**
	 * Liefert TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 * @return TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public boolean get_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin() {
		return regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;
	}

	/**
	 * TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 * @param pRegel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public void set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(
			boolean pRegel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin) {
		regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin = pRegel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;
	}

	/**
	 * Liefert den LK-GK-Modus.
	 * @return den LK-GK-Modus.
	 */
	public int get_lk_gk_modus() {
		return lk_gk_modus;
	}

	/**
	 * Setzt den LK-GK-Modus.
	 * @param lk_gk_modus ein Wert aus {@link #LK_GK_MODUS_BEIDE}, {@link #LK_GK_MODUS_NUR_GK}, {@link #LK_GK_MODUS_NUR_LK}, {@link #LK_GK_MODUS_GETRENNT}.
	 */
	public void set_lk_gk_modus(int lk_gk_modus) {
		this.lk_gk_modus = lk_gk_modus;
	}

}
