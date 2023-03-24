package de.nrw.schule.svws.core.abschluss.ge;

import java.util.List;
import java.util.function.Predicate;

import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFach;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dazu, die beiden Fächergruppen für die Abschlussberechnung zu speichern
 * und allgemeine Methoden für den Zugriff auf diese Fächergruppen bereitzustellen. 
 */
public class AbschlussFaecherGruppen {

	/** Die Fächergruppe 1 */
    public final @NotNull AbschlussFaecherGruppe fg1;
    
	/** Die Fächergruppe 1 */
    public final @NotNull AbschlussFaecherGruppe fg2;
    

    /**
     * Erzeugt eine neues Objekt AbschlussFaecherGruppen
     *  
     * @param fg1   die Fächergruppe 1
     * @param fg2   die Fächergruppe 2
     */
    public AbschlussFaecherGruppen(final @NotNull AbschlussFaecherGruppe fg1, final @NotNull AbschlussFaecherGruppe fg2) {
    	this.fg1 = fg1;
    	this.fg2 = fg2;
    }

    
    /**
     * Prüft, ob eine der beiden Fächergruppen leer ist.
     * 
     * @return true, falls eine der beiden Fächergruppen leer ist.
     */
    public boolean isEmpty() {
        return fg1 == null || fg2 == null || fg1.isEmpty() || fg2.isEmpty();
    }


	/**
	 * Prüft, ob das Fach mit dem angegebenen Fachkürzel in einer der beiden 
	 * Fächergruppen enthalten ist oder nicht.
	 * 
	 * @param kuerzel   das Kürzel des Faches, welches geprüft werden soll.
	 * 
	 * @return true, falls das Fach vorhanden ist, und ansonsten false
	 */
    public boolean contains(final String kuerzel) {
    	return fg1.contains(kuerzel) || fg2.contains(kuerzel);
    }
    
    
    /**
     * Bestimmt alle Fächer beider Fächergruppen, welche dem übergebenen 
     * Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return eine Liste der Fächer, die dem Filterkriterium entsprechen
     */
    public @NotNull List<@NotNull GEAbschlussFach> getFaecher(final @NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	final @NotNull List<@NotNull GEAbschlussFach> faecher = fg1.getFaecher(filter);
    	faecher.addAll(fg2.getFaecher(filter));
    	return faecher; 
    }
    

    /**
     * Gibt die Anzahl der Fächer beider Fächergruppen zurück, welche dem übergebenen 
     * Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return die Anzahl der Fächer
     */
    public long getFaecherAnzahl(final @NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	return fg1.getFaecherAnzahl(filter) + fg2.getFaecherAnzahl(filter);
    }
    
    
    /**
     * Bestimmt die Kürzel aller Fächer beider Fächergruppen, welche dem übergebenen 
     * Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return eine Liste der Kürzel der Fächer, die dem Filterkriterium entsprechen
     */
    public @NotNull List<@NotNull String> getKuerzel(final @NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	final @NotNull List<@NotNull String> faecher = fg1.getKuerzel(filter);
    	faecher.addAll(fg2.getKuerzel(filter));
    	return faecher; 
    }
    
    
    /**
     * Erstellt eine Zeichenkette mit einer Komma-separierten Liste der Kürzel aller Fächer
     * beider Fächergruppen, welche dem übergebenen Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return die Zeichenkette mit einer Komma-separierten Liste der Fächerkürzel
     */
    public @NotNull String getKuerzelListe(final @NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	final @NotNull StringBuilder sb = new StringBuilder();
    	final @NotNull List<@NotNull String> faecher = getKuerzel(filter);
    	for (final String fach : faecher) {
    		if (sb.length() > 0)
    			sb.append(", ");
    		sb.append(fach);
    	}
    	return sb.toString();
    }
    
    
    /**
     * Erstellt eine Zeichenkette mit einer Komma-separierten Liste der Kürzel aller Fächer
     * beider Fächergruppen, welche dem übergebenen Filterkriterium entsprechen. Dabei
     * werden für die Fächergruppen jedoch unterschiedliche Filterkriterien angewendet.
     * 
     * @param filterFG1   die Funktion, die das Kriterium für die gesuchten Fächer der Fächergruppe 1 angibt.
     * @param filterFG2   die Funktion, die das Kriterium für die gesuchten Fächer der Fächergruppe 2 angibt.
     * 
     * @return die Zeichenkette mit einer Komma-separierten Liste der Fächerkürzel
     */
    public @NotNull String getKuerzelListe(final @NotNull Predicate<@NotNull GEAbschlussFach> filterFG1, final @NotNull Predicate<@NotNull GEAbschlussFach> filterFG2) {
    	final @NotNull StringBuilder sb = new StringBuilder();
    	final @NotNull List<@NotNull String> faecherFG1 = fg1.getKuerzel(filterFG1);
    	final @NotNull List<@NotNull String> faecherFG2 = fg2.getKuerzel(filterFG2);
    	for (final String fach : faecherFG1) {
    		if (sb.length() > 0)
    			sb.append(", ");
    		sb.append(fach);
    	}
    	for (final String fach : faecherFG2) {
    		if (sb.length() > 0)
    			sb.append(", ");
    		sb.append(fach);
    	}
    	return sb.toString();
    }
    
}
