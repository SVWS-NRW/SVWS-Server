package de.svws_nrw.core.utils.klausurplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKursklausur}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Kursklausuren eines Gost-Halbjahres eines Abiturjahrgangs verwaltet.
 */
public class GostKursklausurManager {

	/** Die Kursklausuren, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKursklausur> _klausuren;

	/** Eine Map id -> GostKursklausur */
	private final @NotNull Map<@NotNull Long, @NotNull GostKursklausur> _mapIdKursklausur = new HashMap<>();

	/** Eine Map quartal -> Liste von GostKursklausuren */
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull GostKursklausur>> _mapQuartalKursKlausuren = new HashMap<>();


	/** Eine Map idTermin -> Liste von GostKursklausuren */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> _mapTerminKursklausuren = new HashMap<>();

	/** Eine Map quartal, idTermin -> Liste von GostKursklausuren */
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _mapQuartalTerminKursklausuren = new HashMap2D<>();

	/** Eine Map quartal, kursart, idTermin -> Liste von GostKursklausuren */
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _mapQuartalKursartTerminKursklausuren = new HashMap3D<>();


	/** Die Klausurtermine, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKlausurtermin> _termine = new ArrayList<>();

	/** Eine Map quartal -> Liste von GostKlausurterminen */
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull GostKlausurtermin>> _mapQuartalKlausurtermine = new HashMap<>();

	/** Eine Map idTermin -> Liste von Schüler-IDs */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull Long>> _mapTerminSchuelerids = new HashMap<>();

	/** Eine Map idTermin -> GostKlausurtermin */
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurtermin> _mapIdKlausurtermin = new HashMap<>();

