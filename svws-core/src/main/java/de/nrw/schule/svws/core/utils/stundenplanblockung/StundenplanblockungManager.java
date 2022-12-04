package de.nrw.schule.svws.core.utils.stundenplanblockung;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungFach;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungInput;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungKlasse;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungKopplung;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungLehrkraft;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungLerngruppe;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungRaum;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Manipulation des {@link StundenplanblockungInput}-Objektes. <br>
 * Nur Methoden, die mit "state" beginnen verändern den Zustand der Daten. <br>
 * 
 * @author Benjamin A. Bartsch 
 */
public class StundenplanblockungManager {

	/** Die Eingabedaten die dieser Manager manipuliert. */
	private final @NotNull StundenplanblockungInput _daten;
	
	// ############################################################
	// ################## Attribute: Lehrkräfte ###################
	// ############################################################

	/** Lehrkraft-ID --> Lehrkraft-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungLehrkraft> _map_lehrkraftID_lehrkraft = new HashMap<>();

	/** Lehrkraft-ID --> Lerngruppe-ID --> Lerngruppe. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungLerngruppe>> _map_lehrkraftID_lerngruppeID_lerngruppe = new HashMap<>();

	/** Ein Comparator zum Sortieren der Lehrkräfte nach dem Kürzel. */
	private static final @NotNull Comparator<@NotNull StundenplanblockungLehrkraft> _comp_lehrkraft_kuerzel = (
			@NotNull StundenplanblockungLehrkraft a, @NotNull StundenplanblockungLehrkraft b) -> {
		// Sortierung nach Kürzel.
		int cmpKuerzel = a.kuerzel.compareTo(b.kuerzel);
		if (cmpKuerzel != 0)
			return cmpKuerzel;

		// Sortierung nach id.
		return Long.compare(a.id, b.id);
	};

	// ############################################################
	// ################### Attribute: Klassen #####################
	// ############################################################

	/** Klasse-ID --> Klasse-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungKlasse> _map_klasseID_klasse = new HashMap<>();

	/** Ein Comparator zum Sortieren der Klassen nach dem Kürzel. */
	private static final @NotNull Comparator<@NotNull StundenplanblockungKlasse> _comp_klasse_kuerzel = (
			@NotNull StundenplanblockungKlasse a, @NotNull StundenplanblockungKlasse b) -> {
		// Entferne führende Nullen.
		String kuerzelA = a.kuerzel.trim();
		String kuerzelB = b.kuerzel.trim();
		while (kuerzelA.startsWith("0"))
			kuerzelA = kuerzelA.substring(1);
		while (kuerzelB.startsWith("0"))
			kuerzelB = kuerzelB.substring(1);

		// Sortierung nach dem Kürzel.
		int cmpKuerzel = kuerzelA.compareTo(kuerzelB);
		if (cmpKuerzel != 0)
			return cmpKuerzel;

		// Sortierung nach der ID.
		return Long.compare(a.id, b.id);
	};

	// ############################################################
	// ################### Attribute: Fächer ######################
	// ############################################################

	/** Fach-ID --> Fach-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungFach> _map_id_fach = new HashMap<>();

	/** Ein Comparator zum Sortieren der Fächer nach dem Kürzel. */
	private static final @NotNull Comparator<@NotNull StundenplanblockungFach> _comp_fach_sortiernummer = (
			@NotNull StundenplanblockungFach a, @NotNull StundenplanblockungFach b) -> {
		// Sortierung nach der Sortierungsnummer.
		int cmpSortiernummer = Integer.compare(a.sortierung, b.sortierung);
		if (cmpSortiernummer != 0)
			return cmpSortiernummer;

		// Sortierung nach dem Kürzel.
		int cmpKuerzel = a.kuerzel.compareTo(b.kuerzel);
		if (cmpKuerzel != 0)
			return cmpKuerzel;

		// Sortierung nach der ID.
		return Long.compare(a.id, b.id);
	};
	
	// ############################################################
	// #################### Attribute: Räume ######################
	// ############################################################

