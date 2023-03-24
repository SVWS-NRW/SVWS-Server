package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurtermin;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKursklausur}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Kursklausuren eines Gost-Halbjahres eines Abiturjahrgangs verwaltet.
 */
public class GostKursklausurManager {

	/** Die Kursklausuren, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKursklausur> _klausuren;

	/** Die Klausurtermine, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKlausurtermin> _termine = new Vector<>();

	/** Eine Map quartal -> Liste von GostKlausurterminen */
	private final @NotNull HashMap<@NotNull Integer, @NotNull Vector<@NotNull GostKlausurtermin>> _mapQuartalKlausurtermine = new HashMap<>();

	/** Eine Map id -> GostKursklausur */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostKursklausur> _mapIdKursklausur = new HashMap<>();

	/** Eine Map idTermin -> Liste von GostKursklausuren */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> _mapTerminKursklausuren = new HashMap<>();

	/** Eine Map quartal, idTermin -> Liste von GostKursklausuren */
	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>> _mapQuartalTerminKursklausuren = new HashMap<>();

	/** Eine Map quartal, kursart, idTermin -> Liste von GostKursklausuren */
	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>>> _mapQuartalKursartTerminKursklausuren = new HashMap<>();

	/** Eine Map quartal -> Liste von GostKursklausuren */
	private final @NotNull HashMap<@NotNull Integer, @NotNull Vector<@NotNull GostKursklausur>> _mapQuartalKursKlausuren = new HashMap<>();

//	/** Eine Map quartal -> kursart -> Liste von GostKursklausuren */
//	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull String, @NotNull Vector<@NotNull GostKursklausur>>> _mapQuartalKursartKursKlausuren = new HashMap<>();

	/** Eine Map idTermin -> Liste von Schüler-IDs */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull Long>> _mapTerminSchuelerids = new HashMap<>();

	/** Eine Map idTermin -> GostKlausurtermin */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostKlausurtermin> _mapIdKlausurtermin = new HashMap<>();

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
			addTermin(t);
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
			Vector<@NotNull GostKursklausur> listKursklausurenMapQuartalKursKlausuren = _mapQuartalKursKlausuren.get(kk.quartal);
			if (listKursklausurenMapQuartalKursKlausuren == null)
				_mapQuartalKursKlausuren.put(kk.quartal, listKursklausurenMapQuartalKursKlausuren = new Vector<>());
			listKursklausurenMapQuartalKursKlausuren.add(kk);

//			// Füllen von _mapQuartalKursartKursKlausuren
//			HashMap<@NotNull String, @NotNull Vector<@NotNull GostKursklausur>> mapKursklausurenMapQuartalKursartKursKlausuren = _mapQuartalKursartKursKlausuren.get(kk.quartal);
//			if (mapKursklausurenMapQuartalKursartKursKlausuren == null)
//				_mapQuartalKursartKursKlausuren.put(kk.quartal, mapKursklausurenMapQuartalKursartKursKlausuren = new HashMap<>());
//			Vector<@NotNull GostKursklausur> listKursklausurenMapQuartalKursartKursKlausuren = mapKursklausurenMapQuartalKursartKursKlausuren.get(kk.kursartAllg);
//			if (listKursklausurenMapQuartalKursartKursKlausuren == null)
//				mapKursklausurenMapQuartalKursartKursKlausuren.put(kk.kursartAllg, listKursklausurenMapQuartalKursartKursKlausuren = new Vector<>());
//			listKursklausurenMapQuartalKursartKursKlausuren.add(kk);

