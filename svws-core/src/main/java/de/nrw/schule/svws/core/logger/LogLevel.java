package de.nrw.schule.svws.core.logger;

/**
 * Eine Aufzählung mit den unterschiedlichen Log-Leveln für das Logging 
 */
public enum LogLevel {
	
	/** Das Log-Level, bei dem keine Fehler, etc. ausgegeben werden, sondern nur Informationen der Anwendung */
    APP(0),
    
    /** Das Log-Level, welches nur Fehler ausgibt. */
    ERROR(10),
    
    /** Das Log-Level, welches auch Warnungen ausgibt. */
    WARNING(100),
    
    /** Das Log-Level, welches auch Informationen, die keine Warnungen sind, ausgibt. */
    INFO(1000),
    
    /** Das Log-Level, welches zum Debuggen alles ausgibt. */
    DEBUG(10000);

	
	/// der Integer-Wert des Log-Levels
    private final int level;

    
    /**
     * Der von der Aufzählung intern genutzte Konstruktor der Aufzählung 
     * 
     * @param level   der Integer-Wert des Log-Levels.
     */
    LogLevel(int level) {
        this.level = level;
    }
    

    /**
     * Gibt den Integer-Wert des Log-Levels zurück.
     *  
     * @return der Integer-Wert des Log-Levels
     */
    public int toInteger() {
        return level;
    }
}


