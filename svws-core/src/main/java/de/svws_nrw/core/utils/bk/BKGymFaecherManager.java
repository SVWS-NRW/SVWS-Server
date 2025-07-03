package de.svws_nrw.core.utils.bk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des Typs {@link BKGymFach}.
 */
public class BKGymFaecherManager {

	/** Die Menge aller Fremdsprachen, welche am beruflichen Gymnasium ggf. vorkommen können */
	public static final @NotNull Set<Fach> alleFremdsprachen = Set.of(
			Fach.E,
			Fach.C, Fach.C0, Fach.C5, Fach.C6, Fach.C7, Fach.C8, Fach.C9,
			Fach.F, Fach.F0, Fach.F5, Fach.F6, Fach.F7, Fach.F8, Fach.F9,
			Fach.G, Fach.G0, Fach.G5, Fach.G6, Fach.G7, Fach.G8, Fach.G9,
			Fach.H, Fach.H0, Fach.H5, Fach.H6, Fach.H7, Fach.H8, Fach.H9,
			Fach.I, Fach.I0, Fach.I5, Fach.I6, Fach.I7, Fach.I8, Fach.I9,
			Fach.K, Fach.K0, Fach.K5, Fach.K6, Fach.K7, Fach.K8, Fach.K9,
			Fach.L, Fach.L0, Fach.L5, Fach.L6, Fach.L7, Fach.L8, Fach.L9,
			Fach.N, Fach.N0, Fach.N5, Fach.N6, Fach.N7, Fach.N8, Fach.N9,
			Fach.O, Fach.O0, Fach.O5, Fach.O6, Fach.O7, Fach.O8, Fach.O9,
			Fach.R, Fach.R0, Fach.R5, Fach.R6, Fach.R7, Fach.R8, Fach.R9,
			Fach.S, Fach.S0, Fach.S5, Fach.S6, Fach.S7, Fach.S8, Fach.S9,
			Fach.T, Fach.T0, Fach.T5, Fach.T6, Fach.T7, Fach.T8, Fach.T9,
			Fach.Z, Fach.Z0, Fach.Z5, Fach.Z6, Fach.Z7, Fach.Z8, Fach.Z9
	);

	/** Sortiert die Fächer anhand ihrer konfigurierten Sortierung */
	public static final @NotNull Comparator<@AllowNull BKGymFach> comp = (a, b) -> {
		// TODO Sortierung laut Anlage für APO-BK variabel gestalten
		return -1;
	};


	/** Die Liste der Fächer, die im Manager vorhanden sind. */
	private final @NotNull List<BKGymFach> _faecher = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID */
	private final @NotNull HashMap<Long, BKGymFach> _map = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fächer anhand der Bezeichnung des Faches */
	private final @NotNull HashMap<String, List<BKGymFach>> _mapByBezeichnung = new HashMap<>();

	/** das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet - relevant wg. der Fächergültigkeit laut ASD */
	private final int schuljahr;



	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param schuljahr         das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher           die Liste mit den Fächern
	 */
	public BKGymFaecherManager(final int schuljahr, final @NotNull List<BKGymFach> faecher) {
		this.schuljahr = schuljahr;
		addAll(faecher);
	}


	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu. Die interne Sortierung wird nicht korrigiert.
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die ID des Faches negativ ist.
	 */
	private boolean addFachInternal(final @NotNull BKGymFach fach) throws DeveloperNotificationException {
		// Füge das Fach hinzu, wenn es nicht bereits vorhanden ist und gültig ist...
		DeveloperNotificationException.ifSmaller("fach.id", fach.id, 0);
		if (_map.containsKey(fach.id))
			return false;
		if (fach.bezeichnung == null)
			return false;
		final Fach zf = Fach.getBySchluesselOrDefault(fach.kuerzel);
		final FachKatalogEintrag fke = zf.daten(schuljahr);
		if (fke == null)
			return false;
		_map.put(fach.id, fach);
		List<BKGymFach> listForBezeichnung = _mapByBezeichnung.get(fach.bezeichnung);
		if (listForBezeichnung == null) {
			listForBezeichnung = new ArrayList<>();
			_mapByBezeichnung.put(fach.bezeichnung, listForBezeichnung);
		}
		listForBezeichnung.add(fach);
		return _faecher.add(fach);
	}


	/**
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die hinzuzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	private boolean addAll(final @NotNull Collection<BKGymFach> faecher) {
		boolean result = true;
		for (final @NotNull BKGymFach fach : faecher)
			if (!addFachInternal(fach))
				result = false;
		sort();
		return result;
	}


	/**
	 * Führt eine Sortierung der Fächer anhand des Sortierungsfeldes durch.
	 */
	private void sort() {
		_faecher.sort(comp);
	}


	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public boolean isEmpty() {
		return _faecher.isEmpty();
	}


	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public @NotNull List<BKGymFach> faecher() {
		return new ArrayList<>(_faecher);
	}


	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 *
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public BKGymFach get(final long id) {
		return _map.get(id);
	}


	/**
	 * Liefert das Fach mit der angegebenen ID zurück.
	 *
	 * @param idFach   die ID des gesuchten Faches.
	 *
	 * @return Das Fach mit der angegebenen ID zurück.
	 *
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public @NotNull BKGymFach getOrException(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map, idFach);
	}


	/**
	 * Prüft, ob es auch bei dem Fach mit dem angegeben Statistik-Kürzel
	 * um eine Fremdsprache handelt oder nicht
	 *
	 * @param kuerzel   das zu überprüfende Statistik-Kürzel
	 *
	 * @return true, falls es sich um eine Fremdsprache handelt und ansonsten null
	 */
	public static boolean istFremdsprachenKuerzel(final @NotNull String kuerzel) {
		return alleFremdsprachen.contains(Fach.getBySchluesselOrDefault(kuerzel));
	}


	/**
	 * Prüft, ob es auch bei dem Fach um eine Fremdsprache handelt oder nicht
	 *
	 * @param fach   das zu überprüfende Fach
	 *
	 * @return true, falls es sich um eine Fremdsprache handelt und ansonsten null
	 */
	public static boolean istFremdsprache(final @NotNull BKGymFach fach) {
		return alleFremdsprachen.contains(Fach.getBySchluesselOrDefault(fach.kuerzel));
	}


	/**
	 * Liefert das Kürzel der Sprache (ohne Jahrgang) zurück, falls es sich um eine Sprache handelt.
	 *
	 * @param fach   das Fach des beruflichen Gymnasiums
	 *
	 * @return das einstellige Sprach-Kürzel oder null
	 */
	public static String getFremdsprache(final @NotNull BKGymFach fach) {
		if (("".equals(fach.kuerzel)) || !istFremdsprache(fach))
			return null;
		return fach.kuerzel.substring(0, 1).toUpperCase();
	}

}
