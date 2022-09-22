package de.nrw.schule.svws.core.abschluss;

import de.nrw.schule.svws.core.abschluss.bk.a.BKAnlageAFach;
import de.nrw.schule.svws.core.abschluss.bk.a.BKAnlageAFaecher;
import de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnisBerufsbildend;
import de.nrw.schule.svws.core.types.schule.SchulabschlussAllgemeinbildend;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Verwaltung von allgemeinen Informationen
 * zur Abschlussberechnung an berufsbildenden Schule und stellt insbesondere 
 * auch wiederverwendbare Methoden zur Verfügung. 
 */
public class AbschlussManagerBerufsbildend {
	

    /**
     * Erzeugt ein Ergebnis der Abschlussberechnung unter Angabe, ob dieser erworben 
     * wurde. Die Liste der Nachprüfungsfächer ist leer und ein Log ist nicht zugeordnet.
     *  
     * @param hatBSA                      ist der Berufsschulabschluss erreicht
     * @param note                        Note des Abschlusses
     * @param hatBA                       ist der Berufsabschluss erreicht
     * @param abschlussAllgemeinbildend   der allgemeinbildende Abschluss
     * 
     * @return das Ergebnis der Abschlussberechnung 
     */
    public static @NotNull AbschlussErgebnisBerufsbildend getErgebnis(boolean hatBSA, double note, Boolean hatBA, SchulabschlussAllgemeinbildend abschlussAllgemeinbildend) {
    	// Nachprüfungsmöglichkeiten ???
    	@NotNull AbschlussErgebnisBerufsbildend ergebnis = new AbschlussErgebnisBerufsbildend();
    	ergebnis.hatBSA = hatBSA;
    	ergebnis.note = note;
    	ergebnis.hatBA = hatBA;
    	ergebnis.abschlussAllgemeinbildend = (abschlussAllgemeinbildend == null) ? null : abschlussAllgemeinbildend.toString();
    	ergebnis.log = null;
        return ergebnis;
    }

    
    
    
	/**
	 * Berechnet den Notendurchschnitt aller Fächer
	 * 
	 * @param abschluss_faecher   die Fächer für die Abschlussberechnung
	 * 
	 * @return der Notendurchschnitt oder NaN im Fehlerfall
	 */
	public static double getDurchschnitt(@NotNull BKAnlageAFaecher abschluss_faecher) {
		if ((abschluss_faecher.faecher == null) || (abschluss_faecher.faecher.size() == 0))
			return Double.NaN;
		int sum = 0; 
		for (BKAnlageAFach fach : abschluss_faecher.faecher) {
			sum += fach.note;
		}
		return ((double)sum) / abschluss_faecher.faecher.size(); 
	}
	
	
	/**
	 * Berechnet die Anzahl der Defizite 
	 * 
	 * @param abschluss_faecher   die Fächer für die Abschlussberechnung
	 * 
	 * @return die Anzahl der Defizite oder -1 im Fehlerfall
	 */
	public static int getAnzahlDefizite(@NotNull BKAnlageAFaecher abschluss_faecher) {
		if ((abschluss_faecher.faecher == null) || (abschluss_faecher.faecher.size() == 0))
			return -1;
		int sum = 0;
		for (BKAnlageAFach fach : abschluss_faecher.faecher) {
			if (fach.note >= 5)
				sum++;
		}
		return sum;
	}


	/**
	 * Berechnet die Anzahl der Note Ungenügend 
	 * 
	 * @param abschluss_faecher   die Fächer für die Abschlussberechnung
	 * 
	 * @return die Anzahl der Note Ungenügend oder -1 im Fehlerfall
	 */
	public static int getAnzahlUngenuegend(@NotNull BKAnlageAFaecher abschluss_faecher) {
		if ((abschluss_faecher.faecher == null) || (abschluss_faecher.faecher.size() == 0))
			return -1;
		int sum = 0;
		for (BKAnlageAFach fach : abschluss_faecher.faecher) {
			if (fach.note >= 6)
				sum++;
		}
		return sum;
	}
    
}
