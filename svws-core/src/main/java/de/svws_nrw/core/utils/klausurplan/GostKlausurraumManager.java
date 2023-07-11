package de.svws_nrw.core.utils.klausurplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKlausurraum}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Klausurräume eines Gost-Klausurtermins verwaltet.
 */
public class GostKlausurraumManager {

	/** Die Kursklausuren, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKlausurraum> _raeume = new ArrayList<>();

	/** Eine Map id -> GostKursklausur */
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraum> _mapIdRaum = new HashMap<>();


	/** Die Klausurraumstunden, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKlausurraumstunde> _stunden = new ArrayList<>();

	/** Eine Map idRaum -> Liste von Stunden */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKlausurraumstunde>> _mapRaumStunden = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume die Liste der GostKlausurräume eines Gost-Klausurtermins
	 * @param stunden   die Liste der GostKlausurraumstunden eines Gost-Klausurtermins
	 */
	public GostKlausurraumManager(final @NotNull List<@NotNull GostKlausurraum> raeume, final @NotNull List<@NotNull GostKlausurraumstunde> stunden) {
		for (final @NotNull GostKlausurraum r : raeume) {
			addKlausurraum(r);
		}

		for (final @NotNull GostKlausurraumstunde s : stunden) {
			addKlausurraumstunde(s);
		}
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum die ID des Klausurraums
	 *
	 * @return den Klausurraum
	 */
	public @NotNull GostKlausurraum getKlausurraum(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapIdRaum, idRaum);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der Klausurräume
	 */
	public @NotNull List<@NotNull GostKlausurraum> getKlausurraeume() {
		return _raeume;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param raum das Gost-Klausurraum-Objekt
	 */
	public void addKlausurraum(final @NotNull GostKlausurraum raum) {
		DeveloperNotificationException.ifListAddsDuplicate("_raeume", _raeume, raum);
		DeveloperNotificationException.ifMapPutOverwrites(_mapIdRaum, raum.id, raum);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param stunde das Gost-Klausurraumstunde-Objekt
	 */
	public void addKlausurraumstunde(final @NotNull GostKlausurraumstunde stunde) {
		DeveloperNotificationException.ifListAddsDuplicate("_stunden", _stunden, stunde);
		DeveloperNotificationException.ifMapGetIsNull(_mapIdRaum, stunde.idRaum);
		DeveloperNotificationException.ifListAddsDuplicate("_mapRaumStundenList", MapUtils.getOrCreateArrayList(_mapRaumStunden, stunde.idRaum), stunde);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum
	 * geändert hat.
	 *
	 * @param r das GostKlausurraum-Objekt
	 */
	public void patchKlausurraum(final @NotNull GostKlausurraum r) {
		DeveloperNotificationException.ifListRemoveFailes("_raeume", _raeume, r);
		DeveloperNotificationException.ifMapRemoveFailes(_mapIdRaum, r.id);
		_raeume.add(r);
		_mapIdRaum.put(r.id, r);
	}


}
