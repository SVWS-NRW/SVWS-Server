package de.nrw.schule.svws.core.abschluss.gost.abitur.services;


import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.logger.LogLevel;


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
