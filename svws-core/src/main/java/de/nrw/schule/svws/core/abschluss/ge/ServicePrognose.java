package de.nrw.schule.svws.core.abschluss.ge;

import java.util.List;

import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.abschluss.AbschlussManager;
import de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnis;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFach;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFaecher;
import de.nrw.schule.svws.core.types.schule.SchulabschlussAllgemeinbildend;
import de.nrw.schule.svws.logger.LogLevel;
import jakarta.validation.constraints.NotNull;



/**
 * Diese Klasse stellt einen Service zur Prognoseberechnung in Bezug auf die Gesamtschulabschlüsse 
 * zur Verfügung. 
 */
public class ServicePrognose extends Service<@NotNull GEAbschlussFaecher, @NotNull AbschlussErgebnis> {

	
	/**
	 * Prüft, ob Lernbereichsnoten bei den Abschlussfächern zur Verfügung stehen oder nicht.
	 * 
	 * @param faecher   die Abschlussfächer
	 * 
	 * @return true, falls die Lernbereichsnoten vorhanden sind, ansonsten false
	 */
    private static boolean hatLernbereichsnoten(@NotNull GEAbschlussFaecher faecher) {
    	boolean hatLBNW = false; 
    	boolean hatLBAL = false;
    	if (faecher.faecher == null)
    		return false;
		@NotNull List<@NotNull GEAbschlussFach> tmp = faecher.faecher;
    	for (GEAbschlussFach fach : tmp) {
    		if (fach == null)
    			continue;
    		hatLBNW = hatLBNW || "LBNW".equals(fach.kuerzel);
    		hatLBAL = hatLBAL || "LBAL".equals(fach.kuerzel);
    	}
    	return hatLBNW && hatLBAL;
    }
    
	
	/**
	 * Führt die Prognoseberechnung anhand der übergebenen Abschlussfächer durch
	 * und gibt das Berechnungsergebnis zurück.
	 * 
	 * @param input    die Abschlussfächer
	 * 
	 * @return das Ergebnis der Prognoseberechnung
	 */
	@Override
    public @NotNull AbschlussErgebnis handle(@NotNull GEAbschlussFaecher input) {
        // Prüfe, ob genügend leistungsdifferenzierte Kurse vorkommen
        if (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input)) {
            logger.logLn(LogLevel.DEBUG, "Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
            @NotNull AbschlussErgebnis prognose = AbschlussManager.getErgebnis(null, false);
            prognose.log = log.getStrings();
            return prognose;
        }
        	
        // Prüfe, ob Fächerkürzel mehrfach zur Abschlussprüfung übergeben wurden
        if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
            logger.logLn(LogLevel.DEBUG, "Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
            @NotNull AbschlussErgebnis prognose = AbschlussManager.getErgebnis(null, false);
            prognose.log = log.getStrings();
            return prognose;
        }
        		
		// Gehe zunächst davon aus, dass noch kein Abschluss erworben wurde und prüfe dann der Reihe nach auf die einzelnen Abschlüsse HA9, HA10, MSA und MSAQ
        @NotNull SchulabschlussAllgemeinbildend abschluss = SchulabschlussAllgemeinbildend.OA;
    	List<@NotNull String> np_faecher = null;
    	if (!"10".equals(input.jahrgang)) {
    		@NotNull ServiceAbschlussHA9 ha9 = new ServiceAbschlussHA9();          
    		@NotNull AbschlussErgebnis ha9output = ha9.handle(input);
            np_faecher = ha9output.npFaecher;
            if (ha9output.erworben)
            	abschluss = SchulabschlussAllgemeinbildend.HA9;
            log.append(ha9.getLog());
            logger.logLn(LogLevel.INFO, "");
    	} else if ("10".equals(input.jahrgang)) { 
    		// In der Klasse 10 hat man den Abschluss der Klasse 9 bereits mit der Versetzung erhalten. Somit ist das Prognose-Ergebnis HA9 und nicht mehr OA
    		abschluss = SchulabschlussAllgemeinbildend.HA9;
    	}
    	
    	@NotNull ServiceAbschlussHA10 ha10 = new ServiceAbschlussHA10();       
    	@NotNull AbschlussErgebnis ha10output = ha10.handle(input);
        if (ha10output.erworben)
        	abschluss = SchulabschlussAllgemeinbildend.HA10;
        else if ("10".equals(input.jahrgang) || (SchulabschlussAllgemeinbildend.HA9.equals(abschluss)))
        	np_faecher = ha10output.npFaecher;
        log.append(ha10.getLog());
    	
    	if ((!SchulabschlussAllgemeinbildend.OA.equals(abschluss)) || (!hatLernbereichsnoten(input))) {
    		@NotNull ServiceAbschlussMSA msa = new ServiceAbschlussMSA();            
    		@NotNull AbschlussErgebnis msaOutput = msa.handle(input);
            logger.logLn(LogLevel.INFO, "");
            log.append(msa.getLog());
            if (msaOutput.erworben) {
            	abschluss = SchulabschlussAllgemeinbildend.MSA;
            	
            	@NotNull ServiceBerechtigungMSAQ msaq = new ServiceBerechtigungMSAQ();    
            	@NotNull AbschlussErgebnis msaqOutput = msaq.handle(input); 
                if (msaqOutput.erworben) {
                	abschluss = SchulabschlussAllgemeinbildend.MSA_Q;
            	} else {
            		np_faecher = msaqOutput.npFaecher;                	
                }
                logger.logLn(LogLevel.INFO, "");
                log.append(msaq.getLog());
            } else {
            	np_faecher = msaOutput.npFaecher;
            }
    	}
    	
    	@NotNull AbschlussErgebnis prognose = AbschlussManager.getErgebnisNachpruefung(abschluss, np_faecher);
    	prognose.erworben = (!SchulabschlussAllgemeinbildend.OA.equals(abschluss));
    	prognose.log = log.getStrings();
        return prognose;
    }

}
