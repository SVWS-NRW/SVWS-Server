package de.svws_nrw.core.utils.bk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des Typs {@link BKGymFach}.
 */
public class BKGymFaecherManager {
	/** Die Menge aller Fremdsprachen, welche am beruflichen Gymnasium ggf. vorkommen können */
	public static final @NotNull Set<Fach> alleFremdsprachen = Set.of(
			Fach.E, Fach.C, Fach.F, Fach.G, Fach.H, Fach.I, Fach.K, Fach.L,
			Fach.N, Fach.O, Fach.R, Fach.S, Fach.T, Fach.Z
	);

	/** Die Liste der Fächer, die im Manager vorhanden sind. */
	private final @NotNull List<BKGymFach> faecher = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID */
	private final @NotNull HashMap<Long, BKGymFach> map = new HashMap<>();

	/** das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet - relevant wg. der Fächergültigkeit laut ASD */
	private final int schuljahr;

	/** Fachbezeichnungen, die doppelt in der Fächerliste eingetragen sind */
	private final @NotNull Set<String> doppelteFaecher = new HashSet<>();



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
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die hinzuzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	private boolean addAll(final @NotNull Collection<BKGymFach> faecher) {
		final Set<String> setOfBezeichnung = new HashSet<>();
		boolean result = true;
		for (final @NotNull BKGymFach fach : faecher) {
			if (!addFachInternal(fach))
				result = false;
			if (setOfBezeichnung.contains(fach.bezeichnung))
				doppelteFaecher.add(fach.bezeichnung);
			else
				setOfBezeichnung.add(fach.bezeichnung);
		}
		return result;
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
		if (map.containsKey(fach.id))
			return false;
		if (fach.bezeichnung == null)
			return false;
		final Fach zf = Fach.getBySchluesselOrDefault(fach.kuerzel);
		final FachKatalogEintrag fke = zf.daten(schuljahr);
		if (fke == null)
			return false;
		map.put(fach.id, fach);
		return true;
	}


	/**
	 * Getter für die doppelten Fächer als List
	 *
	 * @return die Liste der doppelten Fächer
	 */
	public @NotNull List<String> getDoppelteFaecher() {
		return new ArrayList<>(doppelteFaecher);
	}


	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public boolean isEmpty() {
		return faecher.isEmpty();
	}


	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public @NotNull List<BKGymFach> getFaecher() {
		return new ArrayList<>(faecher);
	}


	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 *
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public BKGymFach get(final long id) {
		return map.get(id);
	}


	/**
	 * liefert zu einer fachID die Fachbezeichnung
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die Fachbezeichnung
	 */
	public @NotNull String getBezeichnungByFachID(final long id) {
		final BKGymFach fach = map.get(id);
		if (fach == null)
			return "";
		if (fach.bezeichnung == null)
			return "";
		return fach.bezeichnung;
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
		return DeveloperNotificationException.ifMapGetIsNull(map, idFach);
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
