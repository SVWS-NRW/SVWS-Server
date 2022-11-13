package de.nrw.schule.svws.core.logger;

import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse implementiert das Funktionale Interface java.util.function.Consumer
 * für Objekt vom Typ LogData. Die über das Interface empfangeben Log-Daten
 * werden in einem Vektor (java.util.Vector) gesammelt.  
 */
public class LogConsumerVector implements Consumer<@NotNull LogData> {

	/** Gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht. */
	public final boolean printTime;
	
	/** Gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht. */
	public final boolean printLevel;
	
	/** Der Vektor mit den gesammelten Log-Informationen. */
	private final @NotNull Vector<@NotNull LogData> logData = new Vector<>();
	
	
	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen, mit den Standardeinstellungen, 
	 * das weder Zeit noch Log-Level mit ausgegeben werden.
	 */
	public LogConsumerVector() {
		printTime = false;
		printLevel = false;
	}
	
	
	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen.
	 * 
	 * @param printTime     gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht
	 * @param printLevel    gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht
	 */
	public LogConsumerVector(boolean printTime, boolean printLevel) {
		this.printTime = printTime;
		this.printLevel = printLevel;
	}


	/**
	 * Hängt einen anderen Log vom gleichen Typ an diesen an.
	 * 
	 * @param log   der anzuhängende Log
	 */
    public void append(@NotNull LogConsumerVector log) {
    	logData.addAll(log.logData);
    }
	

	/**
	 * Diese Methode implementiert das funktionale Interface java.util.function.Consumer
	 * und hängt die empfangenen Log-Informationen an den Vektor an.
	 * 
	 * @param t   die anzuhängenden Log-Informationen 
	 */
	@Override
	public void accept(LogData t) {
		if (t == null)
			return;
		logData.add(t);
	}


	/**
	 * Gibt den Vektor mit den gesammelten Log-Informationen zurück.
	 * 
	 * @return der Vektor mit den gesammelten Log-Informationen
	 */
	public @NotNull List<@NotNull LogData> getLogData() {
		return logData;
	}

	
	/**
	 * Gibt die gesammelten Log-Informationen als Liste von Strings zurück.
	 * 
	 * @return die gesammelten Log-Informationen als Liste von Strings
	 */
    public List<@NotNull String> getStrings() {
    	return this.getStrings("");
    }

    
	/**
	 * Gibt die gesammelten Log-Informationen als Liste von Strings zurück, die alle
	 * als Präfix indet erhalten. Dies dient z.B. dem Einrücken der Log-Informationen.
	 * 
	 * @param indent   das Präfix, welches zum Einrücken der Log-Informationen genutzt wird
	 * 
	 * @return die gesammelten Log-Informationen als Liste von Strings
	 */
    public List<@NotNull String> getStrings(@NotNull String indent) {
    	Vector<@NotNull String> result = new Vector<>();
    	for (int i = 0; i < logData.size(); i++) {
    		@NotNull LogData data = logData.get(i);
    		if (data == null)
    			continue;
    		result.add(indent + data.getText());
    	}
    	return result;
    }

    
    /**
     * Gibt die gesammelten Log-Informationen als Text zurück, bei dem
     * die einzelnen Log-Informationen durch Zeilenumbrüche voneinander
     * getrennt werden. Dabei werden Informationen ausgelassen,
     * die aufgrund des hier vorgegebenen Log-Levels LogLevel.INFO nicht 
     * berücksichtigt werden sollen.   
     *   
     * @return der Text der Log-Informationen für das Log-Level LogLevel.INFO
     */
    public @NotNull String getText() {
        return this.getText(LogLevel.INFO, "");
    }


    /**
     * Gibt die gesammelten Log-Informationen als Text zurück, bei dem
     * die einzelnen Log-Informationen durch Zeilenumbrüche voneinander
     * getrennt werden. Dabei werden Informationen ausgelassen,
     * die aufgrund des angebenen Log-Levels nicht berücksichtigt werden 
     * sollen.   
     *   
     * @param level    das Log-Level, welches mindestens geben sein muss, damit die 
     *                 Log-Informationen berücksichtigt werden.   
     * 
     * @return der Text der Log-Informationen für das angegebene Log-Level
     */
    public @NotNull String getText(@NotNull LogLevel level) {
        return this.getText(level, "");
    }


    /**
     * Gibt die gesammelten Log-Informationen als Text zurück, bei dem
     * die einzelnen Log-Informationen durch Zeilenumbrüche voneinander
     * getrennt werden. Dabei werden Informationen ausgelassen,
     * die aufgrund des angebenen Log-Levels nicht berücksichtigt werden 
     * sollen.   
     *   
     * @param level    das Log-Level, welches mindestens geben sein muss, damit die 
     *                 Log-Informationen berücksichtigt werden.   
     * @param indent   das Präfix, welches zum Einrücken der Log-Informationen genutzt wird
     * 
     * @return der Text der Log-Informationen für das angegebene Log-Level
     */
    public @NotNull String getText(@NotNull LogLevel level, @NotNull String indent) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < logData.size(); i++) {
    		LogData data = logData.get(i);
    		if (data == null)
    			continue;
    		if (data.getLevel().toInteger() > level.toInteger())
    			continue;
    		sb.append(indent);
    		sb.append(data.getText());
    		sb.append("\n");
    	}
    	return sb.toString();
    }
    
    
}
