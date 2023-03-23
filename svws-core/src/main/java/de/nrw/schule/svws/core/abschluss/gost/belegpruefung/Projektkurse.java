package de.nrw.schule.svws.core.abschluss.gost.belegpruefung;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler;
import de.nrw.schule.svws.core.abschluss.gost.GostFachManager;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegungHalbjahr;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. 
 * für die Gesamtprüfungen, welche in Bezug auf Projektkurse durchgeführt werden.
 */
public class Projektkurse extends GostBelegpruefung {

	/// Eine Vektor mit den Projektfächern, die belegt wurden. Dies sollte im Regelfall nur ein Fach sein, können aber ggf. bei einer gültigen Belegung bis zu drei Fächer sein 
	private Vector<@NotNull AbiturFachbelegung> projektkursBelegung;

	/// falls ein Projektkurs gültig gewählt wurde: Der Projektkurs, sonst: null
	private AbiturFachbelegung projektkurs;

	/// ein Vektor, welcher die anrechenbaren Halbjahre eines gültig angewählten Projektkurses beinhaltet
	private Vector<@NotNull GostHalbjahr> projektkursHalbjahre;

	
	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Projektkurse(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}


	@Override
	protected void init() {
		projektkurs = null;
		projektkursBelegung = new Vector<>();
		projektkursHalbjahre = new Vector<>();
		
		// Bestimme die belegten Projektfächer
		final @NotNull List<@NotNull AbiturFachbelegung> alleFachbelegungen = manager.getFachbelegungen();
		for (int i = 0; i < alleFachbelegungen.size(); i++) {
			final AbiturFachbelegung fachbelegung = alleFachbelegungen.get(i);
			if (manager.zaehleBelegung(fachbelegung) <= 0)
				continue;
			
			final GostFach fach = manager.getFach(fachbelegung);
			if ((fach != null) && GostFachManager.istProjektkurs(fach)) {
				projektkursBelegung.add(fachbelegung);
			}
		}
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe auf Projektkurse - diese sind nicht in der EF erlaubt
		pruefeBelegungEF();
	}


