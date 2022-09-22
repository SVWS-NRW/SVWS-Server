package de.nrw.schule.svws.core.types.gost;

import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.core.data.gost.GostFach;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Ennummeration für 
 * die GOSt-Fachbereiche zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class GostFachbereich {
	
	/** Ein Vektor mit allen GOSt-Fachbereichen */
	private static @NotNull Vector<@NotNull GostFachbereich> all = new Vector<>();

	
	private static @NotNull String@NotNull[] kuerzel_DEUTSCH = {"D"};
	private static @NotNull String@NotNull[] kuerzel_FREMDSPRACHE = {
			"A", "A0", "A5", "A6", "A7", "A8", "A9",         // ARABISCH
			"C", "C0", "C5", "C6", "C7", "C8", "C9",         // CHINESISCH
			"E", "E0", "E5", "E6", "E7", "E8", "E9",         // ENGLISCH
			"F", "F0", "F5", "F6", "F7", "F8", "F9",         // FRANZÖSISCH
			"G", "G0", "G5", "G6", "G7", "G8", "G9",         // GRIECHISCH
			"H", "H0", "H5", "H6", "H7", "H8", "H9",         // HEBRÄISCH
			"I", "I0", "I5", "I6", "I7", "I8", "I9",         // ITALIENISCH
			"K", "K0", "K5", "K6", "K7", "K8", "K9",         // JAPANISCH
			"L", "L0", "L5", "L6", "L7", "L8", "L9",         // LATEINISCH
			"N", "N0", "N5", "N6", "N7", "N8", "N9",         // NIEDERLÄNDISCH
			"O", "O0", "O5", "O6", "O7", "O8", "O9",         // PORTUGIESISCH
			"P", "P0", "P5", "P6", "P7", "P8", "P9",         // POLNISCH
			"R", "R0", "R5", "R6", "R7", "R8", "R9",         // RUSSISCH			
			"S", "S0", "S5", "S6", "S7", "S8", "S9",         // SPANISCH
			"T", "T0", "T5", "T6", "T7", "T8", "T9",         // TÜRKISCH
			"U", "U0", "U5", "U6", "U7", "U8", "U9",         // RUMÄNISCH
			"Z", "Z0", "Z5", "Z6", "Z7", "Z8", "Z9",         // NEUGRIECHISCH
	}; 
	private static @NotNull String@NotNull[] kuerzel_KUNST_MUSIK = {"KU", "MU"};
	private static @NotNull String@NotNull[] kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ = {"LI", "IN", "IV", "VO"};
	private static @NotNull String@NotNull[] kuerzel_GESCHICHTE = {"GE"};
	private static @NotNull String@NotNull[] kuerzel_SOZIALWISSENSCHAFTEN = {"SW"};
	private static @NotNull String@NotNull[] kuerzel_PHILOSOPHIE = {"PL"};
	private static @NotNull String@NotNull[] kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE = {"EK", "PA", "PS", "RK"};
	private static @NotNull String@NotNull[] kuerzel_MATHEMATIK = {"M"};
	private static @NotNull String@NotNull[] kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH = {"BI", "CH", "PH"};
	private static @NotNull String@NotNull[] kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND = {"EL", "IF", "TC"};
	private static @NotNull String@NotNull[] kuerzel_RELIGION = {"HR", "OR", "YR", "ER", "KR", "IL"};
	private static @NotNull String@NotNull[] kuerzel_SPORT = {"SP"};
	
	
	/** Fachbereich sprachlich literarisch künstlerisch */
	public static final @NotNull GostFachbereich SPRACHLICH_LITERARISCH_KUENSTLERISCH = new GostFachbereich(kuerzel_DEUTSCH, 
			kuerzel_FREMDSPRACHE, kuerzel_KUNST_MUSIK, kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ);
	
	/** Fachbereich deutsch */
	public static final @NotNull GostFachbereich DEUTSCH = new GostFachbereich(kuerzel_DEUTSCH);
	
	/** Fachbereich fremdsprachllich */
	public static final @NotNull GostFachbereich FREMDSPRACHE = new GostFachbereich(kuerzel_FREMDSPRACHE);
	
	/** Fachbereich künstlerisch musikalisch */
	public static final @NotNull GostFachbereich KUNST_MUSIK = new GostFachbereich(kuerzel_KUNST_MUSIK);
	
	/** Fachbereich literarisch künstlerisch */
	public static final @NotNull GostFachbereich LITERARISCH_KUENSTLERISCH = new GostFachbereich(kuerzel_KUNST_MUSIK, 
			kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ);
	
	/** Fachbereich Ersatz für literarisch künstlerisch */
	public static final @NotNull GostFachbereich LITERARISCH_KUENSTLERISCH_ERSATZ = new GostFachbereich(kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ);
	
	/** Fachbereich gesellschaftswissenschaftlich */
	public static final @NotNull GostFachbereich GESELLSCHAFTSWISSENSCHAFTLICH = new GostFachbereich(kuerzel_GESCHICHTE, 
			kuerzel_SOZIALWISSENSCHAFTEN, kuerzel_PHILOSOPHIE, kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
	
	/** Fachbereich geschichtlich */
	public static final @NotNull GostFachbereich GESCHICHTE = new GostFachbereich(kuerzel_GESCHICHTE);
	
	/** Fachbereich sozialwissenschaftlich */
	public static final @NotNull GostFachbereich SOZIALWISSENSCHAFTEN = new GostFachbereich(kuerzel_SOZIALWISSENSCHAFTEN);
	
	/** Fachbereich philosophisch */
	public static final @NotNull GostFachbereich PHILOSOPHIE = new GostFachbereich(kuerzel_PHILOSOPHIE);
	
	/** Fachbereich Sonstige gesellschaftswissenschaftliche */
	public static final @NotNull GostFachbereich GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE = new GostFachbereich(kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
	
	/** Fachbereich gesellschaftswissenschaftlich mit Religion */
	public static final @NotNull GostFachbereich GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION = new GostFachbereich(kuerzel_GESCHICHTE, kuerzel_SOZIALWISSENSCHAFTEN, 
			kuerzel_PHILOSOPHIE, kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE, kuerzel_RELIGION);
	
	/** Fachbereich mathematisch */
	public static final @NotNull GostFachbereich MATHEMATIK = new GostFachbereich(kuerzel_MATHEMATIK);
	
	/** Fachbereich mathematisch naturwissenschaftlich */
	public static final @NotNull GostFachbereich MATHEMATISCH_NATURWISSENSCHAFTLICH = new GostFachbereich(kuerzel_MATHEMATIK, kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH, 
			kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND);
	
	/** Fachbereich naturwissenschaftlich */
	public static final @NotNull GostFachbereich NATURWISSENSCHAFTLICH = new GostFachbereich(kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH, 
			kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND);
	
	/** Fachbereich klassisch naturwissenschaftlich */
	public static final @NotNull GostFachbereich NATURWISSENSCHAFTLICH_KLASSISCH = new GostFachbereich(kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH);
	
	/** Fachbereich naturwissenschaftlich neueinsetzend */
	public static final @NotNull GostFachbereich NATURWISSENSCHAFTLICH_NEU_EINSETZEND = new GostFachbereich(kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND);
	
	/** Fachbereich religiös */
	public static final @NotNull GostFachbereich RELIGION = new GostFachbereich(kuerzel_RELIGION);
	
	/** Fachbereich sportlich */
	public static final @NotNull GostFachbereich SPORT = new GostFachbereich(kuerzel_SPORT);



	/** Ein Array mit allen Kürzeln der Fächer dieses Fachbereichs */
	private final @NotNull String@NotNull[] kuerzel;

	
	/**
	 * Erstellt einen neuen Fachbereich mit den übergebenen Kürzeln von Fächern
	 * 
	 * @param kuerzel   die Kürzel der Fächer des Fachbereichs
	 */
	private GostFachbereich(@NotNull String@NotNull[]... kuerzelArrays) {
		int pos = 0;
		for (@NotNull String@NotNull[] a : kuerzelArrays) {
			pos += a.length;
		}
		this.kuerzel = new String[pos];
		pos = 0;
		for (@NotNull String@NotNull[] a : kuerzelArrays) {
			System.arraycopy(a, 0, this.kuerzel, pos, a.length);
			pos += a.length;
		}
		all.add(this);
	}

	
	/**
	 * Prüft, ob das übergebene Fach zu diesem Fachbereich gehört.
	 * 
	 * @param fach   das zu prüfende Fach
	 * 
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	@JsonIgnore
	public boolean hat(GostFach fach) {
		return fach == null ? false : hat(fach.kuerzel);
	}
	
	
	/**
	 * Prüft, ob das Fach mit dem übergebenen Kürzel zu diesem Fachbereich gehört.
	 * 
	 * @param kuerzel   das Kürzel des zu prüfenden Faches
	 * 
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	@JsonIgnore
	public boolean hat(String kuerzel) {
		if (kuerzel == null)
			return false;
		@NotNull String@NotNull[] fbAlleKuerzel = this.getAlleKuerzel();
		for (String fbKuerzel : fbAlleKuerzel){
			if (kuerzel.equals(fbKuerzel))
				return true;
		}
		return false;		
	}
	
	
	/**
	 * Liefert alle Kürzel der Fächer zurück, die zu diesem Fachbereich gehören.
	 *  
	 * @return alle Kürzel der Fächer dieses Fachbereichs
	 */
	@JsonIgnore
	public @NotNull String@NotNull[] getAlleKuerzel() {
		return kuerzel;
	}

	
	/**
	 * Ermittelt die, dem Fach zugehörigen, Fachbereiche anhand des Statistik-Kürzels
	 *  
	 * @param kuerzel   das Statistik-Kürzel des Faches
	 * 
	 * @return die zugehörigen Fachbereiche
	 */
	public static @NotNull List<@NotNull GostFachbereich> getBereiche(String kuerzel) {
		@NotNull Vector<@NotNull GostFachbereich> result = new Vector<>();
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).hat(kuerzel))
				result.add(all.get(i));
		}
		return result;
	}
	

	/**
	 * Ermittelt die, dem Fach zugehörigen, Fachbereiche
	 *  
	 * @param fach   das Fach
	 * 
	 * @return die zugehörigen Fachbereiche
	 */
	public static @NotNull List<@NotNull GostFachbereich> getBereiche(GostFach fach) {
		@NotNull Vector<@NotNull GostFachbereich> result = new Vector<>();
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).hat(fach))
				result.add(all.get(i));
		}
		return result;
	}


	/**
	 * Liefert eine Liste mit allen Fachbereichen zurück.
	 *  
	 * @return eine Liste mit allen Fachbereichen.
	 */
	public static @NotNull List<@NotNull GostFachbereich> values() {
		return new Vector<>(all);
	}

}
