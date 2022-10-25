package de.nrw.schule.svws.core.types.fach;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.core.types.RGBFarbe;
import de.nrw.schule.svws.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Fachgruppen zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Fachgruppe {
	
	/** Fachgruppe ID 1: Deutsch */
	FG1_D(1L, 1, 110, "Deutsch", "D", new RGBFarbe(253, 233, 217), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 0, true, null, null),

	/** Fachgruppe ID 2: Arbeitslehre */
	FG2_AL(2L, 2, 400, "Arbeitslehre", "AL", new RGBFarbe(253, 221, 195), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 12, true, null, null),

	/** Fachgruppe ID 3: Fremdsprachen */
	FG3_FS(3L, 2, 100, "Fremdsprachen", "FS", new RGBFarbe(253, 221, 195), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 9, true, null, null),

	/** Fachgruppe ID 4: Kunst und Musik */
	FG4_MS(4L, 3, 500, "Kunst und Musik", "MS", new RGBFarbe(252, 204, 165), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 13, true, null, null),

	/** Fachgruppe ID 5: Literatur, instrumental- oder vokalpraktischer Kurs */
	FG5_ME(5L, 4, null, "Literatur, instrumental- oder vokalpraktischer Kurs", "ME", new RGBFarbe(252, 204, 165), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.SG, 
			Schulform.WB
		), 13, false, null, null),

	/** Fachgruppe ID 6: Gesellschaftswissenschaft */
	FG6_GS(6L, 5, 300, "Gesellschaftswissenschaft", "GS", new RGBFarbe(234, 241, 222), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 11, true, null, null),

	/** Fachgruppe ID 7: Philosophie */
	FG7_PL(7L, 5, null, "Philosophie", "PL", new RGBFarbe(234, 241, 222), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.SG, 
			Schulform.WB
		), 11, false, null, null),

	/** Fachgruppe ID 8: Religion */
	FG8_RE(8L, 6, 900, "Religion", "RE", new RGBFarbe(215, 228, 188), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 6, true, null, null),

	/** Fachgruppe ID 9: Mathematik */
	FG9_M(9L, 7, 700, "Mathematik", "M", new RGBFarbe(197, 217, 241), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 15, true, null, null),

	/** Fachgruppe ID 10: Naturwissenschaften */
	FG10_NW(10L, 8, 200, "Naturwissenschaften", "NW", new RGBFarbe(141, 180, 227), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 10, true, null, null),

	/** Fachgruppe ID 11: weiteres naturwissenschaftliches / technisches Fach */
	FG11_WN(11L, 8, null, "weiteres naturwissenschaftliches / technisches Fach", "WN", new RGBFarbe(141, 180, 227), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.SG, 
			Schulform.WB
		), 10, false, null, null),

	/** Fachgruppe ID 12: Sport */
	FG12_SP(12L, 9, 600, "Sport", "SP", new RGBFarbe(255, 255, 255), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 14, true, null, null),

	/** Fachgruppe ID 13: Vertiefungskurs */
	FG13_VX(13L, 10, 1500, "Vertiefungskurs", "VX", new RGBFarbe(216, 216, 216), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.SG, 
			Schulform.WB
		), 0, false, null, null),

	/** Fachgruppe ID 14: Projektkurs */
	FG14_PX(14L, 11, 1600, "Projektkurs", "PX", new RGBFarbe(191, 191, 191), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.SG, 
			Schulform.WB
		), 0, false, null, null),

	/** Fachgruppe ID 15: Berufsübergreifender Bereich */
	FG15_BUE(15L, null, 10, "Berufsübergreifender Bereich", "BUE", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 1, false, null, null),

	/** Fachgruppe ID 16: Berufsbezogener Bereich */
	FG16_BBS(16L, null, 20, "Berufsbezogener Bereich", "BBS", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 2, false, null, null),

	/** Fachgruppe ID 17: Berufsbezogener Bereich (Schwerpunkt) */
	FG17_BBS(17L, null, 25, "Berufsbezogener Bereich (Schwerpunkt)", "BBS", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 0, false, null, null),

	/** Fachgruppe ID 18: Differenzierungsbereich */
	FG18_DF(18L, null, 30, "Differenzierungsbereich", "DF", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 3, false, null, null),

	/** Fachgruppe ID 19: Berufspraktikum */
	FG19_BP(19L, null, 40, "Berufspraktikum", "BP", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 4, false, null, null),

	/** Fachgruppe ID 20: besondere Lernleistung */
	FG20_BLL(20L, null, 60, "besondere Lernleistung", "BLL", null, Arrays.asList(
			Schulform.GE, 
			Schulform.GY, 
			Schulform.SG, 
			Schulform.WB
		), 8, false, null, null),

	/** Fachgruppe ID 21: Wahlpflichtbereich */
	FG21_WP(21L, null, 800, "Wahlpflichtbereich", "WP", null, Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 16, false, null, null),

	/** Fachgruppe ID 22: Zusätzliche Unterrichtsveranstaltungen */
	FG22_ZUV(22L, null, 1000, "Zusätzliche Unterrichtsveranstaltungen", "ZUV", null, Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 0, false, null, null),

	/** Fachgruppe ID 23: Angleichungskurse */
	FG23_ANG(23L, null, 1100, "Angleichungskurse", "ANG", null, Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 0, false, null, null),

	/** Fachgruppe ID 24: Sprache */
	FG24_D_SP(24L, null, 1200, "Sprache", "D_SP", null, Arrays.asList(
			Schulform.G, 
			Schulform.KS, Schulform.S, 
			Schulform.V
		), 0, true, null, null),

	/** Fachgruppe ID 25: Sachunterricht */
	FG25_SU(25L, null, 1300, "Sachunterricht", "SU", null, Arrays.asList(
			Schulform.G, 
			Schulform.KS, Schulform.S, 
			Schulform.V
		), 0, true, null, null),

	/** Fachgruppe ID 26: Förderunterricht */
	FG26_FOE(26L, null, 1400, "Förderunterricht", "FOE", null, Arrays.asList(
			Schulform.G, 
			Schulform.KS, Schulform.S, 
			Schulform.V
		), 0, true, null, null),

	/** Fachgruppe ID 27: Abschlussarbeit */
	FG27_ABA(27L, null, 1700, "Abschlussarbeit", "ABA", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 0, false, null, null),

	/** Fachgruppe ID 28: Projektarbeit */
	FG28_PA(28L, null, 1800, "Projektarbeit", "PA", null, Arrays.asList(
			Schulform.BK, Schulform.SB
		), 0, false, null, null),

	/** Fachgruppe ID 29: Informatik (Sek I) */
	FG29_IF(29L, null, 1900, "Informatik (Sek I)", "IF", new RGBFarbe(141, 180, 227), Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V, 
			Schulform.WB
		), 10, true, null, null);


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;

	
	/** Eine Map, welche der ID der Fachgruppe die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull Fachgruppe> _mapByID = new HashMap<>();
	
	/** Eine Map, welche dem Kürzel der Fachgruppe die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull String, @NotNull Fachgruppe> _mapByKuerzel = new HashMap<>();


	/** Die eindeutige ID der Fachgruppe */
	public final long id;
	
	/** Die Nummer für den Fachbereich, sofern festgelegt, ansonsten null */
	public final Integer fachbereich;
	
	/** Die alte Fachgruppen-ID, welche in Schild_NRW 2.x verwendet wurde */
	public final Integer idSchild;
	
	/** Die Bezeichnung der Fachgruppe */
	public final @NotNull String bezeichnung;
	
	/** Das Kürzel der Fachgruppe */
	public final @NotNull String kuerzel;
	
	/** Die Farbe, welche der Fachgruppe zugeordnet wurde */
	public final @NotNull RGBFarbe farbe;

	/** Die Kürzel der Schulformen, bei welchen die Fachgruppen vorkommt. */
	public final @NotNull List<@NotNull String> schulformenKuerzel = new Vector<>();
	
	/** Die Schulformen, bei welchen die Fachgruppen vorkommt */
	@JsonIgnore
	private Vector<@NotNull Schulform> schulformen = null;

	/** Ein Zahlwert, welche die Standard-Reihenfolge der Fachgruppen in der Visualisierung angibt. */
	public final int sortierung;
	
	/** Gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht */
	public final boolean fuerZeugnis;
	
	/** Gibt an, in welchem Schuljahr die Fachgruppe einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Fachgruppe gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigBis;


	/**
	 * Erzeugt eine neue Fachgruppe in der Aufzählung.
	 *  
	 * @param id               die eindeutige ID der Fachgruppe
	 * @param fachbereich      die Nummer für den Fachbereich, sofern festgelegt, ansonsten null
	 * @param idSchild         die alte Fachgruppen-ID, welche in Schild_NRW 2.x verwendet wurde 
	 * @param bezeichnung      die Bezeichnung der Fachgruppe
	 * @param kuerzel          das Kürzel der Fachgruppe
	 * @param farbe            die Farbe, die der Fachgruppe zugeordnet wird oder null, falls die Standard-Farbe Weiss 
	 *                         zugeordnet werden soll. Diese 
	 * @param schulformen      eine Liste mit den Schulformen, bei denen die Fachgruppe vorkommt
	 * @param sortierung       ein Zahlwert, welche die Standard-Reihenfolge der Fachgruppen in der Visualisierung angibt
	 * @param fuerZeugnis      gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht
	 * @param gueltigVon       gibt an, in welchem Schuljahr die Fachgruppe einführt wurde - ist kein Schuljahr bekannt, so wird null gesetzt
	 * @param gueltigBis       gibt an, bis zu welchem Schuljahr die Fachgruppe gültig ist - ist kein Schuljahr bekannt, so wird null gesetzt
	 */
	private Fachgruppe(long id, Integer fachbereich, Integer idSchild, @NotNull String bezeichnung, @NotNull String kuerzel, RGBFarbe farbe, 
			@NotNull List<@NotNull Schulform> schulformen, final int sortierung, final boolean fuerZeugnis, final Integer gueltigVon, final Integer gueltigBis) {
		for (@NotNull Schulform schulform : schulformen)
			this.schulformenKuerzel.add(schulform.daten.kuerzel);
		this.id = id;
		this.fachbereich = fachbereich;
		this.idSchild = idSchild;
		this.bezeichnung = bezeichnung;
		this.kuerzel = kuerzel;
		this.farbe = (farbe == null) ? new RGBFarbe(255, 255, 255) : farbe;
		this.sortierung = sortierung;
		this.fuerZeugnis = fuerZeugnis;
		this.gueltigVon = null;
		this.gueltigBis = null;
	}
	

	/**
	 * Gibt eine Map von den IDs der Fachgruppen auf die zugehörigen Fachgruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Fachgruppen auf die zugehörigen Fachgruppen
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull Fachgruppe> getMapByID() {
		if (_mapByID.size() == 0)
			for (Fachgruppe g : Fachgruppe.values())
				_mapByID.put(g.id, g);				
		return _mapByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Fachgruppen auf die zugehörigen Fachgruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Fachgruppen auf die zugehörigen Fachgruppen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Fachgruppe> getMapByKuerzel() {
		if (_mapByKuerzel.size() == 0)
			for (Fachgruppe g : Fachgruppe.values())
				_mapByKuerzel.put(g.kuerzel, g);				
		return _mapByKuerzel;
	}

	
	/**
	 * Liefert alle Schulformen zurück, bei welchen die Schulgliederung vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	@JsonIgnore
	public @NotNull List<@NotNull Schulform> getSchulformen() {
		if (schulformen == null) {
			schulformen = new Vector<>();
			for (int i = 0; i < schulformenKuerzel.size(); i++) {
				Schulform schulform = Schulform.getByKuerzel(schulformenKuerzel.get(i));
				if (schulform != null)
					schulformen.add(schulform);
			}
		}
		return schulformen;
	}
	

	/**
	 * Prüft, ob die Schulform bei diesem Fach in irgendeiner Gliederung der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls das Fach in der Schulform zulässig ist, ansonsten false.
	 */
	@JsonIgnore
	private boolean hasSchulform(Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		if (schulformenKuerzel != null) {
			for (int i = 0; i < schulformenKuerzel.size(); i++) {
				String sfKuerzel = schulformenKuerzel.get(i);
				if (sfKuerzel.equals(schulform.daten.kuerzel))
					return true;
			}			
		}
		return false;
	}


	/**
	 * Liefert die Fachgruppe zu der übergebenen ID der Fachgruppe zurück.
	 * 
	 * @param id   die ID der Fachgruppe
	 * 
	 * @return die Fachgruppe oder null, falls die ID ungültig ist
	 */
	public static Fachgruppe getByID(final long id) {
		return getMapByID().get(id);
	}
	
	
	/**
	 * Liefert die Fachgruppe zu der übergebenen ID der Fachgruppe zurück.
	 * 
	 * @param kuerzel   das Kürzel der Fachgruppe
	 * 
	 * @return die Fachgruppe oder null, falls das Kürzel ungültig ist
	 */
	public static Fachgruppe getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}
	
	
	/**
	 * Bestimmt alle Fachgruppen, die in irgendeiner Gliederung der angegebenen Schulform
	 * zulässig sind. 
	 *  
	 * @param schulform    die Schulform
	 * 
	 * @return die Fachgruppen in der angegebenen Schulform
	 */
	public static @NotNull List<@NotNull Fachgruppe> get(Schulform schulform) {
		@NotNull Vector<@NotNull Fachgruppe> faecher = new Vector<>();
		if (schulform == null)
			return faecher;
		@NotNull Fachgruppe@NotNull[] fachgruppen = Fachgruppe.values();
		for (int i = 0; i < fachgruppen.length; i++) {
			Fachgruppe fg = fachgruppen[i];
			if (fg.hasSchulform(schulform))
				faecher.add(fg);
		}
		return faecher;
	}


}
