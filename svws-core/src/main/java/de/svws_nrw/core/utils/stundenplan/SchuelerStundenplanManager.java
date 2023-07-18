package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link StundenplanKomplett}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class SchuelerStundenplanManager {

	/** Die Stundenplandaten, die im Manager vorhanden sind */
	private final @NotNull StundenplanKomplett _daten;

	/** Eine Map idUnterricht -> StundenplanUnterricht */
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanUnterricht> _mapUnterricht = new HashMap<>();

	/** Eine Map idFach -> StundenplanFach */
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanFach> _mapFaecher = new HashMap<>();

	/** Eine Map idZeitraster -> Liste von Unterricht */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _mapZeitrasterUnterricht = new HashMap<>();

	/** Eine Map wochentyp, idZeitraster -> Liste von Unterricht */
	private final @NotNull Map<@NotNull Integer, @NotNull Map<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>>> _mapWocheZeitrasterUnterricht = new HashMap<>();

	/** Eine Map idZeitraster -> Zeitraster */
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanZeitraster> _mapZeitraster = new HashMap<>();

	/** Eine Map wochentag, stunde -> Zeitraster */
	private final @NotNull Map<@NotNull Integer, @NotNull Map<@NotNull Integer, @NotNull StundenplanZeitraster>> _mapWochentagStundeZeitraster = new HashMap<>();

	/** Eine Map wochentag -> Liste von Zeitrastern */
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _mapWochentagZeitraster = new HashMap<>();

	/** Eine Map stunde -> Liste von Zeitrastern */
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _mapStundeZeitraster = new HashMap<>();

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
	 * Die minimale Unterrichtsstunde, die in den Stundenplandaten vorkommt,
	 * z.B. 1 für die 1. Stunde
	 */
	private int minStunde = Integer.MAX_VALUE;

	/**
	 * Die maximale Unterrichtsstunde, die in den Stundenplandaten vorkommt,
	 * z.B. 9 für die 9. Stunde
	 */
	private int maxStunde = Integer.MIN_VALUE;

	/**
	 * Liefert die ID des Stundenplans
	 *
	 * @return die ID des Stundenplans
	 */
	public long getStundenplanID() {
		return this._daten.daten.id;
	}

	/**
	 * Liefert die ID des Schülers
	 *
	 * @return die ID des Schülers
	 */
	public long getSchuelerID() {
		return this._daten.unterrichtsverteilung.schueler.get(0).id;
	}

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
	public StundenplanUnterricht getUnterrichtById(final long idUnterricht) {
		return _mapUnterricht.get(idUnterricht);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die
	 * im übergeben Zeitraster liegen.
	 *
	 * @param idZeitraster die ID des Zeitrasters
	 *
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public List<@NotNull StundenplanUnterricht> getUnterrichtByZeitrasterId(final long idZeitraster) {
		return _mapZeitrasterUnterricht.get(idZeitraster);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die
	 * im übergeben Wochentyp und im übergebenen Zeitraster liegen.
	 *
	 * @param wochentyp    der Wochentyp
	 * @param idZeitraster die ID des Zeitrasters
	 *
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public List<@NotNull StundenplanUnterricht> getUnterrichtByWocheZeitrasterId(final int wochentyp,
			final long idZeitraster) {
		return getUnterrichtByWocheZeitrasterId(wochentyp, idZeitraster, false);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die
	 * im übergeben Wochentyp und im übergebenen Zeitraster liegen. Je nach
	 * Parameter inklWoche0 wird die Liste um den Unterricht aus Woche 0
	 * ergänzt.
	 *
	 * @param wochentyp    der Wochentyp
	 * @param idZeitraster die ID des Zeitrasters
	 * @param inklWoche0   Ergänzung des Unterrichts um Woche 0
	 *
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public List<@NotNull StundenplanUnterricht> getUnterrichtByWocheZeitrasterId(final int wochentyp,
			final long idZeitraster, final boolean inklWoche0) {
		final Map<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> mapZeitrasterUnterricht_Wochentyp = DeveloperNotificationException
				.ifMapGetIsNull(_mapWocheZeitrasterUnterricht, wochentyp);
		List<@NotNull StundenplanUnterricht> retList = mapZeitrasterUnterricht_Wochentyp.get(idZeitraster);
		if (retList == null)
			retList = new ArrayList<>();
		if (wochentyp != 0 && inklWoche0) {
			final Map<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> mapZeitrasterUnterricht_Woche0 = DeveloperNotificationException
					.ifMapGetIsNull(_mapWocheZeitrasterUnterricht, 0);
			final List<@NotNull StundenplanUnterricht> listUnterricht_Woche0 = DeveloperNotificationException
					.ifMapGetIsNull(mapZeitrasterUnterricht_Woche0, idZeitraster);
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
	public StundenplanZeitraster getZeitrasterById(final long idZeitraster) {
		return _mapZeitraster.get(idZeitraster);
	}

	/**
	 * Liefert das Zeitraster-Objekt an dem übergebenen Wochentag in der
	 * übergebenen Stunde.
	 *
	 * @param wochentag der Wochentag
	 * @param stunde    die Stunde
	 * @return das Zeitraster-Objekt
	 */
	public StundenplanZeitraster getZeitrasterByWochentagStunde(final int wochentag, final int stunde) {
		final Map<@NotNull Integer, @NotNull StundenplanZeitraster> map = DeveloperNotificationException
				.ifMapGetIsNull(_mapWochentagStundeZeitraster, wochentag);
		return map.get(stunde);
	}

	/**
	 * Liefert die Liste von Zeitraster-Objekten am übergebenen Wochentag.
	 *
	 * @param wochentag der Wochentag
	 *
	 * @return Liste von Zeitraster-Objekten
	 */
	public List<@NotNull StundenplanZeitraster> getZeitrasterByWochentag(final int wochentag) {
		return _mapWochentagZeitraster.get(wochentag);
	}

	/**
	 * Liefert die Liste von Zeitraster-Objekten in der übergebenen Stunde.
	 *
	 * @param stunde die Unterrichtsstunde
	 *
	 * @return Liste von Zeitraster-Objekten
	 */
	public List<@NotNull StundenplanZeitraster> getZeitrasterByStunde(final int stunde) {
		return _mapStundeZeitraster.get(stunde);
	}

	/**
	 * Liefert das Stundenplan-Fach-Objekt zur übergebenen
	 * Fach-ID
	 *
	 * @param idFach   die ID des Faches
	 *
	 * @return das StundenplanFach-Objekt
	 */
	public StundenplanFach getFachById(final long idFach) {
		return _mapFaecher.get(idFach);
	}


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Stundenplandaten und
	 * erzeugt die privaten Attribute.
	 *
	 * @param daten die Stundenplandaten
	 */
	public SchuelerStundenplanManager(final @NotNull StundenplanKomplett daten) {
		_daten = daten;
		for (final StundenplanZeitraster sz : _daten.daten.zeitraster) {
			if (sz.wochentag < minWochentag)
				minWochentag = sz.wochentag;
			if (sz.wochentag > maxWochentag)
				maxWochentag = sz.wochentag;
			if (sz.unterrichtstunde < minStunde)
				minStunde = sz.unterrichtstunde;
			if (sz.unterrichtstunde > maxStunde)
				maxStunde = sz.unterrichtstunde;

			_mapZeitraster.put(sz.id, sz);

			List<@NotNull StundenplanZeitraster> listWochentagZeitraster = _mapWochentagZeitraster.get(sz.wochentag);
			if (listWochentagZeitraster == null) {
				listWochentagZeitraster = new ArrayList<>();
				_mapWochentagZeitraster.put(sz.wochentag, listWochentagZeitraster);
			}
			listWochentagZeitraster.add(sz);

			List<@NotNull StundenplanZeitraster> listStundeZeitraster = _mapStundeZeitraster.get(sz.unterrichtstunde);
			if (listStundeZeitraster == null) {
				listStundeZeitraster = new ArrayList<>();
				_mapStundeZeitraster.put(sz.unterrichtstunde, listStundeZeitraster);
			}
			listStundeZeitraster.add(sz);

			Map<@NotNull Integer, @NotNull StundenplanZeitraster> mapStundeUnterricht = _mapWochentagStundeZeitraster
					.get(sz.wochentag);
			if (mapStundeUnterricht == null) {
				mapStundeUnterricht = new HashMap<>();
				_mapWochentagStundeZeitraster.put(sz.wochentag, mapStundeUnterricht);
			}
			mapStundeUnterricht.put(sz.unterrichtstunde, sz);
		}

		for (final StundenplanUnterricht ssu : _daten.unterrichte) {
			_mapUnterricht.put(ssu.id, ssu);
			List<@NotNull StundenplanUnterricht> listZeitrasterUnterricht = _mapZeitrasterUnterricht
					.get(ssu.idZeitraster);
			if (listZeitrasterUnterricht == null) {
				listZeitrasterUnterricht = new ArrayList<>();
				_mapZeitrasterUnterricht.put(ssu.idZeitraster, listZeitrasterUnterricht);
			}
			listZeitrasterUnterricht.add(ssu);

			Map<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> mapZeitrasterUnterricht = _mapWocheZeitrasterUnterricht
					.get(ssu.wochentyp);
			if (mapZeitrasterUnterricht == null) {
				mapZeitrasterUnterricht = new HashMap<>();
				_mapWocheZeitrasterUnterricht.put(ssu.wochentyp, mapZeitrasterUnterricht);
			}
			List<@NotNull StundenplanUnterricht> listWocheZeitrasterUnterricht = mapZeitrasterUnterricht
					.get(ssu.idZeitraster);
			if (listWocheZeitrasterUnterricht == null) {
				listWocheZeitrasterUnterricht = new ArrayList<>();
				mapZeitrasterUnterricht.put(ssu.idZeitraster, listWocheZeitrasterUnterricht);
			}
			listWocheZeitrasterUnterricht.add(ssu);
		}

		for (final StundenplanFach f : _daten.unterrichtsverteilung.faecher)
			_mapFaecher.put(f.id, f);
	}

	/**
	 * Gibt zurück, ob es unterschiedliche Wochentypen gibt.
	 *
	 * @return {@code true}, falls es sich um unterschiedliche Wochentypen
	 *         handelt, {@code false}, falls es nur einen Typen gibt
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
	 * @return die Wochentypen als ArrayList von Integern
	 */
	public @NotNull List<@NotNull Integer> getWochentypen() {
		final List<@NotNull Integer> retVec = new ArrayList<>(_mapWocheZeitrasterUnterricht.keySet());
		if (!isAbWochen())
			return retVec;
		retVec.sort(new IntegerComparator());
		retVec.remove(0);
		return retVec;
	}

}
