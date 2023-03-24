package de.nrw.schule.svws.core.types.kaoa;

import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Optionsarten von KAoA-Zusatzmerkmalen.
 */
public enum KAOAZusatzmerkmaleOptionsarten {

    /** Keine Option für das KAoA-Zusatzmerkmal */
    KEINE(null),

    /** Anschlussoptionen laut SBO 10.7 */
    ANSCHLUSSOPTION("Anschlussoption"),
    
    /** Berufsfelder */
    BERUFSFELD("Berufsfeld"),
    
    /** Freitext */
    FREITEXT("Freitext"),
    
    /** Freitext Beruf */
    FREITEXT_BERUF("Freitext Beruf"),
    
    /** SBO der Ebene 4 (SBO x.x.x.y) */
    SBO_EBENE_4("SBO EB4");


	/** Das Kürzel für die Optionsart */
	public final String kuerzel;


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param kuerzel   das Kürzel  
	 */
	private KAOAZusatzmerkmaleOptionsarten(final String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Gibt die Optionsart anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel
	 * 
	 * @return die Optionsart oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAZusatzmerkmaleOptionsarten getByKuerzel(final String kuerzel) {
	    for (final @NotNull KAOAZusatzmerkmaleOptionsarten art : KAOAZusatzmerkmaleOptionsarten.values())
	        if (art.kuerzel.equals(kuerzel))
	            return art;
		return null;
	}

}
