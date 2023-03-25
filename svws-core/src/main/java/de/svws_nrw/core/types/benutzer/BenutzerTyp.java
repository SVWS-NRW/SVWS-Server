package de.svws_nrw.core.types.benutzer;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist eine Aufzählung der zulässigen Benutzertypen.
 */
public enum BenutzerTyp {
	
	/** Ein allgemeiner Benutzertyp */
    ALLGEMEIN(0, "Allgemein"),

	/** Ein Benutzertyp für Lehrer und weiteres Personal */
    LEHRER(1, "Lehrer/Personal"),

	/** Ein Benutzertyp für Schüler */
    SCHUELER(2, "Schüler"),

	/** Ein Benutzertyp für Erzieher */
    ERZIEHER(3, "Erzieher");



	/** Die ID des Benutzertyps */
    public final int id;
    
    /** Die textuelle Bezeichnung des Benutzertyps. */
    public final @NotNull String bezeichnung;
    
    
    /**
     * Erzeugt einen neuen Benutzertyp für die Aufzählung.
     *
     * @param id                  die ID des Benutzertyps
     * @param bezeichnung         die Bezeichnung des Benutzertyps
     */
    private BenutzerTyp(final int id, final @NotNull String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }


    /** 
     * Gibt den Benutzertyp die Benutzerkompetenz anhand der übergebenen ID zurück. 
     * 
     * @param id    die ID der Benutzerkompetenz
     *  
     * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
     */
    public static BenutzerTyp getByID(final int id) {
    	switch (id) {
    		case 0: return BenutzerTyp.ALLGEMEIN;
    		case 1: return BenutzerTyp.LEHRER;
    		case 2: return BenutzerTyp.SCHUELER;
    		case 3: return BenutzerTyp.ERZIEHER;
    	}
    	return null;
    }
    
    
}
