package de.nrw.schule.svws.core.abschluss.gost.belegpruefung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.gost.GostFachbereich;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;
import de.nrw.schule.svws.core.types.gost.GostAbiturFach;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. 
 * für die Gesamtprüfungen, welche für die ABitur-Fächer durchgeführt werden.
 */
public class AbiFaecher extends GostBelegpruefung {
	
	/// Eine HashMap für den schnellen Zugriff auf die 4 Abiturfachbelegungen, sofern diese zugeordnet sind
	private HashMap<@NotNull GostAbiturFach, @NotNull AbiturFachbelegung> mapAbiturFachbelegungen;
	
	/// Die Anzahl der belegten Abitur-Fächer (sollten 4 sein)
	private int anzahlAbiFaecher;
	
	/// Die Anzahl der Abiturfächer im Bereich Deutsch, Mathematik oder Fremdsprache (muss mindestens 2 sein)
	private int anzahlDeutschMatheFremdsprache;
	
	/// Die Anzahl der Fremdsprachen
	private int anzahlFremdsprachen;
	
	/// Die Anzahl der Abiturfächer im Bereich Sport und Religion (darf maximal 1 sein)
	private int anzahlSportReligion;

	/// Gibt an, ob das AufgabenFeld I abgedeckt ist.
	private boolean hatAufgabenfeldI;

	/// Gibt an, ob das AufgabenFeld II abgedeckt ist.
	private boolean hatAufgabenfeldII;
	
	/// Gibt an, ob das AufgabenFeld III abgedeckt ist.
	private boolean hatAufgabenfeldIII;
	
	
	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public AbiFaecher(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}
	
	
	@Override
	protected void init() {
		mapAbiturFachbelegungen = new HashMap<>();
		anzahlAbiFaecher = 0;
		anzahlDeutschMatheFremdsprache = 0;
		anzahlFremdsprachen = 0;
		anzahlSportReligion = 0;
		hatAufgabenfeldI = false;
		hatAufgabenfeldII = false;
		hatAufgabenfeldIII = false;
		
		// Bestimme die Abiturfächer aus den Belegungsdaten
		@NotNull List<@NotNull AbiturFachbelegung> alleFachbelegungen = manager.getFachbelegungen();
		for (int i = 0; i < alleFachbelegungen.size(); i++) {
			AbiturFachbelegung fachbelegung = alleFachbelegungen.get(i);
			GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach); 
			if (abiturFach == null)
				continue;
			mapAbiturFachbelegungen.put(abiturFach, fachbelegung);
			anzahlAbiFaecher++;
			// Bestimme Aufgabenfelder
			// TODO Erstelle Enum GOStAufgabenfeld
			GostFach fach = manager.getFach(fachbelegung);
			if (fach == null)
				continue;
			if (GostFachbereich.FREMDSPRACHE.hat(fach) || GostFachbereich.DEUTSCH.hat(fach))
				hatAufgabenfeldI = true;
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION.hat(fach)) 
				hatAufgabenfeldII = true;
			if (GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH.hat(fach)) 
				hatAufgabenfeldIII = true;
			
			// Zähle Fächer im Bereich Deutsch, Mathematik und Fremdsprache
			if (GostFachbereich.FREMDSPRACHE.hat(fach) 
					|| GostFachbereich.DEUTSCH.hat(fach) 
					|| GostFachbereich.MATHEMATIK.hat(fach))
				anzahlDeutschMatheFremdsprache++;
			
			// Zähle die Anzahl der Fremdsprachen
			if (GostFachbereich.FREMDSPRACHE.hat(fach))
				anzahlFremdsprachen++;
			
