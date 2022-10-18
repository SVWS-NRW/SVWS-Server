package de.nrw.schule.svws.core.types.kaoa;

import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Optionsarten von KAoA-Merkmalen.
 */
public enum KAOAMerkmaleOptionsarten {

    /** Keine Option für das KAoA-Merkmal */
    KEINE(null);


	/** Das Kürzel für die Optionsart */
	public final String kuerzel;


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param kuerzel   das Kürzel  
	 */
	private KAOAMerkmaleOptionsarten(String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Gibt die Optionsart anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel
	 * 
	 * @return die Optionsart oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAMerkmaleOptionsarten getByKuerzel(String kuerzel) {
	    for (@NotNull KAOAMerkmaleOptionsarten art : KAOAMerkmaleOptionsarten.values())
	        if (art.kuerzel.equals(kuerzel))
	            return art;
		return null;
	}

}
