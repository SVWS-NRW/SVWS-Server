package de.svws_nrw.core.abschluss.gost.abitur.services;


import de.svws_nrw.core.Service;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.logger.LogLevel;


/**
 * TODO
 */
public class AbiturBlockIMarkierAlgorithmus extends Service<Abiturdaten, Abiturdaten> {
	
	/**
	 * Erzeugt einen Markierungs-Dienst zur Markierung der Kurse aus dem Block I der Abiturdaten, welche
	 * in die Punktewertung für die Abiturzulassung und in das Abitur einfliessen. 
	 */
	public AbiturBlockIMarkierAlgorithmus() {
		super();
	}
	
	
	@Override
	public Abiturdaten handle(final Abiturdaten abidaten) {
		if (abidaten == null) {
			logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName() + " hat keine gültigen Abiturdaten erhalten.");
			return null;
		}
		
		// TODO markiere die einzubringenden Kurse automatisch
		logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName() + " ist noch nicht fertig programmiert...");
		
		// gibt das Ergebnis der Belegprüfung zurück.
		return abidaten;
	}	
	
}