	/** Raum-ID --> Raum-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungRaum> _map_id_raum = new HashMap<>();

	/** Ein Comparator zum Sortieren der Räume nach dem Kürzel. */
	private static final @NotNull Comparator<@NotNull StundenplanblockungRaum> _comp_raum_kuerzel = (
			@NotNull StundenplanblockungRaum a, @NotNull StundenplanblockungRaum b) -> {
		// Sortierung nach dem Kürzel.
		int cmpKuerzel = a.kuerzel.compareTo(b.kuerzel);
		if (cmpKuerzel != 0)
			return cmpKuerzel;

		// Sortierung nach der ID.
		return Long.compare(a.id, b.id);
	};

	// ############################################################
	// ################# Attribute: Kopplungen ####################
	// ############################################################
	
	/** Kopplung-ID --> Kopplung-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungKopplung> _map_id_kopplung = new HashMap<>();

	/** Ein Comparator zum Sortieren der Kopplungen nach dem Kürzel. */
	private static final @NotNull Comparator<@NotNull StundenplanblockungKopplung> _comp_kopplung_kuerzel = (
			@NotNull StundenplanblockungKopplung a, @NotNull StundenplanblockungKopplung b) -> {
		// Sortierung nach dem Kürzel.
		int cmpKuerzel = a.kuerzel.compareTo(b.kuerzel);
		if (cmpKuerzel != 0)
			return cmpKuerzel;

		// Sortierung nach der ID.
		return Long.compare(a.id, b.id);
	};
	
	// ############################################################
	// ################ Attribute: Lerngruppen ####################
	// ############################################################
	
	/** Lerngruppe-ID --> Kopplung-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungLerngruppe> _map_id_lerngruppe = new HashMap<>();

	/** Ein Comparator zum Sortieren der Lerngruppen nach ihrer ID. */
	private static final @NotNull Comparator<@NotNull StundenplanblockungLerngruppe> _comp_lerngruppe_id = (
			@NotNull StundenplanblockungLerngruppe a, @NotNull StundenplanblockungLerngruppe b) -> {
		// Sortierung nach der ID.
		return Long.compare(a.id, b.id);
	};
	
	/**
	 * Erzeugt einen neuen Manager mit einem leeren {@link StundenplanblockungInput}-Objekt.
	 */
	public StundenplanblockungManager() {
		_daten = new StundenplanblockungInput();
	}
	
	// ############################################################
	// ######################## Lehrkräfte ########################
	// ############################################################
	
