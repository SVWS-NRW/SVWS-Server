package de.nrw.schule.svws.core.abschluss.gost;

import jakarta.validation.constraints.NotNull;

import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.gost.GostFachbereich;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;

/**
 * Diese Klassen stellt Methoden zum Zugriff auf DTO-Objekte der Klasse
 * {@link GostFach} zur Verfügung. 
 */
public class GostFachManager {

	/**
	 * Gibt an, ob es sich bei dem Fach um ein Projektkursfach handelt.
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * 
	 * @return true, falls es sich um ein Projektkursfach handelt
	 */
	public static boolean istProjektkurs(final @NotNull GostFach fach) {
		return "PX".equals(fach.kuerzel);
	}
	
	
	/**
	 * Gibt an, ob es sich bei dem Fach um ein Vertiefungskursfach handelt.
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * 
	 * @return true, falls es sich um ein Vertiefungskursfach handelt
	 */
	public static boolean istVertiefungskurs(final @NotNull GostFach fach) {
		return "VX".equals(fach.kuerzel);
	}
	
	
	/**
	 * Gibt an, ob das Fach durchgehend von EF.1 bis Q2.2 belegbar ist;
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * 
	 * @return true, falls es so belegbar ist, sonst false
	 */
	public static boolean istDurchgehendBelegbarBisQ22(final GostFach fach) {
		if (fach == null)
			return false;
		return fach.istMoeglichEF1 && fach.istMoeglichEF2 && 
			   fach.istMoeglichQ11 && fach.istMoeglichQ12 && 
			   fach.istMoeglichQ21 && fach.istMoeglichQ22;
	}
	
	
	/**
	 * Gibt an, ob das Fach durchgehend von EF.1 bis EF.2 belegbar ist;
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * 
	 * @return true, falls es so belegbar ist, sonst false
	 */
	public static boolean istBelegbarBisEF2(final @NotNull GostFach fach) {
		return fach.istMoeglichEF1 && fach.istMoeglichEF2;
	}
	
	
	/**
	 * Prüft, ob das Fach zu der angegebenen Sprache gehört
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * @param sprache   das Kürzel der Sprache (1. Zeichen ohne Jahrgang!)
	 * 
	 * @return true, falls das Fach zu der angegebenen Sprache passt, sonst false 
	 */
	public static boolean istFremdsprachenfach(final GostFach fach, final String sprache) {
		if ((fach == null) || (fach.kuerzel == null) || ("".equals(fach.kuerzel)) || 
			!GostFachbereich.FREMDSPRACHE.hat(fach) || (sprache == null))
			return false;
		return (sprache.compareToIgnoreCase(fach.kuerzel.substring(0, 1)) == 0);
	}
	
	
	/**
	 * Liefert das Kürzel der Sprache (ohne Jahrgang) zurück, falls es sich um eine Sprache handelt.
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * 
	 * @return das Kürzel der Sprache oder null
	 */
	public static String getFremdsprache(final @NotNull GostFach fach) {
		if ((fach.kuerzel == null) || ("".equals(fach.kuerzel)) || !GostFachbereich.FREMDSPRACHE.hat(fach))
			return null;
		return fach.kuerzel.substring(0, 1).toUpperCase();
	}
	
	
	/**
	 * Prüft, ob das Fach bilingual unterrichtet wird oder nicht.
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * 
	 * @return true, falls das Fach bilingual unterrichtet wird.
	 */
	public static boolean istBilingual(final @NotNull GostFach fach) {
		return ((fach.biliSprache != null) && (!"".equals(fach.biliSprache)) && (!"D".equals(fach.biliSprache)));
	}

	
	/**
	 * Gibt zurück, ob das Fach in dem angegebenen Halbjahr wählbar ist oder nicht.
	 * 
	 * @param fach   das Fach der gymnasialen Oberstufe 
	 * @param halbjahr   das zu prüfende Halbjahr
	 * 
	 * @return true, falls das Fach in dem Halbjahr wählbar ist, sonst false
	 */
	public static boolean istWaehlbar(final GostFach fach, final @NotNull GostHalbjahr halbjahr) {
		if (fach == null)
			return false;
		if (halbjahr == GostHalbjahr.EF1)
			return fach.istMoeglichEF1;
		if (halbjahr == GostHalbjahr.EF2)
			return fach.istMoeglichEF2;
		if (halbjahr == GostHalbjahr.Q11)
			return fach.istMoeglichQ11;
		if (halbjahr == GostHalbjahr.Q12)
			return fach.istMoeglichQ12;
		if (halbjahr == GostHalbjahr.Q21)
			return fach.istMoeglichQ21;
		if (halbjahr == GostHalbjahr.Q22)
			return fach.istMoeglichQ22;
		return false;
	}
	
}
