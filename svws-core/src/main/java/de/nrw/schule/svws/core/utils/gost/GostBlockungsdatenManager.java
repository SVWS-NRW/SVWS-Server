package de.nrw.schule.svws.core.utils.gost;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungKursLehrer;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsdaten;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisListeneintrag;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/** 
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}. 
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. 
 */
public class GostBlockungsdatenManager {

	/** Ein Comparator für Schienen der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungSchiene> compSchiene =
		(@NotNull GostBlockungSchiene a, @NotNull GostBlockungSchiene b) -> 
		{
			return Integer.compare(a.nummer, b.nummer);
		};

	/** Ein Comparator für Regeln der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungRegel> compRegel =
		(@NotNull GostBlockungRegel a, @NotNull GostBlockungRegel b) -> 
		{
			int result = Integer.compare(a.typ, b.typ);
			if (result != 0)
				return result;
			return Long.compare(a.id, b.id);
		};

	/** Die Blockungsdaten, die im Manager vorhanden sind. */
	private final @NotNull GostBlockungsdaten _daten;

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe. */
	private final @NotNull GostFaecherManager _faecherManager;

	/** Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungKurs> _mapKurse = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungSchiene> _mapSchienen = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungRegel> _mapRegeln = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Schueler> _map_id_schueler = new HashMap<>();

	/** Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostFachwahl>> _map_schuelerID_fachwahlen = new HashMap<>();
	
	/** Schüler-ID --> Fach-ID --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull GostFachwahl>> _map_schulerID_fachID_fachwahl = new HashMap<>();

//	/** Schüler-ID --> List<Facharten> */
//	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Long>> _map_schulerID_facharten = new HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnisListeneintrag} */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisListeneintrag> _mapErgebnis = new HashMap<>();
	
	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_fach_kursart_kursnummer;

	/** Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER). */
	private @NotNull List<@NotNull GostBlockungKurs> _kurse_sortiert_fach_kursart_kursnummer = new Vector<>();

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_kursart_fach_kursnummer;

	/** Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER) */
	private @NotNull List<@NotNull GostBlockungKurs> _kurse_sortiert_kursart_fach_kursnummer = new Vector<>();

	/** Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART) */
	private final @NotNull Comparator<@NotNull GostFachwahl> _compFachwahlen;

	/** Ein Comparator für die {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung. */
	private final @NotNull Comparator<@NotNull GostBlockungsergebnisListeneintrag> _compErgebnisse = new GostBlockungsergebnisComparator();
	
	/** Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf. */
	private long _maxTimeMillis = 1000;

	/** 
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager.
	 */
	public GostBlockungsdatenManager() {
		System.out.println("DEBUG: GostBlockungsdatenManager - Konstrukor - START");
		_faecherManager = new GostFaecherManager();
		_daten = new GostBlockungsdaten();
		_daten.gostHalbjahr = GostHalbjahr.EF1.id;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();
		System.out.println("DEBUG: GostBlockungsdatenManager - Konstrukor - ENDE");
	}

	/** Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 * 
	 * @param pDaten          die Blockungsdaten
	 * @param pFaecherManager der Fächer-Manager 
	 */
	public GostBlockungsdatenManager(@NotNull GostBlockungsdaten pDaten, @NotNull GostFaecherManager pFaecherManager) {
		_faecherManager = pFaecherManager;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();
		
		// Tiefe Kopie (deep copy) der GostBlockungsdaten.
		_daten = new GostBlockungsdaten();
		_daten.id = pDaten.id;
		_daten.name = pDaten.name;
		_daten.abijahrgang = pDaten.abijahrgang;
		_daten.gostHalbjahr = pDaten.gostHalbjahr;
		_daten.istAktiv = pDaten.istAktiv;
		
		// Kopieren und Mappings aufbauen. 
		addSchienListe(pDaten.schienen); // Muss vor den Kursen erzeugt werden.
		addRegelListe(pDaten.regeln);
		addKursListe(pDaten.kurse);
		addSchuelerListe(pDaten.schueler);
		addFachwahlListe(pDaten.fachwahlen);
		addErgebnisListe(pDaten.ergebnisse);
	}