	/** Eine Map date -> GostKlausurtermin */
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull GostKlausurtermin>> _mapDateKlausurtermin = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param klausuren die Liste der GostKursklausuren eines Abiturjahrgangs und
	 *                  Gost-Halbjahres
	 * @param termine   die Liste der GostKlausurtermine eines Abiturjahrgangs und
	 *                  Gost-Halbjahres
	 */
	public GostKursklausurManager(final @NotNull List<@NotNull GostKursklausur> klausuren, final @NotNull List<@NotNull GostKlausurtermin> termine) {
		_klausuren = klausuren;
		helpKonstruktor();
		for (final @NotNull GostKlausurtermin t : termine) {
			addKlausurtermin(t);
		}
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und erzeugt die privaten Attribute.
	 *
	 * @param klausuren die Liste der GostKursklausuren eines Abiturjahrgangs und
	 *                  Gost-Halbjahres
	 */
	public GostKursklausurManager(final @NotNull List<@NotNull GostKursklausur> klausuren) {
		_klausuren = klausuren;
		helpKonstruktor();
	}

	private void helpKonstruktor() {
		for (final @NotNull GostKursklausur kk : _klausuren) {
			// Füllen von _mapIdKursklausuren
			_mapIdKursklausur.put(kk.id, kk);

			addKlausurToInternalMaps(kk);

			// Füllen von _mapQuartalKursKlausuren
			MapUtils.getOrCreateArrayList(_mapQuartalKursKlausuren, kk.quartal).add(kk);

			// Füllen von _mapTerminSchuelerids
			if (kk.idTermin != null)
				MapUtils.getOrCreateArrayList(_mapTerminSchuelerids, kk.idTermin).addAll(kk.schuelerIds);

		}
	}

	private void addKlausurToInternalMaps(final @NotNull GostKursklausur kk) {

		// Füllen von _mapTermineKursklausuren
		MapUtils.getOrCreateArrayList(_mapTerminKursklausuren, kk.idTermin != null ? kk.idTermin : -1).add(kk);

		// Füllen von _mapQuartalTerminKursklausuren
		Map2DUtils.getOrCreateArrayList(_mapQuartalTerminKursklausuren, kk.quartal, kk.idTermin != null ? kk.idTermin : -1).add(kk);

		// Füllen von _mapQuartalKursartTerminKursklausuren
		Map3DUtils.getOrCreateArrayList(_mapQuartalKursartTerminKursklausuren, kk.quartal, kk.kursart, kk.idTermin != null ? kk.idTermin : -1).add(kk);

	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich z.B. das Datum eines
	 * Termins geändert hat.
	 *
	 * @param id die ID des GostKlausurtermin-Objekts
	 * @param datum das neue Datum der Klausur
	 *
	 */
	public void patchKlausurterminDatum(final @NotNull Long id, final @NotNull String datum) {
		final @NotNull GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_mapIdKlausurtermin, id);
		if (termin.datum != null)
			DeveloperNotificationException.ifListRemoveFailes("_mapDateKlausurterminList", DeveloperNotificationException.ifMapGetIsNull(_mapDateKlausurtermin, termin.datum), termin);
		termin.datum = datum;
		if (termin.datum != null)
			MapUtils.getOrCreateArrayList(_mapDateKlausurtermin, termin.datum).add(termin);
	}

	/**
	 * Löscht ein GostKlausurtermin-Objekt aus den internen Strukturen.
	 *
	 * @param id die ID des GostKlausurtermin-Objekts
	 */
	public void removeKlausurtermin(final @NotNull Long id) {

		final @NotNull GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_mapIdKlausurtermin, id);
		_mapIdKlausurtermin.remove(termin.id);

		DeveloperNotificationException.ifListRemoveFailes("_termine", _termine, termin);

		DeveloperNotificationException.ifListRemoveFailes("listKlausurtermineMapQuartalKlausurtermine", DeveloperNotificationException.ifMapGetIsNull(_mapQuartalKlausurtermine, termin.quartal),
				termin);

		final @NotNull List<@NotNull GostKursklausur> listKlausurenZuTermin = DeveloperNotificationException.ifMapGetIsNull(_mapTerminKursklausuren, id);
		for (final @NotNull GostKursklausur k : new ArrayList<>(listKlausurenZuTermin)) {
			k.idTermin = null;
			updateKursklausur(k);
		}

		if (termin.datum != null)
			DeveloperNotificationException.ifListRemoveFailes("_mapDateKlausurterminList", DeveloperNotificationException.ifMapGetIsNull(_mapDateKlausurtermin, termin.datum), termin);

		_mapTerminSchuelerids.remove(termin.id);

		_mapTerminKursklausuren.remove(termin.id);

		final @NotNull List<@NotNull GostKursklausur> klausuren = _mapQuartalTerminKursklausuren.getNonNullOrException(termin.quartal, termin.id);
		_mapQuartalTerminKursklausuren.removeOrException(termin.quartal, termin.id);
		for (@NotNull final GostKursklausur klausur : klausuren)
			_mapQuartalKursartTerminKursklausuren.remove(termin.quartal, klausur.kursart, termin.id);


	}

