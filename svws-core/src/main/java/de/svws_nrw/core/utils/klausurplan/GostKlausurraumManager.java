package de.svws_nrw.core.utils.klausurplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurraumstunde;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.Map2DUtils;
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

	/** Eine Map id -> GostKlausurraum */
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraum> _mapIdRaum = new HashMap<>();

	/** Die Klausurraumstunden, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKlausurraumstunde> _stunden = new ArrayList<>();

	/** Eine Map id -> GostKlausurraumstunde */
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraumstunde> _mapIdRaumStunde = new HashMap<>();

	/** Eine Map idRaum -> Liste von Stunden */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKlausurraumstunde>> _mapRaumStunden = new HashMap<>();

	/** Eine Map idRaum, idKursklausur -> Liste von Schülerklausuren */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull GostSchuelerklausur>> _mapRaumKursklausurSchuelerklausur = new HashMap2D<>();

	/** Eine Map idRaum, idZeitraster -> Klausurraumstunde */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull GostKlausurraumstunde> _mapRaumZeitrasterStunde = new HashMap2D<>();

	/** Ein Comparator für die GostKlausurräume. */
	private static final @NotNull Comparator<@NotNull GostKlausurraum> _compRaumId = (final @NotNull GostKlausurraum a, final @NotNull GostKlausurraum b) -> {
		return Long.compare(a.id, b.id);
	};

	/** Die Schuelerklausuren, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostSchuelerklausur> _schuelerklausuren = new ArrayList<>();

	/** Eine Map Schülerklausur-Id -> GostSchuelerklausuren */
	private final @NotNull Map<@NotNull Long, @NotNull GostSchuelerklausur> _mapIdSchuelerklausur = new HashMap<>();

	/** Eine Map Kursklausur-Id -> Liste von GostSchuelerklausuren */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausur>> _mapKkidSk = new HashMap<>();

	/** Eine Map Raumstunde-Id -> Liste von Schuelerklausurraumstunden */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausurraumstunde>> _mapidRsSkrs = new HashMap<>();

	/** Eine Map Schuelerklausur-Id -> Liste von Raumstunden */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKlausurraumstunde>> _mapidRsSkrsRevert = new HashMap<>();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raum              der Gost-Klausurraum
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 */
	public GostKlausurraumManager(final @NotNull GostKlausurraum raum, final @NotNull List<@NotNull GostKlausurraumstunde> stunden,
			final @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren) {
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
	 * @param listRs            die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param listSkrs          die Liste der Schülerklausurraumstunden
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 */
	public GostKlausurraumManager(final @NotNull List<@NotNull GostKlausurraum> raeume, final @NotNull List<@NotNull GostKlausurraumstunde> listRs,
			final @NotNull List<@NotNull GostSchuelerklausurraumstunde> listSkrs, final @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren) {
		for (final @NotNull GostKlausurraum r : raeume)
			addKlausurraum(r);
		for (final @NotNull GostKlausurraumstunde s : listRs)
			addKlausurraumstunde(s);
		for (final @NotNull GostSchuelerklausurraumstunde s : listSkrs)
			addSchuelerklausurraumstunde(s);
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
	 * @param idRaum       die ID des Klausurraums
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
		DeveloperNotificationException.ifMapPutOverwrites(_mapIdRaumStunde, stunde.id, stunde);
		DeveloperNotificationException.ifListAddsDuplicate("_mapRaumStundenList", MapUtils.getOrCreateArrayList(_mapRaumStunden, stunde.idRaum), stunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(_mapRaumZeitrasterStunde, stunde.idRaum, stunde.idZeitraster, stunde);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param stunde das Gost-Klausurraumstunde-Objekt
	 */
	public void addSchuelerklausurraumstunde(final @NotNull GostSchuelerklausurraumstunde stunde) {
		DeveloperNotificationException.ifListAddsDuplicate("_mapidRsSkrsList", MapUtils.getOrCreateArrayList(_mapidRsSkrs, stunde.idRaumstunde), stunde);
		DeveloperNotificationException.ifListAddsDuplicate("_mapidRsSkrsRevertList", MapUtils.getOrCreateArrayList(_mapidRsSkrsRevert, stunde.idSchuelerklausur),
				DeveloperNotificationException.ifMapGetIsNull(_mapIdRaumStunde, stunde.idRaumstunde));

	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param klausur das Gost-Klausurraum-Objekt
	 */
	public void addSchuelerklausur(final @NotNull GostSchuelerklausur klausur) {
		// Füllen von _mapKkidSk
		DeveloperNotificationException.ifMapPutOverwrites(_mapIdSchuelerklausur, klausur.idSchuelerklausur, klausur);
		DeveloperNotificationException.ifListAddsDuplicate("_mapKkidSkList", MapUtils.getOrCreateArrayList(_mapKkidSk, klausur.idKursklausur), klausur);

		List<@NotNull GostKlausurraumstunde> raumstunden = _mapidRsSkrsRevert.get(klausur.idSchuelerklausur);
		DeveloperNotificationException.ifListAddsDuplicate("_mapRaumKursklausurSchuelerklausurList",
				Map2DUtils.getOrCreateArrayList(_mapRaumKursklausurSchuelerklausur, raumstunden == null || raumstunden.isEmpty() ? -1L : raumstunden.get(0).idRaum, klausur.idKursklausur), klausur);
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
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param skids             die IDs der Schülerklausuren
	 * @param collectionSkrsKrs das GostKlausurraum-Objekt
	 */
	public void setzeRaumZuSchuelerklausuren(final @NotNull List<@NotNull Long> skids, final @NotNull GostKlausurenCollectionSkrsKrs collectionSkrsKrs) {
		for (final long skid : skids) {
			final GostSchuelerklausur schuelerklausur = DeveloperNotificationException.ifMapGetIsNull(_mapIdSchuelerklausur, skid);
			List<GostKlausurraumstunde> listKrs = _mapidRsSkrsRevert.get(skid);
			if (listKrs == null) {
				DeveloperNotificationException.ifMap2DGetIsNull(_mapRaumKursklausurSchuelerklausur, -1L, schuelerklausur.idKursklausur).remove(schuelerklausur);
				continue;
			}
			DeveloperNotificationException.ifMapRemoveFailes(_mapidRsSkrsRevert, skid);
			for (final GostKlausurraumstunde rsid : listKrs) {
				if (rsid == null)
					continue;
				final @NotNull List<@NotNull GostSchuelerklausurraumstunde> skrsList = DeveloperNotificationException.ifMapGetIsNull(_mapidRsSkrs, rsid.id);
				final List<@NotNull GostSchuelerklausurraumstunde> toRemove = new ArrayList<>();
				for (final @NotNull GostSchuelerklausurraumstunde skrs : skrsList) {
					if (skrs.idSchuelerklausur == skid)
						toRemove.add(skrs);
				}
				skrsList.removeAll(toRemove);
				DeveloperNotificationException.ifMap2DGetIsNull(_mapRaumKursklausurSchuelerklausur, rsid.idRaum, schuelerklausur.idKursklausur).remove(schuelerklausur);
			}
		}

		final @NotNull List<@NotNull GostKlausurraumstunde> raumstunden = collectionSkrsKrs.raumstunden;
		final @NotNull List<@NotNull GostSchuelerklausurraumstunde> skRaumstunden = collectionSkrsKrs.skRaumstunden;
		for (final @NotNull GostKlausurraumstunde rs : raumstunden)
			addKlausurraumstunde(rs);
		for (final @NotNull GostSchuelerklausurraumstunde skrs : skRaumstunden)
			addSchuelerklausurraumstunde(skrs);
		for (final long skid : skids) {
			final GostSchuelerklausur schuelerklausur = DeveloperNotificationException.ifMapGetIsNull(_mapIdSchuelerklausur, skid);
			DeveloperNotificationException.ifListAddsDuplicate("_mapRaumKursklausurSchuelerklausurList",
					Map2DUtils.getOrCreateArrayList(_mapRaumKursklausurSchuelerklausur, collectionSkrsKrs.idKlausurraum, schuelerklausur.idKursklausur), schuelerklausur);
		}

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

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idKlausur die Id der Kursklausur
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> getSchuelerklausurenByKursklausur(final long idKlausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapKkidSk, idKlausur);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 * @param manager der Kursklausurmanager
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenInRaum(final long idRaum, final @NotNull GostKursklausurManager manager) {
		List<@NotNull GostKursklausur> kursklausuren = new ArrayList<>();
		if (!_mapRaumKursklausurSchuelerklausur.containsKey1(idRaum))
			return kursklausuren;
		for (final long idKK : _mapRaumKursklausurSchuelerklausur.getKeySetOf(idRaum)) {
			if (!_mapRaumKursklausurSchuelerklausur.getNonNullOrException(idRaum, idKK).isEmpty())
				kursklausuren.add(manager.getKursklausurById(idKK));
		}
		return kursklausuren;
	}

}
