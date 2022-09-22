package de.nrw.schule.svws.logger;

import java.util.Vector;
import java.util.function.Consumer;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Loggen von Informationen im SVWS-Server. Dabei
 * können mehrere Ziele (Consumer) für die Log-Informationen definiert werden,
 * welche diese weiterverarbeiten.
 */
public class Logger {

	/// Ein interner Vektor zum Speichern der Consumer von Log-Informationen.
	private final @NotNull Vector<@NotNull Consumer<@NotNull LogData>> consumer = new Vector<>();
	
	/// Das Standard-Log-Level, welches für neue Log-Informationen genutzt wird. 
	private @NotNull LogLevel defaultLevel = LogLevel.INFO;
	
	/// Die Einrückung für die Ausgabe, die bei neuen Log-Informationen genutzt wird.
	private int indent = 0;

	
	/**
	 * Fügt einen Consumer für Log-Informationen zum Logger hinzu.
	 * 
	 * @param c   der hinzuzufügende Consumer von Log-Informationen
	 */
	public void addConsumer(@NotNull Consumer<@NotNull LogData> c) {
		consumer.add(c);
	}
	
	
	/**
	 * Fügt alle Consumer des anderen Loggers zu diesem hinzu.
	 * 
	 * @param other   der andere Logger
	 */
	public void copyConsumer(@NotNull Logger other) {
		consumer.addAll(other.consumer);
	}
	
	
	/**
	 * Entfernt den angegeben Consumer für Log-Informationen aus dem Logger.
	 * 
	 * @param c   der zu entfernende Consumer von Log-Informationen
	 */
	public void removeConsumer(@NotNull Consumer<@NotNull LogData> c) {
		consumer.remove(c);
	}
	
	
	/**
	 * Gibt das aktuelle Default-Log-Level für neue Log-Informationen zurück.
	 * 
	 * @return das aktuelle Default-Log-Level für neue Log-Informationen
	 */
    public @NotNull LogLevel getDefaultLevel() {
		return defaultLevel;
	}

    
    /**
     * Setzt das Default-Log-Level für neue Log-Informationen.
     * 
     * @param defaultLevel   das neue Default-Log-Level für neue Log-Informationen
     */
	public void setDefaultLevel(@NotNull LogLevel defaultLevel) {
		this.defaultLevel = defaultLevel;
	}
	
	
	/**
	 * Setzt die Anzahl der Leerzeichen, die für die Einrückung bei einer Ausgabe von 
	 * neuen Log-Informationen genutzt wird.
	 * 
	 * @param indent   die Anzahl der Leerzeichen
	 */
	public void setIndent(int indent) {
		this.indent = (indent < 0) ? 0 : indent;
	}
	
	
	/**
	 * Verändert die Anzahl der Leerzeichen, die für die Einrückung bei einer Ausgabe von 
	 * neuen Log-Informationen genutzt wird.
	 * 
	 * @param indent   die Veränderung bei der Anzahl der Leerzeichen
	 */
	public void modifyIndent(int indent) {
		this.indent = (this.indent + indent < 0) ? 0 : this.indent + indent;
	}
	

	
	/**
	 * Loggt die übergebenen Log-Informationen bei diesem Debugger. 
	 * 
	 * @param data   die Log-Informationen
	 */
    private void log(@NotNull LogData data) {
    	for (int i = 0; i < consumer.size(); i++) {
    		@NotNull Consumer<@NotNull LogData> c = consumer.get(i);
    		if (c == null)
    			continue;
    		c.accept(data);
    	}
    }
	
	

    /**
     * Loggt den angebenen Text mit dem angegebenen Log-Level und der angebenen Einrückung.
     * 
     * @param level    das Log-Level des zu loggenden Textes  
     * @param indent   die Einrückung, die bei dem Text verwendet werden soll
     * @param text     der Text
     */
    public void log(@NotNull LogLevel level, int indent, @NotNull String text) {
    	log(new LogData(level, indent, false, text));
    }

    
    /**
     * Loggt den angebenen Text mit dem angegebenen Log-Level und der angebenen Einrückung
     * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
     * 
     * @param level    das Log-Level des zu loggenden Textes  
     * @param indent   die Einrückung, die bei dem Text verwendet werden soll
     * @param text     der Text
     */
    public void logLn(@NotNull LogLevel level, int indent, @NotNull String text) {
    	log(new LogData(level, indent, true, text));
    }

    
    /**
     * Loggt den angebenen Text mit dem angegebenen Log-Level und der Standard-Einrückung.
     * 
     * @param level    das Log-Level des zu loggenden Textes  
     * @param text     der Text
     */
    public void log(@NotNull LogLevel level, @NotNull String text) {
    	log(level, this.indent, text);
    }

    
    /**
     * Loggt den angebenen Text mit dem angegebenen Log-Level und der Standard-Einrückung
     * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
     * 
     * 
     * @param level    das Log-Level des zu loggenden Textes  
     * @param text     der Text
     */
    public void logLn(@NotNull LogLevel level, @NotNull String text) {
    	logLn(level, this.indent, text);
    }

    
    /**
     * Loggt den angebenen Text mit dem Standard-Log-Level und der angebenen Einrückung.
     * 
     * @param indent   die Einrückung, die bei dem Text verwendet werden soll
     * @param text     der Text
     */
    public void log(int indent, @NotNull String text) {
    	log(defaultLevel, indent, text);
    }

    
    /**
     * Loggt den angebenen Text mit dem Standard-Log-Level und der angebenen Einrückung
     * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
     * 
     * @param indent   die Einrückung, die bei dem Text verwendet werden soll
     * @param text     der Text
     */
    public void logLn(int indent, @NotNull String text) {
    	logLn(defaultLevel, indent, text);
    }

    
    /**
     * Loggt den angebenen Text mit dem Standard-Log-Level und der Standard-Einrückung.
     * 
     * @param text     der Text
     */
    public void log(@NotNull String text) {
    	log(defaultLevel, text);
    }

    
    /**
     * Loggt den angebenen Text mit dem Standard-Log-Level und der Standard-Einrückung
     * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
     * 
     * @param text     der Text
     */
    public void logLn(@NotNull String text) {
    	logLn(defaultLevel, text);
    }
    
}