	private @NotNull Comparator<@NotNull GostBlockungKurs> createComparatorKursFachKursartNummer() {
		@NotNull Comparator<@NotNull GostBlockungKurs> comp = 
		(@NotNull GostBlockungKurs a, @NotNull GostBlockungKurs b) -> 
		{
			@NotNull GostFach aFach = _faecherManager.getOrException(a.fach_id);
			@NotNull GostFach bFach = _faecherManager.getOrException(b.fach_id);
			int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;
			
			if (a.kursart < b.kursart) return -1;
			if (a.kursart > b.kursart) return +1;
			
			return Integer.compare(a.nummer, b.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostBlockungKurs> createComparatorKursKursartFachNummer() {
		@NotNull Comparator<@NotNull GostBlockungKurs> comp = 
		(@NotNull GostBlockungKurs a, @NotNull GostBlockungKurs b) -> 
		{
			if (a.kursart < b.kursart) return -1;
			if (a.kursart > b.kursart) return +1;
			
			@NotNull GostFach aFach = _faecherManager.getOrException(a.fach_id);
			@NotNull GostFach bFach = _faecherManager.getOrException(b.fach_id);
			int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;
			
			return Integer.compare(a.nummer, b.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostFachwahl> createComparatorFachwahlen() {
		@NotNull Comparator<@NotNull GostFachwahl> comp = 
		(@NotNull GostFachwahl a, @NotNull GostFachwahl b) -> 
		{
			if (a.schuelerID < b.schuelerID) return -1;
			if (a.schuelerID > b.schuelerID) return +1;
			
			if (a.kursartID < b.kursartID) return -1;
			if (a.kursartID > b.kursartID) return +1;
			
			@NotNull GostFach aFach = _faecherManager.getOrException(a.fachID);
			@NotNull GostFach bFach = _faecherManager.getOrException(b.fachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		};
		return comp;
	}
	
	private boolean getIstBlockungsVorlage() {
		return _daten.ergebnisse.size() == 1; 
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public int getOfBewertung1Wert(long pErgebnisID) {
		@NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		int summe = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung1Intervall(long pErgebnisID) {
		double summe = getOfBewertung1Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public int getOfBewertung2Wert(long pErgebnisID) {
		@NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		int summe = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung2Intervall(long pErgebnisID) {
		double summe = getOfBewertung2Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public int getOfBewertung3Wert(long pErgebnisID) {
		@NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Intervall(long pErgebnisID) {
		int wert = getOfBewertung3Wert(pErgebnisID);
		if (wert > 0)
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public int getOfBewertung4Wert(long pErgebnisID) {
		@NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung4Intervall(long pErgebnisID) {
		int wert = getOfBewertung4Wert(pErgebnisID);
		return 1 - 1 / (0.25 * wert + 1);
	}
	
	/** Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 * 
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager}) */
	public @NotNull GostFaecherManager faecherManager() {
		return this._faecherManager;
	}

	/** Liefert die Anzahl an Fächern.
	 * 
	 * @return Die Anzahl an Fächern. */
	public int getFaecherAnzahl() {
		return _faecherManager.faecher().size();
	}

	/** Liefert die Anzahl verschiedenen Kursarten. Dies passiert indem über alle Fachwahlen summiert wird.
	 * 
	 * @return Die Anzahl verschiedenen Kursarten. */
	public int getKursartenAnzahl() {
		HashSet<@NotNull Integer> setKursartenIDs = new HashSet<>();
		for (@NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/** Gibt die Blockungsdaten zurück.
	 * 
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten}) */
	public @NotNull GostBlockungsdaten daten() {
		return this._daten;
	}

	/** Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die Blockung angelegt wurde.
	 * 
	 * @return das Halbjahr der gymnasialen Oberstufe */
	public @NotNull GostHalbjahr getHalbjahr() {
		return GostHalbjahr.fromIDorException(_daten.gostHalbjahr);
	}

	/** Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 * 
	 * @param pHalbjahr das Halbjahr der gymnasialen Oberstufe */
	public void setHalbjahr(@NotNull GostHalbjahr pHalbjahr) {
		_daten.gostHalbjahr = pHalbjahr.id;
	}

	/** Gibt die ID der Blockung zurück.
	 * 
	 * @return die ID der Blockung */
	public long getID() {
		return _daten.id;
	}

	/** Setzt die ID der Blockung
	 * 
	 * @param pBlockungsID die ID, welche der Blockung zugewiesen wird. */
	public void setID(long pBlockungsID) {
		if (pBlockungsID < 0)
			throw new IllegalArgumentException("Die Blockungs-ID ist "+pBlockungsID+", sie muss aber positiv sein!");
		_daten.id = pBlockungsID;
	}

	/** Gibt den Namen der Blockung zurück.
	 * 
	 * @return der Name der Blockung */
	public @NotNull String getName() {
		return _daten.name;
	}

	/** Setzt den Namen der Blockung
	 * 
	 * @param pName der Name, welcher der Blockung zugewiesen wird. */
	public void setName(@NotNull String pName) {
		if ("".equals(pName))
			throw new IllegalArgumentException("Ein leerer Name ist für die Blockung nicht zulässig.");
		_daten.name = pName;
	}

	// TODO Getter und Setter für das aktivierte Zwischenergebnis...

	// ##### GostBlockungsergebnisListeneintrag #####
	
	/** Liefert die maximale Blockungszeit in Millisekunden.
	 * 
	 * @return Die maximale Blockungszeit in Millisekunden. */
	public long getMaxTimeMillis() {
		return _maxTimeMillis;
	}

	/** Setzt die maximale Blockungszeit in Millisekunden.
	 * 
	 * @param pZeit die maximale Blockungszeit in Millisekunden. */
	public void setMaxTimeMillis(long pZeit) {
		_maxTimeMillis = pZeit;
	}

	// ##### GostBlockungsergebnisListeneintrag #####
	
	private void addErgebnisOhneSortierung(@NotNull GostBlockungsergebnisListeneintrag pErgebnis) {
		// Datenkonsistenz überprüfen. 
		if (pErgebnis.id < 1)
			throw new NullPointerException("Ergebnis.id = " + pErgebnis.id + " --> zu gering!");
		if (_mapErgebnis.containsKey(pErgebnis.id))
			throw new NullPointerException("Ergebnis.id =  " + pErgebnis.id + " --> doppelt!");
		if (pErgebnis.blockungID < 1)
			throw new NullPointerException("Ergebnis.blockungID = " + pErgebnis.blockungID + " --> zu gering!");
		if (GostHalbjahr.fromID(pErgebnis.gostHalbjahr) == null)
			throw new NullPointerException("Ergebnis.gostHalbjahr = " + pErgebnis.gostHalbjahr + " --> unbekannt!");
	
		// Hinzufügen des Kurses.
		_daten.ergebnisse.add(pErgebnis);
		_mapErgebnis.put(pErgebnis.id, pErgebnis);
	}

	/** 
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 * 
	 * @param pErgebnis Das {@link GostBlockungsergebnisListeneintrag}-Objekt, welches hinzugefügt wird.
	 */
	public void addErgebnis(@NotNull GostBlockungsergebnisListeneintrag pErgebnis) {
		addErgebnisOhneSortierung(pErgebnis);
		
		// Liste sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnisListeneintrag} hinzu.
	 * 
	 * @param pErgebnisse Die Menge an Ergebnissen.
	 */
	public void addErgebnisListe(@NotNull List<@NotNull GostBlockungsergebnisListeneintrag> pErgebnisse) {
		for (@NotNull GostBlockungsergebnisListeneintrag ergebnis : pErgebnisse)
			addErgebnisOhneSortierung(ergebnis);
		
		// Liste sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}
	
	/**
	 * Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 * 
	 * @param pErgebnisID           Die Datenbank-ID des Ergebnisses. 
	 * @return                      Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * @throws NullPointerException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnisListeneintrag getErgebnis(long pErgebnisID) throws NullPointerException {
		GostBlockungsergebnisListeneintrag e = _mapErgebnis.get(pErgebnisID);
		if (e == null)
			throw new NullPointerException("Ergebnis mit ID = " + pErgebnisID + " nicht vorhanden!");
		return e;
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.    
	 * 
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisListeneintrag> getErgebnisseSortiertNachBewertung() {
		return _daten.ergebnisse;
	}
	
	/** 
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 * 
	 * @param pErgebnisID Die Datenbank-ID des zu entfernenden Ergebnisses.
	 */
	public void removeErgebnisByID(long pErgebnisID) {
		// Gibt es das Ergebnis?
		@NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		
		// Entfernen des Ergebnisses. Neusortierung nicht nötig.
		_daten.ergebnisse.remove(e);
		_mapErgebnis.remove(pErgebnisID);
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 * 
	 * @param pErgebnis Das zu entfernende Ergebnis.
	 */
	public void removeErgebnis(@NotNull GostBlockungsergebnisListeneintrag pErgebnis) {
		removeErgebnisByID(pErgebnis.id);
	}
	
	/**
	 * Aktualisiert die Bewertung im {@link GostBlockungsdatenManager} mit der aus dem {@link GostBlockungsergebnis}. <br>
	 * Wirft eine Exception, falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 *  
	 * @param pErgebnis              Das Ergebnis mit der neuen Bewertung.
	 * @throws NullPointerException  Falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 */
	public void updateErgebnisBewertung(@NotNull GostBlockungsergebnis pErgebnis) throws NullPointerException {
		// Datenkonsistenz überprüfen.
		if (pErgebnis.id < 0)
			throw new NullPointerException("GostBlockungsergebnis.id=" + pErgebnis.id + " zu klein!");
		if (pErgebnis.blockungID < 0)
			throw new NullPointerException("GostBlockungsergebnis.blockungID=" + pErgebnis.blockungID + " zu klein!");
		
		// Bewertung aktualisieren.
		for (@NotNull GostBlockungsergebnisListeneintrag eintrag : _daten.ergebnisse)
			if (eintrag.id == pErgebnis.id) 
				eintrag.bewertung = pErgebnis.bewertung;
		
		// Ergebnisse sortieren.
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	// ##### GostBlockungKurs #####
	
	private void addKursOhneSortierung(@NotNull GostBlockungKurs pKurs) {
		if (pKurs.id < 0)
			throw new NullPointerException("GostBlockungKurs.id=" + pKurs.id + " zu klein!");
		if (_mapKurse.containsKey(pKurs.id))
			throw new NullPointerException("GostBlockungKurs.id =  " + pKurs.id + " --> doppelt!");
		if (pKurs.anzahlSchienen < 1)
			throw new NullPointerException("GostBlockungKurs.anzahlSchienen = " + pKurs.anzahlSchienen + " --> zu gering!");
		int nSchienen = getSchienenAnzahl();
		if (pKurs.anzahlSchienen > nSchienen)
			throw new NullPointerException("GostBlockungKurs.anzahlSchienen = " + nSchienen + " --> zu groß!");
		if (pKurs.nummer < 1)
			throw new NullPointerException("GostBlockungKurs.nummer = " + pKurs.nummer + " --> zu gering!");
		if (_faecherManager.get(pKurs.fach_id) == null)
			throw new NullPointerException("GostBlockungKurs.fach_id = " + pKurs.fach_id + " --> unbekannt!");
		if (GostKursart.fromIDorNull(pKurs.kursart) == null)
			throw new NullPointerException("GostBlockungKurs.kursart = " + pKurs.kursart + " --> unbekannt!");
		if (pKurs.wochenstunden < 0)
			throw new NullPointerException("GostBlockungKurs.wochenstunden = " + pKurs.wochenstunden + " --> zu gering!");
		
		// Hinzufügen des Kurses.
		_daten.kurse.add(pKurs);
		_mapKurse.put(pKurs.id, pKurs);
		_kurse_sortiert_fach_kursart_kursnummer.add(pKurs);
		_kurse_sortiert_kursart_fach_kursnummer.add(pKurs);
	}
	
	/** 
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 * 
	 * @param pKurs Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 */
	public void addKurs(@NotNull GostBlockungKurs pKurs) {
		// Hinzufügen des Kurses.
		addKursOhneSortierung(pKurs);
		
		// Sortieren der Kursmengen.
		_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 * 
	 * @param pKurse Die Menge an Kursen.
	 */
	public void addKursListe(@NotNull List<@NotNull GostBlockungKurs> pKurse) {
		// Hinzufügen der Kurse.
		for (@NotNull GostBlockungKurs gKurs : pKurse) 
			addKursOhneSortierung(gKurs);

		// Sortieren der Kursmengen.
		_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
	}
	
	/** Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 * 
	 * @param  pKursID                   die ID des Kurses
	 * @return                      der Kurs
	 * @throws NullPointerException falls der Kurs nicht in der Blockung existiert */
	public @NotNull GostBlockungKurs getKurs(long pKursID) throws NullPointerException {
		GostBlockungKurs kurs = _mapKurse.get(pKursID);
		if (kurs == null)
			throw new NullPointerException("Kurs mit ID = " + pKursID + " existiert nicht in der Blockung!");
		return kurs;
	}
	
	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 * 
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return         TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public boolean getKursExistiert(long pKursID) {
		GostBlockungKurs kurs = _mapKurse.get(pKursID);
		return kurs != null;
	}

	/** Liefert die Anzahl an Kursen.
	 * 
	 * @return Die Anzahl an Kursen.
	 */
	public int getKursAnzahl() {
		return _mapKurse.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]+[Kursart][Kursnummer], beispielsweise D-GK1.
	 * 
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @return          Den Namen des Kurses der Form [Fach]+[Kursart][Kursnummer], beispielsweise D-GK1.
	 */
	public @NotNull String getNameOfKurs(long pKursID) {
		@NotNull GostBlockungKurs kurs = getKurs(pKursID);
		@NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		return gFach.kuerzel + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<@NotNull GostBlockungKurs> getKursmengeSortiertNachFachKursartNummer() {
		return _kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<@NotNull GostBlockungKurs> getKursmengeSortiertNachKursartFachNummer() {
		return _kurse_sortiert_kursart_fach_kursnummer;
	}

	/** 
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 * 
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @return                       TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws NullPointerException  Falls der Kurs nicht existiert.
	 */
	public boolean removeKursAllowed(long pKursID) throws NullPointerException {
		return (getKurs(pKursID) != null) && getIstBlockungsVorlage();
	}

	/** 
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 * 
	 * @param pKursID Die Datenbank-ID des zu entfernenden Kurses.
	 */
	public void removeKursByID(long pKursID) {
		if (getIstBlockungsVorlage() == false)
			throw new NullPointerException("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!");
		
		// Entfernen des Kurses.
		@NotNull GostBlockungKurs kurs = this.getKurs(pKursID);
		_kurse_sortiert_fach_kursart_kursnummer.remove(kurs); // Neusortierung nicht nötig.
		_kurse_sortiert_kursart_fach_kursnummer.remove(kurs); // Neusortierung nicht nötig.
		_mapKurse.remove(pKursID);
		_daten.kurse.remove(kurs);
	}

	/** 
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 * 
	 * @param pKurs  Der zu entfernende Kurs. 
	 */
	public void removeKurs(@NotNull GostBlockungKurs pKurs) {
		removeKursByID(pKurs.id);
	}

	// ##### GostBlockungKursLehrer #####
	
	/**
	 * Fügt den übergebenen Kurslehrer zu den Blockungs-Daten hinzu
	 *  
	 * @param pKursLehrer   der Kurslehrer
	 */
	public void addKursLehrer(@NotNull GostBlockungKursLehrer pKursLehrer) {
		// TODO Implementierung
	}

	// TODO Getter, HashMaps, Remove, ...
	
	// ##### GostBlockungSchiene #####

	private void addSchieneOhneSortierung(@NotNull GostBlockungSchiene pSchiene) throws NullPointerException {
		// Datenkonsistenz überprüfen.
		if (pSchiene.id < 1)
			throw new NullPointerException("GostBlockungSchiene.id =  " + pSchiene.id + " --> zu klein!");
		if ("".equals(pSchiene.bezeichnung))
			throw new NullPointerException("GostBlockungSchiene.bezeichnung darf nicht leer sein!");
		if (pSchiene.nummer < 1)
			throw new NullPointerException("GostBlockungSchiene.nummer =  " + pSchiene.nummer + " --> zu klein!");
		if (pSchiene.wochenstunden < 1)
			throw new NullPointerException("GostBlockungSchiene.wochenstunden =  " + pSchiene.wochenstunden + " --> zu klein!");
		if (_mapSchienen.containsKey(pSchiene.id))
			throw new NullPointerException("GostBlockungSchiene " + pSchiene.id + " doppelt!");
		
		// Hinzufügen der Schiene.
		_mapSchienen.put(pSchiene.id, pSchiene);
		_daten.schienen.add(pSchiene);
	}
	
	/** 
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 * 
	 * @param pSchiene               Die hinzuzufügende Schiene
	 * @throws NullPointerException  Falls es eine Schienen-ID-Dopplung gibt. 
	 */
	public void addSchiene(@NotNull GostBlockungSchiene pSchiene) throws NullPointerException {
		// Hinzufügen der Schiene.
		addSchieneOhneSortierung(pSchiene);
		
		// Sortieren der Schienenmenge.
		_daten.schienen.sort(compSchiene);
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * 
	 * @param pSchienen Die Menge an Schienen.
	 */
	public void addSchienListe(@NotNull List<@NotNull GostBlockungSchiene> pSchienen) {
		// Hinzufügen der Schienen.
		for (@NotNull GostBlockungSchiene schiene : pSchienen)
			addSchieneOhneSortierung(schiene);
		
		// Sortieren der Schienenmenge.
		_daten.schienen.sort(compSchiene);
	}

	/** 
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 * 
	 * @param  pSchienenID          Die Datenbank-ID der Schiene.
	 * @return                      die Schiene
	 * @throws NullPointerException falls die Schiene nicht in der Blockung existiert
	 */
	public @NotNull GostBlockungSchiene getSchiene(long pSchienenID) throws NullPointerException {
		GostBlockungSchiene schiene = _mapSchienen.get(pSchienenID);
		if (schiene == null)
			throw new NullPointerException("Eine Schiene mit der angegebenen ID existiert nicht in der Blockung.");
		return schiene;
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 * 
	 * @param pSchienenID  Die Datenbank-ID der Schiene.
	 * @return             TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public boolean getSchieneExistiert(long pSchienenID) {
		return _mapSchienen.get(pSchienenID) != null;
	}

	/** 
	 * Liefert die Anzahl an Schienen.
	 * 
	 * @return Die Anzahl an Schienen. 
	 */
	public int getSchienenAnzahl() {
		return _mapSchienen.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen. 
	 * Das ist die interne Referenz zur Liste der Schienen im {@link GostBlockungsdaten}-Objekt. 
	 * Diese Liste ist stets sortiert nach der Schienen-Nummer.
	 * 
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public @NotNull List<@NotNull GostBlockungSchiene> getMengeOfSchienen() {
		return _daten.schienen;
	}

	/** 
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Die Schiene muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 * 
	 * @param  pSchienenID           Die Datenbank-ID der Schiene.
	 * @return                       TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws NullPointerException  Falls die Schiene nicht existiert.
	 */
	public boolean removeSchieneAllowed(long pSchienenID) throws NullPointerException {
		return (getSchiene(pSchienenID) != null) && getIstBlockungsVorlage();
	}
	
	/** 
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein, sonst Exception.
	 * (2) Die Schienen werden neu nummeriert. <br> 
	 * (3) Die Konsistenz der sortierten Schienen muss überprüft werden. <br>
	 * (4) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 * 
	 * @param pSchienenID           Die Datenbank-ID der zu entfernenden Schiene.
	 * @throws NullPointerException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void removeSchieneByID(long pSchienenID) throws NullPointerException {
		// (1)
		if (getIstBlockungsVorlage() == false)
			throw new NullPointerException("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!");
		@NotNull GostBlockungSchiene schieneR = this.getSchiene(pSchienenID);
		
		// (2)
		_mapSchienen.remove(pSchienenID);
		_daten.schienen.remove(schieneR);
		for (@NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;

		// (3)
		for (int index = 0; index < _daten.schienen.size(); index++)
			if (_daten.schienen.get(index).nummer != index + 1)
				throw new NullPointerException("Schiene am Index " + index + " hat nicht Nr. " + (index + 1) + "!");

		// (4)
		Iterator<@NotNull GostBlockungRegel> iRegel = _daten.regeln.iterator();
		while (iRegel.hasNext()) {
			@NotNull GostBlockungRegel r = iRegel.next();
			long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a == null)
				iRegel.remove();
			else
				for (int i = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}

	}

	/** 
	 * Entfernt die übergebene Schiene aus der Blockung.
	 * 
	 * @param pSchiene Die zu entfernende Schiene. 
	 */
	public void removeSchiene(@NotNull GostBlockungSchiene pSchiene) {
		removeSchieneByID(pSchiene.id);
	}

	/** 
	 * Gibt die Default-Anzahl von Schienen zurück, die für die eine neue Blockung verwendet wird.
	 * 
	 * @param  pHalbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 * @return            Die Anzahl an Schienen für eine Vorauswahl. 
	 */
	public static int getDefaultSchienenAnzahl(@NotNull GostHalbjahr pHalbjahr) {
		return (pHalbjahr.id < 2) ? 13 : 11;
	}

	// ##### GostBlockungRegel #####

	private void addRegelOhneSortierung(@NotNull GostBlockungRegel pRegel) {
		// Datenkonsistenz überprüfen.
		if (pRegel.id < 1)
			throw new NullPointerException("Regel.id =  " + pRegel.id + " --> zu klein!");
		if (_mapRegeln.containsKey(pRegel.id))
			throw new NullPointerException("Regel.id = " + pRegel.id + " --> doppelt!");
		if (GostKursblockungRegelTyp.fromTyp(pRegel.typ) == GostKursblockungRegelTyp.UNDEFINIERT)
			throw new NullPointerException("Regel.typ = " + pRegel.typ + " --> unbekannt!");
		
		// Hinzufügen der Regel.
		_daten.regeln.add(pRegel);
		_mapRegeln.put(pRegel.id, pRegel);
	}

	
	/** Fügt die übergebene Regel zu der Blockung hinzu
	 * 
	 * @param pRegel die hinzuzufügende Regel 
	 */
	public void addRegel(@NotNull GostBlockungRegel pRegel) {
		// Regel hinzufügen.
		addRegelOhneSortierung(pRegel);
		
		// Sortieren der Regelmenge.
		_daten.regeln.sort(compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 * 
	 * @param pRegeln Die Menge an Regeln.
	 */
	public void addRegelListe(@NotNull List<@NotNull GostBlockungRegel> pRegeln) {
		// Regeln hinzufügen.
		for (@NotNull GostBlockungRegel regel : pRegeln)
			addRegelOhneSortierung(regel);
		
		// Sortieren der Regelmenge.
		_daten.regeln.sort(compRegel);
	}

	/** 
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 * 
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @return                       Das GostBlockungRegel-Objekt.
	 * @throws NullPointerException  Falls die Regel nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungRegel getRegel(long pRegelID) throws NullPointerException {
		GostBlockungRegel regel = _mapRegeln.get(pRegelID);
		if (regel == null)
			throw new NullPointerException("Regel.id = " + pRegelID + " existiert nicht in der Blockung.");
		return regel;
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 * 
	 * @param pRegelID  Die Datenbank-ID der Regel.
	 * @return          TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public boolean getRegelExistiert(long pRegelID) {
		return _mapRegeln.get(pRegelID) != null;
	}

	/** 
	 * Liefert die Anzahl an Regeln.
	 * 
	 * @return Die Anzahl an Regeln. 
	 */
	public int getRegelAnzahl() {
		return _mapRegeln.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln. 
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt. 
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 * 
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public @NotNull List<@NotNull GostBlockungRegel> getMengeOfRegeln() {
		return _daten.regeln;
	}

	/** 
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist. <br>
	 * Kriterium: Die Regel muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 * 
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @return                       TRUE, falls ein Löschen der Regel erlaubt ist.
	 * @throws NullPointerException  Falls die Regel nicht existiert.
	 */
	public boolean removeRegelAllowed(long pRegelID) throws NullPointerException {
		return (getRegel(pRegelID) != null) && getIstBlockungsVorlage();
	}

	/** 
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 * Wirft eine Exception, falls es sich nicht um eine Blockungsvorlage handelt.
	 * 
	 * @param pRegelID              Die Datenbank-ID der zu entfernenden Regel. 
	 * @throws NullPointerException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void removeRegelByID(long pRegelID) throws NullPointerException {
		// Datenkonsistenz überprüfen.
		if (getIstBlockungsVorlage() == false)
			throw new NullPointerException("Ein Löschen einer Regel ist nur bei einer Blockungsvorlage erlaubt!");
		
		// Regel entfernen.
		@NotNull GostBlockungRegel regel = this.getRegel(pRegelID);
		_mapRegeln.remove(pRegelID);
		_daten.regeln.remove(regel);
	}

	/** 
	 * Entfernt die übergebene Regel aus der Blockung.
	 * 
	 * @param regel die zu entfernende Regel 
	 */
	public void removeRegel(@NotNull GostBlockungRegel regel) {
		removeRegelByID(regel.id);
	}
	
	// ##### Schueler #####

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls es eine Schüler-ID-Dopplung gibt. 
	 * 
	 * @param pSchueler             Der Schüler, der hinzugefügt wird.
	 * @throws NullPointerException Falls es eine Schüler-ID-Dopplung gibt.
	 */
	public void addSchueler(@NotNull Schueler pSchueler) {
		// Datenkonsistenz überprüfen.
		if (pSchueler.id < 1)
			throw new NullPointerException("Schueler.id =  " + pSchueler.id + " --> zu klein!");
		if (_map_id_schueler.containsKey(pSchueler.id))
			throw new NullPointerException("Schueler.id =  " + pSchueler.id + " --> doppelt!");
		if (pSchueler.geschlecht < 0)
			throw new NullPointerException("Schueler.geschlecht =  " + pSchueler.geschlecht + " --> zu klein!");
		
		// Schüler hinzufügen
		_daten.schueler.add(pSchueler);
		_map_id_schueler.put(pSchueler.id, pSchueler);
		if (_map_schuelerID_fachwahlen.containsKey(pSchueler.id) == false)
			_map_schuelerID_fachwahlen.put(pSchueler.id, new Vector<>());
		if (_map_schulerID_fachID_fachwahl.containsKey(pSchueler.id) == false)
			_map_schulerID_fachID_fachwahl.put(pSchueler.id, new HashMap<>());
	}
	
	/**
	 * Fügt alle Schüler hinzu.
	 * 
	 * @param pSchueler Die Menge an Schülern.
	 */
	public void addSchuelerListe(@NotNull List<@NotNull Schueler> pSchueler) {
		for (@NotNull Schueler schueler : pSchueler) 
			addSchueler(schueler);
	}
	
	/** 
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param  pSchuelerID           Die Datenbank-ID des Schülers.
	 * @return                       Das {@link Schueler}-Objekt.
	 * @throws NullPointerException  Falls die ID nicht existiert. 
	 */
	public @NotNull Schueler getSchueler(long pSchuelerID) throws NullPointerException {
		Schueler schueler = _map_id_schueler.get(pSchuelerID);
		if (schueler == null)
			throw new NullPointerException("Schüler-ID = " + pSchuelerID + " existiert nicht!");
		return schueler;
	}

	/**
	 * Liefert nur die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 * 
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public int getSchuelerAnzahlMitFachwahlen() {
		HashSet<@NotNull Long> setSchuelerIDs = new HashSet<>();
		for (@NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 * 
	 * @return Die Anzahl an Schülern.
	 */
	public int getSchuelerAnzahl() {
		return _daten.schueler.size();
	}

	/**
	 * Liefert zu (Schüler, Fach) die jeweilige Kursart. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 * 
	 * @param pSchuelerID            Die Datenbank-ID des Schülers.
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Die zu (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws NullPointerException  Falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostKursart getOfSchuelerOfFachKursart(long pSchuelerID, long pFachID) throws NullPointerException {
		@NotNull GostFachwahl fachwahl = getOfSchuelerOfFachFachwahl(pSchuelerID, pFachID);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zu (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 * 
	 * @param pSchuelerID            Die Datenbank-ID des Schülers.
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Die zu (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws NullPointerException  Falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostFachwahl getOfSchuelerOfFachFachwahl(long pSchuelerID, long pFachID) throws NullPointerException {
		HashMap<@NotNull Long, @NotNull GostFachwahl> mapFachFachwahl = _map_schulerID_fachID_fachwahl.get(pSchuelerID);
		if (mapFachFachwahl == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + " unbekannt!");
	
		GostFachwahl fachwahl = mapFachFachwahl.get(pFachID);
		if (fachwahl == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + ", Fach-ID=" + pFachID + " unbekannt!");
		
		return fachwahl;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers. 
	 * Diese Liste ist stets sortiert nach (KURSART, FACH.sortierung).
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers, sortiert (KURSART, FACH.sortierung).
	 */
	public @NotNull List<@NotNull GostFachwahl> getOfSchuelerFacharten(long pSchuelerID) {
		List<@NotNull GostFachwahl> fachwahlenDesSchuelers = _map_schuelerID_fachwahlen.get(pSchuelerID);
		if (fachwahlenDesSchuelers == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + " unbekannt!");
		return fachwahlenDesSchuelers;
	}

	// ##### GostFachwahl #####

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *  
	 * @param pSchuelerID   Die Datenbank.ID des Schülers.
	 * @param pFach         Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param pKursart      Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 * @return              TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 */	
	public boolean getOfSchuelerHatFachart(long pSchuelerID, long pFach, long pKursart) {
		HashMap<@NotNull Long, @NotNull GostFachwahl> map = _map_schulerID_fachID_fachwahl.get(pSchuelerID);
		if (map == null)
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!");
		GostFachwahl wahl = map.get(pFach);
		if (wahl == null)
			return false;
		return wahl.kursartID == pKursart;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 * Wirft eine Exception, falls das Paar (Schüler-ID, FachartID) doppelt existiert. 
	 * 
	 * @param pFachwahl              Die Fachwahl, die hinzugefügt wird.
	 * @throws NullPointerException  Falls es eine FachwahlDopplung gibt.
	 */
	public void addFachwahl(@NotNull GostFachwahl pFachwahl) throws NullPointerException {
		// ########## _map_schulerID_fachID_fachwahl ##########
		
		// Pfad: Schüler-ID --> Fach --> GostFachwahl 
		HashMap<@NotNull Long, @NotNull GostFachwahl> mapSFA = _map_schulerID_fachID_fachwahl.get(pFachwahl.schuelerID);
		if (mapSFA == null) {
			mapSFA = new HashMap<>();
			_map_schulerID_fachID_fachwahl.put(pFachwahl.schuelerID, mapSFA);
		}

		// Hinzufügen '_map_schulerID_fachID_fachwahl'
		long fachID = pFachwahl.fachID;
		if (mapSFA.put(fachID, pFachwahl) != null) 
			throw new NullPointerException("Schüler-ID=" + pFachwahl.schuelerID + ", Fach-ID=" + fachID + " doppelt!");
		
		// ########## _map_schuelerID_fachwahlen ##########
		
		// Pfad: Schüler-ID --> Vector<GostFachwahl>
		List<@NotNull GostFachwahl> fachwahlenDesSchuelers = _map_schuelerID_fachwahlen.get(pFachwahl.schuelerID);
		if (fachwahlenDesSchuelers == null) {
			fachwahlenDesSchuelers = new Vector<>();
			_map_schuelerID_fachwahlen.put(pFachwahl.schuelerID, fachwahlenDesSchuelers);
		}
		fachwahlenDesSchuelers.add(pFachwahl);
		fachwahlenDesSchuelers.sort(_compFachwahlen);
		
		// ########## _daten.fachwahlen ##########
		_daten.fachwahlen.add(pFachwahl);
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 * 
	 * @param pFachwahlen Die Menge an Fachwahlen. 
	 */
	public void addFachwahlListe(@NotNull List<@NotNull GostFachwahl> pFachwahlen) {
		for (@NotNull GostFachwahl gFachwahl : pFachwahlen) 
			addFachwahl(gFachwahl);
	}

	/** Liefert die Anzahl an Fachwahlen.
	 * 
	 * @return Die Anzahl an Fachwahlen.
	 */
	public int getFachwahlAnzahl() {
		return _daten.fachwahlen.size();
	}

}