	/**
	 * Fügt den internen Strukturen einen neuen Klausurtermin hinzu.
	 *
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public void addKlausurtermin(final @NotNull GostKlausurtermin termin) {
		_termine.add(termin);

		_mapIdKlausurtermin.put(termin.id, termin);

		MapUtils.getOrCreateArrayList(_mapTerminKursklausuren, termin.id);

		MapUtils.getOrCreateArrayList(_mapTerminSchuelerids, termin.id);

		MapUtils.getOrCreateArrayList(_mapQuartalKlausurtermine, termin.quartal).add(termin);

		if (termin.datum != null)
			MapUtils.getOrCreateArrayList(_mapDateKlausurtermin, termin.datum).add(termin);

		Map2DUtils.getOrCreateArrayList(_mapQuartalTerminKursklausuren, termin.quartal, termin.id);

	}


	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Termin einer Klausur
	 * geändert hat.
	 *
	 * @param klausur das GostKursklausur-Objekt
	 */
	public void updateKursklausur(final @NotNull GostKursklausur klausur) {

		final List<GostKursklausur> terminNeuKlausuren = _mapTerminKursklausuren.get(klausur.idTermin != null ? klausur.idTermin : -1);
		if (terminNeuKlausuren == null || !terminNeuKlausuren.contains(klausur)) {
			// Termin-ID hat sich geändert
			Long oldTerminId = -2L;

			// aus _mapTerminKursklausuren löschen
			for (final @NotNull Entry<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> e : _mapTerminKursklausuren.entrySet()) {
				@NotNull final List<@NotNull GostKursklausur> list = e.getValue();
				if (list.contains(klausur)) {
					oldTerminId = e.getKey();
					list.remove(klausur);
				}
			}

			// aus _mapQuartalTerminKursklausuren löschen
			final @NotNull List<@NotNull GostKursklausur> listOldQuartalTerminKursklausuren = _mapQuartalTerminKursklausuren.getNonNullOrException(klausur.quartal, oldTerminId);
			DeveloperNotificationException.ifListRemoveFailes("listOldQuartalTerminKursklausuren", listOldQuartalTerminKursklausuren, klausur);

			// aus _mapQuartalKursartTerminKursklausuren löschen
			final @NotNull List<@NotNull GostKursklausur> listOldQuartalKursartTerminKursklausuren = DeveloperNotificationException.ifMap3DGetIsNull(_mapQuartalKursartTerminKursklausuren, klausur.quartal, klausur.kursart, oldTerminId);
			DeveloperNotificationException.ifListRemoveFailes("listOldQuartalKursartTerminKursklausuren", listOldQuartalKursartTerminKursklausuren, klausur);

			// _mapQuartalKursKlausuren muss nicht geändert werden

			addKlausurToInternalMaps(klausur);

			// _mapTerminSchuelerids aktualisieren
			updateSchuelerIdsZuTermin(oldTerminId);
			if (klausur.idTermin != null)
				updateSchuelerIdsZuTermin(klausur.idTermin);

		}

	}

	private void updateSchuelerIdsZuTermin(final long idTermin) {
		final ArrayList<@NotNull Long> listSchuelerIds = new ArrayList<>();
		_mapTerminSchuelerids.put(idTermin, listSchuelerIds);
		final List<@NotNull GostKursklausur> listKlausurenZuTermin = _mapTerminKursklausuren.get(idTermin);
		if (listKlausurenZuTermin == null)
			return;
		for (final @NotNull GostKursklausur k : listKlausurenZuTermin) {
			listSchuelerIds.addAll(k.schuelerIds);
		}
	}


	/**
	 * Fügt den internen Strukturen eine neue Kursklausur hinzu.
	 *
	 * @param klausur das GostKursklausur-Objekt
	 */
	public void addKlausur(final @NotNull GostKursklausur klausur) {
		_klausuren.add(klausur);
		_mapIdKursklausur.put(klausur.id, klausur);
		addKlausurToInternalMaps(klausur);
	}

	/**
	 * Fügt den internen Strukturen neue Kursklausuren hinzu.
	 *
	 * @param klausuren die Liste von GostKursklausur-Objekten
	 */
	public void addKlausuren(final @NotNull List<@NotNull GostKursklausur> klausuren) {
		for (final @NotNull GostKursklausur klausur : klausuren) {
			addKlausur(klausur);
		}
	}

