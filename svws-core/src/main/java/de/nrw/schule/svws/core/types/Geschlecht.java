package de.nrw.schule.svws.core.types;

import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die zulässigen Arten des Geschlechts.
 */
public enum Geschlecht {

	/** Männlich mit Statistikcode 3 */
	M(3, "m", "männlich", "männlich"), 
	
	/** weiblich mit Statistikcode 4 */
	W(4, "w", "weiblich", "weiblich"), 
	
	/** divers mit Statistikcode 5 */
	D(5, "d", "divers", "divers"),
	
	/** ohne Angabe mit Statistikcode 6 */
	X(6, "x", "ohne Angabe", "ohne Angabe im Geburtenregister");


	/** Die ID des Geschlechtes, welche im Rahmen der amtlichen Schulstatistik verwendet wird. */
    public final int id;
    
    /** Das Geschlecht als einstelliges Kürzel */
    public final @NotNull String kuerzel;
    
    /** Die Kurz-Bezeichnung des Geschlechtes */
    public final @NotNull String text;
 
    /** Die ausführliche Bezeichnung des Geschlechtes */
    public final @NotNull String textLang;


    /**
     * Erzeugt ein neues Geschlecht für die Aufzählung der Geschlechter. 
     *  
     * @param id          die ID des Geschlechtes, welche im Rahmen der amtlichen Schulstatistik verwendet wird
     * @param kuerzel     das Geschlecht als einstelliges Kürzel
     * @param text        die textuelle Kurz-Bezeichnung des Geschlechtes
     * @param textLang    die ausführliche textuelle Bezeichnung des Geschlechtes
     */
    private Geschlecht(int id, @NotNull String kuerzel, @NotNull String text, @NotNull String textLang) {
        this.id = id;
        this.kuerzel = kuerzel;
        this.text = text;
        this.textLang = textLang;
    }
 
    
    /** 
     * Bestimmt das Geschlecht anhand der ID.
     * 
     * @param value   die ID des Geschlechtes
     * 
     * @return das Geschlecht oder null, falls die ID fehlerhaft ist
     */
    public static Geschlecht fromValue(Integer value) {
    	if (value == null)
    		return null;
    	switch (value) {
    		case 3: return Geschlecht.M; 
    		case 4: return Geschlecht.W; 
    		case 5: return Geschlecht.D; 
    		case 6: return Geschlecht.X;    		
    	}
    	return null;
    }
    
    
    /** 
     * Bestimmt das Geschlecht anhand des übergebenen Strings.
     * Enthält der übegebene String einen ungültigen Wert,
     * so wird als Geschlecht "x", d.h. ohne Angabe im Geburtenregister 
     * zurückgegebeben.
     * 
     * @param text         die textuelle Beschreibung des Geschlechts
     * 
     * @return das Geschlecht als Type
     */
    public static @NotNull Geschlecht fromStringValue(String text) {
    	if ((text == null) || "".equals(text))
    		return Geschlecht.X;
    	String upperValue = text.toUpperCase();
    	switch (upperValue) {
    		case "MÄNNLICH", "MAENNLICH", "M":
    			return Geschlecht.M;
    		case "WEIBLICH", "W":
    			return Geschlecht.W;
    		case "DIVERS", "D":
    			return Geschlecht.D;
    		case "-", "X", "OHNE ANGABE", "OHNE_ANGABE", "OHNE ANGABE IM GEBURTENREGISTER":
    			return Geschlecht.X;
    	}
  		return Geschlecht.X;
    }
    
    
    /**
     * Gibt die Anrede für eine Person dieses Geschlechts in Abhängigkeit vom Alter zurück.
     * 
     * @param alter   das Alter der Person
     * 
     * @return die Anrede
     */
    public String getAnrede(int alter) {
		switch (this.id) {
			case 3 : return (alter < 18) ? "Lieber" : "Sehr geehrter Herr"; 
			case 4 : return (alter < 18) ? "Liebe" : "Sehr geehrte Frau"; 
			case 5 : return "Guten Tag";
			case 6 : return "Guten Tag";
		}
		return "Guten Tag";
    }


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}
    

}
