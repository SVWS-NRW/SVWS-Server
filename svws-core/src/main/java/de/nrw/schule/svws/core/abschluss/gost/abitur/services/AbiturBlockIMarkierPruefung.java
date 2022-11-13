package de.nrw.schule.svws.core.abschluss.gost.abitur.services;


import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.logger.LogLevel;


/**
 * TODO
 */
public class AbiturBlockIMarkierPruefung extends Service<Abiturdaten, Boolean> {
	
	/**
	 * TODO
	 */
	public AbiturBlockIMarkierPruefung() {
		super();
	}
	
	
	@Override
	public Boolean handle(final Abiturdaten abidaten) {
		if (abidaten == null) {
			logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName() + " hat keine gültigen Abiturdaten erhalten.");
			return null;
		}
		
		boolean ergebnis = false;
		
		// TODO prüfe die Markierung der Kurse unter Berücksichtigung der Noten
		logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName() + " ist noch nicht fertig programmiert...");
		
		// gibt das Ergebnis der Belegprüfung zurück.
		return ergebnis;
	}	
	
}