	@Override
	protected void pruefeGesamt() {
		// Prüfe die Belegung des Projektkurses und der Leitfächer
		pruefeBelegungEF();
		pruefeAufAnrechenbarenProjektkurs();
		pruefeBelegungHalbjahre();
		pruefeBelegungLeitfaecher();
		
		// ist der Kurs eine besondere Lernleistung?
		if (manager.istProjektKursBesondereLernleistung())
			addFehler((projektkurs != null) ? GostBelegungsfehler.PF_16_INFO : GostBelegungsfehler.PF_15);
	}

	
	
	
	
	
	/**
	 * Prüft, ob ein Projektfach in der EF belegt wurde. Eine solche Belegung ist nicht zulässig.
	 */
	private void pruefeBelegungEF() {
		if (projektkursBelegung == null)
			return;
		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if (belegungHalbjahr == null)
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);				
				if ((halbjahr == GostHalbjahr.EF1) || (halbjahr == GostHalbjahr.EF2))
					addFehler(GostBelegungsfehler.PF_10);
			}
		}
	}
	
	
	/**
	 * Prüft, ob ein anrechenbarer Projektkurs unter den belegten Projektfächern existiert. Es darf aber 
	 * auch nur genau ein anrechenbarer Projektkurs existieren! 
	 */
	private void pruefeAufAnrechenbarenProjektkurs() {
		if (projektkursBelegung == null)
			return;
		
		// Prüfe ggf. auch mehrere Projektfächer, da abgebrochene Einzelbelegungen vorliegen können
		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			// Prüfe die einzelnen Halbjahresbelegungen der Projektfächer
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if (belegungHalbjahr == null)
					continue;
				
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;
				
				// Ignoriere fehlerhafte EF-Belegungen an dieser Stelle
				if ((halbjahr == GostHalbjahr.EF1) || (halbjahr == GostHalbjahr.EF2))
					continue; 
				
				// Der Projektkurs ist nur anrechenbar, sofern das Fach im nachfolgenden Halbjahr belegt wurde.
				final GostHalbjahr nextHalbjahr = halbjahr.next();
				if (nextHalbjahr == null) {
					addFehler(GostBelegungsfehler.PF_18);
					continue;
				} else if (!manager.pruefeBelegung(fachbelegung, nextHalbjahr)) {
					addFehler(GostBelegungsfehler.PF_17_INFO);
					continue;
				}

				// Wurde bereits ein anderer Projektkurs in zwei aufeinanderfolgenden Jahren belegt?
				if (projektkurs != null) {
					addFehler(GostBelegungsfehler.PF_14);
					break;
				}
				
				// Speichere den anrechenbaren Projektkurs für spätere Prüfungen
				projektkurs = fachbelegung;
				if (projektkursHalbjahre == null)
					projektkursHalbjahre = new Vector<>();
				projektkursHalbjahre.add(halbjahr);
				projektkursHalbjahre.add(nextHalbjahr);
				break;
			}
		}
	}



	/**
	 * Prüfe die Halbjahresbelegungen der belegten Projektfächer. Hierbei Darf 
	 * es zu Einzelbelegungen neben dem anrechenbaren Projektkurs kommen. Diese dürfen
	 * aber nur vor der Belegung des anrechenbaren Kurses liegen. Außerdem dürfen in 
	 * einem Halbjahr nicht mehrere Projektfächer belegt sein.  
	 */
	private void pruefeBelegungHalbjahre() {
		if (projektkursBelegung == null)
			return;
		
		// Erstelle eine Menge zur Prüfung, ob in einem Halbjahr mehr als ein Projektkurs belegt wurde. 
		final @NotNull HashSet<@NotNull GostHalbjahr> pjkHalbjahre = new HashSet<>();
		
		// Gehe alle Projektfächer durch und prüfe die Halbjahresbelegungen auf ungültige Einzelbelegungen
		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if (belegungHalbjahr == null)
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;
				
				// Ignoriere fehlerhafte EF-Belegungen an dieser Stelle
				if ((halbjahr == GostHalbjahr.EF1) || (halbjahr == GostHalbjahr.EF2))
					continue; 
				
				// Prüfe, ob bereits ein anderes Projektfach in diesem Halbjahr belegt wurde
				if (!pjkHalbjahre.add(halbjahr)) {
					addFehler(GostBelegungsfehler.PF_14);
					continue;
				}

				// Prüfe, ob die aktuelle Halbjahres-Belegung dem anrechenbaren Projektkurs zugeordnet wurde. 
				// -> dann ist es eine zulässige Belegung.
				if ((projektkurs != null) && projektkurs.equals(fachbelegung) && (projektkursHalbjahre != null) && projektkursHalbjahre.contains(halbjahr))
					continue;
				
				// Prüfe, ob im Folgehalbjahr eine Belegung möglich ist
				//    und ob kein anrechenbares Projektfach existiert oder die Einzelbelegung vor den Halbjahren
				//    des anrechenbaren Projektfaches liegt
				final GostHalbjahr nextHalbjahr = halbjahr.next();
				if ((nextHalbjahr != null) && (GostFachManager.istWaehlbar(manager.getFach(fachbelegung), nextHalbjahr))
						&& ((projektkurs == null) || (projektkursHalbjahre == null) || (halbjahr.compareTo(projektkursHalbjahre.get(0)) < 0))) 
					continue;

				// Ansonsten ist die Belegung des Projektkurses ungültig!
				addFehler(GostBelegungsfehler.PF_14);
			}
		}
	}

	
	/**
	 * Prüft die Belegung der Leitfächer 
	 */
	private void pruefeBelegungLeitfaecher() {
		if (projektkursBelegung == null)
			return;

		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			final GostFach fach = manager.getFach(fachbelegung);
			if (fach == null)
				continue;
			final AbiturFachbelegung leitfach1 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			final AbiturFachbelegung leitfach2 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
			if ((leitfach1 != null) && pruefeBelegungLeitfachbelegung(fachbelegung, leitfach1))
				continue;
			if ((leitfach2 != null) && pruefeBelegungLeitfachbelegung(fachbelegung, leitfach2))
				continue;
			addFehler(GostBelegungsfehler.PF_13);
		}
	}
	
	
	/**
	 * Prüft, ob das Leitfach in Bezug auf die Belegung des Projektfaches die korrekten Halbjahresbelegungen hat.
	 * 
	 * @param projektkurs   die Fachbelegungen des Projektfaches
	 * @param leitfach      die Fachbelegungen des Leitfaches
	 * 
	 * @return true, falls das Leitfach eine geeigneten Belegung aufweist, sonst false
	 */
	private boolean pruefeBelegungLeitfachbelegung(final AbiturFachbelegung projektkurs, final AbiturFachbelegung leitfach) {
		// Prüfe, ob eine normale Belegung eines Projektkurse zuvor eine zulässige Leitfachbelegung hatte
		if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
			if ((leitfach != null) && manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12))
				return true;
		} else if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q12, GostHalbjahr.Q21)) {
			if ((leitfach != null) && 
					(manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12) || 
							manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21)))
				return true;
		} else if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			if ((leitfach != null) && ((manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q21, GostHalbjahr.Q22))))
				return true;
		// Prüfe, ob eine Einzelbelegungen eines Projektkurse zuvor eine zulässige Leitfachbelegung hatte
		} else if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q11)) {
			if ((leitfach != null) && manager.pruefeBelegung(leitfach, GostHalbjahr.Q11))
				return true;
		} else if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q12)) {
			if ((leitfach != null) && 
					(manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12) || 
							manager.pruefeBelegung(leitfach, GostHalbjahr.Q12)))
				return true;
		} else if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q21)) {			
			if ((leitfach != null) && ((manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q21))))
				return true;
		} else if (manager.pruefeBelegung(projektkurs, GostHalbjahr.Q22)) {
			// dieser Spezial-Fall muss durch einen anderen Fehlercode korrigiert werden, aber nicht durch die Leitfachbelegung...
			if ((leitfach != null) && ((manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q21, GostHalbjahr.Q22))
					|| (manager.pruefeBelegung(leitfach, GostHalbjahr.Q22))))
				return true;			
		}
		return false;
	}
	
	
	/**
	 * Gibt den belegten Projektkurs zurück, fall ein Kurs gültig belegt wurde.
	 * 
	 * @return die Fachbelegung des Projektkurses oder null
	 */
	public AbiturFachbelegung getProjektkurs() {
		return projektkurs;
	}
	
	
	/**
	 * Gibt zurück, ob die angegebene Fachbelegung des Halbjahres eine Fachbelegung des
	 * angewählten Projektkurses ist und anrechenbar ist. Sollte sie Teil des Projektkurses 
	 * sein, aber auch zu einer besonderen Lernleistung gehören, so ist sie nicht anrechenbar.
	 * 
	 * @param fachbelegungHalbjahr   die Fachbelegung des Halbjahres
	 * 
	 * @return true, wenn die Fachbelegung anrechenbar ist.
	 */
	public boolean istAnrechenbar(AbiturFachbelegungHalbjahr fachbelegungHalbjahr) {
		if (fachbelegungHalbjahr == null)
			return false;
		if (GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel) != GostKursart.PJK)
			return false;
		final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
		if ((projektkurs == null) || (projektkursHalbjahre == null) || (manager.istProjektKursBesondereLernleistung()))
			return false;
		return (halbjahr == projektkursHalbjahre.get(0)) || (halbjahr == projektkursHalbjahre.get(1)); 		
	}
	
	
	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück
	 * 
	 * @return die Anzahl der anrechenbaren Kurse
	 */
	public int getAnrechenbareKurse() {
		if ((projektkurs == null) || (manager.istProjektKursBesondereLernleistung()))
			return 0;
		return 2;
	}


}
