package de.svws_nrw.core.abschluss.gost.abitur.services;


import de.svws_nrw.core.Service;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.logger.LogLevel;


/**
 * TODO
 */
public final class AbiturBlockIMarkierPruefung extends Service<Abiturdaten, Boolean> {

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
			return false;
		}

		final boolean ergebnis = false;

		// TODO prüfe die Markierung der Kurse unter Berücksichtigung der Noten
		logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName() + " ist noch nicht fertig programmiert...");

		// gibt das Ergebnis der Belegprüfung zurück.
		return ergebnis;
	}

}
