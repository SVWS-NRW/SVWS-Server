package de.nrw.schule.svws.core.utils.gost;

import java.util.Comparator;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class GostBlockungsdatenManager {

	/** Ein Comparator für Schienen der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungSchiene> compSchiene = (@NotNull GostBlockungSchiene a, @NotNull GostBlockungSchiene b) -> {
		return Integer.compare(a.nummer, b.nummer); 
	};

	/** Ein Comparator für Regeln der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungRegel> compRegel = (@NotNull GostBlockungRegel a, @NotNull GostBlockungRegel b) -> {
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
	private final @NotNull HashMap<Long, GostBlockungKurs> _mapKurse = new HashMap<>();
	
	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID */
	private final @NotNull HashMap<Long, GostBlockungSchiene> _mapSchienen = new HashMap<>();
	
	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID */
	private final @NotNull HashMap<Long, GostBlockungRegel> _mapRegeln = new HashMap<>();
	
	/** Das Halbjahr, für welches die Blockung angelegt wurde. */
	private @NotNull GostHalbjahr _halbjahr;


	/**
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem
	 * leeren Fächer-Manager.
	 */
	public GostBlockungsdatenManager() {
		_daten = new GostBlockungsdaten();
		_faecherManager = new GostFaecherManager();
		_halbjahr = GostHalbjahr.EF1;
	}


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und
	 * dem Fächer-Manager
	 *  
	 * @param daten            die Blockungsdaten
	 * @param faecherManager   der Fächer-Manager
	 */
	public GostBlockungsdatenManager(@NotNull GostBlockungsdaten daten, @NotNull GostFaecherManager faecherManager) {
		_daten = daten;
		_faecherManager = faecherManager;
		GostHalbjahr halbjahr = GostHalbjahr.fromID(daten.gostHalbjahr);
		if (halbjahr == null)
			throw new NullPointerException("Blockungsdaten müssen einem Halbjahr der gymnasialen Oberstufe zugeordnet sein.");
		_halbjahr = halbjahr;
		// Füge alle Schienen der Map hinzu
		for (@NotNull GostBlockungSchiene schiene : daten.schienen)
			_mapSchienen.put(schiene.id, schiene);
		// Füge alle Kurse der Map hinzu
		for (@NotNull GostBlockungKurs kurs : daten.kurse)
			_mapKurse.put(kurs.id, kurs);
		// Füge alle Regeln der Map hinzu
		for (@NotNull GostBlockungRegel regel : daten.regeln)
			_mapRegeln.put(regel.id, regel);
	}


	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 * 
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public @NotNull GostFaecherManager faecherManager() {
		return this._faecherManager;
	}


	/**
	 * Gibt die Blockungsdaten zurück.
	 * 
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public @NotNull GostBlockungsdaten daten() {
		return this._daten;
	}


	/**
	 * Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die 
	 * Blockung angelegt wurde.
	 * 
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public @NotNull GostHalbjahr getHalbjahr() {
		return _halbjahr;
	}


	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die 
	 * Blockung angelegt wurde.
	 * 
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	public void setHalbjahr(@NotNull GostHalbjahr halbjahr) {
		_halbjahr = halbjahr;
		_daten.gostHalbjahr = halbjahr.id;
	}


	/**
	 * Gibt die ID der Blockung zurück.
	 * 
	 * @return die ID der Blockung
	 */
	public long getID() {
		return _daten.id;
	}


	/**
	 * Setzt die ID der Blockung
	 * 
	 * @param id   die ID, welche der Blockung zugewiesen wird.
	 */
	public void setID(long id) {
		if (id < 0)
			throw new IllegalArgumentException("Die Blockungs-ID muss positiv sein und ist daher ungültig.");
		_daten.id = id;
	}


	/**
	 * Gibt den Namen der Blockung zurück.
	 * 
	 * @return der Name der Blockung
	 */
	public @NotNull String getName() {
		return _daten.name;
	}


	/**
	 * Setzt den Namen der Blockung
	 * 
	 * @param name   der Name, welcher der Blockung zugewiesen wird.
	 */
	public void setName(@NotNull String name) {
		if ("".equals(name))
			throw new IllegalArgumentException("Ein leerer Name ist für die Blockung nicht zulässig.");
		_daten.name = name;
	}

	// TODO Getter und Setter für das aktivierte Zwischenergebnis...

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu
	 * 
	 * @param kurs   der hinzuzufügende Kurs
	 */
	public void addKurs(@NotNull GostBlockungKurs kurs) {
		@NotNull Comparator<@NotNull GostBlockungKurs> comp = (@NotNull GostBlockungKurs a, @NotNull GostBlockungKurs b) -> {
			GostFach aFach = _faecherManager.get(a.fach_id);
			GostFach bFach = _faecherManager.get(b.fach_id);
			if (aFach == null)
				return (bFach == null) ? 0 : -1;
			if (bFach == null)
				return 1;
			int result = Integer.compare(aFach.sortierung, bFach.sortierung);
			if (result != 0)
				return result;
			result = Integer.compare(a.kursart, b.kursart);
			if (result != 0)
				return result;
			return Integer.compare(a.nummer, b.nummer); 
		};
		_daten.kurse.add(kurs);
		_daten.kurse.sort(comp);
		_mapKurse.put(kurs.id, kurs);
	}


	/**
	 * Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return der Kurs
	 * 
	 * @throws NullPointerException falls der Kurs nicht in der Blockung existiert 
	 */
	public @NotNull GostBlockungKurs getKurs(long id) throws NullPointerException {
		GostBlockungKurs kurs = _mapKurse.get(id);
		if (kurs == null)
			throw new NullPointerException("Ein Kurs mit der angegebenen ID existiert nicht in der Blockung.");
		return kurs;
	}


	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung
	 * 
	 * @param id   die ID des zu entfernenden Kurses
	 */
	public void removeKursByID(long id) {
		@NotNull GostBlockungKurs kurs = this.getKurs(id);
		_daten.kurse.remove(kurs);
		_mapKurse.remove(id);
	}


	/**
	 * Entfernt den übergebenen Kurs aus der Blockung
	 * 
	 * @param kurs   der zu entfernende Kurs
	 */
	public void removeKurs(@NotNull GostBlockungKurs kurs) {
		removeKursByID(kurs.id);
	}


	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu
	 * 
	 * @param schiene   die hinzuzufügende Schiene
	 */
	public void addSchiene(@NotNull GostBlockungSchiene schiene) {
		_daten.schienen.add(schiene);
		_daten.schienen.sort(compSchiene);
		_mapSchienen.put(schiene.id, schiene);
	}


	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die Schiene
	 * 
	 * @throws NullPointerException falls die Schiene nicht in der Blockung existiert 
	 */
	public @NotNull GostBlockungSchiene getSchiene(long id) throws NullPointerException {
		GostBlockungSchiene schiene = _mapSchienen.get(id);
		if (schiene == null)
			throw new NullPointerException("Eine Schiene mit der angegebenen ID existiert nicht in der Blockung.");
		return schiene;
	}


	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung
	 * 
	 * @param id   die ID der zu entfernenden Schiene
	 */
	public void removeSchieneByID(long id) {
		@NotNull GostBlockungSchiene schiene = this.getSchiene(id);
		_daten.schienen.remove(schiene);
		_mapSchienen.remove(id);
	}


	/**
	 * Entfernt die übergebene Schiene aus der Blockung
	 * 
	 * @param schiene   die zu entfernende Schiene
	 */
	public void removeSchiene(@NotNull GostBlockungSchiene schiene) {
		removeSchieneByID(schiene.id);
	}


	/**
	 * Gibt die Default-Anzahl von Schienen zurück, die für die eine neue
	 * Blockung verwendet wird.
	 * 
	 * @param halbjahr   das Halbjahr, für welches die Blockung angelegt werden soll 
	 * 
	 * @return die Anzahl an Schienen für eine Vorauswahl 
	 */
	public static int getDefaultSchienenAnzahl(@NotNull GostHalbjahr halbjahr) {
		return (halbjahr.id < 2) ? 13 : 11;
	}


	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu
	 * 
	 * @param regel   die hinzuzufügende Regel
	 */
	public void addRegel(@NotNull GostBlockungRegel regel) {
		_daten.regeln.add(regel);
		_daten.regeln.sort(compRegel);
		_mapRegeln.put(regel.id, regel);
	}


	/**
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 * 
	 * @param id   die ID der Regel
	 * 
	 * @return die Regel
	 * 
	 * @throws NullPointerException falls die Regel nicht in der Blockung existiert 
	 */
	public @NotNull GostBlockungRegel getRegel(long id) throws NullPointerException {
		GostBlockungRegel regel = _mapRegeln.get(id);
		if (regel == null)
			throw new NullPointerException("Eine Regel mit der angegebenen ID existiert nicht in der Blockung.");
		return regel;
	}


	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung
	 * 
	 * @param id   die ID der zu entfernenden Regel
	 */
	public void removeRegelByID(long id) {
		@NotNull GostBlockungRegel regel = this.getRegel(id);
		_daten.regeln.remove(regel);
		_mapRegeln.remove(id);
	}


	/**
	 * Entfernt die übergebene Regel aus der Blockung
	 * 
	 * @param regel   die zu entfernende Regel
	 */
	public void removeRegel(@NotNull GostBlockungRegel regel) {
		removeRegelByID(regel.id);
	}

}
