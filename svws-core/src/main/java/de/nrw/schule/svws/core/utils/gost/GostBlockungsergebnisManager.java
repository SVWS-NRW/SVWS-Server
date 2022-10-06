package de.nrw.schule.svws.core.utils.gost;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchuelerzuordnung;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsergebnis}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class GostBlockungsergebnisManager {

	/** Das Blockungsergebnis */
	private final @NotNull GostBlockungsergebnis ergebnis;

	/** Eine Map für den schnellen Zugriff auf die Liste der Kurszuordnungen eines Schülers über die ID des Schülers */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> mapSchuelerKursZuordnungen = new HashMap<>();
	
	/** Eine Map für den schnellen Zugriff auf die Liste der Schülerzuordnungen eines Kurses über die ID des Kurses */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisKurs> mapKursSchuelerZuordnungen = new HashMap<>();

	/** Eine Map für den schnellen Zugriff auf die Liste der Schülerzuordnungen eines Kurses über die ID des Kurses */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostBlockungsergebnisKurs>> mapKursSchuelerZuordnungenFuerFach = new HashMap<>();
	
	/** Eine Map für den schnellen Zugriff auf die Liste der Kurszuordnungen einer Schiene über die ID der Schiene */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisSchiene> mapSchienenKursZuordnungen = new HashMap<>();
	
	/** Eine Map für den schnellen Zugriff auf die Schiene, welcher der Kurs mit der ID zugeordnet ist */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisSchiene> mapKursSchienenZuordnungen = new HashMap<>();
	
	/** Eine Map, welche einer Kurs-ID die Menge von Schülern zuordnet */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull Long>> mapKursSchuelermenge = new HashMap<>();

	/** Eine Map für den schnellen Zugriff auf die Daten eines Schülers über dessen ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull Schueler> mapSchueler = new HashMap<>();

	/** Eine Map für den schnellen Zugriff auf die Kurszuordnung eines Schüler und einem Fach */
	private final @NotNull HashMap<@NotNull Long, HashMap<@NotNull Long, @NotNull GostBlockungsergebnisKurs>> mapSchuelerFachKursZuordnungen = new HashMap<>();
	
	/** Eine Map für den schnellen Zugriff auf die Daten eines Faches über dessen ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostFach> mapFaecher = new HashMap<>();

	/** Eine Map für den schnellen Zugriff auf die Daten einer Schiene über dessen ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungSchiene> mapSchienen = new HashMap<>();
	
	/** Eine Map für den schnellen Zugriff auf die Daten eines Kurse über dessen ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungKurs> mapKurse = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager mit dem übergebenen Daten und erstellt ein neuen
	 * Objekt vom Typ {@link GostBlockungsergebnis}. Die Kurs-Schienen und Kurs-Schülerzuordnung 
	 * bei dem Blockungsergebnis ist leer.
	 * 
	 * @param id           die ID des Zwischenergebnis 
	 * @param blockungID   die ID der zugehörigen Blockung
	 * @param name         der Name der Blockung
	 * @param halbjahr     das Halbjahr der gymnasialen Oberstufe für welches das Blockungsergebnis erstellt wurde
	 * @param schueler     die Liste der Schüler 
	 * @param faecher      die Fächer der gymnasialen Oberstufe
	 * @param schienen     die Schienen der Blockung, welche in dem Ergebnis zugeordnet werden
	 * @param kurse        die Kurse der Blockung, welche in dem Ergebnis zugeordnet werden 
	 */
	public GostBlockungsergebnisManager(long id, long blockungID, @NotNull String name, GostHalbjahr halbjahr,
			@NotNull List<@NotNull Schueler> schueler, 
			@NotNull List<@NotNull GostFach> faecher, @NotNull List<@NotNull GostBlockungSchiene> schienen, 
			@NotNull List<@NotNull GostBlockungKurs> kurse) {
		this.ergebnis = new GostBlockungsergebnis();
		this.ergebnis.id = id;
		this.ergebnis.blockungID = blockungID;
		this.ergebnis.name = name;
		this.ergebnis.gostHalbjahr = halbjahr.id;
		initSchueler(schueler);
		initFaecher(faecher);
		initSchienen(schienen);
		initKurse(kurse);
		update();
	}

	
	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis
	 * 
	 * @param ergebnis     die Daten des Blockungsergebnis 
	 * @param schueler     die Liste der Schüler 
	 * @param faecher      die Fächer der gymnasialen Oberstufe
	 * @param schienen     die Schienen der Blockung, welche in dem Ergebnis zugeordnet werden
	 * @param kurse        die Kurse der Blockung, welche in dem Ergebnis zugeordnet werden 
	 */
	public GostBlockungsergebnisManager(@NotNull GostBlockungsergebnis ergebnis, 
			@NotNull List<@NotNull Schueler> schueler, @NotNull List<@NotNull GostFach> faecher, 
			@NotNull List<@NotNull GostBlockungSchiene> schienen,  @NotNull List<@NotNull GostBlockungKurs> kurse) {
		this.ergebnis = ergebnis;
		initSchueler(schueler);
		initFaecher(faecher);
		initSchienen(schienen);
		for (@NotNull GostBlockungsergebnisSchiene schiene : ergebnis.schienen) {
			mapSchienenKursZuordnungen.put(schiene.id, schiene);
			for (@NotNull GostBlockungsergebnisKurs kurs : schiene.kurse) {
				mapKursSchienenZuordnungen.put(kurs.id, schiene);
				mapKursSchuelerZuordnungen.put(kurs.id, kurs);
				Vector<@NotNull GostBlockungsergebnisKurs> fachKurse = mapKursSchuelerZuordnungenFuerFach.get(kurs.fachID);
				if (fachKurse != null)
					fachKurse.add(kurs);
			}
		}
		initKurse(kurse);
		update();
	}

	
	/**
	 * Initialisiert die Map für die Schüler, um einen schnellen Zugriff auf deren Daten zu ermöglichen.
	 * 
	 * @param schueler   die Liste mit der Definition der Schüler
	 */
	private void initSchueler(@NotNull List<@NotNull Schueler> schueler) {
		for (@NotNull Schueler s : schueler) {
			mapSchueler.put(s.id, s);
			mapSchuelerKursZuordnungen.put(s.id, new HashSet<>());
			mapSchuelerFachKursZuordnungen.put(s.id, new HashMap<>());
		}
	}


	/**
	 * Initialisiert die Map für die Fächer, um einen schnellen Zugriff auf deren Daten zu ermöglichen.
	 * 
	 * @param faecher   die Liste mit der Definition der Fächer
	 */
	private void initFaecher(@NotNull List<@NotNull GostFach> faecher) {
		for (@NotNull GostFach fach : faecher) {
			mapFaecher.put(fach.id, fach);
			mapKursSchuelerZuordnungenFuerFach.put(fach.id, new Vector<>());
		}
	}


	/**
	 * Initialisiert die Map für die Kurse, um einen schnellen Zugriff auf dessen Daten zu ermöglichen.
	 * 
	 * @param kurse   die Liste mit der Definition der Kurse der Blockung
	 */
	private void initKurse(@NotNull List<@NotNull GostBlockungKurs> kurse) {
		for (@NotNull GostBlockungKurs kurs : kurse) {
			mapKurse.put(kurs.id, kurs);
			GostBlockungsergebnisKurs erg = mapKursSchuelerZuordnungen.get(kurs.id);
			if (erg == null) {
				@NotNull GostFach fach = this.getFach(kurs.fach_id);
				erg = new GostBlockungsergebnisKurs();
				erg.id = kurs.id;
				erg.schienenID = null;   // noch nicht zugeordnet
				erg.fachID = kurs.fach_id;
				erg.kursart = GostKursart.fromID(kurs.kursart).kuerzel;
				erg.name = fach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
				if (!"".equals(kurs.suffix))
					erg.name += "-" + kurs.suffix;
				erg.anzahlKollisionen = 0;
				mapKursSchuelerZuordnungen.put(kurs.id, erg);
				Vector<@NotNull GostBlockungsergebnisKurs> fachKurse = mapKursSchuelerZuordnungenFuerFach.get(kurs.fach_id);
				if (fachKurse != null)
					fachKurse.add(erg);
			}
			@NotNull HashSet<@NotNull Long> schuelerIDs = new HashSet<>();
			for (@NotNull GostBlockungsergebnisSchuelerzuordnung schueler : erg.schueler) {
				schuelerIDs.add(schueler.id);
				@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> schuelerKursZuordnung = getSchuelerKursZuordnung(schueler.id);
				schuelerKursZuordnung.add(erg);
				HashMap<@NotNull Long, @NotNull GostBlockungsergebnisKurs> sfk = mapSchuelerFachKursZuordnungen.get(schueler.id);
				if (sfk != null)
					sfk.put(erg.fachID, erg);
			}
			mapKursSchuelermenge.put(kurs.id, schuelerIDs);
		}
	}
	
	
	/**
	 * Initialisiert die Map für die Schienen, um einen schnellen Zugriff auf deren Daten zu ermöglichen.
	 * 
	 * @param schienen   die Liste mit der Definition der Schienen der Blockung
	 */
	private void initSchienen(@NotNull List<@NotNull GostBlockungSchiene> schienen) {
		boolean doAdd = (ergebnis.schienen.size() == 0);
		for (@NotNull GostBlockungSchiene schiene : schienen) {
			mapSchienen.put(schiene.id, schiene);
			if (doAdd) {
				@NotNull GostBlockungsergebnisSchiene zuordnungen = new GostBlockungsergebnisSchiene();
				zuordnungen.id = schiene.id;
				zuordnungen.name = schiene.bezeichnung;
				zuordnungen.anzahlKollisionen = 0;
				this.ergebnis.schienen.add(zuordnungen);
			}
		}
		for (@NotNull GostBlockungsergebnisSchiene zuordnungen : this.ergebnis.schienen)
			mapSchienenKursZuordnungen.put(zuordnungen.id, zuordnungen);
	}
	

	/**
	 * Ermittelt den Schüler für die angegebene ID. Erzeugt eine NullPointerException
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Schülers
	 * 
	 * @return die Daten zu dem Schüler der Blockung
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull Schueler getSchueler(long id) throws NullPointerException {
		Schueler schueler = mapSchueler.get(id);
		if (schueler == null)
			throw new NullPointerException("ID des Schülers ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");		
		return schueler;
	}
	

	/**
	 * Ermittelt den Kurs für die angegebene ID. Erzeugt eine NullPointerException
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Daten zu dem Kurs der Blockung
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull GostBlockungKurs getKurs(long id) throws NullPointerException {
		GostBlockungKurs kurs = mapKurse.get(id);
		if (kurs == null)
			throw new NullPointerException("ID des Kurses ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");		
		return kurs;
	}
	

	/**
	 * Ermittelt das Fach für die angegebene ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Faches
	 * 
	 * @return die Daten zu dem Fach
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull GostFach getFach(long id) throws NullPointerException {
		GostFach fach = mapFaecher.get(id);
		if (fach == null)
			throw new NullPointerException("ID des Faches ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return fach;
	}
	
	
	/**
	 * Ermittelt die Schiene für die angegebene ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die Daten zu der Schiene
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull GostBlockungSchiene getSchiene(long id) throws NullPointerException {
		GostBlockungSchiene schiene = mapSchienen.get(id);
		if (schiene == null)
			throw new NullPointerException("ID der Schiene ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return schiene;
	}
	

	/**
	 * Gibt die Gesamtzahl der Kollisionen aggregiert über alle Schienen zurück.
	 *  
	 * @return die Anzahl der Kollisionen
	 */
	public int getAnzahlKollisionen() {
		int anzahlKollisionen = 0;
		for (@NotNull GostBlockungsergebnisSchiene schiene : mapSchienenKursZuordnungen.values())
			anzahlKollisionen += schiene.anzahlKollisionen;
		return anzahlKollisionen;
	}
	

	/**
	 * Gibt die Anzahl der Kollisionen in einer Schiene mit der übergebenen ID zurück.
	 *  
	 * @param id   die ID der Schiene
	 * 
	 * @return die Anzahl der Kollisionen in der Schiene
	 */
	public int getAnzahlKollisionenSchiene(long id) {
		GostBlockungsergebnisSchiene schiene = mapSchienenKursZuordnungen.get(id);
		if (schiene == null)
			return 0;
		return schiene.anzahlKollisionen;
	}

	
	/**
	 * Gibt die Anzahl der Schüler in einer Schiene mit der übergebenen ID zurück.
	 *  
	 * @param id   die ID der Schiene
	 * 
	 * @return die Anzahl der Schüler in der Schiene
	 */
	public int getAnzahlSchuelerSchiene(long id) {
		GostBlockungsergebnisSchiene schiene = mapSchienenKursZuordnungen.get(id);
		if (schiene == null)
			return 0;
		int result = 0;
		for (@NotNull GostBlockungsergebnisKurs kurs : schiene.kurse)
			result += kurs.schueler.size();
		return result;
	}
	
	
	/**
	 * Ermittelt die Schienen-Kurs-Zuordnungen für die Schiene mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die Schienen-Kurs-Zuordnungen zu der Schiene
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchienenKursZuordnung(long id) throws NullPointerException {
		GostBlockungsergebnisSchiene zuordnungen = mapSchienenKursZuordnungen.get(id);
		if (zuordnungen == null)
			throw new NullPointerException("ID der Schiene ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return zuordnungen;
	}
	
	
	/**
	 * Ermittelt die Schienen-Zuordnung des Kurses mit der angegebenen ID. Liegt keine Zuordnung vor,
	 * so wird null zurückgegeben.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Kurs-Schienen-Zuordnungen der Schiene, zu welcher der Kurs zugeordnet ist
	 */
	public GostBlockungsergebnisSchiene getKursSchienenZuordnung(long id) {
		return mapKursSchienenZuordnungen.get(id);
	}


	/**
	 * Ermittelt die Kurs-Schüler-Zuordnungen für den Kurs mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Kurs-Schüler-Zuordnungen des Kurses
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull GostBlockungsergebnisKurs getKursSchuelerZuordnung(long id) throws NullPointerException {
		GostBlockungsergebnisKurs zuordnungen = mapKursSchuelerZuordnungen.get(id);
		if (zuordnungen == null)
			throw new NullPointerException("ID des Kurses ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return zuordnungen;
	}
	

	/**
	 * Ermittelt die Kurs-Schüler-Zuordnungen für die Kurse mit der angegebenen Fach-ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Faches
	 * 
	 * @return die Kurs-Schüler-Zuordnungen der Kurse mit der angegebenen Fach-ID
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull Vector<@NotNull GostBlockungsergebnisKurs> getKursSchuelerZuordnungenFuerFach(long id) throws NullPointerException {
		Vector<@NotNull GostBlockungsergebnisKurs> zuordnungen = mapKursSchuelerZuordnungenFuerFach.get(id);
		if (zuordnungen == null)
			throw new NullPointerException("ID des Faches ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return zuordnungen;
	}

	
	/**
	 * Ermittelt die Kurs-Schüler-Zuordnungen für den Schüler und das Fach mit den angegebenen IDs.
	 * 
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * 
	 * @return die Kurs-Schüler-Zuordnungen des Schülers und des Faches oder null, falls die Kombination nicht vorliegt
	 */
	public GostBlockungsergebnisKurs getKursSchuelerZuordnungFuerSchuelerUndFach(long idSchueler, long idFach) {
		HashMap<@NotNull Long, @NotNull GostBlockungsergebnisKurs> sfk = mapSchuelerFachKursZuordnungen.get(idSchueler);
		if (sfk == null)
			return null;
		return sfk.get(idFach);
	}
	

	/**
	 * Ermittelt die Schüler-Kurs-Zuordnungen für den Schüler mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Schülers
	 * 
	 * @return die Schüler-Kurs-Zuordnungen des Schülers
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getSchuelerKursZuordnung(long id) throws NullPointerException {
		HashSet<@NotNull GostBlockungsergebnisKurs> zuordnungen = mapSchuelerKursZuordnungen.get(id);
		if (zuordnungen == null)
			throw new NullPointerException("ID des Schülers ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return zuordnungen;
	}
	

	/**
	 * Ermittelt die Kurs-Schülermenge für den Kurs mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Kurs-Schülermenge des Kurses
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public @NotNull HashSet<@NotNull Long> getKursSchuelermenge(long id) throws NullPointerException {
		HashSet<@NotNull Long> zuordnungen = mapKursSchuelermenge.get(id);
		if (zuordnungen == null)
			throw new NullPointerException("ID des Kurses ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.");
		return zuordnungen;
	}
	
	/**
	 * Liefert das Blockungsergebnis zurück.
	 * 
	 * @return das Blockungsergebnis
	 */
	public @NotNull GostBlockungsergebnis getErgebnis() {
		return ergebnis;
	}


	
	/**
	 * Aktualisiert für alle Schienen-Kurs-Zuordnungen die Anzahl der Kollisionen
	 * und die Kollisionseinträge bei den Schülern
	 */
	private void update() {
		for (@NotNull GostBlockungsergebnisSchiene schienenKursZuordnung : ergebnis.schienen)
			updateSchiene(schienenKursZuordnung);
	}


	/**
	 * Aktualisiert für die übergebene Schienen-Kurs-Zuordnung die Anzahl der Kollisionen
	 * und die Kollisionseinträge bei den Schülern
	 * 
	 * @param schienenKursZuordnung   die Schienen-Kurs-Zuordnung
	 */
	private void updateSchiene(@NotNull GostBlockungsergebnisSchiene schienenKursZuordnung) {
		schienenKursZuordnung.anzahlKollisionen = 0;
		if (schienenKursZuordnung.kurse.size() < 1)
			return;
		for (int i = 0; i < schienenKursZuordnung.kurse.size(); i++) {
			// Bestimme die Menge der Schüler aller anderer Kurse
			@NotNull HashSet<@NotNull Long> andereSchueler = new HashSet<>();
			for (int j = 0; j < schienenKursZuordnung.kurse.size(); j++) {
				if (i == j)
					continue;
				@NotNull GostBlockungsergebnisKurs andererKurs = schienenKursZuordnung.kurse.get(j);
				andereSchueler.addAll(getKursSchuelermenge(andererKurs.id));
			}
			
			// Prüfe auf Kollision mit diesem Kurs
			@NotNull GostBlockungsergebnisKurs kurs = schienenKursZuordnung.kurse.get(i);
			@NotNull HashSet<@NotNull Long> dieseSchueler = getKursSchuelermenge(kurs.id);
			@NotNull HashSet<@NotNull Long> kollisionenSchueler = new HashSet<>();
			kollisionenSchueler.addAll(dieseSchueler);
			kollisionenSchueler.retainAll(andereSchueler);
			kurs.anzahlKollisionen = kollisionenSchueler.size();
			schienenKursZuordnung.anzahlKollisionen += kurs.anzahlKollisionen;
			
			// Setze die Kollisionsinformationen bei den Schülern
			for (@NotNull GostBlockungsergebnisSchuelerzuordnung schueler : kurs.schueler)
				schueler.hatKollisionen = kollisionenSchueler.contains(schueler.id);
		}
	}
	
	
	/**
	 * Ordnet einen Kurs einer Schiene zu. Ist die ID der Schiene null,
	 * so wird eine vorherige Zuordnung aufgehoben.
	 *  
	 * @param idKurs      die ID des Kurses
	 * @param idSchiene   die ID der Schiene
	 *  
	 * @return true, falls der Kurs der Schiene zugeordnet wurde 
	 */
	public boolean assignKursSchiene(long idKurs, Long idSchiene) {
		GostBlockungsergebnisSchiene kursSchienenZuordnung = this.getKursSchienenZuordnung(idKurs);
		// Prüfe, ob sich überhaupt etwas verändert...
		if ((kursSchienenZuordnung != null) && (kursSchienenZuordnung.id == idSchiene))
			return false;

		@NotNull GostBlockungsergebnisKurs kursSchuelerZuordnung = getKursSchuelerZuordnung(idKurs);
		
		// Entferne ggf. eine alte Zuordnung
		if (kursSchienenZuordnung != null) {
			kursSchienenZuordnung.kurse.remove(kursSchuelerZuordnung);
			if (kursSchuelerZuordnung.anzahlKollisionen > 0) {
				updateSchiene(kursSchienenZuordnung);
			}
		}
		
		// Setze die neue Schiene
		kursSchuelerZuordnung.schienenID = idSchiene;
		if (idSchiene != null) {
			@NotNull GostBlockungsergebnisSchiene schienenKursZuordnung = this.getSchienenKursZuordnung(idSchiene);
			schienenKursZuordnung.kurse.add(kursSchuelerZuordnung);
			updateSchiene(schienenKursZuordnung);
		}
		return true;
	}
	
	
	/**
	 * Ordnet einen Schüler einem Kurs zu.
	 * 
	 * @param idSchueler   die ID des Schüler
	 * @param idKurs       die ID des Kurses
	 * @param undo         normally false, but true if the assignment should be removed
	 * 
	 * @return true, falls der Schüler dem Kurs zugeordnet wurde 
	 */
	public boolean assignSchuelerKurs(long idSchueler, long idKurs, boolean undo) {
		@NotNull Schueler schueler = getSchueler(idSchueler);
		@NotNull GostBlockungsergebnisKurs kursSchuelerZuordnung = getKursSchuelerZuordnung(idKurs);
		@NotNull HashSet<Long> schuelerMenge = getKursSchuelermenge(idKurs);
		boolean isAssigned = schuelerMenge.contains(idSchueler);
		if (undo) {
			// Entfernen
			if (!isAssigned)
				return false; // keine Änderung nötig
			schuelerMenge.remove(idSchueler);
			for (int i = 0; i < kursSchuelerZuordnung.schueler.size(); i++) {
				@NotNull GostBlockungsergebnisSchuelerzuordnung zuordnung = kursSchuelerZuordnung.schueler.get(i);
				if (zuordnung.id == idSchueler) {
					kursSchuelerZuordnung.schueler.remove(i);
					break;
				}
			}
			@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> schuelerKursZuordnungen = getSchuelerKursZuordnung(idSchueler);
			schuelerKursZuordnungen.remove(kursSchuelerZuordnung);
		} else {
			// Hinzufügen
			if (isAssigned)
				return false; // keine Änderung nötig
			schuelerMenge.add(idSchueler);
			@NotNull GostBlockungsergebnisSchuelerzuordnung zuordnung = new GostBlockungsergebnisSchuelerzuordnung();
			zuordnung.id = schueler.id;
			zuordnung.name = schueler.nachname + ", " + schueler.vorname;
			kursSchuelerZuordnung.schueler.add(zuordnung);
			@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> schuelerKursZuordnungen = getSchuelerKursZuordnung(idSchueler);
			schuelerKursZuordnungen.add(kursSchuelerZuordnung);
		}
		if (kursSchuelerZuordnung.schienenID != null) {
			long idSchiene = kursSchuelerZuordnung.schienenID;
			updateSchiene(this.getSchienenKursZuordnung(idSchiene));
		}
		return true;
	}

}
