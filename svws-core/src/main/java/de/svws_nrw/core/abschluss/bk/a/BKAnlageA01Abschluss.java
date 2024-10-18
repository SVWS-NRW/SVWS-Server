package de.svws_nrw.core.abschluss.bk.a;

import de.svws_nrw.core.Service;
import de.svws_nrw.core.abschluss.AbschlussManagerBerufsbildend;
import de.svws_nrw.core.data.abschluss.AbschlussErgebnisBerufsbildend;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import jakarta.validation.constraints.NotNull;


/**
 * TODO
 *
 * Für Anlagen A01, A03, ...
 *	- Prüfung BSA
 *	  * Anzahl der Defizite entscheidend (max. zulässig: 1x5)
 *	  * HSA10 wenn bestanden, Vergabe nur, wenn HSA10 oder höher nicht schon erreicht
 *	- Prüfung MSA
 *	  * Englisch-GER B1
 *    * Durchschnittsnote besser als oder gleich 3.5
 *	- Prüfung MSA/Q
 *	  * Englisch-GER B1
 *    * Durchschnittsnote besser als oder gleich 2.5
 */
public class BKAnlageA01Abschluss extends Service<BKAnlageAFaecher, AbschlussErgebnisBerufsbildend> {

	/**
	 * TODO
	 *
	 * @param input   die Fächer für die Abschlussprüfung
	 *
	 * @return das Ergebnis der Abschlussprüfung
	 */
	@Override
	public @NotNull AbschlussErgebnisBerufsbildend handle(final @NotNull BKAnlageAFaecher input) {
		// Prüfe auf BSA
		logger.log(LogLevel.INFO, "Prüfe BSA:");
		if (AbschlussManagerBerufsbildend.getAnzahlUngenuegend(input) > 0) {
			logger.logLn(LogLevel.INFO,
					" nicht erreicht (kein ungenügend erlaubt, insgesamt " + AbschlussManagerBerufsbildend.getAnzahlUngenuegend(input) + ").");
			return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), false,
					SchulabschlussAllgemeinbildend.OA);
		} else if (AbschlussManagerBerufsbildend.getAnzahlDefizite(input) > 1) {
			logger.logLn(LogLevel.INFO, " nicht erreicht (mehr als 1 Defizit, insgesamt " + AbschlussManagerBerufsbildend.getAnzahlDefizite(input) + ").");
			return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), false,
					SchulabschlussAllgemeinbildend.OA);
		}
		logger.logLn(LogLevel.INFO, " erreicht.");

		// HSA wird automatisch vergeben, wenn BSA erworben wurde, für einen höheren Abschluss ist ein GeR-Niveau von mind. B1 nötig
		if ((input.englischGeR == null) || (Sprachreferenzniveau.B1.compareByKuerzel(input.englischGeR) < 0)) {
			if (input.englischGeR == null) {
				logger.logLn(LogLevel.INFO, "Das Sprachreferenzniveau in Englisch wurde nicht angegeben. Eine Prüfung auf MSA ist daher nicht möglich.");
			} else {
				logger.logLn(LogLevel.INFO, "Das Sprachreferenzniveau in Englisch ist für den MSA nicht ausreichend.");
			}
			logger.logLn(LogLevel.INFO, "HSA10 wurde erreicht.");
			return AbschlussManagerBerufsbildend.getErgebnis(true, AbschlussManagerBerufsbildend.getDurchschnitt(input),
					input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.HA10);
		}

		if (AbschlussManagerBerufsbildend.getDurchschnitt(input) <= 2.5) {
			logger.logLn(LogLevel.INFO, "Die Durschnittsnote ist besser als oder gleich 2,5.");
			logger.logLn(LogLevel.INFO, "MSA-Q wurde erreicht.");
			return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input),
					input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.MSA_Q);
		} else if (AbschlussManagerBerufsbildend.getDurchschnitt(input) <= 3.5) {
			logger.logLn(LogLevel.INFO, "Die Durschnittsnote ist besser als oder gleich 3,5, aber schlechter als 2,5.");
			logger.logLn(LogLevel.INFO, "MSA wurde erreicht.");
			return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input),
					input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.MSA);
		}

		// nur HSA
		logger.logLn(LogLevel.INFO, "Die Durschnittsnote ist schlechter als 3,5.");
		logger.logLn(LogLevel.INFO, "HSA10 wurde erreicht.");
		return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), input.hatBestandenBerufsAbschlussPruefung,
				SchulabschlussAllgemeinbildend.HA10);
	}


}