	private void lehrkraftAddOhneSortierung(@NotNull StundenplanblockungLehrkraft pLehrkraft) throws NullPointerException {
		// Existiert die Lehrkraft bereits?
		if (_map_lehrkraftID_lehrkraft.containsKey(pLehrkraft.id))
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraft.id + " existiert bereits!");
		// Lehrkraft hinzufügen.
		_map_lehrkraftID_lehrkraft.put(pLehrkraft.id, pLehrkraft);
		_map_lehrkraftID_lerngruppeID_lerngruppe.put(pLehrkraft.id, new HashMap<>());
		_daten.lehrkraefte.add(pLehrkraft);
	}

	/**
	 * Fügt die Lehrkraft hinzu. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID bereits existiert.
	 * 
	 * @param pLehrkraft             Das Lehrkraft-Objekt.
	 * @throws NullPointerException  Falls die Lehrkraft-ID bereits existiert.
	 */
	public void lehrkraftAdd(@NotNull StundenplanblockungLehrkraft pLehrkraft) throws NullPointerException {
		lehrkraftAddOhneSortierung(pLehrkraft);
		_daten.lehrkraefte.sort(_comp_lehrkraft_kuerzel);
	}

	/**
	 * Fügt alle Lehrkräfte hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Lehrkräfte bereits existiert. 
	 * 
	 * @param pLehrkraefte           Die Menge der Lehrkräfte, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Lehrkräfte bereits existiert.
	 */
	public void lehrkraftAddAll(@NotNull List<@NotNull StundenplanblockungLehrkraft> pLehrkraefte) throws NullPointerException {
		for (@NotNull StundenplanblockungLehrkraft lehrkraft : pLehrkraefte)
			lehrkraftAddOhneSortierung(lehrkraft);
		_daten.lehrkraefte.sort(_comp_lehrkraft_kuerzel);
	}
	
	/**
	 * Liefert TRUE, falls die Lehrkraft-ID existiert. 
	 * 
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @return              TRUE, falls die Lehrkraft-ID existiert.
	 */
	public boolean lehrkraftExists(long pLehrkraftID) {
		return _map_lehrkraftID_lehrkraft.containsKey(pLehrkraftID);
	}

	/**
	 * Liefert das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @return                       Das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungLehrkraft lehrkraftGet(long pLehrkraftID) throws NullPointerException {
		StundenplanblockungLehrkraft lehrkraft = _map_lehrkraftID_lehrkraft.get(pLehrkraftID);
		if (lehrkraft == null)
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " unbekannt!");
		return lehrkraft;
	}

	// ############################################################
	// ###################### Klassen/Stufen ######################
	// ############################################################
	

	/**
	 * Liefert eine Map der Lerngruppen der Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraft             Das {@link StundenplanblockungLehrkraft}-Objekt.
	 * @return                       Eine Map der Lerngruppen der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungLerngruppe> lehrkraftGetMapLerngruppen(@NotNull StundenplanblockungLehrkraft pLehrkraft) throws NullPointerException {
		HashMap<@NotNull Long, @NotNull StundenplanblockungLerngruppe> map = _map_lehrkraftID_lerngruppeID_lerngruppe.get(pLehrkraft.id);
		if (map == null)
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraft.id + " unbekannt!");
		return map;
	}

	/**
	 * Liefert die Menge aller Lehrkräfte sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Lehrkräfte sortiert nach dem Kürzel.
	 */
	public Vector<StundenplanblockungLehrkraft> lehrkraftGetMengeSortiertNachKuerzel() {
		return _daten.lehrkraefte;
	}

	// ############################################################
	// ###################### Klassen/Stufen ######################
	// ############################################################
	
	
	/**
	 * Löscht die übergebene Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public void lehrkraftRemove(long pLehrkraftID) throws NullPointerException {
		// Lehrkraft holen.
		@NotNull StundenplanblockungLehrkraft lehrkraft = lehrkraftGet(pLehrkraftID);
		// Lehrkraft löschen.
		_map_lehrkraftID_lehrkraft.remove(pLehrkraftID);
		_daten.lehrkraefte.remove(lehrkraft);
	}

	/**
	 * Ändert das Kürzel der Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel               Das neue Kürzel der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public void lehrkraftSetKuerzel(long pLehrkraftID, @NotNull String pKuerzel) throws NullPointerException {
		@NotNull StundenplanblockungLehrkraft lehrkraft = lehrkraftGet(pLehrkraftID);
		lehrkraft.kuerzel = pKuerzel;
		_daten.lehrkraefte.sort(_comp_lehrkraft_kuerzel);
	}

	/**
	 * Ändert, ob die Lehrkraft prinzipiell vertreten dürfte. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pDarfVertreten         TRUE, falls die Lehrkraft prinzipiell vertreten dürfte.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public void lehrkraftSetDarfVertreten(long pLehrkraftID, boolean pDarfVertreten) throws NullPointerException {
		@NotNull StundenplanblockungLehrkraft lehrkraft = lehrkraftGet(pLehrkraftID);
		lehrkraft.darfVertreten = pDarfVertreten;
	}

	// ############################################################
	// ###################### Klassen/Stufen ######################
	// ############################################################

	
	private void klasseAddOhneSortierung(@NotNull StundenplanblockungKlasse pKlasse) throws NullPointerException {
		// Existiert die Klasse bereits?
		if (_map_klasseID_klasse.containsKey(pKlasse.id))
			throw new NullPointerException("Klasse-ID " + pKlasse.id + " existiert bereits!");
		// Klasse hinzufügen.
		_map_klasseID_klasse.put(pKlasse.id, pKlasse);
		_daten.klassen.add(pKlasse);
	}
	
	/**
	 * Fügt die Klasse hinzu. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 * 
	 * @param pKlasse                Das Klassen-Objekt.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public void klasseAdd(@NotNull StundenplanblockungKlasse pKlasse) throws NullPointerException {
		klasseAddOhneSortierung(pKlasse);
		_daten.klassen.sort(_comp_klasse_kuerzel);
	}
	
	/**
	 * Fügt alle Klassen hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Klassen bereits existiert. 
	 * 
	 * @param pKlassen               Die Menge der Klassen, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Klassen bereits existiert.
	 */
	public void klasseAddAll(@NotNull List<@NotNull StundenplanblockungKlasse> pKlassen) throws NullPointerException {
		for (@NotNull StundenplanblockungKlasse klasse : pKlassen)
			klasseAddOhneSortierung(klasse);
		_daten.klassen.sort(_comp_klasse_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Klasse-ID existiert. 
	 * 
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @return           TRUE, falls die Klasse-ID existiert.
	 */
	public boolean klasseExists(long pKlasseID) {
		return _map_klasseID_klasse.containsKey(pKlasseID);
	}

	/**
	 * Liefert das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @return                       Das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungKlasse klasseGet(long pKlasseID) throws NullPointerException {
		StundenplanblockungKlasse klasse = _map_klasseID_klasse.get(pKlasseID);
		if (klasse == null)
			throw new NullPointerException("Klasse-ID " + pKlasseID + " unbekannt!");
		return klasse;
	}

	/**
	 * Liefert die Menge aller Klassen sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Klassen sortiert nach dem Kürzel.
	 */
	public Vector<StundenplanblockungKlasse> klasseGetMengeSortiertNachKuerzel() {
		return _daten.klassen;
	}

	/**
	 * Löscht die übergebene Klasse. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public void klasseRemove(long pKlasseID) throws NullPointerException {
		// Klasse holen.
		@NotNull StundenplanblockungKlasse klasse = klasseGet(pKlasseID);
		// Klasse löschen.
		_map_klasseID_klasse.remove(pKlasseID);
		_daten.klassen.remove(klasse);
	}

	/**
	 * Ändert das Kürzel der Klasse. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @param pKuerzel               Das neue Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public void klasseSetKuerzel(long pKlasseID, @NotNull String pKuerzel) throws NullPointerException {
		@NotNull StundenplanblockungKlasse klasse = klasseGet(pKlasseID);
		klasse.kuerzel = pKuerzel;
		_daten.klassen.sort(_comp_klasse_kuerzel);
	}

	// ############################################################
	// ########################## Fächer ##########################
	// ############################################################

	
	private void fachAddOhneSortierung(@NotNull StundenplanblockungFach pFach) throws NullPointerException {
		// Existiert das Fach bereits?
		if (_map_id_fach.containsKey(pFach.id))
			throw new NullPointerException("Fach-ID " + pFach.id + " existiert bereits!");
		// Fach hinzufügen.
		_map_id_fach.put(pFach.id, pFach);
		_daten.faecher.add(pFach);
	}
	
	/**
	 * Fügt das Fach hinzu. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID bereits existiert.
	 * 
	 * @param pFach                  Das Fach-Objekt.
	 * @throws NullPointerException  Falls die Fach-ID bereits existiert.
	 */
	public void fachAdd(@NotNull StundenplanblockungFach pFach) throws NullPointerException {
		fachAddOhneSortierung(pFach);
		_daten.faecher.sort(_comp_fach_sortiernummer);
	}
	
	/**
	 * Fügt alle Fächer hinzu. <br>
	 * Wirft eine NullPointerException, falls eines der Fächer bereits existiert. 
	 * 
	 * @param pFaecher               Die Menge der Fächer, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eines der Fächer bereits existiert.
	 */
	public void fachAddAll(@NotNull List<@NotNull StundenplanblockungFach> pFaecher) throws NullPointerException {
		for (@NotNull StundenplanblockungFach fach : pFaecher)
			fachAddOhneSortierung(fach);
		_daten.faecher.sort(_comp_fach_sortiernummer);
	}
	
	/**
	 * Liefert TRUE, falls die Fach-ID existiert. 
	 * 
	 * @param pFachID  Die Datenbank-ID des Faches.
	 * @return         TRUE, falls die Fach-ID existiert.
	 */
	public boolean fachExists(long pFachID) {
		return _map_id_fach.containsKey(pFachID);
	}

	/**
	 * Liefert das {@link StundenplanblockungFach}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Das {@link StundenplanblockungFach}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungFach fachGet(long pFachID) throws NullPointerException {
		StundenplanblockungFach fach = _map_id_fach.get(pFachID);
		if (fach == null)
			throw new NullPointerException("Fach-ID " + pFachID + " unbekannt!");
		return fach;
	}

	/**
	 * Liefert die Menge aller Fächer sortiert nach der Sortiernummer.
	 * 
	 * @return Die Menge aller Fächer sortiert nach der Sortiernummer.
	 */
	public Vector<StundenplanblockungFach> fachGetMengeSortiertNachSortiernummer() {
		return _daten.faecher;
	}

	/**
	 * Löscht das übergebene Fach. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public void fachRemove(long pFachID) throws NullPointerException {
		// Fach holen.
		@NotNull StundenplanblockungFach fach = fachGet(pFachID);
		// Fach löschen.
		_map_id_fach.remove(pFachID);
		_daten.faecher.remove(fach);
	}

	/**
	 * Ändert das Kürzel des Faches. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @param pKuerzel               Das neue Kürzel des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public void fachSetKuerzel(long pFachID, @NotNull String pKuerzel) throws NullPointerException {
		@NotNull StundenplanblockungFach fach = fachGet(pFachID);
		fach.kuerzel = pKuerzel;
		_daten.faecher.sort(_comp_fach_sortiernummer);
	}

	/**
	 * Ändert die Sortiernummer des Faches. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @param pSortiernummer         Die neue Sortiernummer des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public void fachSetSortiernummer(long pFachID, int pSortiernummer) throws NullPointerException {
		@NotNull StundenplanblockungFach fach = fachGet(pFachID);
		fach.sortierung = pSortiernummer;
		_daten.faecher.sort(_comp_fach_sortiernummer);
	}

	// ############################################################
	// ########################## Räume ###########################
	// ############################################################

	private void raumAddOhneSortierung(@NotNull StundenplanblockungRaum pRaum) throws NullPointerException {
		// Existiert der Raum bereits?
		if (_map_id_raum.containsKey(pRaum.id))
			throw new NullPointerException("Raum-ID " + pRaum.id + " existiert bereits!");
		// Raum hinzufügen.
		_map_id_raum.put(pRaum.id, pRaum);
		_daten.raeume.add(pRaum);
	}

	/**
	 * Fügt den Raum hinzu. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID bereits existiert.
	 * 
	 * @param pRaum                  Das Raum-Objekt.
	 * @throws NullPointerException  Falls die Raum-ID bereits existiert.
	 */
	public void raumAdd(@NotNull StundenplanblockungRaum pRaum) throws NullPointerException {
		raumAddOhneSortierung(pRaum);
		_daten.raeume.sort(_comp_raum_kuerzel);
	}

	/**
	 * Fügt alle Räume hinzu. <br>
	 * Wirft eine NullPointerException, falls einer der Räume bereits existiert. 
	 * 
	 * @param pRaeume                Die Menge der Räume, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls einer der Räume bereits existiert.
	 */
	public void raumAddAll(@NotNull List<@NotNull StundenplanblockungRaum> pRaeume) throws NullPointerException {
		for (@NotNull StundenplanblockungRaum raum : pRaeume)
			raumAddOhneSortierung(raum);
		_daten.raeume.sort(_comp_raum_kuerzel);
	}
	
	/**
	 * Liefert TRUE, falls die Raum-ID existiert. 
	 * 
	 * @param pRaumID  Die Datenbank-ID des Raumes.
	 * @return         TRUE, falls die Raum-ID existiert.
	 */
	public boolean raumExists(long pRaumID) {
		return _map_id_raum.containsKey(pRaumID);
	}

	/**
	 * Liefert das {@link StundenplanblockungRaum}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 * 
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @return                       Das {@link StundenplanblockungRaum}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungRaum raumGet(long pRaumID) throws NullPointerException {
		StundenplanblockungRaum raum = _map_id_raum.get(pRaumID);
		if (raum == null)
			throw new NullPointerException("Raum-ID " + pRaumID + " unbekannt!");
		return raum;
	}

	/**
	 * Liefert die Menge aller Räume sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Räume sortiert nach dem Kürzel.
	 */
	public Vector<StundenplanblockungRaum> raumGetMengeSortiertNachKuerzel() {
		return _daten.raeume;
	}

	/**
	 * Löscht den übergebenen Raum. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 * 
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public void raumRemove(long pRaumID) throws NullPointerException {
		// Raum holen.
		@NotNull StundenplanblockungRaum raum = raumGet(pRaumID);
		// Raum löschen.
		_map_id_raum.remove(pRaumID);
		_daten.raeume.remove(raum);
	}

	/**
	 * Ändert das Kürzel des Raumes. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 * 
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @param pKuerzel               Das neue Kürzel des Raumes.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public void raumSetKuerzel(long pRaumID, @NotNull String pKuerzel) throws NullPointerException {
		@NotNull StundenplanblockungRaum raum = raumGet(pRaumID);
		raum.kuerzel = pKuerzel;
		_daten.raeume.sort(_comp_raum_kuerzel);
	}
	
	// ############################################################
	// ####################### Kopplungen #########################
	// ############################################################

	private void kopplungAddOhneSortierung(@NotNull StundenplanblockungKopplung pKopplung) throws NullPointerException {
		// Existiert die Kopplung bereits?
		if (_map_id_kopplung.containsKey(pKopplung.id))
			throw new NullPointerException("Kopplung-ID " + pKopplung.id + " existiert bereits!");
		// Kopplung hinzufügen.
		_map_id_kopplung.put(pKopplung.id, pKopplung);
		_daten.kopplungen.add(pKopplung);
	}

	/**
	 * Fügt die Kopplung hinzu. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID bereits existiert.
	 * 
	 * @param pKopplung              Das Kopplung-Objekt.
	 * @throws NullPointerException  Falls die Kopplung-ID bereits existiert.
	 */
	public void kopplungAdd(@NotNull StundenplanblockungKopplung pKopplung) throws NullPointerException {
		kopplungAddOhneSortierung(pKopplung);
		_daten.kopplungen.sort(_comp_kopplung_kuerzel);
	}

	/**
	 * Fügt alle Kopplungen hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Kopplungen bereits existiert. 
	 * 
	 * @param pKopplungen            Die Menge der Kopplungen, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Kopplungen bereits existiert.
	 */
	public void kopplungAddAll(@NotNull List<@NotNull StundenplanblockungKopplung> pKopplungen) throws NullPointerException {
		for (@NotNull StundenplanblockungKopplung kopplung : pKopplungen)
			kopplungAddOhneSortierung(kopplung);
		_daten.kopplungen.sort(_comp_kopplung_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Kopplung-ID existiert. 
	 * 
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @return             TRUE, falls die Kopplung-ID existiert.
	 */
	public boolean kopplungExists(long pKopplungID) {
		return _map_id_kopplung.containsKey(pKopplungID);
	}

	/**
	 * Liefert das {@link StundenplanblockungKopplung}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 * 
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @return                       Das {@link StundenplanblockungKopplung}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungKopplung kopplungGet(long pKopplungID) throws NullPointerException {
		StundenplanblockungKopplung kopplung = _map_id_kopplung.get(pKopplungID);
		if (kopplung == null)
			throw new NullPointerException("Kopplung-ID " + pKopplungID + " unbekannt!");
		return kopplung;
	}

	/**
	 * Liefert die Menge aller Kopplungen sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Kopplungen sortiert nach dem Kürzel.
	 */
	public Vector<StundenplanblockungKopplung> kopplungGetMengeSortiertNachKuerzel() {
		return _daten.kopplungen;
	}

	/**
	 * Löscht die übergebenen Kopplung. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 * 
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public void kopplungRemove(long pKopplungID) throws NullPointerException {
		// Kopplung holen.
		@NotNull StundenplanblockungKopplung kopplung = kopplungGet(pKopplungID);
		// Kopplung löschen.
		_map_id_kopplung.remove(pKopplungID);
		_daten.kopplungen.remove(kopplung);
	}

	/**
	 * Ändert das Kürzel der Kopplung. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 * 
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @param pKuerzel               Das neue Kürzel der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public void kopplungSetKuerzel(long pKopplungID, @NotNull String pKuerzel) throws NullPointerException {
		@NotNull StundenplanblockungKopplung kopplung = kopplungGet(pKopplungID);
		kopplung.kuerzel = pKuerzel;
		_daten.kopplungen.sort(_comp_kopplung_kuerzel);
	}
	// ############################################################
	// ###################### Lerngruppen #########################
	// ############################################################

	private void lerngruppeAddOhneSortierung(@NotNull StundenplanblockungLerngruppe pLerngruppe) throws NullPointerException {
		// Existiert die Lerngruppe bereits?
		if (_map_id_lerngruppe.containsKey(pLerngruppe.id))
			throw new NullPointerException("Lerngruppe-ID " + pLerngruppe.id + " existiert bereits!");
		// Lerngruppe hinzufügen.
		_map_id_lerngruppe.put(pLerngruppe.id, pLerngruppe);
		_daten.lerngruppen.add(pLerngruppe);
	}

	/**
	 * Fügt die Lerngruppe hinzu. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID bereits existiert.
	 * 
	 * @param pLerngruppe            Das Lerngruppe-Objekt.
	 * @throws NullPointerException  Falls die Lerngruppe-ID bereits existiert.
	 */
	public void lerngruppeAdd(@NotNull StundenplanblockungLerngruppe pLerngruppe) throws NullPointerException {
		lerngruppeAddOhneSortierung(pLerngruppe);
		_daten.lerngruppen.sort(_comp_lerngruppe_id);
	}

	/**
	 * Fügt alle Lerngruppen hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Lerngruppen bereits existiert. 
	 * 
	 * @param pLerngruppen           Die Menge der Lerngruppen, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Lerngruppen bereits existiert.
	 */
	public void lerngruppeAddAll(@NotNull List<@NotNull StundenplanblockungLerngruppe> pLerngruppen) throws NullPointerException {
		for (@NotNull StundenplanblockungLerngruppe lerngruppe : pLerngruppen)
			lerngruppeAddOhneSortierung(lerngruppe);
		_daten.lerngruppen.sort(_comp_lerngruppe_id);
	}

	/**
	 * Liefert TRUE, falls die Lerngruppe-ID existiert. 
	 * 
	 * @param pLerngruppeID  Die Lerngruppe-ID der Kopplung.
	 * @return               TRUE, falls die Lerngruppe-ID existiert.
	 */
	public boolean lerngruppeExists(long pLerngruppeID) {
		return _map_id_lerngruppe.containsKey(pLerngruppeID);
	}

	/**
	 * Liefert das {@link StundenplanblockungLerngruppe}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID unbekannt ist.
	 * 
	 * @param pLerngruppeID          Die Lerngruppe-ID der Kopplung.
	 * @return                       Das {@link StundenplanblockungLerngruppe}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Lerngruppe-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungLerngruppe lerngruppeGet(long pLerngruppeID) throws NullPointerException {
		StundenplanblockungLerngruppe lerngruppe = _map_id_lerngruppe.get(pLerngruppeID);
		if (lerngruppe == null)
			throw new NullPointerException("Lerngruppe-ID " + pLerngruppeID + " unbekannt!");
		return lerngruppe;
	}

	/**
	 * Liefert die Menge aller Lerngruppen sortiert nach ihrer ID.
	 * 
	 * @return Die Menge aller Lerngruppen sortiert nach ihrer ID.
	 */
	public Vector<StundenplanblockungLerngruppe> lerngruppeGetMengeSortiertNachID() {
		return _daten.lerngruppen;
	}

	/**
	 * Löscht die übergebenen Lerngruppe. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID unbekannt ist.
	 * 
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe.
	 * @throws NullPointerException  Falls die Lerngruppe-ID unbekannt ist.
	 */
	public void lerngruppeRemove(long pLerngruppeID) throws NullPointerException {
		// Lerngruppe holen.
		@NotNull StundenplanblockungLerngruppe lerngruppe = lerngruppeGet(pLerngruppeID);
		// Lerngruppe löschen.
		_map_id_lerngruppe.remove(pLerngruppeID);
		_daten.lerngruppen.remove(lerngruppe);
	}

	private void lerngruppeAddLehrkraftOhneSortierung(@NotNull StundenplanblockungLerngruppe pLerngruppe, @NotNull StundenplanblockungLehrkraft pLehrkraft) {
		// Fehler?
		@NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungLerngruppe> mapLerngruppe = lehrkraftGetMapLerngruppen(pLehrkraft);
		if (mapLerngruppe.containsKey(pLerngruppe.id) == true)
			throw new NullPointerException("(Lerngruppe="+pLerngruppe.id+", Lehrkraft="+pLehrkraft.id+") ist doppelt!");
		// Hinzufügen
		mapLerngruppe.put(pLerngruppe.id, pLerngruppe);
	}

	/**
	 * Ordnet einer Lerngruppe eine Lehrkraft zu. <br>
	 * Wirft eine NullPointerException, falls eine ID nicht existiert, oder die Zuordnung bereits existiert. 
	 * 
	 * @param pLerngruppeID          Die Datenbank-ID der Kopplung.
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @throws NullPointerException  Falls eine ID nicht existiert, oder die Zuordnung bereits existiert.
	 */
	public void lerngruppeAddLehrkraft(long pLerngruppeID, long pLehrkraftID) throws NullPointerException {
		@NotNull StundenplanblockungLerngruppe lerngruppe = lerngruppeGet(pLerngruppeID);
		@NotNull StundenplanblockungLehrkraft lehrkraft = lehrkraftGet(pLehrkraftID);
		lerngruppeAddLehrkraftOhneSortierung(lerngruppe, lehrkraft);
		// TODO Lerngruppe-Lehrkraft sortieren?
	}

	// ############################################################
	// ######################## Sonstiges #########################
	// ############################################################
	
	/**
	 * Diese Methode überprüft alle Datenstrukturen auf ihre Konsistenz.
	 * Liefert TRUE, falls die Daten in Ordnung (konsistent) sind.
	 * 
	 * @return TRUE, falls die Daten in Ordnung (konsistent) sind.
	 */
	public boolean miscCheckConsistency() {
		
		// Lehrkräfte
		if (_daten.lehrkraefte.size() != _map_lehrkraftID_lehrkraft.size())
			return false;
		for (@NotNull StundenplanblockungLehrkraft lehrkraft : _daten.lehrkraefte)
			if (_map_lehrkraftID_lehrkraft.get(lehrkraft.id) != lehrkraft)
				return false;
		for (int i = 1 ; i < _daten.lehrkraefte.size() ; i++)
			if (_comp_lehrkraft_kuerzel.compare(_daten.lehrkraefte.get(i-1), _daten.lehrkraefte.get(i)) > 0)
				return false;
				
		// Klassen
		if (_daten.klassen.size() != _map_klasseID_klasse.size())
			return false;
		for (@NotNull StundenplanblockungKlasse klasse : _daten.klassen)
			if (_map_klasseID_klasse.get(klasse.id) != klasse)
				return false;
		for (int i = 1 ; i < _daten.klassen.size() ; i++)
			if (_comp_klasse_kuerzel.compare(_daten.klassen.get(i-1), _daten.klassen.get(i)) > 0)
				return false;
				
		// Fächer
		if (_daten.faecher.size() != _map_id_fach.size())
			return false;
		for (@NotNull StundenplanblockungFach fach : _daten.faecher)
			if (_map_id_fach.get(fach.id) != fach)
				return false;
		for (int i = 1 ; i < _daten.faecher.size() ; i++)
			if (_comp_fach_sortiernummer.compare(_daten.faecher.get(i-1), _daten.faecher.get(i)) > 0)
				return false;
				
		// Räume
		if (_daten.raeume.size() != _map_id_raum.size())
			return false;
		for (@NotNull StundenplanblockungRaum raum : _daten.raeume)
			if (_map_id_raum.get(raum.id) != raum)
				return false;
		for (int i = 1 ; i < _daten.raeume.size() ; i++)
			if (_comp_raum_kuerzel.compare(_daten.raeume.get(i-1), _daten.raeume.get(i)) > 0)
				return false;
		
		// Kopplungen
		if (_daten.kopplungen.size() != _map_id_kopplung.size())
			return false;
		for (@NotNull StundenplanblockungKopplung kopplung : _daten.kopplungen)
			if (_map_id_kopplung.get(kopplung.id) != kopplung)
				return false;
		for (int i = 1 ; i < _daten.kopplungen.size() ; i++)
			if (_comp_kopplung_kuerzel.compare(_daten.kopplungen.get(i-1), _daten.kopplungen.get(i)) > 0)
				return false;
		
		
		return true;
	}
	
}
