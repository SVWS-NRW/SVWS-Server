package de.nrw.schule.svws.core.abschluss.ge;

import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.abschluss.AbschlussManager;
import de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnis;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFach;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFaecher;
import de.nrw.schule.svws.core.types.ge.GELeistungsdifferenzierteKursart;
import de.nrw.schule.svws.core.types.schule.SchulabschlussAllgemeinbildend;
import de.nrw.schule.svws.logger.LogLevel;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

/**
 * Diese Klasse stellt einen Service zur Abschlussberechnung in Bezug auf den Mittleren Schulabschluss 
 * nach Klasse 10 zur Verfügung. 
 */
public class ServiceAbschlussMSA extends Service<@NotNull GEAbschlussFaecher, @NotNull AbschlussErgebnis> {
	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizite = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && ((f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)));	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizite1NS = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 5)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 4)));	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizite2NS = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 6)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 5)));	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefiziteMehrAls1NS = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 6)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note >= 5)));
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefiziteMehrAls2NS = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 6));
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefiziteMitNPOption = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 5));	
	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizitWP = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && (f.note > 4) && "WP".equalsIgnoreCase(f.kuerzel); 
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizitNichtWP = (@NotNull GEAbschlussFach f) -> !f.ausgeglichen && (f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) && !"WP".equalsIgnoreCase(f.kuerzel); 	

	private @NotNull Predicate<@NotNull GEAbschlussFach> filterBenoetigte3er = (@NotNull GEAbschlussFach f) -> !f.ausgleich && (f.note <= 3) && (GELeistungsdifferenzierteKursart.Sonstige.hat(f.kursart));
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterDefiziteBenoetigte3erMitNPOption = (@NotNull GEAbschlussFach f) -> !f.ausgleich && (f.note == 4) && (GELeistungsdifferenzierteKursart.Sonstige.hat(f.kursart));
	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterAusgleiche = (@NotNull GEAbschlussFach f) -> !f.ausgleich && ((f.note < 3) || ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note < 4)));	
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterAusgleiche3er = (@NotNull GEAbschlussFach f) -> !f.ausgleich && (f.note < 3);	
		
	private @NotNull Predicate<@NotNull GEAbschlussFach> filterEKurse = (@NotNull GEAbschlussFach f) -> (GELeistungsdifferenzierteKursart.E.hat(f.kursart));
	
	
	/**
	 * Bestimmt anhand der übergebenen Fächer die Zuordnung zu den beiden Fächergruppen.
	 * 
	 * @param input   die Abschlussfächer
	 * 
	 * @return die Zuordnung der Abschlussfächer zu beiden Fachgruppen 1 und 2
	 */
	public static @NotNull AbschlussFaecherGruppen getFaechergruppen(@NotNull List<@NotNull GEAbschlussFach> input) {
		@NotNull AbschlussFaecherGruppen faecher = new AbschlussFaecherGruppen(
        	new AbschlussFaecherGruppe(input, Arrays.asList("D", "M", "E", "WP"), null),
        	new AbschlussFaecherGruppe(input, null, Arrays.asList("D", "M", "E", "WP", "LBNW", "LBAL")
        ));
        return faecher;
	}



	/**
	 * Führt die Abschlussberechnung anhand der übergebenen Abschlussfächer durch
	 * und gibt das Berechnungsergebnis zurück.
	 * 
	 * @param input    die Abschlussfächer
	 * 
	 * @return das Ergebnis der Abschlussberechnung
	 */
    @Override
    public @NotNull AbschlussErgebnis handle(@NotNull GEAbschlussFaecher input) {
        logger.logLn(LogLevel.INFO, "Prüfe MSA:");
        logger.logLn(LogLevel.DEBUG, "==========");

        // Prüfe, ob genügend leistungsdifferenzierte Kurse vorkommen
        if ((input.faecher == null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
            logger.logLn(LogLevel.DEBUG, "______________________________");
            logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
            return AbschlussManager.getErgebnis(null, false);
        }
        	
        // Prüfe, ob Fächerkürzel mehrfach zur Abschlussprüfung übergeben wurden
        if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
            logger.logLn(LogLevel.DEBUG, "______________________________");
            logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
            return AbschlussManager.getErgebnis(null, false);
        }
        
        // Bestimme die Fächergruppen für die Berechnung des Abschlusses
        @NotNull AbschlussFaecherGruppen faecher = ServiceAbschlussMSA.getFaechergruppen(input.faecher);
    	
        // Prüfe, ob alle nötigen Fächer in der FG1 vorhanden sind
        if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M", "E", "WP"))) {
            logger.logLn(LogLevel.DEBUG, "______________________________");
            logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
            return AbschlussManager.getErgebnis(null, false);
        }

        // Prüfe, ob Fächer in der Fächergruppe II vorhanden sind
        if (faecher.fg2.isEmpty()) {
            logger.logLn(LogLevel.DEBUG, "______________________________");
            logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
            return AbschlussManager.getErgebnis(null, false);
        }

        // Prüfe ob mindestens zwei E-Kurse vorhanden sind.
        long anzahlEKurse = faecher.getFaecherAnzahl(filterEKurse);
        if (anzahlEKurse < 2) {
            logger.logLn(LogLevel.DEBUG, "______________________________");
            logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - nicht genügend E-Kurse belegt");
            return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
        } else if (anzahlEKurse > 2) {
        	long zuviel = anzahlEKurse - 2;
        	GEAbschlussFach eKursFG2 = faecher.fg2.getFach(filterEKurse);
        	if (eKursFG2 != null) {
                int note = eKursFG2.note;
                int note_neu = (note == 1) ? 1 : note - 1;
                logger.logLn(LogLevel.DEBUG, "   " + eKursFG2.kuerzel + ":(E)" + note + "->(G)" + note_neu);
                eKursFG2.note = note_neu;
                eKursFG2.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
                zuviel--;
        	}
        	while (zuviel > 0) {
        		GEAbschlussFach eKursFG1 = faecher.fg1.getFach(filterEKurse);
            	if (eKursFG1 != null) {
                    int note = eKursFG1.note;
                    int note_neu = (note == 1) ? 1 : note - 1;
                    logger.logLn(LogLevel.DEBUG, "   " + eKursFG1.kuerzel + ":(E)" + note + "->(G)" + note_neu);
                    eKursFG1.note = note_neu;
                    eKursFG1.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
                    zuviel--;
            	}        		
        		zuviel--;
        	}
        }

        // Gibt die Fächer und Noten der Fächergruppen aus, welche bei der Berechnung des Abschlusses verwendet werden
        logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString());
        logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString());

        // Prüfe anhand der Defizite, ob ein Abschluss erworben wurde
        @NotNull AbschlussErgebnis abschlussergebnis = this.pruefeDefizite(faecher, "");
        if (abschlussergebnis.erworben) {
            logger.logLn(LogLevel.DEBUG, "______________________________");
            logger.logLn(LogLevel.INFO, " => MSA (FOR): APO-SI §42 (3)");
        } else if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
            logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis));
        } else {
            logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - KEINE Nachprüfungsmöglichkeiten!");
        }
        return abschlussergebnis;
    }

    
    
    /**
     * Prüft in Bezug auf Defizite, ob der Abschluss erworben wurde.
     * 
     * @param faecher      die Asbchlussfächer nach Fächergruppen sortiert
     * @param log_indent   die Einrückung für das Logging
     * 
     * @return das Ergebnis der Abschlussberechnung in Bezug die Defizitberechnung
     */
    private @NotNull AbschlussErgebnis pruefeDefizite(@NotNull AbschlussFaecherGruppen faecher, @NotNull String log_indent) {
        // Erfasse den Status der Optionen für das "Beheben" von Defiziten
        boolean ignorieren_genutzt = false;
        boolean ausgleich_genutzt = false;  // gibt an, ob bereits ein Ausgleich genutzt wurde
        boolean nachpruefung_genutzt = false;
        @NotNull List<@NotNull GEAbschlussFach> npFaecher = new Vector<>();

        // Bestimme die Defizite in den beiden Fächergruppen
        long fg1_defizite = faecher.fg1.getFaecherAnzahl(filterDefizite);
        long fg2_defizite = faecher.fg2.getFaecherAnzahl(filterDefizite);

        long fg1_anzahlAusgleiche = faecher.fg1.getFaecherAnzahl(filterAusgleiche);

        if (fg1_defizite > 0)
            logger.logLn(LogLevel.DEBUG, log_indent + " -> FG1: Defizit" + (fg1_defizite > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(filterDefizite));
        if (fg2_defizite > 0)
            logger.logLn(LogLevel.DEBUG, log_indent + " -> FG2: Defizit" + (fg2_defizite > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(filterDefizite));

        // Eine Minderleistung von 2 oder mehr Notenstufe in FG1 ist unzulässig
        if (faecher.fg1.getFaecherAnzahl(filterDefiziteMehrAls1NS) > 0) {
            logger.logLn(LogLevel.DEBUG, log_indent + " -> in FG1 unzulässig: mind. 1x6 oder bei einem G-Kurs 1x5");
            return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
        }

        // Prüfe auf eine Abweichung von drei Notenstufe in FG2
        if (faecher.fg2.getFaecherAnzahl(filterDefiziteMehrAls2NS) > 0) {
            logger.logLn(LogLevel.DEBUG, log_indent + " -> in FG2 unzulässig: in einem G-Kurs 1x6");
            return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);        	
        }
        
        // Prüfe auf eine Abweichung von zwei Notenstufen in FG2 - darf nur 1x vorkommen und muss dann unberücksichtigt bleiben
        @NotNull List<@NotNull GEAbschlussFach> sonstige_ungenuegend = faecher.fg2.getFaecher(filterDefizite2NS);
        if (sonstige_ungenuegend.size() > 1) {
            logger.logLn(LogLevel.DEBUG, log_indent + " -> zu viele Defizite, kann nicht mehr als eine Note mit 6 (bzw. 5 bei einem G-Kurs) in FG2 unberücksichtigt lassen");
            return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
        } else if (sonstige_ungenuegend.size() == 1) {
            // Muss unberücksichtigt bleiben -> "Ignorieren", aber nur, falls es sich nicht um das leistungsdifferenzierte Fach handelt, dann ist dies ggf. ein notwendiges Nachprüfungsfach
        	@NotNull GEAbschlussFach defizitFach = sonstige_ungenuegend.get(0);
            if (GELeistungsdifferenzierteKursart.Sonstige.hat(defizitFach.kursart)) {
	            defizitFach.ausgeglichen = true;
	            logger.logLn(LogLevel.DEBUG, log_indent + " -> unberücksichtigt: Defizit in " + defizitFach.kuerzel + " (2 Notenstufen)");
	            ignorieren_genutzt = true;
            } else if ((GELeistungsdifferenzierteKursart.E.hat(defizitFach.kursart)) && (defizitFach.note == 6)) {
                logger.logLn(LogLevel.DEBUG, log_indent + "   -> Ein ungenügend in dem E-Kurs " + defizitFach.kuerzel + " kann nicht ausgelichen werden und eine Nachprüfung ist nicht zulässig!");
                return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
            } else {
                logger.logLn(LogLevel.DEBUG, log_indent + "   -> Nachprüfung muss falls möglich in " + defizitFach.kuerzel + " stattfinden!");
                nachpruefung_genutzt = true;
                npFaecher.add(defizitFach); 
                defizitFach.note--; // verbessere für die weitere Berechnung
            }
        }


        // drei Defizite in FG1 führen dazu, dass ein Abschluss nicht mehr möglich ist, zwei immer wenn WP kein Defizitfach ist oder kein Ausgleich in FG1 verfügbar ist.
        GEAbschlussFach wp_defizit = faecher.fg1.getFach(filterDefizitWP);
        if ((fg1_defizite > 2)
                || ((fg1_defizite == 2) && (wp_defizit == null))
                || ((fg1_defizite == 2) && (fg1_anzahlAusgleiche == 0))
                || ((fg1_defizite == 1) && (wp_defizit == null) && (fg1_anzahlAusgleiche == 0))) {
            logger.logLn(LogLevel.DEBUG, log_indent + " -> zu viele Defizite in FG1");
            return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
        }

        // Bei zwei Defiziten muss WP in die Nachprüfung und das andere Fach muss ausgeglichen werden
        // - Dies geht nur, falls die Nachprüfungsoption nicht bereits genutzt wurde für das leistungsdifferenzierte Fach in FG2
        if ((fg1_defizite == 2) && (wp_defizit != null)) {
        	if (nachpruefung_genutzt) {
                logger.logLn(LogLevel.DEBUG, log_indent + " -> zu viele Defizite in FG1, eine Nachprüfung in mehreren Fächern (WP, " + sonstige_ungenuegend.get(0).kuerzel + ") ist nicht möglich!");
                return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
        	}
            // Setze WP als Nachprüfungsfach
            logger.logLn(LogLevel.DEBUG, log_indent + " -> WP-Defizite in FG1, eine Nachprüfung ist, sofern möglich, in WP nötig!");
            npFaecher.add(wp_defizit);
            nachpruefung_genutzt = true;
        }

        // Ein einzelnen FG1-Defizit muss auch ausgeglichen werden, falls WP kein Defizit ist (implizit ist hier ein Ausgleich vorhanden, da sonst zuvor abgebrochen worden wäre, s.o.) 
        if ((fg1_defizite == 2) || ((fg1_defizite == 1) && (wp_defizit == null))) {
            // Nutze die Ausgleichs-Option für FG1
            ausgleich_genutzt = true;
            GEAbschlussFach defizitFach = faecher.fg1.getFach(filterDefizitNichtWP);
            if (defizitFach == null)
            	throw new NullPointerException();
            defizitFach.ausgeglichen = true;
            GEAbschlussFach ausgleichsFach = faecher.fg1.getFach(filterAusgleiche);
            if (ausgleichsFach == null)
            	throw new NullPointerException();
            ausgleichsFach.ausgleich = true;
            logger.logLn(LogLevel.DEBUG, log_indent + " -> Gleiche das Defizit (FG1) in " + defizitFach.kuerzel + " mit " + ausgleichsFach.kuerzel + " (FG1) aus.");
        }

        // Bei einem Defizit in WP kann man entweder den Ausgleichs- oder die Nachprüfungs-Option wählen, sofern diese nicht für das leistungsdifferenzierte Fach in der FG2 genutzt wurde
        if (((fg1_defizite == 1) && (wp_defizit != null))) {
            // Prüfe erst die Ausgleichsoption
            @NotNull GEAbschlussFach defizitFach = wp_defizit;
            GEAbschlussFach ausgleichsFach = faecher.fg1.getFach(filterAusgleiche);
            if (ausgleichsFach != null) {
                ausgleich_genutzt = true;
                defizitFach.ausgeglichen = true;
                ausgleichsFach.ausgleich = true;
                logger.logLn(LogLevel.DEBUG, log_indent + " -> Prüfe mit Ausgleich: Gleiche das Defizit (FG1) in " + defizitFach.kuerzel + " mit " + ausgleichsFach.kuerzel + " (FG1) aus. " + defizitFach.kuerzel + " alternativ als Nachprüfungsfach denkbar.");
                @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent + "  ", npFaecher, 2, ignorieren_genutzt, ausgleich_genutzt, nachpruefung_genutzt);
                // Sollte eine Nachprüfung nötig sein, so kann dies auch WP sein -> ergänze dieses Fach
                if (!abschlussergebnis.erworben && abschlussergebnis.npFaecher != null && AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis) && wp_defizit.kuerzel != null)
                	abschlussergebnis.npFaecher.add(wp_defizit.kuerzel);
                return abschlussergebnis;
            }

            // Setze WP als Nachprüfungsfach, da kein Ausgleich in FG1 vorhanden ist. Dies geht nur, wenn nicht auch eine Nachprüfung in dem leistungsdifferenzierten Fach der FG2 nötig ist.
            if ((sonstige_ungenuegend.size() == 1) && (!sonstige_ungenuegend.get(0).ausgeglichen)) {
                logger.logLn(LogLevel.DEBUG, log_indent + " -> das Defizit in WP kann nicht ausgeglichen werden und eine Nachprüfung in mehreren Fächern (WP, " + sonstige_ungenuegend.get(0).kuerzel + ") ist nicht möglich!");
                return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);            	
            }
            logger.logLn(LogLevel.DEBUG, log_indent + " -> WP-Defizite in FG1 ohne Ausgleichsmöglichkeit, eine Nachprüfung ist, sofern möglich, in WP nötig!");
            npFaecher.add(wp_defizit);
            nachpruefung_genutzt = true;
        }

        // Kein Defizit in FG1        
        @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent, npFaecher, 2, ignorieren_genutzt, ausgleich_genutzt, nachpruefung_genutzt);
        if ((nachpruefung_genutzt) && abschlussergebnis.erworben)
        	return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA, AbschlussManager.getKuerzel(npFaecher));
        return abschlussergebnis;
    }



    /**
     * Führt eine Detailprüfung in der Fächergruppe 2 durch. Diese Methode wird ggf. mehrfach - auch rekursiv - aufgerufen. 
     * 
     * @param faecher                die Abschlussfächer nach Fächergruppen sortiert
     * @param log_indent             die Einrückung für das Logging
     * @param npFaecher              die Liste der Nachprüfungsfächer, die bisher schon feststehen
     * @param benoetige3er           die Anzahl der 3er, die noch in FG2 benötigt werden
     * @param ignorieren_genutzt     gibt an, ob die Möglichkeit eine defizitäre Leistung in FG2 zu ignorieren schon genutzt wurde 
     * @param ausgleich_genutzt      gibt an, ob die Möglichkeit des Ausgleichs über ein anderes Fach schon genutzt wurde
     * @param nachpruefung_genutzt   gibt an, ob die Nachprüfungsmöglichkeit bereits eingesetzt werden musste
     * 
     * @return das Ergebnis der Abschlussberechnung in Bezug auf den Stand dieser Detailprüfung
     */
    private @NotNull AbschlussErgebnis pruefeFG2(@NotNull AbschlussFaecherGruppen faecher, @NotNull String log_indent, @NotNull List<@NotNull GEAbschlussFach> npFaecher, long benoetige3er, boolean ignorieren_genutzt, boolean ausgleich_genutzt, boolean nachpruefung_genutzt) {
        // Prufe, ob weitere Defizite vorliegen
    	@NotNull List<@NotNull GEAbschlussFach> defizite = faecher.fg2.getFaecher(filterDefizite);
    	@NotNull List<@NotNull GEAbschlussFach> mangelhaft = faecher.fg2.getFaecher(filterDefizite1NS);
        boolean hat_defizit = defizite.size() > 0;
        boolean hat_defizit_sonstige_3er = faecher.fg2.getFaecherAnzahl(filterBenoetigte3er) < benoetige3er;

        if ((!hat_defizit) && (!hat_defizit_sonstige_3er))
        	return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, true);

        if (!ignorieren_genutzt) {
            // Versuche zuerst ein "Mangelhaft" unberücksichtigt zu lassen
            for (GEAbschlussFach defizitFach : mangelhaft) {
            	if (!GELeistungsdifferenzierteKursart.Sonstige.hat(defizitFach.kursart))
            		continue;
                defizitFach.ausgeglichen = true;
                logger.logLn(LogLevel.DEBUG, log_indent + " -> Prüfe: Defizit unberücksichtigt in " + defizitFach.kuerzel);
                // rekursiver Aufruf mit einem Defizit weniger
                @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent + "  ", npFaecher, benoetige3er, true, ausgleich_genutzt, nachpruefung_genutzt);
                if (abschlussergebnis.erworben)
                    return abschlussergebnis;
                defizitFach.ausgeglichen = false;
            }
        } 
        if (!ausgleich_genutzt) {
            // Versuche einen Ausgleich für eine fehlende 3 zu bekommen - hier besteht keine Nachprüfungsoption
            // - es wird von einer "4" als Defizit ausgegangen, da ansonsten ein anderes Kriterium greifen wird
            // - 2 oder besser nötig
            if (hat_defizit_sonstige_3er) {
                GEAbschlussFach ausgleichsFach = faecher.fg2.getFach(filterAusgleiche3er);
                if (ausgleichsFach == null) {
                    logger.logLn(LogLevel.DEBUG, log_indent + " -> Kein Ausgleich für eine fehlende 3 vorhanden. ");
                } else {
                    logger.logLn(LogLevel.DEBUG, log_indent + " -> Prüfe: Ausgleich einer fehlende 3 durch " + ausgleichsFach.kuerzel);
                    ausgleichsFach.ausgleich = true;
                    // rekursiver Aufruf - es wird auch eine 3 weniger benötigt
                    @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent + "  ", npFaecher, benoetige3er - 1, ignorieren_genutzt, true, nachpruefung_genutzt);
                    if (abschlussergebnis.erworben)
                        return abschlussergebnis;
                    ausgleichsFach.ausgleich = false;
                }
            // Konnte eine fehlende 3 nicht ausgeglichen werden, so macht ein Ausgleich einer 4 nur Sinn, falls die Nachprüfungsoption bei einer 
            // fehlenden 3 (siehe unten) über einen rekursiven Aufruf funktioniert. Dann wird dieser Teil aber über die Rekursion erneut aufgerufen (daher nur else ohne else if)
            } else {
            	@NotNull List<@NotNull GEAbschlussFach> ausgleichsFaecher = faecher.getFaecher(filterAusgleiche);
	            if (ausgleichsFaecher.size() <= benoetige3er) {
	                logger.logLn(LogLevel.DEBUG, log_indent + " -> kann Ausgleichsregelung nicht nutzen, da nicht genügend 3er-Fächer vorhanden sind.");
	            } else {
	                long anzahlSonstigeFaecherMind3 = faecher.fg2.getFaecherAnzahl(filterBenoetigte3er);
	                // Versuche mangelhafte Fächer auszugleichen
	                for (GEAbschlussFach defizitFach : defizite) {
	                    for (GEAbschlussFach ausgleichsFach : ausgleichsFaecher) {
	                        logger.logLn(LogLevel.DEBUG, log_indent + " -> Prüfe: Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
	                        if ((GELeistungsdifferenzierteKursart.Sonstige.hat(ausgleichsFach.kursart)) && (anzahlSonstigeFaecherMind3 <= benoetige3er)) {
	                            logger.logLn(LogLevel.DEBUG, log_indent + "   -> " + ausgleichsFach.kuerzel + " nicht als Ausgleich möglich, da für die Mindestanforderung mind. " + benoetige3er + "x3 benötigt wird, aber nur " + anzahlSonstigeFaecherMind3 + "x3 zur Verfügung steht.");
	                        } else {
	                            defizitFach.ausgeglichen = true;
	                            ausgleichsFach.ausgleich = true;
	                            // rekursiver Aufruf mit einem Defizit weniger
	                            @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent + "  ", npFaecher, benoetige3er, ignorieren_genutzt, true, nachpruefung_genutzt);
	                            if (abschlussergebnis.erworben)
	                                return abschlussergebnis;
	                            defizitFach.ausgeglichen = false;
	                            ausgleichsFach.ausgleich = false;
	                        }
	                    }
	                }
	            }
            }
        }

        if (!nachpruefung_genutzt) {
        	if (hat_defizit_sonstige_3er) {
        		// Die fehlende 3 wurde nicht durch einen Ausgleich behoben, daher muss hier die Nachprüfungsmöglichkeit bei diesem Fach prioritär geprüft werden
        		// Gehe hierzu alle ausreichend in der Fächergruppe 2 durch, die durch eine NP auf 3 verbessert werden können, aber nicht bereist als Ausgleich genutzt wurden
        		@NotNull List<@NotNull GEAbschlussFach> npKandidaten = faecher.fg2.getFaecher(filterDefiziteBenoetigte3erMitNPOption);
	            for (GEAbschlussFach defizitFach : npKandidaten) {
	                logger.logLn(LogLevel.DEBUG, log_indent + " -> Prüfe: Nachprüfung in " + defizitFach.kuerzel + " auf befriedigend möglich?");
	                defizitFach.ausgeglichen = true;
	                defizitFach.note--; // verbessere kurzfristig
	                // rekursiver Aufruf - prüft, ob mit der Nachprüfung ein Abschluss möglich wäre
	                @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent + "  ", npFaecher, benoetige3er, ignorieren_genutzt, ausgleich_genutzt, true);
	                logger.logLn(LogLevel.DEBUG, log_indent + (abschlussergebnis.erworben ? "   -> Ja!" : "   -> Nein!"));
	                if (abschlussergebnis.erworben)
	                    npFaecher.add(defizitFach);
	                defizitFach.note++; // verschlechtere wieder
	                defizitFach.ausgeglichen = false;
	            }
        	} else {
	            // Gehe alle "mangelhaft" durch und prüfe, ob eine Nachprüfung einen Abschluss ermöglicht - G-Kurse werden also nicht beachtet
        		@NotNull List<@NotNull GEAbschlussFach> npKandidaten = faecher.fg2.getFaecher(filterDefiziteMitNPOption);
	            for (GEAbschlussFach defizitFach : npKandidaten) {
	                logger.logLn(LogLevel.DEBUG, log_indent + " -> Prüfe: Nachprüfung in " + defizitFach.kuerzel + " möglich?");
	                defizitFach.ausgeglichen = true;
	                defizitFach.note--; // verbessere kurzfristig
	                // rekursiver Aufruf - prüft, ob mit der Nachprüfung ein Abschluss möglich wäre
	                @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_indent + "  ", npFaecher, benoetige3er, ignorieren_genutzt, ausgleich_genutzt, true);
	                logger.logLn(LogLevel.DEBUG, log_indent + (abschlussergebnis.erworben ? "   -> Ja!" : "   -> Nein!"));
	                if (abschlussergebnis.erworben)
	                    npFaecher.add(defizitFach);
	                defizitFach.note++; // verschlechtere wieder
	                defizitFach.ausgeglichen = false;
	            }
        	}
        }

        if ((!nachpruefung_genutzt) && (npFaecher.size() > 0))
            return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA, AbschlussManager.getKuerzel(npFaecher));

        return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
    }


    
}
