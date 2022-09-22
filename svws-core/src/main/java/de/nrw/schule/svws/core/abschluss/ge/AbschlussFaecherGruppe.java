package de.nrw.schule.svws.core.abschluss.ge;

import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.abschluss.AbschlussManager;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFach;
import de.nrw.schule.svws.core.types.ge.GELeistungsdifferenzierteKursart;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse enthält die Informationen für die Abschlussberechnung, welche Fächer einer Fächergruppe zugeordnet sind.
 * Hierzu wird intern eine Lister zur Verfügung gestellt und es werden mehrere Hilfemethoden zur Verfügung gestellt,
 * um Operationen auf dieser Fachgruppe auszuführen.
 */
@XmlRootElement(name = "AbschlussFaecherGruppe")
@Schema(name="AbschlussFaecherGruppe", description="die Fachinformationen für die Abschlussberechnung, welche den jeweiligen Fächergruppen zugeordnet ist.")
public class AbschlussFaecherGruppe {

	/** Eine Liste mit allen Fächern dieser Fachgruppe */
    private final @NotNull Vector<@NotNull GEAbschlussFach> faecher = new Vector<>();


    /**
     * Erzeugt eine Fächergruppe aus den angegebenen Fächern und den vorgegebenen Kriterien.
     *
     * @param faecherAlle       eine Liste aller vorhandenen Leistungen
     * @param faecherNutzen     nur die gelisteten Fächer nutzen, null bedeutet grundsätzlich alle benoteten Fächer nutzen (außer den gefilterten)
     * @param faecherFiltern    null bedeutet keinen Filter verwenden, ansonsten werden die gelisteten Fächer gefiltert
     */
    public AbschlussFaecherGruppe(@NotNull List<@NotNull GEAbschlussFach> faecherAlle, List<@NotNull String> faecherNutzen, List<@NotNull String> faecherFiltern) {
        if (faecherAlle == null)
            return;

        // Gehe alle Leistungsdaten des Lernabschnittes durch und füge alle Fächer hinzu, die den Kriterien entsprechen
        for (int i = 0; i < faecherAlle.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecherAlle.get(i);
        	if (fach.kuerzel == null)
        		continue;
        	if ((faecherFiltern != null) && faecherFiltern.contains(fach.kuerzel))
        		continue;
        	if ((faecherNutzen != null) && !faecherNutzen.contains(fach.kuerzel))
        		continue;
        	faecher.add(AbschlussManager.erstelleAbschlussFach(fach.kuerzel, fach.bezeichnung, fach.note, GELeistungsdifferenzierteKursart.from(fach.kursart), fach.istFremdsprache));
        }
    }
    
    
    /**
     * Prüft, ob die gelisteten Fächer in der Fächergruppe sind und nur diese.
     * 
     * @param faecherAbgleich   die Fächer, welche in der Fächergruppe sein sollen.
     * 
     * @return true, falls die angegebenen Fächer und nur diese in der Fächergruppe sind, ansonsten false.
     */
	public boolean istVollstaendig(List<@NotNull String> faecherAbgleich) {
		if (faecherAbgleich == null)
			return true;
		if (isEmpty())
			return false;
		for (String kuerzel : faecherAbgleich) {
			if (!this.contains(kuerzel))
				return false;
		}
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
			if (!faecherAbgleich.contains(fach.kuerzel))
				return false;
		}
		return true;
	}
    

	/**
	 * Gibt zurück, ob die Fächergruppe leer ist oder mindestens ein Fach beinhaltet.
	 *  
	 * @return true, falls die Fächergruppe leer ist, ansonsten false
	 */
	public boolean isEmpty() {
        return (faecher == null) || (faecher.isEmpty());
    }


	/**
	 * Prüft, ob das Fach mit dem angegebenen Fachkürzel in der Fächergruppe enthalten ist
	 * oder nicht.
	 * 
	 * @param kuerzel   das Kürzel des Faches, welches geprüft werden soll.
	 * 
	 * @return true, falls das Fach vorhanden ist, und ansonsten false
	 */
    public boolean contains(String kuerzel) {
    	if (kuerzel == null)
    		return false;
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
			if ((fach != null) && (fach.kuerzel.equals(kuerzel)))
				return true;
		}
    	return false;
    }
    
    
    /**
     * Entfernt alle Fächer aus der Fächergruppe, die dem übergebenen Filter entsprechen.
     * 
     * @param filter   die Funktion, um die zu entfernenden Fächer zu bestimmen
     * 
     * @return die Liste der tatsächlich entfernten Fächer
     */
    public @NotNull List<@NotNull GEAbschlussFach> entferneFaecher(@NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	@NotNull Vector<@NotNull GEAbschlussFach> selected = new Vector<>();
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
    		if (filter.test(fach))
    			selected.add(fach);
    	}
    	faecher.removeAll(selected);
    	return selected;
    }
    

    /**
     * Bestimmt das Fach, welches dem übergebenen Filter entspricht. Entsprechen
     * mehrere Fächer dem Filterkriterium, so wird nur das erste Fach 
     * der internen Liste zurückgegeben.
     * 
     * @param filter   die Funktion, die das Kriterium für das gesuchte Fach angibt.
     * 
     * @return das Fach, sofern eines gefunden wurde, ansonsten false
     */
    public GEAbschlussFach getFach(@NotNull Predicate<@NotNull GEAbschlussFach> filter) {
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
    		if (filter.test(fach))
    			return fach;
    	}
    	return null;
    }


    /**
     * Bestimmt alle Fächer, welche dem übergebenen Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return eine Liste der Fächer, die dem Filterkriterium entsprechen
     */
    public @NotNull List<@NotNull GEAbschlussFach> getFaecher(@NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	@NotNull Vector<@NotNull GEAbschlussFach> result = new Vector<>();
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
    		if (filter.test(fach))
    			result.add(fach);
    	}
    	return result;
    }


    /**
     * Gibt die Anzahl der Fächer zurück, welche dem übergebenen Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return die Anzahl der Fächer
     */
    public long getFaecherAnzahl(@NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	long count = 0;
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
    		if (filter.test(fach))
    			count++;
    	}
    	return count;
    }
    
    
    /**
     * Bestimmt die Kürzel aller Fächer, welche dem übergebenen Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return eine Liste der Kürzel der Fächer, die dem Filterkriterium entsprechen
     */
    public @NotNull List<@NotNull String> getKuerzel(@NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	@NotNull Vector<@NotNull String> result = new Vector<>();
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
    		if (filter.test(fach) && (fach.kuerzel != null))
    			result.add(fach.kuerzel);
    	}
    	return result;
    }
    
    
    /**
     * Erstellt eine Zeichenkette mit einer Komma-separierten Liste der Kürzel aller Fächer, 
     * welche dem übergebenen Filterkriterium entsprechen.
     * 
     * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
     * 
     * @return die Zeichenkette mit einer Komma-separierten Liste der Fächerkürzel
     */
    public @NotNull String getKuerzelListe(@NotNull Predicate<@NotNull GEAbschlussFach> filter) {
    	@NotNull StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
    		if (filter.test(fach)) {
    			if (sb.length() > 0)
    				sb.append(", ");
    			sb.append(fach.kuerzel);
    		}
    	}
    	return sb.toString();
    }
    


    /**
     * Gibt eine Komma-separierte Liste, der Abschlussfächer aus. Dabei wird
     * die toString Methode der Klasse AbschlussFach verwendet.
     */
    @Override
    public @NotNull String toString() {
    	@NotNull StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < faecher.size(); i++) {
        	@NotNull GEAbschlussFach fach = faecher.get(i);
			if (sb.length() > 0)
				sb.append(", ");
			@NotNull String diffkursinfo = "";
			if ((fach.kursart == null) || (fach.kuerzel == null))
				continue;
	        if (!GELeistungsdifferenzierteKursart.Sonstige.hat(fach.kursart))
	            diffkursinfo += fach.kursart + ",";
	        sb.append(fach.kuerzel + "(" + diffkursinfo + fach.note + ")");
    	}
    	return sb.toString();
    }
    

}
