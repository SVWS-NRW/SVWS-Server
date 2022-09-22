package de.nrw.schule.svws.core.utils.stundenplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplan;
import de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplanUnterricht;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link SchuelerStundenplan}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class SchuelerStundenplanManager {

	/** Die Stundenplandaten, die im Manager vorhanden sind. */
	private final @NotNull SchuelerStundenplan _daten;

	/**
	 * Eine interne Map zum schnellen Zugriff auf den Unterricht gruppiert nach dem
	 * Wochentyp
	 */
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull SchuelerStundenplanUnterricht>> _mapWochenTypUnterricht;

	/**
	 * Eine interne Liste zum schnellen Zugriff auf den Unterricht in Wochentyp 0
	 */
	private final List<@NotNull SchuelerStundenplanUnterricht> _woche0Unterricht;

	/**
	 * Erstellt einen neuen Manager mit den angegebenen Stundenplandaten
	 * 
	 * @param daten die Stundenplandaten
	 */
	public SchuelerStundenplanManager(@NotNull SchuelerStundenplan daten) {
		_daten = daten;
//		_mapWocheUnterricht = _daten.unterricht.stream().collect(Collectors.groupingBy(u -> u.wochentyp));
		_mapWochenTypUnterricht = new HashMap<>();
		for (@NotNull SchuelerStundenplanUnterricht ssu : _daten.unterricht ) {
			List<@NotNull SchuelerStundenplanUnterricht> ssul = _mapWochenTypUnterricht.get(ssu.wochentyp);
			if (ssul == null) {
				ssul = new Vector<>();
				_mapWochenTypUnterricht.put(ssu.wochentyp, ssul);
			}
			ssul.add(ssu);
		}
		_woche0Unterricht = _mapWochenTypUnterricht.get(0);
		_mapWochenTypUnterricht.remove(0);
		if (_woche0Unterricht != null) {
			for (@NotNull List<@NotNull SchuelerStundenplanUnterricht> l : _mapWochenTypUnterricht.values()) {
				l.addAll(_woche0Unterricht);
			}
//			_mapWocheUnterricht.values().stream().forEach(l -> l.addAll(_woche0Unterricht));
		}
	}

	/**
	 * Gibt zurück, ob es unterschiedliche Wochentypen gibt.
	 * 
	 * @return {@code true}, falls es sich um unterschiedliche Wochentypen handelt,
	 *         {@code false}, falls es nur einen Typen gibt
	 */
	public boolean isAbWochen() {
		return _mapWochenTypUnterricht.size() > 0;
	}

	/**
	 * Gibt die Anzahl der Wochentypen zurück.
	 * 
	 * @return die Anzahl der Wochentypen
	 */
	public int getAnzahlWochentypen() {
		return isAbWochen() ? _mapWochenTypUnterricht.size() : 1;
	}

	/**
	 * Gibt die Wochentypen zurück.
	 * 
	 * @return die Wochentypen als Set von Integern
	 */
	public @NotNull Set<@NotNull Integer> getWochentypen() {
		if (!isAbWochen()) {
			// TODO Exception
		}
		return _mapWochenTypUnterricht.keySet();
	}

	/**
	 * Gibt den Unterricht ohne A/B-Wochen zurück.
	 * 
	 * @return die Liste, die den Unterricht enthält
	 */
	public @NotNull List<@NotNull SchuelerStundenplanUnterricht> getUnterricht() {
		if (isAbWochen() || (_woche0Unterricht == null))
			throw new RuntimeException("Der Unterricht ist für den Schüler in Wochentypen organisiert.");
		return _woche0Unterricht;
	}
	
	/**
	 * Gibt den Unterricht in einem bestimmten Wochentyp zurück.
	 * 
	 * @param woche der Wochentyp
	 * 
	 * @return die Liste, die den Unterricht im angegebenen Wochentyp enthält
	 */
	public List<@NotNull SchuelerStundenplanUnterricht> getUnterricht(int woche) {
		if (!isAbWochen() || !_mapWochenTypUnterricht.containsKey(woche)) {
			// TODO Exception
		}
		return _mapWochenTypUnterricht.get(woche);
	}

}
