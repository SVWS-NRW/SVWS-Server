package de.svws_nrw.core.abschluss;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.abschluss.AbschlussErgebnis;
import de.svws_nrw.core.data.abschluss.GEAbschlussFach;
import de.svws_nrw.core.data.abschluss.GEAbschlussFaecher;
import de.svws_nrw.core.types.ge.GELeistungsdifferenzierteKursart;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Verwaltung von allgemeinen Informationen
 * zur Abschlussberechnung und stellt insbesondere auch wiederverwendbare
 * Methoden zur Verfügung. 
 */
public class AbschlussManager {
	

    /**
     * Erzeugt ein Ergebnis der Abschlussberechnung unter Angabe, ob dieser erworben 
     * wurde. Die Liste der Nachprüfungsfächer ist leer und ein Log ist nicht zugeordnet.
     * Sollten Nachprüfungsmöglichkeiten bestehen so ist die Methode 
     * {@link AbschlussManager#getErgebnisNachpruefung} zu nutzen.
     * und ob dieser erworben wurde. 
     *  
     * @param abschluss   der Abschluss für den das Ergebnis erzeugt wird 
     * @param erworben    true, falls der Abschluss erworben wurde, sonst false
     * 
     * @return das Ergebnis der Abschlussberechnung 
     */
    public static @NotNull AbschlussErgebnis getErgebnis(final SchulabschlussAllgemeinbildend abschluss, final boolean erworben) {
    	final @NotNull AbschlussErgebnis ergebnis = new AbschlussErgebnis();
    	ergebnis.abschluss = abschluss == null ? null : abschluss.toString();
    	ergebnis.erworben = erworben;
    	ergebnis.npFaecher = null;
    	ergebnis.log = null;
        return ergebnis;
    }


    /**
     * Erzeugt ein Ergebnis der Abschlussberechnung, wo der Abschluss nicht erreicht wurde, aber ggf. 
     * noch durch Nachprüfungen erreicht werden kann. Ein log wird nicht zugeordnet.
     * 
     * @param abschluss    der Abschluss für den das Ergebnis erzeugt wird 
     * @param np_faecher   eine Liste von Nachprüfungsfächern, falls eine Nachprüfung möglich ist, 
     *                     ansonsten null oder eine leere Liste
     *
     * @return das Ergebnis der Abschlussberechnung 
     */
    public static @NotNull AbschlussErgebnis getErgebnisNachpruefung(final SchulabschlussAllgemeinbildend abschluss, final List<@NotNull String> np_faecher) {
    	final @NotNull AbschlussErgebnis ergebnis = new AbschlussErgebnis();
    	ergebnis.abschluss = abschluss == null ? null : abschluss.toString();
    	ergebnis.erworben = false;
        if ((np_faecher == null) || (np_faecher.size() == 0))
        	ergebnis.npFaecher = null;
        else 
        	ergebnis.npFaecher = np_faecher;
        ergebnis.log = null;
        return ergebnis;
    }


    /**
     * Gibt an, ob für einen Abschluss eine Nachprüfungsmöglichkeit besteht.
     * 
     * @param ergebnis   das Abschluss-Ergebnis bei dem auf eine Nachprüfungsmöglichkeit 
     *                   geprüft werden soll. 
     * 
     * @return true, falls eine Nachprüfungsmöglichkeit besteht, sonst false
     */
    public static boolean hatNachpruefungsmoeglichkeit(final @NotNull AbschlussErgebnis ergebnis) {
        return (ergebnis.npFaecher != null) && ergebnis.npFaecher.size() > 0;
    }


    /**
     * Gibt die Nachprüfungsfächer als Komma-separierten String zurück.
     * 
     * @param ergebnis   das Abschluss-Ergebnis bei dem die Nachprüfungsmöglichkeiten 
     *                   ausgegeben werden sollen
     *                    
     * @return die Nachprüfungsfächer als Komma-separierten String
     */
    public static @NotNull String getNPFaecherString(final @NotNull AbschlussErgebnis ergebnis) {
    	if (ergebnis.npFaecher == null)
    		return "";
    	final StringBuilder sb = new StringBuilder();
    	for (final String fach : ergebnis.npFaecher) {
    		if (sb.length() > 0)
    			sb.append(", ");
    		sb.append(fach);
    	}
        return sb.toString();
    }

    
    /**
     * Vergleicht die beiden Abschlüsse, ob sie identisch sind. Ein
     * Übergabewert null wird als {@link SchulabschlussAllgemeinbildend#OA}
     * interpretiert.
     *  
     * @param a   der eine Abschluss
     * @param b   der andere Abschluss
     * 
     * @return true, falls sie identisch sind und ansonsten false
     */
    public static boolean equalsAbschluesse(final String a, final String b) {
    	if ((a == null) || (SchulabschlussAllgemeinbildend.OA.is(a)))
    		return (b == null) || (SchulabschlussAllgemeinbildend.OA.is(b));
    	return a.equals(b);
    }
    

