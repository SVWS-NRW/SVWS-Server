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

	/** Eine Map quartal -> Liste von GostKursklausuren */
	private final @NotNull HashMap<@NotNull Integer, @NotNull Vector<@NotNull GostKursklausur>> _mapQuartalKursKlausuren = new HashMap<>();

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
	public GostKursklausurManager(@NotNull List<@NotNull GostKursklausur> klausuren, @NotNull List<@NotNull GostKlausurtermin> termine) {
		_klausuren = klausuren;
		helpKonstruktor();
		for (@NotNull GostKlausurtermin t : termine) {
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
	public GostKursklausurManager(@NotNull List<@NotNull GostKursklausur> klausuren) {
		_klausuren = klausuren;
		helpKonstruktor();
	}

	private void helpKonstruktor() {
		for (@NotNull GostKursklausur kk : _klausuren) {
			// Füllen von _mapIdKursklausuren
			_mapIdKursklausur.put(kk.id, kk);

			addKlausurToInternalMaps(kk);

			// Füllen von _mapQuartalKursKlausuren
			Vector<@NotNull GostKursklausur> listKursklausurenMapQuartalKursKlausuren = _mapQuartalKursKlausuren.get(kk.quartal);
			if (listKursklausurenMapQuartalKursKlausuren == null) {
				listKursklausurenMapQuartalKursKlausuren = new Vector<>();
				_mapQuartalKursKlausuren.put(kk.quartal, listKursklausurenMapQuartalKursKlausuren);
			}
			listKursklausurenMapQuartalKursKlausuren.add(kk);

			// Füllen von _mapTerminSchuelerids
			if (kk.idTermin != null) {
				Vector<@NotNull Long> listSchuelerIds = _mapTerminSchuelerids.get(kk.idTermin);
				if (listSchuelerIds == null) {
					listSchuelerIds = new Vector<>();
					_mapTerminSchuelerids.put(kk.idTermin, listSchuelerIds);
				}
				listSchuelerIds.addAll(kk.schuelerIds);
			}

		}
	}

	private void addKlausurToInternalMaps(@NotNull GostKursklausur kk) {

		// Füllen von _mapTermineKursklausuren
		Vector<@NotNull GostKursklausur> listKursklausurenMapTermine = _mapTerminKursklausuren.get(kk.idTermin == null ? -1 : kk.idTermin);
		if (listKursklausurenMapTermine == null) {
			listKursklausurenMapTermine = new Vector<>();
			_mapTerminKursklausuren.put(kk.idTermin == null ? -1 : kk.idTermin, listKursklausurenMapTermine);
		}
		listKursklausurenMapTermine.add(kk);

		// Füllen von _mapQuartalTerminKursklausuren
		HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapTerminKursklausuren = _mapQuartalTerminKursklausuren.get(kk.quartal);
		if (mapTerminKursklausuren == null) {
			mapTerminKursklausuren = new HashMap<>();
			_mapQuartalTerminKursklausuren.put(kk.quartal, mapTerminKursklausuren);
		}
		Vector<@NotNull GostKursklausur> listKursklausurenMapQuartalmapTermine = mapTerminKursklausuren.get(kk.idTermin == null ? -1 : kk.idTermin);
		if (listKursklausurenMapQuartalmapTermine == null) {
			listKursklausurenMapQuartalmapTermine = new Vector<>();
			mapTerminKursklausuren.put(kk.idTermin == null ? -1 : kk.idTermin, listKursklausurenMapQuartalmapTermine);
		}
		listKursklausurenMapQuartalmapTermine.add(kk);

	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Termin einer Klausur
	 * geändert hat.
	 * 
	 * @param klausur das GostKursklausur-Objekt
	 */
	public void updateKursklausur(@NotNull GostKursklausur klausur) {

		List<GostKursklausur> terminNeuKlausuren = _mapTerminKursklausuren.get(klausur.idTermin == null ? -1 : klausur.idTermin);
		if (terminNeuKlausuren == null || !terminNeuKlausuren.contains(klausur)) {
			// Termin-ID hat sich geändert
			long oldTerminId = -2;

			// aus _mapTerminKursklausuren löschen
			// for (@NotNull Entry<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>>
			// entry : _mapTerminKursklausuren.entrySet()) {
			for (@NotNull Long key : _mapTerminKursklausuren.keySet()) {
				Vector<@NotNull GostKursklausur> entry = _mapTerminKursklausuren.get(key);
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
			HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> quartalMap = _mapQuartalTerminKursklausuren.get(klausur.quartal);
			if (quartalMap != null) {
				List<@NotNull GostKursklausur> listOldQuartalTerminKursklausuren = quartalMap.get(oldTerminId);
				if (listOldQuartalTerminKursklausuren != null)
					listOldQuartalTerminKursklausuren.remove(klausur);
			} else {
				// TODO Fehler, denn kann eigentlich nicht sein.
			}

			// _mapQuartalKursKlausuren muss nicht geändert werden

			// _mapTerminSchuelerids aktualisieren
			updateSchuelerIdsZuTermin(oldTerminId);
			if (klausur.idTermin != null)
				updateSchuelerIdsZuTermin(klausur.idTermin);

			addKlausurToInternalMaps(klausur);
		}

	}

	private void updateSchuelerIdsZuTermin(long idTermin) {
		Vector<@NotNull Long> listSchuelerIds = new Vector<>();
		_mapTerminSchuelerids.put(idTermin, listSchuelerIds);
		List<@NotNull GostKursklausur> listKlausurenZuTermin = _mapTerminKursklausuren.get(idTermin);
		if (listKlausurenZuTermin == null)
			return;
		for (@NotNull GostKursklausur k : listKlausurenZuTermin) {
			listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	/**
	 * Fügt den internen Strukturen einen neuen Klausurtermin hinzu.
	 * 
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public void addTermin(@NotNull GostKlausurtermin termin) {
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
	public void addKlausur(@NotNull GostKursklausur klausur) {
		_klausuren.add(klausur);
		_mapIdKursklausur.put(klausur.id, klausur);
		addKlausurToInternalMaps(klausur);
	}

	/**
	 * Fügt den internen Strukturen neue Kursklausuren hinzu.
	 * 
	 * @param klausuren die Liste von GostKursklausur-Objekten
	 */
	public void addKlausuren(@NotNull List<@NotNull GostKursklausur> klausuren) {
		for (@NotNull GostKursklausur klausur : klausuren) {
			addKlausur(klausur);
		}
	}

	/**
	 * Löscht einen Klausurtermin aus den internen Strukturen
	 * 
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public void removeTermin(@NotNull GostKlausurtermin termin) {
		Vector<@NotNull GostKlausurtermin> listKlausurtermineMapQuartalKlausurtermine = _mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine == null) {
			// TODO Fehlerbehandlung
			return;
		}
		listKlausurtermineMapQuartalKlausurtermine.remove(termin);

		List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausuren(termin.id);
		if (listKlausurenZuTermin != null) {
			listKlausurenZuTermin = new Vector<>(listKlausurenZuTermin);
			for (@NotNull GostKursklausur k : listKlausurenZuTermin) {
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
	public @NotNull List<@NotNull GostKursklausur> getKursklausuren(Long idTermin) {
		return _mapTerminKursklausuren.get(idTermin == null ? -1 : idTermin);
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
	public List<@NotNull GostKursklausur> getKursklausuren(int quartal) {
		return _mapQuartalKursKlausuren.get(quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenOhneTermin() {
		return getKursklausurenOhneTermin(-1);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> getKursklausurenOhneTermin(int quartal) {
		HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapTerminKursklausuren = _mapQuartalTerminKursklausuren.get(quartal <= 0 ? -1 : quartal);
		if (mapTerminKursklausuren == null) {
			// TODO Fehlerbehandlung?
			return new Vector<>();
		}
		List<@NotNull GostKursklausur> klausuren = mapTerminKursklausuren.get(-1L);
		return klausuren != null ? klausuren : new Vector<>();
	}

	/**
	 * Gibt das GostKlausurtermin-Objekt zur übergebenen id zurück.
	 * 
	 * @param idTermin die ID des Klausurtermins
	 * 
	 * @return das GostKlausurtermin-Objekt
	 */
	public GostKlausurtermin gibGostKlausurtermin(long idTermin) {
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
	public List<@NotNull Long> gibSchuelerIDsZuTermin(long idTermin) {
		List<@NotNull Long> schuelerIds = _mapTerminSchuelerids.get(idTermin);
		return schuelerIds != null || !_mapIdKlausurtermin.containsKey(idTermin) ? schuelerIds : new Vector<>();
	}

	/**
	 * Gibt das GostKursklausur-Objekt zur übergebenen id zurück.
	 * 
	 * @param idKursklausur die ID der Kursklausur
	 * 
	 * @return das GostKursklausur-Objekt
	 */
	public GostKursklausur gibKursklausur(long idKursklausur) {
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
	public GostKlausurtermin gibKlausurtermin(long idTermin) {
		return _mapIdKlausurtermin.get(idTermin);
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> getKlausurtermine(int quartal) {
		List<@NotNull GostKlausurtermin> termine = _mapQuartalKlausurtermine.get(quartal <= 0 ? -1 : quartal);
		return termine != null ? termine : new Vector<>();
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
	public @NotNull List<@NotNull Long> gibKonfliktTerminKursklausur(long idTermin, long idKursklausur) {
		List<@NotNull Long> schuelerIds = gibSchuelerIDsZuTermin(idTermin);
		if (schuelerIds == null) {
			return new Vector<>();
		}

		GostKursklausur klausur = _mapIdKursklausur.get(idKursklausur);
		if (klausur == null) {
			// TODO Errorhandling
			return new Vector<>();
		}

		@NotNull List<@NotNull Long> konflikte = new Vector<>(schuelerIds);

		konflikte.retainAll(klausur.schuelerIds);
		return konflikte;
	}

	/**
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben..
	 * 
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 * 
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public int gibAnzahlKonflikteZuTermin(long idTermin) {
		int anzahl = 0;
		List<@NotNull GostKursklausur> listKlausurenZuTermin = getKursklausuren(idTermin);
		if (listKlausurenZuTermin != null) {
			List<@NotNull GostKursklausur> copyListKlausurenZuTermin = new Vector<>(listKlausurenZuTermin);
			for (@NotNull GostKursklausur k1 : listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (@NotNull GostKursklausur k2 : copyListKlausurenZuTermin)
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
	public @NotNull List<@NotNull Long> gibKonfliktKursklausurKursklausur(long idKursklausur1, long idKursklausur2) {
		GostKursklausur klausur1 = _mapIdKursklausur.get(idKursklausur1);
		GostKursklausur klausur2 = _mapIdKursklausur.get(idKursklausur2);
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
	public @NotNull List<@NotNull Long> gibKonfliktKursklausurKursklausur(GostKursklausur klausur1, GostKursklausur klausur2) {
		if (klausur1 == null || klausur2 == null) {
			// TODO Errorhandling
			return new Vector<>();
		}
		List<@NotNull Long> konflikte = new Vector<>(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
	}

}