			// Zähle Fächer im Bereich Sport und Religion
			if (GostFachbereich.SPORT.hat(fach) || GostFachbereich.RELIGION.hat(fach))
				anzahlSportReligion++;
		}
	}


	@Override
	protected void pruefeEF1() {
		// nichts zu tun, da diese in der EF.1 noch nicht festgelegt werden...
	}


	@Override
	protected void pruefeGesamt() {
		pruefeLK1();
		pruefeAnzahlUndAufgabenfelderAbiFaecher();
		pruefeMehrfacheAbiturfaecher();
		pruefeSchriftlichkeitAB3undAB4();
	}





	/**
	 * Gesamtprüfung Punkt 70:
	 * Prüfe, ob der erste LK eine fortgeführte Fremdsprache, eine klassische Naturwissenschaft, Mathematik oder Deutsch ist
	 */
	private void pruefeLK1() {
		AbiturFachbelegung lk1 = mapAbiturFachbelegungen == null ? null : mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		GostFach lk1fach = manager.getFach(lk1);
		if ((lk1 == null) || (lk1fach == null) || !((GostFachbereich.DEUTSCH.hat(lk1fach)) || 
                (GostFachbereich.FREMDSPRACHE.hat(lk1fach) && !lk1.istFSNeu) ||
                (GostFachbereich.MATHEMATIK.hat(lk1fach)) ||
                (GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(lk1fach))))
			addFehler(GostBelegungsfehler.LK1_11);
	}

	
	/**
	 * Gesamtprüfung Punkt 71-74:
	 * Prüfe, ob die Zahl der Abiturfächer 4 ist und diese alle Aufgabenfelder abdecken
	 *    und ob mindestens 2 Fächer im Bereich Deutsch, Fremdsprache, Mathematik liegen
	 *    und ob maximale 1 Fach im Bereich Sport und Religion liegt
	 *    und ob Sport nicht als erstes oder drittes Abiturfach gewählt wurde 
	 */
	private void pruefeAnzahlUndAufgabenfelderAbiFaecher() {
		if ((anzahlAbiFaecher != 4) || (!hatAufgabenfeldI) || (!hatAufgabenfeldII) || (!hatAufgabenfeldIII))
			addFehler(GostBelegungsfehler.LK1_13);
		if (anzahlDeutschMatheFremdsprache < 2)
			addFehler(GostBelegungsfehler.ABI_10);
		if ((anzahlDeutschMatheFremdsprache < 3) && (anzahlFremdsprachen > 1))
			addFehler(GostBelegungsfehler.ABI_19);
		if (anzahlSportReligion > 1)
			addFehler(GostBelegungsfehler.ABI_11);
		AbiturFachbelegung lk1 = mapAbiturFachbelegungen == null ? null : mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		GostFach lk1fach = manager.getFach(lk1);
		AbiturFachbelegung ab3 = mapAbiturFachbelegungen == null ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		GostFach ab3fach = manager.getFach(ab3);
		if (((lk1fach != null) && (GostFachbereich.SPORT.hat(lk1fach.kuerzel))) ||
		    ((ab3fach != null) && (GostFachbereich.SPORT.hat(ab3fach.kuerzel))))
			addFehler(GostBelegungsfehler.ABI_15);
	}

	
	/**
	 * Gesamtprüfung: Prüfe, ob eines der Abiturfächer mehrfach belegt wurde. Es ist nicht zulässig
	 * Abiturfächer mehrfach belegt zu haben. 
	 */
	private void pruefeMehrfacheAbiturfaecher() {
		@NotNull HashSet<@NotNull GostAbiturFach> abiFaecher = new HashSet<>(); 
		@NotNull List<@NotNull AbiturFachbelegung> alleFachbelegungen = manager.getFachbelegungen();
		for (int i = 0; i < alleFachbelegungen.size(); i++) {
			AbiturFachbelegung fachbelegung = alleFachbelegungen.get(i);
			GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach); 
			if (abiturFach == null)
				continue;
			if (!abiFaecher.contains(abiturFach)) {
				abiFaecher.add(abiturFach);
				continue;
			}
			switch (abiturFach) {
				case LK1:
					addFehler(GostBelegungsfehler.ABI_21);
					break;
				case LK2:
					addFehler(GostBelegungsfehler.ABI_22);
					break;
				case AB3:
					addFehler(GostBelegungsfehler.ABI_23);
					break;
				case AB4:
					addFehler(GostBelegungsfehler.ABI_24);
					break;
			}
		}
	}
	

	/**
	 * Gesamtprüfung Punkte 76 und 77:
	 * Prüfe ob das 3. Abiturfach von Q1.1 bis Q2.2 schriftlich belegt wurde 
	 *   und on das 4. Abiturfach von Q1.1 bis Q2.1 schriftlich und in Q2.2 mündlich belegt wurde
	 * 
	 */
	private void pruefeSchriftlichkeitAB3undAB4() {
		AbiturFachbelegung ab3 = mapAbiturFachbelegungen == null ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		if (ab3 != null) {
			if (!manager.pruefeBelegungMitSchriftlichkeit(ab3, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21 ))
				addFehler(GostBelegungsfehler.ABI_17);
			if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab3, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22 ))
				addFehler(GostBelegungsfehler.ABI_12);
		}
		AbiturFachbelegung ab4 = mapAbiturFachbelegungen == null ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB4);
		if (ab4 != null) {
			if (!manager.pruefeBelegungMitSchriftlichkeit(ab4, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21 ))
				addFehler(GostBelegungsfehler.ABI_18);
			if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab4, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.Q22 ))
				addFehler(GostBelegungsfehler.ABI_13);
		}
	}
	

	/**
	 * Liefert die zugehörige Abitur-Fachbelegung zurück.
	 * 
	 * @param abifach  die Art des Abifachs (1., 2., 3. oder 4. Fach)
	 * 
	 * @return die Abitur-Fachbelegung oder null, falls es (noch) nicht festgelegt wurde
	 */
	public AbiturFachbelegung getAbiturfach(GostAbiturFach abifach) {
		return mapAbiturFachbelegungen == null ? null : mapAbiturFachbelegungen.get(abifach);
	}
	
	
}
