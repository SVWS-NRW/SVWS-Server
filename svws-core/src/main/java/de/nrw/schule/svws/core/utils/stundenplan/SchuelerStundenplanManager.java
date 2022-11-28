package de.nrw.schule.svws.core.utils.stundenplan;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplan;
import de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplanUnterricht;
import de.nrw.schule.svws.core.data.stundenplan.StundenplanZeitraster;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link SchuelerStundenplan}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class SchuelerStundenplanManager {

	/** Die Stundenplandaten, die im Manager vorhanden sind */
	private final @NotNull SchuelerStundenplan _daten;

	/** Eine Map idUnterricht -> SchuelerStundenplanUnterricht */
	private final @NotNull HashMap<@NotNull Long, @NotNull SchuelerStundenplanUnterricht> _mapUnterricht = new HashMap<>();

	/** Eine Map idZeitraster -> Liste von Unterricht */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull SchuelerStundenplanUnterricht>> _mapZeitrasterUnterricht = new HashMap<>();

	/** Eine Map wochentyp, idZeitraster -> Liste von Unterricht */
	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull SchuelerStundenplanUnterricht>>> _mapWocheZeitrasterUnterricht = new HashMap<>();

	/** Eine Map idZeitraster -> Zeitraster */
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _mapZeitraster = new HashMap<>();

	/** Eine Map wochentag, stunde -> Zeitraster */
	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull Integer, @NotNull StundenplanZeitraster>> _mapWochentagStundeZeitraster = new HashMap<>();

	/** Eine Map wochentag -> Liste von Zeitrastern */
	private final @NotNull HashMap<@NotNull Integer, @NotNull Vector<@NotNull StundenplanZeitraster>> _mapWochentagZeitraster = new HashMap<>();

	/** Eine Map stunde -> Liste von Zeitrastern */
	private final @NotNull HashMap<@NotNull Integer, @NotNull Vector<@NotNull StundenplanZeitraster>> _mapStundeZeitraster = new HashMap<>();

	/**
	 * Der minimale Wochentag, der in den Stundenplandaten vorkommt, z.B. 1 für
	 * Montag
	 */
	private int minWochentag = Integer.MAX_VALUE;

	/**
	 * Der maximale Wochentag, der in den Stundenplandaten vorkommt, z.B. 5 für
	 * Freitag
	 */
	private int maxWochentag = Integer.MIN_VALUE;

	/**
	 * Die minimale Unterrichtsstunde, die in den Stundenplandaten vorkommt, z.B. 1
	 * für die 1. Stunde
	 */
	private int minStunde = Integer.MAX_VALUE;

	/**
	 * Die maximale Unterrichtsstunde, die in den Stundenplandaten vorkommt, z.B. 9
	 * für die 9. Stunde
	 */
	private int maxStunde = Integer.MIN_VALUE;

	/**
	 * Liefert den minimalen Wochentag als int, z.B. 1 für Montag
	 * 
	 * @return den minimalen Wochentag
	 */
	public int getMinWochentag() {
		return minWochentag;
	}

	/**
	 * Liefert den maximalen Wochentag als int, z.B. 5 für Freitag
	 * 
	 * @return den maximalen Wochentag
	 */
	public int getMaxWochentag() {
		return maxWochentag;
	}

	/**
	 * Liefert die minimale Unterrichtsstunde als int, z.B. 1 für die 1. Stunde
	 * 
	 * @return die minimale Unterrichtsstunde
	 */
	public int getMinStunde() {
		return minStunde;
	}

	/**
	 * Liefert die maximale Unterrichtsstunde als int, z.B. 9 für die 9. Stunde
	 * 
	 * @return die maximale Unterrichtsstunde
	 */
	public int getMaxStunde() {
		return maxStunde;
	}

	/**
	 * Liefert das SchulerStundenplanUnterricht-Objekt zur übergebenen
	 * Unterrichts-ID
	 * 
	 * @param idUnterricht die ID des Unterrichts
	 * 
	 * @return das SchulerStundenplanUnterricht-Objekt
	 */
	public SchuelerStundenplanUnterricht getUnterrichtById(long idUnterricht) {
		return _mapUnterricht.get(idUnterricht);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die im übergeben Zeitraster liegen.
	 * 
	 * @param idZeitraster die ID des Zeitrasters
	 * 
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public List<@NotNull SchuelerStundenplanUnterricht> getUnterrichtByZeitrasterId(long idZeitraster) {
		return _mapZeitrasterUnterricht.get(idZeitraster);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die im übergeben Wochentyp und im übergebenen Zeitraster liegen.
	 * 
	 * @param wochentyp der Wochentyp
	 * @param idZeitraster die ID des Zeitrasters
	 * 
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public List<@NotNull SchuelerStundenplanUnterricht> getUnterrichtByWocheZeitrasterId(int wochentyp,
			long idZeitraster) {
		return getUnterrichtByWocheZeitrasterId(wochentyp, idZeitraster, false);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die im übergeben Wochentyp und im übergebenen Zeitraster liegen.
	 * Je nach Parameter inklWoche0 wird die Liste um den Unterricht aus Woche 0 ergänzt.
	 * 
	 * @param wochentyp der Wochentyp
	 * @param idZeitraster die ID des Zeitrasters
	 * @param inklWoche0 Ergänzung des Unterrichts um Woche 0
	 * 
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public List<@NotNull SchuelerStundenplanUnterricht> getUnterrichtByWocheZeitrasterId(int wochentyp,
			long idZeitraster, boolean inklWoche0) {
		HashMap<@NotNull Long, @NotNull Vector<@NotNull SchuelerStundenplanUnterricht>> mapZeitrasterUnterricht_Wochentyp = _mapWocheZeitrasterUnterricht
				.get(wochentyp);
		if (mapZeitrasterUnterricht_Wochentyp == null) {
			// TODO Exceptionhandling?
			return null;
		}
		List<@NotNull SchuelerStundenplanUnterricht> retList = mapZeitrasterUnterricht_Wochentyp
				.get(idZeitraster);
		if (retList == null)
			retList = new Vector<>();
		if (wochentyp != 0 && inklWoche0) {
			HashMap<@NotNull Long, @NotNull Vector<@NotNull SchuelerStundenplanUnterricht>> mapZeitrasterUnterricht_Woche0 = _mapWocheZeitrasterUnterricht
					.get(0);
			if (mapZeitrasterUnterricht_Woche0 == null) {
				// TODO Exceptionhandling?
				return retList;
			}
			List<@NotNull SchuelerStundenplanUnterricht> listUnterricht_Woche0 = mapZeitrasterUnterricht_Woche0
					.get(idZeitraster);
			if (listUnterricht_Woche0 == null) {
				// TODO Exceptionhandling?
				return retList;
			}
			retList.addAll(listUnterricht_Woche0);
		}
		return retList;
	}

	/**
	 * Liefert das Zeitraster-Objekt zur übergebenen ID.
	 * 
	 * @param idZeitraster die ID des Zeitrasters
	 * 
	 * @return das Zeitraster-Objekt
	 */
	public StundenplanZeitraster getZeitrasterById(long idZeitraster) {
		return _mapZeitraster.get(idZeitraster);
	}

	/**
	 * Liefert das Zeitraster-Objekt an dem übergebenen Wochentag in der übergebenen Stunde.
	 * 
	 * @param wochentag der Wochentag
	 * @param stunde die Stunde
	 * @return das Zeitraster-Objekt
	 */
	public StundenplanZeitraster getZeitrasterByWochentagStunde(int wochentag, int stunde) {
		HashMap<@NotNull Integer, @NotNull StundenplanZeitraster> map = _mapWochentagStundeZeitraster.get(wochentag);
		if (map == null) {
			// TODO Exceptionhandling
			return null;
		}
		return map.get(stunde);
	}

	/**
	 * Liefert die Liste von Zeitraster-Objekten am übergebenen Wochentag.
	 * 
	 * @param wochentag der Wochentag
	 * 
	 * @return Liste von Zeitraster-Objekten
	 */
	public Vector<@NotNull StundenplanZeitraster> getZeitrasterByWochentag(int wochentag) {
		return _mapWochentagZeitraster.get(wochentag);
	}

	/**
	 * Liefert die Liste von Zeitraster-Objekten in der übergebenen Stunde.
	 * 
	 * @param stunde die Unterrichtsstunde
	 * 
	 * @return Liste von Zeitraster-Objekten
	 */
	public Vector<@NotNull StundenplanZeitraster> getZeitrasterByStunde(int stunde) {
		return _mapStundeZeitraster.get(stunde);
	}

	/**
	 * Erstellt einen neuen Manager mit den angegebenen Stundenplandaten und erzeugt
	 * die privaten Attribute.
	 * 
	 * @param daten die Stundenplandaten
	 */
	public SchuelerStundenplanManager(@NotNull SchuelerStundenplan daten) {
		_daten = daten;
		for (StundenplanZeitraster sz : _daten.zeitraster) {
			if (sz.wochentag < minWochentag)
				minWochentag = sz.wochentag;
			if (sz.wochentag > maxWochentag)
				maxWochentag = sz.wochentag;
			if (sz.unterrichtstunde < minStunde)
				minStunde = sz.unterrichtstunde;
			if (sz.unterrichtstunde > maxStunde)
				maxStunde = sz.unterrichtstunde;

			_mapZeitraster.put(sz.id, sz);

			Vector<@NotNull StundenplanZeitraster> listWochentagZeitraster = _mapWochentagZeitraster.get(sz.wochentag);
			if (listWochentagZeitraster == null) {
				listWochentagZeitraster = new Vector<>();
				_mapWochentagZeitraster.put(sz.wochentag, listWochentagZeitraster);
			}
			listWochentagZeitraster.add(sz);

			Vector<@NotNull StundenplanZeitraster> listStundeZeitraster = _mapStundeZeitraster.get(sz.unterrichtstunde);
			if (listStundeZeitraster == null) {
				listStundeZeitraster = new Vector<>();
				_mapStundeZeitraster.put(sz.unterrichtstunde, listStundeZeitraster);
			}
			listStundeZeitraster.add(sz);

			HashMap<@NotNull Integer, @NotNull StundenplanZeitraster> mapStundeUnterricht = _mapWochentagStundeZeitraster
					.get(sz.wochentag);
			if (mapStundeUnterricht == null) {
				mapStundeUnterricht = new HashMap<>();
				_mapWochentagStundeZeitraster.put(sz.wochentag, mapStundeUnterricht);
			}
			mapStundeUnterricht.put(sz.unterrichtstunde, sz);
		}

		for (SchuelerStundenplanUnterricht ssu : _daten.unterricht) {
			_mapUnterricht.put(ssu.idUnterricht, ssu);
			Vector<@NotNull SchuelerStundenplanUnterricht> listZeitrasterUnterricht = _mapZeitrasterUnterricht
					.get(ssu.idZeitraster);
			if (listZeitrasterUnterricht == null) {
				listZeitrasterUnterricht = new Vector<>();
				_mapZeitrasterUnterricht.put(ssu.idZeitraster, listZeitrasterUnterricht);
			}
			listZeitrasterUnterricht.add(ssu);

			HashMap<@NotNull Long, @NotNull Vector<@NotNull SchuelerStundenplanUnterricht>> mapZeitrasterUnterricht = _mapWocheZeitrasterUnterricht
					.get(ssu.wochentyp);
			if (mapZeitrasterUnterricht == null) {
				mapZeitrasterUnterricht = new HashMap<>();
				_mapWocheZeitrasterUnterricht.put(ssu.wochentyp, mapZeitrasterUnterricht);
			}
			Vector<@NotNull SchuelerStundenplanUnterricht> listWocheZeitrasterUnterricht = mapZeitrasterUnterricht
					.get(ssu.idZeitraster);
			if (listWocheZeitrasterUnterricht == null) {
				listWocheZeitrasterUnterricht = new Vector<>();
				mapZeitrasterUnterricht.put(ssu.idZeitraster, listWocheZeitrasterUnterricht);
			}
			listWocheZeitrasterUnterricht.add(ssu);
		}

//		_mapWocheUnterricht = _daten.unterricht.stream().collect(Collectors.groupingBy(u -> u.wochentyp));
		/*
		 * _mapWochenTypUnterricht = new HashMap<>(); for (@NotNull
		 * SchuelerStundenplanUnterricht ssu : _daten.unterricht) {
		 * 
		 * @NotNull List<@NotNull SchuelerStundenplanUnterricht> ssul =
		 * _mapWochenTypUnterricht.get(ssu.wochentyp); if (ssul == null) { ssul = new
		 * Vector<>(); _mapWochenTypUnterricht.put(ssu.wochentyp, ssul); }
		 * ssul.add(ssu); } _woche0Unterricht = _mapWochenTypUnterricht.get(0);
		 * _mapWochenTypUnterricht.remove(0); if (_woche0Unterricht != null) { for
		 * (@NotNull List<@NotNull SchuelerStundenplanUnterricht> l :
		 * _mapWochenTypUnterricht.values()) { l.addAll(_woche0Unterricht); } //
		 * _mapWocheUnterricht.values().stream().forEach(l ->
		 * l.addAll(_woche0Unterricht)); }
		 */
	}

	/**
	 * Gibt zurück, ob es unterschiedliche Wochentypen gibt.
	 * 
	 * @return {@code true}, falls es sich um unterschiedliche Wochentypen handelt,
	 *         {@code false}, falls es nur einen Typen gibt
	 */
	public boolean isAbWochen() {
		return _mapWocheZeitrasterUnterricht.size() > 1;
	}

	/**
	 * Gibt die Anzahl der Wochentypen zurück.
	 * 
	 * @return die Anzahl der Wochentypen
	 */
	public int getAnzahlWochentypen() {
		return isAbWochen() ? _mapWocheZeitrasterUnterricht.size() - 1 : 1;
	}

	/**
	 * Gibt die Wochentypen ohne Typ 0 zurück.
	 * 
	 * @return die Wochentypen als Vector von Integern
	 */
	public @NotNull Vector<@NotNull Integer> getWochentypen() {
		Vector<@NotNull Integer> retVec = new Vector<>(_mapWocheZeitrasterUnterricht.keySet());
		if (!isAbWochen()) {
			return retVec;
		}
		retVec.sort(new IntegerComparator());
		;
		retVec.remove(0);
		return retVec;
	}

}
