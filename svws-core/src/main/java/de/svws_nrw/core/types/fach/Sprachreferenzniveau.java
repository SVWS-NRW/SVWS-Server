package de.svws_nrw.core.types.fach;

import java.util.HashMap;

import de.svws_nrw.core.data.fach.SprachreferenzniveauKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Sprachreferenznniveaus.
 */
public enum Sprachreferenzniveau  {
	
	/** Referenzniveau nach GeR A1. */
	A1(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(1, "A1", null, null)
	}),
	
	/** Referenzniveau nach GeR A1 Plus */
	A1P(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(2, "A1+", null, null)
	}),
	
	/** Referenzniveau nach GeR A1A2 */
	A1A2(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(3, "A1/A2", null, null)
    }),
	
	/** Referenzniveau nach GeR A2 */
	A2(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(4, "A2", null, null)
    }),
	
	/** Referenzniveau nach GeR A2 Plus */
	A2P(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(5, "A2+", null, null)
    }),
	
	/** Referenzniveau nach GeR A2B1. */
	A2B1(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(6, "A2/B1", null, null)
    }),
	
	/** Referenzniveau nach GeR B1. */
	B1(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(7, "B1", null, null)
    }),
	
	/** Referenzniveau nach GeR B1 Plus. */
	B1P(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(8, "B1+", null, null)
    }),
	
	/** Referenzniveau nach GeR B1B2. */
	B1B2(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(9, "B1/B2", null, null)
    }),
	
	/** Referenzniveau nach GeR B2. */
	B2(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(10, "B2", null, null)
    }),
	
	/** Referenzniveau nach GeR B2C1. */
	B2C1(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(11, "B2/C1", null, null)
    }),
	
	/** Referenzniveau nach GeR C1. */
	C1(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(12, "C1", null, null)
    }),
	
	/** Referenzniveau nach GeR C2. */
	C2(new SprachreferenzniveauKatalogEintrag[] {
	    new SprachreferenzniveauKatalogEintrag(13, "C2", null, null)
    });


    /** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
    public static long VERSION = 1;

    /** Der aktuellen Daten des Sprachprüfungsniveaus */
    public final @NotNull SprachreferenzniveauKatalogEintrag daten;

    /** Die Historie mit den Einträgen des Sprachprüfungsniveaus */
    public final @NotNull SprachreferenzniveauKatalogEintrag@NotNull[] historie; 

    /** Die Zuordnung der Sprachreferenzniveaus zu ihren IDs */
    private static final @NotNull HashMap<@NotNull Integer, @NotNull Sprachreferenzniveau> _mapID = new HashMap<>();

    /** Die Zuordnung der Sprachreferenzniveaus zu ihren Bezeichnungen */
    private static final @NotNull HashMap<@NotNull String, @NotNull Sprachreferenzniveau> _mapKuerzel = new HashMap<>();


	/** 
	 * Erstellt ein neues Sprachreferenzniveau dieser Aufzählung.

     * @param historie   die Historie des Sprachreferenzniveaus, welche ein Array von 
     *                   {@link SprachreferenzniveauKatalogEintrag} ist  
	 */
	private Sprachreferenzniveau(final @NotNull SprachreferenzniveauKatalogEintrag@NotNull[] historie) {
        this.historie = historie;
        this.daten = historie[historie.length - 1];
	}

	
    /**
     * Gibt eine Map von den IDs der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
     * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
     *    
     * @return die Map von den IDs der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
     */
    private static @NotNull HashMap<@NotNull Integer, @NotNull Sprachreferenzniveau> getMapByID() {
        if (_mapID.size() == 0)
            for (final Sprachreferenzniveau l : Sprachreferenzniveau.values())
                _mapID.put(l.daten.id, l);              
        return _mapID;
    }

    
    /**
     * Gibt eine Map von den Bezeichnungen der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
     * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
     *    
     * @return die Map von den Bezeichnungen der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
     */
    private static @NotNull HashMap<@NotNull String, @NotNull Sprachreferenzniveau> getMapByKuerzel() {
        if (_mapKuerzel.size() == 0)
            for (final Sprachreferenzniveau l : Sprachreferenzniveau.values())
                _mapKuerzel.put(l.daten.kuerzel, l);                
        return _mapKuerzel;
    }


    /**
     * Gibt das Sprachreferenzniveau für die übergebene ID zurück.
     *
     * @param id   die ID des Sprachreferenzniveaus
     *
     * @return das Sprachreferenzniveau oder null, wenn die ID ungültig ist
     */
    public static Sprachreferenzniveau getByID(final Integer id) {
        return getMapByID().get(id);
    }


    /**
     * Gibt das Sprachreferenzniveau für das übergebene Kürzel zurück.
     * 
     * @param kuerzel   das Kürzel des Sprachreferenzniveaus
     * 
     * @return das Sprachreferenzniveau oder null, wenn das Kürzel ungültig ist
     */
    public static Sprachreferenzniveau getByKuerzel(final String kuerzel) {
        return getMapByKuerzel().get(kuerzel);
    }

    
	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem Niveau des übergebenen Kürzels.
	 * 
	 * @param kuerzel   das Kürzel des anderen Sprachreferenzniveaus
	 * 
	 * @return -1 (kleiner), 0 (gleich) oder 1 (größer)
	 */
	public int vergleiche(final String kuerzel) {
	    final Sprachreferenzniveau other = getByKuerzel(kuerzel);
	    if (other == null)
	        return 1;
	    return this.compareTo(other);
	}
	
}
