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
	public List<@NotNull GostKursklausur> getKursklausurenOhneTermin(int quartal) {
		HashMap<@NotNull Long, @NotNull Vector<@NotNull GostKursklausur>> mapTerminKursklausuren = _mapQuartalTerminKursklausuren.get(quartal);
		if (mapTerminKursklausuren == null) {
			// TODO Fehlerbehandlung?
			return new Vector<>();
		}
		return mapTerminKursklausuren.get(-1L);
	}

	private void helpKonstruktor() {
		for (@NotNull
		GostKursklausur kk : _klausuren) {

			// Füllen von _mapIdKursklausuren
			_mapIdKursklausur.put(kk.id, kk);

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
			_mapIdKlausurtermin.put(t.id, t);
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
		if (klausur1 == null || klausur2 == null) {
			// TODO Errorhandling
			return new Vector<>();
		}
		List<@NotNull Long> konflikte = new Vector<>(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
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
		return _mapTerminSchuelerids.get(idTermin);
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

}
