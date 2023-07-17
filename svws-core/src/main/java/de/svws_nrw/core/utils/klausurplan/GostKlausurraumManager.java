package de.svws_nrw.core.utils.klausurplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
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

	/** Eine Map idRaum, idZeitraster -> Klausurraumstunde */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull GostKlausurraumstunde> _mapRaumZeitrasterStunde = new HashMap2D<>();

	/** Ein Comparator für die GostKlausurräume. */
	private static final @NotNull Comparator<@NotNull GostKlausurraum> _compRaumId = (final @NotNull GostKlausurraum a, final @NotNull GostKlausurraum b) -> {
		return Long.compare(a.id, b.id);
	};

	/** Die Schuelerklausuren, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostSchuelerklausur> _schuelerklausuren = new ArrayList<>();

	/** Eine Map Kursklausur-Id -> Liste von GostSchuelerklausuren */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausur>> _mapKkidSk = new HashMap<>();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raum            der Gost-Klausurraum
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 */
	public GostKlausurraumManager(final @NotNull GostKlausurraum raum, final @NotNull List<@NotNull GostKlausurraumstunde> stunden, final @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren) {
		addKlausurraum(raum);
		for (final @NotNull GostKlausurraumstunde s : stunden)
			addKlausurraumstunde(s);
		for (final @NotNull GostSchuelerklausur k : schuelerklausuren)
			addSchuelerklausur(k);
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume            die Liste der GostKlausurräume eines
	 *                          Gost-Klausurtermins
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 */
	public GostKlausurraumManager(final @NotNull List<@NotNull GostKlausurraum> raeume, final @NotNull List<@NotNull GostKlausurraumstunde> stunden,
			final @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren) {
		for (final @NotNull GostKlausurraum r : raeume)
			addKlausurraum(r);
		for (final @NotNull GostKlausurraumstunde s : stunden)
			addKlausurraumstunde(s);
		for (final @NotNull GostSchuelerklausur k : schuelerklausuren)
			addSchuelerklausur(k);
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
	 * @param idRaum die ID des Klausurraums
	 * @param idZeitraster die ID des Zeitrasters
	 *
	 * @return die Klausurraumstunde
	 */
	public GostKlausurraumstunde getKlausurraumstundeByRaumZeitraster(final long idRaum, final long idZeitraster) {
		return _mapRaumZeitrasterStunde.getOrNull(idRaum, idZeitraster);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param raum das Gost-Klausurraum-Objekt
	 */
	public void addKlausurraum(final @NotNull GostKlausurraum raum) {
		DeveloperNotificationException.ifListAddsDuplicate("_raeume", _raeume, raum);
		_raeume.sort(_compRaumId);
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
		DeveloperNotificationException.ifMap2DPutOverwrites(_mapRaumZeitrasterStunde, stunde.idRaum, stunde.idZeitraster, stunde);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param klausur das Gost-Klausurraum-Objekt
	 */
	public void addSchuelerklausur(final @NotNull GostSchuelerklausur klausur) {
		// Füllen von _mapKkidSk
		DeveloperNotificationException.ifListAddsDuplicate("_mapKkidSkList", MapUtils.getOrCreateArrayList(_mapKkidSk, klausur.idKursklausur), klausur);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param r das GostKlausurraum-Objekt
	 */
	public void patchKlausurraum(final @NotNull GostKlausurraum r) {
		DeveloperNotificationException.ifListRemoveFailes("_raeume", _raeume, r);
		DeveloperNotificationException.ifMapRemoveFailes(_mapIdRaum, r.id);
		_raeume.add(r);
		_mapIdRaum.put(r.id, r);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param manager das GostKlausurraumManager-Objekt
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausuren(final @NotNull GostKursklausurManager manager) {
		List<@NotNull GostKursklausur> kursklausuren = new ArrayList<>();
		for (final long kkId : _mapKkidSk.keySet()) {
			kursklausuren.add(manager.getKursklausurById(kkId));
		}
		return kursklausuren;
	}

}