	/**
	 * Liefert das GostKursklausur-Objekt zum übergebenen Termin und Kurs
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idKurs   die ID des Kurses
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public GostKursklausur getKursklausurByTerminKurs(final Long idTermin, final Long idKurs) {
		final List<@NotNull GostKursklausur> klausuren = getKursklausurenByTermin(idTermin);
		for (final @NotNull GostKursklausur klaus : klausuren) {
			if (klaus.idKurs == idKurs)
				return klaus;
		}
		return null;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> getKlausurtermineByDatum(final @NotNull String datum) {
		final List<@NotNull GostKlausurtermin> termine = _mapDateKlausurtermin.get(datum);
		return termine != null ? termine : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum   das Datum der Klausurtermine
	 * @param zr      Zeitraster
	 * @param manager Manager
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public GostKlausurtermin getKlausurtermineByDatumUhrzeit(final @NotNull String datum, final @NotNull StundenplanZeitraster zr, final @NotNull StundenplanManager manager) {
		final List<@NotNull GostKlausurtermin> termine = getKlausurtermineByDatum(datum);
		for (final @NotNull GostKlausurtermin termin : termine) {
			final StundenplanZeitraster zrTermin = manager
					.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(zr.wochentag), DeveloperNotificationException.ifNull("Startzeit des Klausurtermins", termin.startzeit), 1)
					.get(0);
			if (zrTermin != null && zrTermin.id == zr.id)
				return termin;
		}
		return null;
		// return termine != null ? termine : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenByTermin(final Long idTermin) {
		final List<@NotNull GostKursklausur> klausuren = _mapTerminKursklausuren.get(idTermin != null ? idTermin : -1);
		return klausuren != null ? klausuren : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten des Halbjahres
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausuren() {
		return _klausuren;
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public List<@NotNull GostKursklausur> getKursklausurenByQuartal(final int quartal) {
		return _mapQuartalKursKlausuren.get(quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenOhneTermin() {
		return getKursklausurenByTermin(-1L);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenOhneTerminByQuartal(final int quartal) {
		final List<@NotNull GostKursklausur> klausuren = _mapQuartalTerminKursklausuren.getOrNull(quartal <= 0 ? -1 : quartal, -1L);
		return klausuren != null ? klausuren : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull List<@NotNull GostKursklausur>> getKursklausurenKursartOhneTermin(final int quartal) {
		final List<@NotNull List<@NotNull GostKursklausur>> retList = new ArrayList<>();
		final Map<@NotNull String, @NotNull Map<@NotNull Long, List<@NotNull GostKursklausur>>> mapKursartTerminKursklausuren = _mapQuartalKursartTerminKursklausuren
				.getMap2OrNull(quartal <= 0 ? -1 : quartal);
		if (mapKursartTerminKursklausuren != null) {
			for (final Map<@NotNull Long, List<@NotNull GostKursklausur>> mapKursarten : mapKursartTerminKursklausuren.values()) {
				if (mapKursarten != null)
					retList.add(mapKursarten.get(-1L));
			}
		}
		return retList;
	}

	/**
	 * Gibt das GostKlausurtermin-Objekt zur übergebenen id zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return das GostKlausurtermin-Objekt
	 */
	public GostKlausurtermin gibGostKlausurtermin(final long idTermin) {
		return _mapIdKlausurtermin.get(idTermin);
	}

	/**
	 * Gibt eine Liste von Schüler-IDs zurück, die vom übergebenen Termin betroffen
	 * sind.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der betroffenen Schüler-IDs
	 */
	public List<@NotNull Long> gibSchuelerIDsZuTermin(final long idTermin) {
		final List<@NotNull Long> schuelerIds = _mapTerminSchuelerids.get(idTermin);
		return schuelerIds != null || !_mapIdKlausurtermin.containsKey(idTermin) ? schuelerIds : new ArrayList<>();
	}