	/**
	 * Gibt den Abschluss zurück. Im Falle das kein Abschluss angegeben ist
	 * wird {@link SchulabschlussAllgemeinbildend#OA} zurückgegeben.
	 * 
	 * @param ergebnis   das Ergebnis 
	 * 
	 * @return der Abschluss
	 */
	public static @NotNull String getAbschluss(final @NotNull AbschlussErgebnis ergebnis) {
		return ergebnis.abschluss == null ? SchulabschlussAllgemeinbildend.OA.toString() : ergebnis.abschluss;
	}



	
	
    /**
     * Die Methode dient dem Erzeugen eines Faches für die Abschlussberechnung.
     * 
     * @param kuerzel           das Kürzel des Faches
     * @param bezeichnung       die Bezeichnung des Faches
     * @param note              die Note, die in dem Fach erteilt wurde
     * @param kursart           gibt die Kursart Faches an: leistungsdifferenzierter (E-Kurs, G-Kurs) oder sonstiger Kurs
     * @param istFremdsprache   gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
     * 
     * @return das Abschlussfach 
     */
    public static @NotNull GEAbschlussFach erstelleAbschlussFach(final @NotNull String kuerzel, final String bezeichnung, final int note, final @NotNull GELeistungsdifferenzierteKursart kursart, final Boolean istFremdsprache) {
    	final @NotNull GEAbschlussFach fach = new GEAbschlussFach();
    	fach.kuerzel = kuerzel;
        fach.bezeichnung = (bezeichnung == null || "".equals(bezeichnung)) ? "---" : bezeichnung;
        fach.note = note;
        fach.kursart = kursart.kuerzel;
        fach.istFremdsprache = istFremdsprache == null ? false : istFremdsprache;
        return fach;
    }

    
    
    
    
	/**
	 * Liefert eine List mit den Fachkürzeln aus der übergebenen Liste mit Abschlussfächern.
	 * 
	 * @param faecher   die Liste mit Abschlussfächern
	 * 
	 * @return die Liste mit den Fachkürzeln
	 */
	public static @NotNull List<@NotNull String> getKuerzel(final @NotNull List<@NotNull GEAbschlussFach> faecher) {
		final @NotNull Vector<@NotNull String> result = new Vector<>();
		for (int i = 0; i < faecher.size(); i++) {
			final @NotNull GEAbschlussFach fach = faecher.get(i);
			if ((fach == null) || fach.kuerzel == null)
				continue;
			if (result.contains(fach.kuerzel))
				continue;
			result.add(fach.kuerzel);
		}
		return result;
	}
	

	/**
	 * Prüft, ob vier leistungsdifferenzierte Fächer belegt wurden. Dabei wird nicht geprüft, ob 
	 * es sich um G oder E-Kurse handelt.
	 * 
	 * @param abschluss_faecher   die Abschlussfächer 
	 * 
	 * @return true, falls vier leistungsdifferenzierte Fächer belegt wurden, sonst false
	 */
	public static boolean pruefeHat4LeistungsdifferenzierteFaecher(final @NotNull GEAbschlussFaecher abschluss_faecher) {
		if (abschluss_faecher.faecher == null)
			return false;
		int count = 0;
		final @NotNull List<@NotNull GEAbschlussFach> faecher = abschluss_faecher.faecher;
		for (final GEAbschlussFach fach : faecher) {
			if (fach == null)
				continue;
			final @NotNull GELeistungsdifferenzierteKursart kursart = GELeistungsdifferenzierteKursart.from(fach.kursart); 
			if ((kursart == GELeistungsdifferenzierteKursart.E) || (kursart == GELeistungsdifferenzierteKursart.G))
				count++;
		}
		return (count == 4);
	}
	
	
	
	/**
	 * Prüft, ob Duplikate bei den Kürzeln der Fächer vorkommen. Dies darf zur korrekten
	 * Ausführung des Abschlussalgorithmus nicht vorkommen.
	 * 
	 * @param abschluss_faecher   die Abschlussfächer 
	 * 
	 * @return true, falls keine Duplikate vorkommen, sonst false
	 */
	public static boolean pruefeKuerzelDuplikate(final @NotNull GEAbschlussFaecher abschluss_faecher) {
		if (abschluss_faecher.faecher == null)
			return true;
		final @NotNull HashSet<@NotNull String> kuerzel = new HashSet<>(); 
		final @NotNull List<@NotNull GEAbschlussFach> faecher = abschluss_faecher.faecher;
		for (final GEAbschlussFach fach : faecher) {
			if ((fach == null) || (fach.kuerzel == null))
				continue;
			if (!kuerzel.add(fach.kuerzel))
				return false;
		}
		return true;
	}

    
}
