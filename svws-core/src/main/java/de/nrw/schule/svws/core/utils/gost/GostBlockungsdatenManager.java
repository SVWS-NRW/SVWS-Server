package de.nrw.schule.svws.core.utils.gost;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/** Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}. Hierbei werden auch Hilfsmethoden zur
 * Interpretation der Daten erzeugt. */
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

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe */
	private final @NotNull GostFaecherManager _faecherManager;

	/** Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungKurs> _mapKurse = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungSchiene> _mapSchienen = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungRegel> _mapRegeln = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Schueler> _map_ID_schueler = new HashMap<>();

	/** Schüler-ID --> Vector<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostFachwahl>> _map_schuelerID_fachwahlen = new HashMap<>();
	
	/** Schüler-ID --> Fach-ID --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull GostKursart>> _map_schulerID_fachID_kursart = new HashMap<>();

	/** Schüler-ID --> Vector<Facharten> */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull Long>> _map_schulerID_facharten = new HashMap<>();

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER) */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_fach_kursart_kursnummer;

	/** Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER) */
	private Vector<@NotNull GostBlockungKurs> _kurse_sortiert_fach_kursart_kursnummer = null;

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_kursart_fach_kursnummer;

	/** Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER) */
	private Vector<@NotNull GostBlockungKurs> _kurse_sortiert_kursart_fach_kursnummer = null;

	/** Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART) */
	private final @NotNull Comparator<@NotNull GostFachwahl> _compFachwahlen;

	/** Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf. */
	private long _maxTimeMillis = 1000;

	/** Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager. */
	public GostBlockungsdatenManager() {
		_faecherManager = new GostFaecherManager();
		_daten = new GostBlockungsdaten();
		_daten.gostHalbjahr = GostHalbjahr.EF1.id;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();
	}

	/** Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager
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
		_daten.vorlageID = pDaten.vorlageID;
		_daten.ergebnisse.addAll(pDaten.ergebnisse); // TODO BARTSCH-BACHRAN --> Ergebnisse kopieren, ist das richtig?
		
		// Kopieren und Mappings aufbauen. 
		addSchienListe(pDaten.schienen);
		addRegelListe(pDaten.regeln);
		addKursListe(pDaten.kurse);
		addSchuelerListe(pDaten.schueler);
		addFachwahlListe(pDaten.fachwahlen);
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
	
	/** Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 * 
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager}) */
	public @NotNull GostFaecherManager faecherManager() {
		return this._faecherManager;
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
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe */
	public void setHalbjahr(@NotNull GostHalbjahr halbjahr) {
		_daten.gostHalbjahr = halbjahr.id;
	}

	/** Gibt die ID der Blockung zurück.
	 * 
	 * @return die ID der Blockung */
	public long getID() {
		return _daten.id;
	}

	/** Setzt die ID der Blockung
	 * 
	 * @param id die ID, welche der Blockung zugewiesen wird. */
	public void setID(long id) {
		if (id < 0)
			throw new IllegalArgumentException("Die Blockungs-ID muss positiv sein und ist daher ungültig.");
		_daten.id = id;
	}

	/** Gibt den Namen der Blockung zurück.
	 * 
	 * @return der Name der Blockung */
	public @NotNull String getName() {
		return _daten.name;
	}

	/** Setzt den Namen der Blockung
	 * 
	 * @param name der Name, welcher der Blockung zugewiesen wird. */
	public void setName(@NotNull String name) {
		if ("".equals(name))
			throw new IllegalArgumentException("Ein leerer Name ist für die Blockung nicht zulässig.");
		_daten.name = name;
	}

	// TODO Getter und Setter für das aktivierte Zwischenergebnis...

	/** Fügt den übergebenen Kurs zu der Blockung hinzu
	 * 
	 * @param kurs der hinzuzufügende Kurs */
	public void addKurs(@NotNull GostBlockungKurs kurs) {
		if (_mapKurse.containsKey(kurs.id))
			throw new NullPointerException("Kurs " + kurs.id + " doppelt!");
		_daten.kurse.add(kurs);
		_mapKurse.put(kurs.id, kurs);
		// Cache der sortierten Kurslisten löschen. 
		_kurse_sortiert_fach_kursart_kursnummer = null;
		_kurse_sortiert_kursart_fach_kursnummer = null;
	}


	/**
	 * Fügt die Menge an Kursen hinzu.
	 * 
	 * @param pKurse Die Menge an Kursen.
	 */
	public void addKursListe(@NotNull List<@NotNull GostBlockungKurs> pKurse) {
		for (@NotNull GostBlockungKurs gKurs : pKurse) 
			addKurs(gKurs);
	}
	
	/** Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 * 
	 * @param  id                   die ID des Kurses
	 * @return                      der Kurs
	 * @throws NullPointerException falls der Kurs nicht in der Blockung existiert */
	public @NotNull GostBlockungKurs getKurs(long id) throws NullPointerException {
		GostBlockungKurs kurs = _mapKurse.get(id);
		if (kurs == null)
			throw new NullPointerException("Ein Kurs mit der angegebenen ID existiert nicht in der Blockung.");
		return kurs;
	}
	
	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull Vector<@NotNull GostBlockungKurs> getKursmengeSortiertNachFachKursartNummer() {
		if (_kurse_sortiert_fach_kursart_kursnummer == null) {
			_kurse_sortiert_fach_kursart_kursnummer = new Vector<>(_daten.kurse);
			_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		}
		return _kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull Vector<@NotNull GostBlockungKurs> getKursmengeSortiertNachKursartFachNummer() {
		if (_kurse_sortiert_kursart_fach_kursnummer == null) {
			_kurse_sortiert_kursart_fach_kursnummer = new Vector<>(_daten.kurse);
			_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
		}
		return _kurse_sortiert_kursart_fach_kursnummer;
	}

	/** Entfernt den Kurs mit der übergebenen ID aus der Blockung
	 * 
	 * @param id die ID des zu entfernenden Kurses */
	public void removeKursByID(long id) {
		@NotNull GostBlockungKurs kurs = this.getKurs(id);
		_daten.kurse.remove(kurs);
		_mapKurse.remove(id);
		// Cache der sortierten Kurslisten löschen. 
		_kurse_sortiert_fach_kursart_kursnummer = null;
		_kurse_sortiert_kursart_fach_kursnummer = null;
	}

	/** Entfernt den übergebenen Kurs aus der Blockung
	 * 
	 * @param kurs der zu entfernende Kurs */
	public void removeKurs(@NotNull GostBlockungKurs kurs) {
		removeKursByID(kurs.id);
	}

	/** Fügt die übergebene Schiene zu der Blockung hinzu
	 * 
	 * @param pSchiene die hinzuzufügende Schiene
	 * @throws NullPointerException Falls es eine Schienen-ID-Dopplung gibt. 
	 */
	public void addSchiene(@NotNull GostBlockungSchiene pSchiene) {
		if (_mapSchienen.containsKey(pSchiene.id))
			throw new NullPointerException("Schiene " + pSchiene.id + " doppelt!");
		_daten.schienen.add(pSchiene);
		_daten.schienen.sort(compSchiene);
		_mapSchienen.put(pSchiene.id, pSchiene);
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * 
	 * @param pSchienen Die Menge an Schienen.
	 */
	public void addSchienListe(@NotNull List<@NotNull GostBlockungSchiene> pSchienen) {
		for (@NotNull GostBlockungSchiene schiene : pSchienen)
			addSchiene(schiene);
	}

	/** Gibt die Schiene der Blockung anhand von deren ID zurück.
	 * 
	 * @param  id                   die ID der Schiene
	 * @return                      die Schiene
	 * @throws NullPointerException falls die Schiene nicht in der Blockung existiert */
	public @NotNull GostBlockungSchiene getSchiene(long id) throws NullPointerException {
		GostBlockungSchiene schiene = _mapSchienen.get(id);
		if (schiene == null)
			throw new NullPointerException("Eine Schiene mit der angegebenen ID existiert nicht in der Blockung.");
		return schiene;
	}

	/** Entfernt die Schiene mit der übergebenen ID aus der Blockung
	 * 
	 * @param id die ID der zu entfernenden Schiene */
	public void removeSchieneByID(long id) {
		@NotNull GostBlockungSchiene schiene = this.getSchiene(id);
		_daten.schienen.remove(schiene);
		_daten.schienen.sort(compSchiene);
		_mapSchienen.remove(id);
	}

	/** Entfernt die übergebene Schiene aus der Blockung
	 * 
	 * @param schiene die zu entfernende Schiene */
	public void removeSchiene(@NotNull GostBlockungSchiene schiene) {
		removeSchieneByID(schiene.id);
	}

	/** Gibt die Default-Anzahl von Schienen zurück, die für die eine neue Blockung verwendet wird.
	 * 
	 * @param  halbjahr das Halbjahr, für welches die Blockung angelegt werden soll
	 * @return          die Anzahl an Schienen für eine Vorauswahl */
	public static int getDefaultSchienenAnzahl(@NotNull GostHalbjahr halbjahr) {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	/** Fügt die übergebene Regel zu der Blockung hinzu
	 * 
	 * @param pRegel die hinzuzufügende Regel 
	 */
	public void addRegel(@NotNull GostBlockungRegel pRegel) {
		if (_mapRegeln.containsKey(pRegel.id))
			throw new NullPointerException("Regel " + pRegel.id + " doppelt!");		
		_daten.regeln.add(pRegel);
		_daten.regeln.sort(compRegel);
		_mapRegeln.put(pRegel.id, pRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 * 
	 * @param regeln Die Menge an Regeln.
	 */
	public void addRegelListe(@NotNull List<@NotNull GostBlockungRegel> regeln) {
		for (@NotNull GostBlockungRegel regel : regeln)
			addRegel(regel);
	}

	/** Gibt die Regel der Blockung anhand von deren ID zurück.
	 * 
	 * @param  id                   die ID der Regel
	 * @return                      die Regel
	 * @throws NullPointerException falls die Regel nicht in der Blockung existiert */
	public @NotNull GostBlockungRegel getRegel(long id) throws NullPointerException {
		GostBlockungRegel regel = _mapRegeln.get(id);
		if (regel == null)
			throw new NullPointerException("Eine Regel mit der angegebenen ID existiert nicht in der Blockung.");
		return regel;
	}

	/** Entfernt die Regel mit der übergebenen ID aus der Blockung
	 * 
	 * @param id die ID der zu entfernenden Regel */
	public void removeRegelByID(long id) {
		@NotNull GostBlockungRegel regel = this.getRegel(id);
		_daten.regeln.remove(regel);
		_daten.regeln.sort(compRegel);		
		_mapRegeln.remove(id);
	}

	/** Entfernt die übergebene Regel aus der Blockung
	 * 
	 * @param regel die zu entfernende Regel */
	public void removeRegel(@NotNull GostBlockungRegel regel) {
		removeRegelByID(regel.id);
	}

	/** Ermittelt den Schüler für die angegebene ID. Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht
	 * bekannt ist.
	 * 
	 * @param  id                   die ID des Schülers
	 * @return                      die Daten zu dem Schüler der Blockung
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull Schueler getSchueler(long id) throws NullPointerException {
		Schueler schueler = _map_ID_schueler.get(id);
		if (schueler == null)
			throw new NullPointerException("ID des Schülers ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return schueler;
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls es eine Schüler-ID-Dopplung gibt. 
	 * 
	 * @param pSchueler Der Schüler, der hinzugefügt wird.
	 * @throws NullPointerException Falls es eine Schüler-ID-Dopplung gibt.
	 */
	public void addSchueler(@NotNull Schueler pSchueler) {
		if (_map_ID_schueler.containsKey(pSchueler.id))
			throw new NullPointerException("Schüler " + pSchueler.id + " doppelt!");
		_daten.schueler.add(pSchueler);
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
	 * Fügt eine Fachwahl hinzu.
	 * Wirft eine Exception, falls das Paar (Schüler-ID, FachartID) doppelt existiert. 
	 * 
	 * @param pFachwahl Die Fachwahl, die hinzugefügt wird.
	 * @throws NullPointerException  Falls es eine FachwahlDopplung gibt.
	 */
	public void addFachwahl(@NotNull GostFachwahl pFachwahl) throws NullPointerException {
		// ########## _map_schulerID_fachID_kursart ##########
		
		// Pfad: Schüler-ID --> Fach --> Kursart 
		HashMap<@NotNull Long, @NotNull GostKursart> mapSFA = _map_schulerID_fachID_kursart.get(pFachwahl.schuelerID);
		if (mapSFA == null) {
			mapSFA = new HashMap<>();
			_map_schulerID_fachID_kursart.put(pFachwahl.schuelerID, mapSFA);
		}

		// Hinzufügen '_map_schulerID_fachID_kursart'
		long fachID = pFachwahl.fachID;
		@NotNull GostKursart kursart = GostKursart.fromFachwahlOrException(pFachwahl);
		if (mapSFA.put(fachID, kursart) != null) 
			throw new NullPointerException("Schüler-ID=" + pFachwahl.schuelerID + ", Fach-ID=" + fachID + " doppelt!");
		
		// ########## _map_schuelerID_fachwahlen ##########
		
		// Pfad: Schüler-ID --> Vector<GostFachwahl>
		Vector<@NotNull GostFachwahl> fachwahlenDesSchuelers = _map_schuelerID_fachwahlen.get(pFachwahl.schuelerID);
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

	/** Liefert die Anzahl an Schienen.
	 * 
	 * @return Die Anzahl an Schienen. */
	public int getSchienenAnzahl() {
		return _mapSchienen.size();
	}

	/** Liefert die Anzahl an Kursen.
	 * 
	 * @return Die Anzahl an Kursen. */
	public int getKursAnzahl() {
		return _mapKurse.size();
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

	/** Liefert die Anzahl an Fachwahlen.
	 * 
	 * @return Die Anzahl an Fachwahlen. */
	public int getFachwahlAnzahl() {
		return _daten.fachwahlen.size();
	}

	/** Liefert die Anzahl an Regeln.
	 * 
	 * @return Die Anzahl an Regeln. */
	public int getRegelAnzahl() {
		return _mapRegeln.size();
	}

	/**
	 * Liefert den Namen des Kurses. Der Name wird automatisch erzeugt aus dem Fach, der Kursart und der Nummer,
	 * beispielsweise D-GK1.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * 
	 * @return         Die Datenbank-ID des Kurses.
	 */
	public @NotNull String getNameOfKurs(long pKursID) {
		@NotNull GostBlockungKurs kurs = getKurs(pKursID);
		@NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		return gFach.kuerzel + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
	}

	/**
	 * Liefert die zu (Schüler, Fach) die jeweilige Kursart. <br>
	 * Liefert eine Exception, falls (Schüler, Fach) nicht existiert.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID     Die Datenbank-ID des Faches.
	 * 
	 * @return Die zu (Schüler, Fach) die jeweilige Kursart.
	 */
	public @NotNull GostKursart getOfSchuelerOfFachKursart(long pSchuelerID, long pFachID) {

		HashMap<@NotNull Long, @NotNull GostKursart> mapFachKursart = _map_schulerID_fachID_kursart.get(pSchuelerID);
		if (mapFachKursart == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + " unbekannt!");

		GostKursart kursart = mapFachKursart.get(pFachID);
		if (kursart == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + ", Fach-ID=" + pFachID + " unbekannt!");

		return kursart;
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
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers. 
	 * Diese Liste ist stets sortiert nach (KURSART, FACH.sortierung).
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers, sortiert (KURSART, FACH.sortierung).
	 */
	public @NotNull Vector<@NotNull GostFachwahl> getOfSchuelerFacharten(long pSchuelerID) {
		Vector<@NotNull GostFachwahl> fachwahlenDesSchuelers = _map_schuelerID_fachwahlen.get(pSchuelerID);
		if (fachwahlenDesSchuelers == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + " unbekannt!");
		return fachwahlenDesSchuelers;
	}

}