	/**
	 * Gibt das GostKursklausur-Objekt zur übergebenen id zurück.
	 *
	 * @param idKursklausur die ID der Kursklausur
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public GostKursklausur gibKursklausur(final long idKursklausur) {
		return _mapIdKursklausur.get(idKursklausur);
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> getKlausurtermine() {
		return _termine;
	}

	/**
	 * Gibt das GostKlausurtermin-Objekt zur übergebenen id zurück.
	 *
	 * @param idTermin die ID des GostKlausurtermins
	 *
	 * @return das GostKlausurtermin-Objekt
	 */
	public GostKlausurtermin gibKlausurtermin(final long idTermin) {
		return _mapIdKlausurtermin.get(idTermin);
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> getKlausurtermineByQuartal(final int quartal) {
		final List<@NotNull GostKlausurtermin> termine = _mapQuartalKlausurtermine.get(quartal <= 0 ? -1 : quartal);
		return termine != null ? termine : new ArrayList<>();
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param termin  der zu prüfende Klausurtermin
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public @NotNull List<@NotNull Long> gibKonfliktTerminInternKursklausur(final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur klausur) {
		final @NotNull List<@NotNull Long> konflikte = new ArrayList<>();

		final List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausurenByTermin(termin.id);
		if (listKlausurenZuTermin == null)
			return konflikte;

		for (final @NotNull GostKursklausur klausurInTermin : listKlausurenZuTermin) {
			konflikte.addAll(gibKonfliktKursklausurKursklausur(klausur, klausurInTermin));
		}

		return konflikte;
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param termin  der zu prüfende Klausurtermin
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public @NotNull List<@NotNull Long> gibKonfliktTerminKursklausur(final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur klausur) {
		if (klausur.idTermin == termin.id) {
			return new ArrayList<>();
		}

		final List<@NotNull Long> schuelerIds = gibSchuelerIDsZuTermin(termin.id);
		if (schuelerIds == null) {
			return new ArrayList<>();
		}

		final @NotNull List<@NotNull Long> konflikte = new ArrayList<>(schuelerIds);

		konflikte.retainAll(klausur.schuelerIds);
		return konflikte;
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param idTermin      die ID des zu prüfenden Klausurtermins
	 * @param idKursklausur die ID der zu prüfenden Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public @NotNull List<@NotNull Long> gibKonfliktTerminKursklausurIds(final long idTermin, final long idKursklausur) {
		final GostKursklausur klausur = DeveloperNotificationException.ifMapGetIsNull(_mapIdKursklausur, idKursklausur);
		final GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_mapIdKlausurtermin, idTermin);

		return gibKonfliktTerminKursklausur(termin, klausur);
	}

	/**
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben.
	 *
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 *
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public int gibAnzahlKonflikteZuTermin(final long idTermin) {
		int anzahl = 0;
		final List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausurenByTermin(idTermin);
		if (listKlausurenZuTermin != null) {
			final List<@NotNull GostKursklausur> copyListKlausurenZuTermin = new ArrayList<>(listKlausurenZuTermin);
			for (final @NotNull GostKursklausur k1 : listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (final @NotNull GostKursklausur k2 : copyListKlausurenZuTermin)
					anzahl += gibKonfliktKursklausurKursklausurIds(k1.id, k2.id).size();
			}
		}
		return anzahl;
	}

	/**
	 * Prüft, ob die Schülermengen zweier Kursklausuren disjunkt sind. Es werden die
	 * Schüler-IDs, die beide Klausuren schreiben, als Liste zurückgegeben. Wenn die
	 * zurückgegebene Liste leer ist, gibt es keine Übereinstimmungen.
	 *
	 * @param idKursklausur1 die ID der ersten zu prüfenden Kursklausur
	 * @param idKursklausur2 die ID der zweiten zu prüfenden Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die beide Klausuren schreiben.
	 */
	public @NotNull List<@NotNull Long> gibKonfliktKursklausurKursklausurIds(final long idKursklausur1, final long idKursklausur2) {
		final GostKursklausur klausur1 = DeveloperNotificationException.ifMapGetIsNull(_mapIdKursklausur, idKursklausur1);
		final GostKursklausur klausur2 = DeveloperNotificationException.ifMapGetIsNull(_mapIdKursklausur, idKursklausur2);
		return gibKonfliktKursklausurKursklausur(klausur1, klausur2);
	}

	/**
	 * Prüft, ob die Schülermengen zweier Kursklausuren disjunkt sind. Es werden die
	 * Schüler-IDs, die beide Klausuren schreiben, als Liste zurückgegeben. Wenn die
	 * zurückgegebene Liste leer ist, gibt es keine Übereinstimmungen.
	 *
	 * @param klausur1 die erste zu prüfende Kursklausur
	 * @param klausur2 die zweite zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die beide Klausuren schreiben.
	 */
	public @NotNull List<@NotNull Long> gibKonfliktKursklausurKursklausur(final @NotNull GostKursklausur klausur1, final @NotNull GostKursklausur klausur2) {
		if (klausur1 == klausur2) {
			return new ArrayList<>();
		}
		final List<@NotNull Long> konflikte = new ArrayList<>(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
	}

}
