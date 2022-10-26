package de.nrw.schule.svws.core.types.fach;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.RGBFarbe;
import de.nrw.schule.svws.core.data.fach.FachgruppenKatalogEintrag;
import de.nrw.schule.svws.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Fachgruppen zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Fachgruppe {
	
	/** Fachgruppe Deutsch */
	FG_D(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(1L, 1, 110, "Deutsch", "D", new RGBFarbe(253, 233, 217), Arrays.asList(
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
    		), 0, true, null, null)
	}),

	/** Fachgruppe Arbeitslehre */
	FG_AL(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(2L, 2, 400, "Arbeitslehre", "AL", new RGBFarbe(253, 221, 195), Arrays.asList(
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
    		), 12, true, null, null)
	}),

	/** Fachgruppe Fremdsprachen */
	FG_FS(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(3L, 2, 100, "Fremdsprachen", "FS", new RGBFarbe(253, 221, 195), Arrays.asList(
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
    		), 9, true, null, null)
	}),

	/** Fachgruppe Kunst und Musik */
	FG_MS(new FachgruppenKatalogEintrag[] {
	        new FachgruppenKatalogEintrag(4L, 3, 500, "Kunst und Musik", "MS", new RGBFarbe(252, 204, 165), Arrays.asList(
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
    		), 13, true, null, null)
	}),

	/** Fachgruppe Literatur, instrumental- oder vokalpraktischer Kurs */
	FG5_ME(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(5L, 4, null, "Literatur, instrumental- oder vokalpraktischer Kurs", "ME", new RGBFarbe(252, 204, 165), Arrays.asList(
    			Schulform.BK, Schulform.SB,
    			Schulform.GE, 
    			Schulform.GY, 
    			Schulform.SG, 
    			Schulform.WB
    		), 13, false, null, null)
	}),

	/** Fachgruppe Gesellschaftswissenschaft */
	FG_GS(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(6L, 5, 300, "Gesellschaftswissenschaft", "GS", new RGBFarbe(234, 241, 222), Arrays.asList(
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
    		), 11, true, null, null)
    }),

	/** Fachgruppe Philosophie */
	FG_PL(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(7L, 5, null, "Philosophie", "PL", new RGBFarbe(234, 241, 222), Arrays.asList(
    			Schulform.BK, Schulform.SB,
    			Schulform.GE, 
    			Schulform.GY, 
    			Schulform.SG, 
    			Schulform.WB
    		), 11, false, null, null)
	}),

	/** Fachgruppe Religion */
	FG_RE(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(8L, 6, 900, "Religion", "RE", new RGBFarbe(215, 228, 188), Arrays.asList(
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
    		), 6, true, null, null)
    }),

	/** Fachgruppe Mathematik */
	FG_M(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(9L, 7, 700, "Mathematik", "M", new RGBFarbe(197, 217, 241), Arrays.asList(
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
    		), 15, true, null, null)
	}),

	/** Fachgruppe Naturwissenschaften */
	FG_NW(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(10L, 8, 200, "Naturwissenschaften", "NW", new RGBFarbe(141, 180, 227), Arrays.asList(
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
    		), 10, true, null, null)
	}),

	/** Fachgruppe weiteres naturwissenschaftliches / technisches Fach */
	FG_WN(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(11L, 8, null, "weiteres naturwissenschaftliches / technisches Fach", "WN", new RGBFarbe(141, 180, 227), Arrays.asList(
    			Schulform.BK, Schulform.SB,
    			Schulform.GE, 
    			Schulform.GY, 
    			Schulform.SG, 
    			Schulform.WB
    		), 10, false, null, null)
	}),

	/** Fachgruppe Sport */
	FG_SP(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(12L, 9, 600, "Sport", "SP", new RGBFarbe(255, 255, 255), Arrays.asList(
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
    		), 14, true, null, null)
	}),

	/** Fachgruppe Vertiefungskurs */
	FG_VX(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(13L, 10, 1500, "Vertiefungskurs", "VX", new RGBFarbe(216, 216, 216), Arrays.asList(
    			Schulform.BK, Schulform.SB,
    			Schulform.GE, 
    			Schulform.GY, 
    			Schulform.SG, 
    			Schulform.WB
    		), 0, false, null, null)
	}),

	/** Fachgruppe Projektkurs */
	FG_PX(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(14L, 11, 1600, "Projektkurs", "PX", new RGBFarbe(191, 191, 191), Arrays.asList(
    			Schulform.BK, Schulform.SB,
    			Schulform.GE, 
    			Schulform.GY, 
    			Schulform.SG, 
    			Schulform.WB
    		), 0, false, null, null)
    }),

	/** Fachgruppe Berufsübergreifender Bereich */
	FG15_BUE(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(15L, null, 10, "Berufsübergreifender Bereich", "BUE", new RGBFarbe(), Arrays.asList(
    			Schulform.BK, Schulform.SB
    		), 1, false, null, null)
	}),

	/** Fachgruppe Berufsbezogener Bereich */
	FG_BBS(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(16L, null, 20, "Berufsbezogener Bereich", "BBS", new RGBFarbe(), Arrays.asList(
    			Schulform.BK, Schulform.SB
    		), 2, false, null, null)
	}),

	/** Fachgruppe Berufsbezogener Bereich (Schwerpunkt) */
	FG_BBS_SCHWERPUNKT(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(17L, null, 25, "Berufsbezogener Bereich (Schwerpunkt)", "BBS", new RGBFarbe(), Arrays.asList(
        		Schulform.BK, Schulform.SB
        	), 0, false, null, null)
	}),

	/** Fachgruppe Differenzierungsbereich */
	FG_DF(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(18L, null, 30, "Differenzierungsbereich", "DF", new RGBFarbe(), Arrays.asList(
    			Schulform.BK, Schulform.SB
    		), 3, false, null, null)
	}),

	/** Fachgruppe Berufspraktikum */
	FG_BP(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(19L, null, 40, "Berufspraktikum", "BP", new RGBFarbe(), Arrays.asList(
    			Schulform.BK, Schulform.SB
    		), 4, false, null, null)
	}),

	/** Fachgruppe besondere Lernleistung */
	FG_BLL(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(20L, null, 60, "besondere Lernleistung", "BLL", new RGBFarbe(), Arrays.asList(
    			Schulform.GE, 
    			Schulform.GY, 
    			Schulform.SG, 
    			Schulform.WB
    		), 8, false, null, null)
	}),

	/** Fachgruppe Wahlpflichtbereich */
	FG_WP(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(21L, null, 800, "Wahlpflichtbereich", "WP", new RGBFarbe(), Arrays.asList(
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
    		), 16, false, null, null)
    }),

	/** Fachgruppe Zusätzliche Unterrichtsveranstaltungen */
	FG_ZUV(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(22L, null, 1000, "Zusätzliche Unterrichtsveranstaltungen", "ZUV", new RGBFarbe(), Arrays.asList(
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
		), 0, false, null, null)
	}),

	/** Fachgruppe Angleichungskurse */
	FG_ANG(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(23L, null, 1100, "Angleichungskurse", "ANG", new RGBFarbe(), Arrays.asList(
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
    		), 0, false, null, null)
	}),

	/** Fachgruppe Sprache */
	FG_D_SP(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(24L, null, 1200, "Sprache", "D_SP", new RGBFarbe(), Arrays.asList(
    			Schulform.G, 
    			Schulform.KS, Schulform.S, 
    			Schulform.V
    		), 0, true, null, null)
	}),

	/** Fachgruppe Sachunterricht */
	FG_SU(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(25L, null, 1300, "Sachunterricht", "SU", new RGBFarbe(), Arrays.asList(
    			Schulform.G, 
    			Schulform.KS, Schulform.S, 
    			Schulform.V
    		), 0, true, null, null)
	}),

	/** Fachgruppe Förderunterricht */
	FG_FOE(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(26L, null, 1400, "Förderunterricht", "FOE", new RGBFarbe(), Arrays.asList(
    			Schulform.G, 
    			Schulform.KS, Schulform.S, 
    			Schulform.V
    		), 0, true, null, null)
	}),

	/** Fachgruppe Abschlussarbeit */
	FG_ABA(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(27L, null, 1700, "Abschlussarbeit", "ABA", new RGBFarbe(), Arrays.asList(
    			Schulform.BK, Schulform.SB
    		), 0, false, null, null)
    }),

	/** Fachgruppe Projektarbeit */
	FG_PA(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(28L, null, 1800, "Projektarbeit", "PA", new RGBFarbe(), Arrays.asList(
    			Schulform.BK, Schulform.SB
    		), 0, false, null, null)
	}),

	/** Fachgruppe Informatik (Sek I) */
	FG29_IF(new FachgruppenKatalogEintrag[] {
	    new FachgruppenKatalogEintrag(29L, null, 1900, "Informatik (Sek I)", "IF", new RGBFarbe(141, 180, 227), Arrays.asList(
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
    		), 10, true, null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;

    /** Der aktuellen Daten der Fachgruppe */
    public final @NotNull FachgruppenKatalogEintrag daten;

    /** Die Historie mit den Einträgen der Fachgruppe */
    public final @NotNull FachgruppenKatalogEintrag@NotNull[] historie; 

    /** Eine Map, welche der ID der Fachgruppe die Instanz dieser Aufzählung zuordnet. */
    private static final @NotNull HashMap<@NotNull Long, @NotNull FachgruppenKatalogEintrag> _mapEintragByID = new HashMap<>();
    
	/** Eine Map, welche der ID der Fachgruppe die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull Fachgruppe> _mapByID = new HashMap<>();
	
	/** Eine Map, welche dem Kürzel der Fachgruppe die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull String, @NotNull Fachgruppe> _mapByKuerzel = new HashMap<>();

    /** Die Schulformen, bei welchen die Fachgruppe vorkommt */
    private @NotNull Vector<@NotNull Schulform>@NotNull[] schulformen;
	

	/**
     * Erzeugt eine neue Fachgruppe in der Aufzählung.
     * 
     * @param historie   die Historie der Fachgruppe, welche ein Array von 
     *                   {@link FachgruppenKatalogEintrag} ist  
	 */
	@SuppressWarnings("unchecked")
    private Fachgruppe(@NotNull FachgruppenKatalogEintrag@NotNull[] historie) {
        this.historie = historie;
        this.daten = historie[historie.length - 1];
        // Erzeuge ein zweites Array mit der Schulformzuordnung für dei Historie
        this.schulformen = (@NotNull Vector<@NotNull Schulform>@NotNull[])Array.newInstance(Vector.class, historie.length); 
        for (int i = 0; i < historie.length; i++) {
            this.schulformen[i] = new Vector<>();
            for (@NotNull String kuerzel : historie[i].schulformen) {
                Schulform sf = Schulform.getByKuerzel(kuerzel);
                if (sf != null)
                    this.schulformen[i].add(sf);
            }
        }
	}
	

	/**
	 * Gibt eine Map von den IDs der Fachgruppen auf die zugehörigen Katalog-Einträge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Fachgruppen auf die zugehörigen Katalog-Einträge
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull FachgruppenKatalogEintrag> getMapEintragByID() {
		if (_mapEintragByID.size() == 0)
			for (Fachgruppe g : Fachgruppe.values())
			    for (FachgruppenKatalogEintrag k : g.historie)
			        _mapEintragByID.put(k.id, k);
		return _mapEintragByID;
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
                _mapByID.put(g.daten.id, g);
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
				_mapByKuerzel.put(g.daten.kuerzel, g);				
		return _mapByKuerzel;
	}

	
	/**
	 * Liefert alle Schulformen zurück, bei welchen die Fachgruppe vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	public @NotNull List<@NotNull Schulform> getSchulformen() {
        return schulformen[historie.length - 1];
	}
	

	/**
	 * Prüft, ob die Schulform bei diesem Fach in irgendeiner Fachgruppe der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls die Fachgruppe in der Schulform zulässig ist, ansonsten false.
	 */
	private boolean hasSchulform(Schulform schulform) {
        if ((schulform == null) || (schulform.daten == null))
            return false;
        if (daten.schulformen != null) {
            for (int i = 0; i < daten.schulformen.size(); i++) {
                String sfKuerzel = daten.schulformen.get(i);
                if (sfKuerzel.equals(schulform.daten.kuerzel))
                    return true;
            }           
        }
        return false;
	}


	/**
	 * Liefert den Katalog-Eintrag der Fachgruppe zu der übergebenen ID zurück.
	 * 
	 * @param id   die ID des Fachgruppen-Katalog-Eintrags
	 * 
	 * @return der Fachgruppen-Katalog-Eintrag oder null, falls die ID ungültig ist
	 */
	public static FachgruppenKatalogEintrag getKatalogEintragByID(final long id) {
		return getMapEintragByID().get(id);
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
	 * Bestimmt alle Fachgruppen, die in der angegebenen Schulform zulässig sind. 
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