			// Füllen von _mapTerminSchuelerids
			if (kk.idTermin != null) {
				Vector<@NotNull Long> listSchuelerIds = _mapTerminSchuelerids.get(kk.idTermin);
				if (listSchuelerIds == null)
					_mapTerminSchuelerids.put(kk.idTermin, listSchuelerIds = new Vector<>());
				listSchuelerIds.addAll(kk.schuelerIds);
			}

		}
	}

	private void addKlausurToInternalMaps(final @NotNull GostKursklausur kk) {

		// Füllen von _mapTermineKursklausuren
		Vector<@NotNull GostKursklausur> listKursklausurenMapTermine = _mapTerminKursklausuren.get(kk.idTermin == null ? -1 : kk.idTermin);
		if (listKursklausurenMapTermine == null)
			_mapTerminKursklausuren.put(kk.idTermin == null ? -1 : kk.idTermin, listKursklausurenMapTermine = new Vector<>());
		listKursklausurenMapTermine.add(kk);

		// Füllen von _mapQuartalTerminKursklausuren
		HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapTerminKursklausuren = _mapQuartalTerminKursklausuren.get(kk.quartal);
		if (mapTerminKursklausuren == null)
			_mapQuartalTerminKursklausuren.put(kk.quartal, mapTerminKursklausuren = new HashMap<>());
		Vector<@NotNull GostKursklausur> listKursklausurenMapQuartalmapTermine = mapTerminKursklausuren.get(kk.idTermin == null ? -1 : kk.idTermin);
		if (listKursklausurenMapQuartalmapTermine == null)
			mapTerminKursklausuren.put(kk.idTermin == null ? -1 : kk.idTermin, listKursklausurenMapQuartalmapTermine = new Vector<>());
		listKursklausurenMapQuartalmapTermine.add(kk);

		// Füllen von _mapQuartalKursartTerminKursklausuren
		HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>> mapKursartTerminKursklausuren = _mapQuartalKursartTerminKursklausuren.get(kk.quartal);
		if (mapKursartTerminKursklausuren == null)
			_mapQuartalKursartTerminKursklausuren.put(kk.quartal, mapKursartTerminKursklausuren = new HashMap<>());

		HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapKursklausurenMapQuartalKursartTerminKursKlausuren = mapKursartTerminKursklausuren.get(kk.kursart);
		if (mapKursklausurenMapQuartalKursartTerminKursKlausuren == null)
			mapKursartTerminKursklausuren.put(kk.kursart, mapKursklausurenMapQuartalKursartTerminKursKlausuren = new HashMap<>());

		Vector<@NotNull GostKursklausur> listKursklausurenMapQuartalKursartmapTermine = mapKursklausurenMapQuartalKursartTerminKursKlausuren.get(kk.idTermin == null ? -1 : kk.idTermin);
		if (listKursklausurenMapQuartalKursartmapTermine == null)
			mapKursklausurenMapQuartalKursartTerminKursKlausuren.put(kk.idTermin == null ? -1 : kk.idTermin, listKursklausurenMapQuartalKursartmapTermine = new Vector<>());
		listKursklausurenMapQuartalKursartmapTermine.add(kk);

	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Termin einer Klausur
	 * geändert hat.
	 * 
	 * @param klausur das GostKursklausur-Objekt
	 */
	public void updateKursklausur(final @NotNull GostKursklausur klausur) {

		final List<GostKursklausur> terminNeuKlausuren = _mapTerminKursklausuren.get(klausur.idTermin == null ? -1 : klausur.idTermin);
		if (terminNeuKlausuren == null || !terminNeuKlausuren.contains(klausur)) {
			// Termin-ID hat sich geändert
			long oldTerminId = -2;

			// aus _mapTerminKursklausuren löschen
			// for (final @NotNull Entry<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>
			// entry : _mapTerminKursklausuren.entrySet()) {
			for (final @NotNull Long key : _mapTerminKursklausuren.keySet()) {
				final Vector<@NotNull GostKursklausur> entry = _mapTerminKursklausuren.get(key);
				if (entry == null) {
					// TODO Fehler, denn kann eigentlich nicht sein.
				} else {
					if (entry.contains(klausur)) {
						oldTerminId = key;
						entry.remove(klausur);
					}
				}
			}

			// aus _mapQuartalTerminKursklausuren löschen
			final HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> quartalMap = _mapQuartalTerminKursklausuren.get(klausur.quartal);
			if (quartalMap != null) {
				final List<@NotNull GostKursklausur> listOldQuartalTerminKursklausuren = quartalMap.get(oldTerminId);
				if (listOldQuartalTerminKursklausuren != null)
					listOldQuartalTerminKursklausuren.remove(klausur);
			} else {
				// TODO Fehler, denn kann eigentlich nicht sein.
			}

			// aus _mapQuartalTerminKursklausuren löschen
			final HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>> quartalKursartMap = _mapQuartalKursartTerminKursklausuren.get(klausur.quartal);
			if (quartalKursartMap != null) {
				final HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> kursartMap = quartalKursartMap.get(klausur.kursart);
				if (kursartMap != null) {
					final List<@NotNull GostKursklausur> listOldQuartalTerminKursklausuren = kursartMap.get(oldTerminId);
					if (listOldQuartalTerminKursklausuren != null)
						listOldQuartalTerminKursklausuren.remove(klausur);
				} else {
					// TODO Fehler, denn kann eigentlich nicht sein.
				}
			} else {
				// TODO Fehler, denn kann eigentlich nicht sein.
			}

			// _mapQuartalKursKlausuren muss nicht geändert werden

			addKlausurToInternalMaps(klausur);

			// _mapTerminSchuelerids aktualisieren
			updateSchuelerIdsZuTermin(oldTerminId);
			if (klausur.idTermin != null)
				updateSchuelerIdsZuTermin(klausur.idTermin);

		}

	}

	private void updateSchuelerIdsZuTermin(final long idTermin) {
		final Vector<@NotNull Long> listSchuelerIds = new Vector<>();
		_mapTerminSchuelerids.put(idTermin, listSchuelerIds);
		final List<@NotNull GostKursklausur> listKlausurenZuTermin = _mapTerminKursklausuren.get(idTermin);
		if (listKlausurenZuTermin == null)
			return;
		for (final @NotNull GostKursklausur k : listKlausurenZuTermin) {
			listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	/**
	 * Fügt den internen Strukturen einen neuen Klausurtermin hinzu.
	 * 
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public void addTermin(final @NotNull GostKlausurtermin termin) {
		_termine.add(termin);
		// Füllen von _mapIdKlausurtermin
		_mapIdKlausurtermin.put(termin.id, termin);

		// Füllen von _mapQuartalKlausurtermine
		Vector<@NotNull GostKlausurtermin> listKlausurtermineMapQuartalKlausurtermine = _mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine == null) {
			listKlausurtermineMapQuartalKlausurtermine = new Vector<>();
			_mapQuartalKlausurtermine.put(termin.quartal, listKlausurtermineMapQuartalKlausurtermine);
		}
		listKlausurtermineMapQuartalKlausurtermine.add(termin);
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
	 * Löscht einen Klausurtermin aus den internen Strukturen
	 * 
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public void removeTermin(final @NotNull GostKlausurtermin termin) {
		final Vector<@NotNull GostKlausurtermin> listKlausurtermineMapQuartalKlausurtermine = _mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine == null) {
			// TODO Fehlerbehandlung
			return;
		}
		listKlausurtermineMapQuartalKlausurtermine.remove(termin);

		List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausuren(termin.id);
		if (listKlausurenZuTermin != null) {
			listKlausurenZuTermin = new Vector<>(listKlausurenZuTermin);
			for (final @NotNull GostKursklausur k : listKlausurenZuTermin) {
				k.idTermin = null;
				updateKursklausur(k);
			}
		}
		_termine.remove(termin);
		_mapIdKlausurtermin.remove(termin.id);

	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin
	 * 
	 * @param idTermin die ID des Klausurtermins
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausuren(final Long idTermin) {
		final List<@NotNull GostKursklausur> klausuren = _mapTerminKursklausuren.get(idTermin == null ? -1 : idTermin);
		return klausuren != null ? klausuren : new Vector<>();
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
	public List<@NotNull GostKursklausur> getKursklausuren(final int quartal) {
		return _mapQuartalKursKlausuren.get(quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenOhneTermin() {
		return getKursklausuren(-1L);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenOhneTermin(final int quartal) {
		final HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapTerminKursklausuren = _mapQuartalTerminKursklausuren.get(quartal <= 0 ? -1 : quartal);
		if (mapTerminKursklausuren == null) {
			// TODO Fehlerbehandlung?
			return new Vector<>();
		}
		final List<@NotNull GostKursklausur> klausuren = mapTerminKursklausuren.get(-1L);
		return klausuren != null ? klausuren : new Vector<>();
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
		final List<@NotNull List<@NotNull GostKursklausur>> retList = new Vector<>();
		final HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>> mapKursartTerminKursklausuren = _mapQuartalKursartTerminKursklausuren
				.get(quartal <= 0 ? -1 : quartal);
		if (mapKursartTerminKursklausuren != null) {
			for (final HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapKursarten : mapKursartTerminKursklausuren.values()) {
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
		return schuelerIds != null || !_mapIdKlausurtermin.containsKey(idTermin) ? schuelerIds : new Vector<>();
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
	public @NotNull List<@NotNull GostKlausurtermin> getKlausurtermine(final int quartal) {
		final List<@NotNull GostKlausurtermin> termine = _mapQuartalKlausurtermine.get(quartal <= 0 ? -1 : quartal);
		return termine != null ? termine : new Vector<>();
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
		final @NotNull List<@NotNull Long> konflikte = new Vector<>();

		final List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausuren(termin.id);
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
			return new Vector<>();
		}

		final List<@NotNull Long> schuelerIds = gibSchuelerIDsZuTermin(termin.id);
		if (schuelerIds == null) {
			return new Vector<>();
		}

		final @NotNull List<@NotNull Long> konflikte = new Vector<>(schuelerIds);

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
	public @NotNull List<@NotNull Long> gibKonfliktTerminKursklausur(final long idTermin, final long idKursklausur) {
		final GostKursklausur klausur = _mapIdKursklausur.get(idKursklausur);
		final GostKlausurtermin termin = _mapIdKlausurtermin.get(idTermin);

		if (klausur == null || termin == null) {
			// TODO Errorhandling
			return new Vector<>();
		}

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
		final List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausuren(idTermin);
		if (listKlausurenZuTermin != null) {
			final List<@NotNull GostKursklausur> copyListKlausurenZuTermin = new Vector<>(listKlausurenZuTermin);
			for (final @NotNull GostKursklausur k1 : listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (final @NotNull GostKursklausur k2 : copyListKlausurenZuTermin)
					anzahl += gibKonfliktKursklausurKursklausur(k1.id, k2.id).size();
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
	public @NotNull List<@NotNull Long> gibKonfliktKursklausurKursklausur(final long idKursklausur1, final long idKursklausur2) {
		final GostKursklausur klausur1 = _mapIdKursklausur.get(idKursklausur1);
		final GostKursklausur klausur2 = _mapIdKursklausur.get(idKursklausur2);
		if (klausur1 == null || klausur2 == null) {
			// TODO Errorhandling
			return new Vector<>();
		}
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
			return new Vector<>();
		}
		final List<@NotNull Long> konflikte = new Vector<>(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
	}

}
