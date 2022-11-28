package de.nrw.schule.svws.core.utils.stundenplanblockung;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungFach;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungInput;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungKlasse;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungLehrkraft;
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

	/** Lehrkraft-ID --> Lehrkraft-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungLehrkraft> _map_id_lehrkraft = new HashMap<>();

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

	/** Klasse-ID --> Klasse-Objekt. */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungKlasse> _map_id_klasse = new HashMap<>();

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

	/**
	 * Erzeugt einen neuen Manager mit einem leeren {@link StundenplanblockungInput}-Objekt.
	 */
	public StundenplanblockungManager() {
		_daten = new StundenplanblockungInput();
	}

	// ############################################################
	// ######################## Lehrkräfte ########################
	// ############################################################

	/**
	 * Erzeugt eine Lehrkraft anhand der übergebenen Daten. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID bereits existiert.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel               Das Kürzel der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID bereits existiert.
	 */
	public void lehrkraftCreate(long pLehrkraftID, @NotNull String pKuerzel) throws NullPointerException {
		// Existiert die Lehrkraft bereits?
		if (_map_id_lehrkraft.containsKey(pLehrkraftID))
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " existiert bereits!");

		// Lehrkraft erzeugen.
		StundenplanblockungLehrkraft le = new StundenplanblockungLehrkraft();
		le.id = pLehrkraftID;
		le.kuerzel = pKuerzel;

		// Lehrkraft hinzufügen.
		_map_id_lehrkraft.put(pLehrkraftID, le);
		_daten.lehrkraefte.add(le);
		_daten.lehrkraefte.sort(_comp_lehrkraft_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft-ID existiert. 
	 * 
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @return              TRUE, falls die Lehrkraft-ID existiert.
	 */
	public boolean lehrkraftExists(long pLehrkraftID) {
		return _map_id_lehrkraft.containsKey(pLehrkraftID);
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
		StundenplanblockungLehrkraft lehrkraft = _map_id_lehrkraft.get(pLehrkraftID);
		if (lehrkraft == null)
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " unbekannt!");
		return lehrkraft;
	}

	/**
	 * Liefert die Menge aller Lehrkräfte sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Lehrkräfte sortiert nach dem Kürzel.
	 */
	public Vector<StundenplanblockungLehrkraft> lehrkraftGetMengeSortiertNachKuerzel() {
		return _daten.lehrkraefte;
	}

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
		_map_id_lehrkraft.remove(pLehrkraftID);
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

	/**
	 * Erzeugt eine Klasse anhand der übergebenen Daten. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @param pKuerzel               Das Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public void klasseCreate(long pKlasseID, @NotNull String pKuerzel) throws NullPointerException {
		// Existiert die Klasse bereits?
		if (_map_id_klasse.containsKey(pKlasseID))
			throw new NullPointerException("Klasse-ID " + pKlasseID + " existiert bereits!");

		// Klasse erzeugen.
		StundenplanblockungKlasse kl = new StundenplanblockungKlasse();
		kl.id = pKlasseID;
		kl.kuerzel = pKuerzel;

		// Klasse hinzufügen.
		_map_id_klasse.put(pKlasseID, kl);
		_daten.klassen.add(kl);
		_daten.klassen.sort(_comp_klasse_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Klasse-ID existiert. 
	 * 
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @return           TRUE, falls die Klasse-ID existiert.
	 */
	public boolean klasseExists(long pKlasseID) {
		return _map_id_klasse.containsKey(pKlasseID);
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
		StundenplanblockungKlasse klasse = _map_id_klasse.get(pKlasseID);
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
		_map_id_klasse.remove(pKlasseID);
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

	/**
	 * Erzeugt ein Fach anhand der übergebenen Daten. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID bereits existiert.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @param pKuerzel               Das Kürzel des Faches.
	 * @param pSortierung            Eine Zahlenwert Als Grundlage zur Sortierung der Fächer. 
	 * @throws NullPointerException  Falls die Fach-ID bereits existiert.
	 */
	public void fachCreate(long pFachID, @NotNull String pKuerzel, int pSortierung) throws NullPointerException {
		// Existiert das Fach bereits?
		if (_map_id_fach.containsKey(pFachID))
			throw new NullPointerException("Fach-ID " + pFachID + " existiert bereits!");

		// Fach erzeugen.
		StundenplanblockungFach fa = new StundenplanblockungFach();
		fa.id = pFachID;
		fa.kuerzel = pKuerzel;
		fa.sortierung = pSortierung;

		// Fach hinzufügen.
		_map_id_fach.put(pFachID, fa);
		_daten.faecher.add(fa);
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
	
}
