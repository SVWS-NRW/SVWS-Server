package de.svws_nrw.transpiler.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager, um die Daten eines Core-Types zu verwalten, die aus
 * einer JSON-Datei geladen wurden.
 *
 * @param <T>   der Type der geladenen Daten
 * @param <U>   der Core-Type
 */
public class CoreTypeEnumDataManager<@NotNull T extends @NotNull CoreTypeData, @NotNull U extends @NotNull Enum<@NotNull U> & @NotNull CoreType<@NotNull T, @NotNull U>> {

	/** Eine Map mit den Daten der initisierten Core-Types */
	private static @NotNull Map<@NotNull String, @NotNull CoreTypeEnumDataManager<? extends @NotNull CoreTypeData, ?>> _data = new HashMap<>();

	/**
	 * Fügt für den Core-Type einen Core-Type-Manager hinzu
	 *
	 * @param <T>       der Typ der Katalog-Einträge
	 * @param <U>       der Typ des Core-Types
	 * @param clazz     die Klassen des Core-Types
	 * @param manager   der Core-Type-Manager
	 */
	public static <@NotNull T extends @NotNull CoreTypeData, @NotNull U extends @NotNull Enum<@NotNull U> & @NotNull CoreType<@NotNull T, @NotNull U>> void putManager(final @NotNull Class<@NotNull U> clazz, final @NotNull CoreTypeEnumDataManager<@NotNull T, @NotNull U> manager) {
		if (_data.containsKey(clazz.getCanonicalName()))
			throw new RuntimeException("Der Core-Type %s wurde bereits initialisiert. Der erneute Aufruf der Initialisierung ist ein Programmierfehler.".formatted(clazz.getCanonicalName()));
		_data.put(clazz.getCanonicalName(), manager);
	}

	/**
	 * Ermittelt den Core-Type-Manager für den Core-Type, sofern dieser
	 * initialisiert wurde. Ist dies nicht der Fall, so wird eine Exception erzeugt.
	 *
	 * @param <T>       der Typ der Katalog-Einträge
	 * @param <U>       der Typ des Core-Types
	 * @param clazz     die Klassen des Core-Types
	 *
	 * @return der Core-Type-Manager
	 */
	public static <@NotNull T extends @NotNull CoreTypeData, @NotNull U extends @NotNull Enum<@NotNull U> & @NotNull CoreType<@NotNull T, @NotNull U>> @NotNull CoreTypeEnumDataManager<@NotNull T, @NotNull U> getManager(final @NotNull Class<@NotNull U> clazz) {
		@SuppressWarnings("unchecked")
		final CoreTypeEnumDataManager<T, U> manager = (CoreTypeEnumDataManager<T, U>) _data.get(clazz.getCanonicalName());
		if (manager == null)
			throw new RuntimeException("Der Core-Type " + clazz.getSimpleName() + " wurde noch nicht initialisiert.");
		return manager;
	}


	/* ----- Die nachfolgenden Attribute werden mithilfe des Konstruktors initialisiert und der Werte/Inhalte werden anschließend nicht mehr modifiziert ----- */

	/** Der name des Core-Types */
	private final @NotNull String _name;

	/** Die Version der Core-Type-Daten, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	private final long _version;

	/** Die Liste aller zulässigen Werte des Core-Types, d.h. der Enumeration */
	private final @NotNull List<@NotNull U> _listWerte;

	/** Die Daten des Core-Types mit der Zuordnung der Liste mit den Historien-Einträgen zu den Bezeichnern des Core-Types */
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull T>> _mapBezeichnerToHistorie;

	/** Eine Map mit der Zuordnung der Enum-Einträge zu den Bezeichnern */
	private final @NotNull HashMap<@NotNull String, @NotNull U> _mapBezeichnerToEnum = new HashMap<>();

	/** Eine Map mit der Zuordnung der Historieneinträge zu den Enum-Einträgen */
	private final @NotNull HashMap<@NotNull U, @NotNull List<@NotNull T>> _mapEnumToHistorie = new HashMap<>();

	/** Eine Map mit der Zuordnung der Historieneinträge zu deren ID */
	private final @NotNull Map<@NotNull Long, @NotNull T> _mapIDToEintrag = new HashMap<>();

	/** Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen ID */
	private final @NotNull Map<@NotNull Long, @NotNull U> _mapIDToEnum = new HashMap<>();

	/** Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen numerischen Schlüssel */
	private final @NotNull Map<@NotNull String, @NotNull U> _mapSchluesselToEnum = new HashMap<>();

	/** Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen Kürzel */
	private final @NotNull Map<@NotNull String, @NotNull U> _mapKuerzelToEnum = new HashMap<>();


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** Eine Map mit der Zuordnung der Liste der Werte zu einem Schuljahr */
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull U>> _mapSchuljahrToWerte = new HashMap<>();

	/** Eine geschachtelte Map mit der Zuordnung eines Historien-Eintrags zu einem Schuljahr und einem Core-Type-Wert */
	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull U, @NotNull T>> _mapWertAndSchuljahrToEintrag = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager für die übergebenen Daten
	 *
	 * @param version   die Version der Daten
	 * @param clazz     die Core-Type-Klasse
	 * @param values    ein Array mit allen Werten des Core-Types
	 * @param data      die Daten für den Core-Type
	 */
	public CoreTypeEnumDataManager(final long version, final @NotNull Class<@NotNull U> clazz,
			final @NotNull U@NotNull[] values,
			final @NotNull Map<@NotNull String, @NotNull List<@NotNull T>> data) {
		_name = clazz.getSimpleName();
		// Prüfe und setze die Version des Core-Types
		if (version <= 0)
			throw new RuntimeException(_name + ": Der Core-Type soll mit einer ungültigen Version (kleiner oder gleich 0) initialisiert werden. Die Daten sind fehlerhaft.");
		_version = version;
		// Erstelle die Map von den Bezeichnern zu den einzelnen Werte des Core-Types
		this._listWerte = Arrays.asList(values);
		this._mapBezeichnerToHistorie = data;
		for (final @NotNull U coreTypeValue : values) {
			_mapBezeichnerToEnum.put(coreTypeValue.name(), coreTypeValue);
			final List<@NotNull T> historie = _mapBezeichnerToHistorie.get(coreTypeValue.name());
			if (historie == null)
				throw new RuntimeException(_name + ": Der Core-Type-Bezeichner " + coreTypeValue.name() + "hat keine Daten zugeordnet. Der Core-Type konnte nicht vollständig initialisiert werden.");
			_mapEnumToHistorie.put(coreTypeValue, historie);
		}
		// Prüfe, ob alle Daten auch als Core-Type-Werte existieren
		for (final @NotNull String bezeichner : _mapBezeichnerToHistorie.keySet()) {
			final U coreTypeValue = _mapBezeichnerToEnum.get(bezeichner);
			if (coreTypeValue == null)
				throw new RuntimeException(_name + ": Der Bezeichner " + bezeichner + " kann keinem Core-Type-Wert zugeordnet werden. Der Core-Type konnte nicht vollständig initialisiert werden.");
		}
		// Prüfe alle Historien-Einträge auf Plausibilität und erzeugen jeweils eine Zuordnung des Core-Type-Wertes bzw. des Historieneintrages zu der ID des Eintrags
		final Set<Long> setIDs = new HashSet<>();
		for (final Map.Entry<@NotNull U, @NotNull List<@NotNull T>> entry : _mapEnumToHistorie.entrySet()) {
			Integer schuljahr = null;
			final @NotNull U coreTypeEntry = entry.getKey();
			final @NotNull List<@NotNull T> historie = entry.getValue();
			for (final @NotNull T eintrag : historie) {
				// Prüfe zunächst die Historie auf plausible Einträge ...
				if ((schuljahr != null) && ((eintrag.gueltigVon == null) || (eintrag.gueltigVon < 2000) || (eintrag.gueltigBis > 3000) || (Integer.compare(eintrag.gueltigVon, schuljahr) <= 0)))
					throw new RuntimeException(_name + ": Die Historie ist fehlerhaft beim Eintrag für " + coreTypeEntry.name() + ". Neuere Historieneinträge müssen weiter unten in der Liste stehen.");
				schuljahr = (eintrag.gueltigBis == null) ? Integer.MAX_VALUE : eintrag.gueltigBis;
				// ... dann prüfe, ob die ID doppelt vorkommt ...
				if (setIDs.contains(eintrag.id))
					throw new RuntimeException(_name + ": Die Historie ist fehlerhaft beim Eintrag für " + coreTypeEntry.name() + ". Die ID " + eintrag.id + " kommt mehrfach vor.");
				setIDs.add(eintrag.id);
				// ... und befülle dann die beiden Maps
				_mapIDToEintrag.put(eintrag.id, eintrag);
				_mapIDToEnum.put(eintrag.id, coreTypeEntry);
				_mapSchluesselToEnum.put(eintrag.schluessel, coreTypeEntry);
				_mapKuerzelToEnum.put(eintrag.kuerzel, coreTypeEntry);
			}
		}
	}


	/**
	 * Gibt den Namen des Core-Types zurück.
	 *
	 * @return der Name des Core-Types
	 */
	public @NotNull String getName() {
		return _name;
	}


	/**
	 * Gibt die Version der Core-Type-Daten zurück. Diese wird z.B. genutzt,
	 * um bei einem Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 *
	 * @return die Version
	 */
	public long getVersion() {
		return _version;
	}


	/**
	 * Gibt die nicht veränderbare Liste aller Werte des Core-Type zurück.
	 *
	 * @return die nicht veränderbare Liste aller Werte
	 */
	public @NotNull List<@NotNull U> getWerte() {
		return Collections.unmodifiableList(_listWerte);
	}


	/**
	 * Gibt die Historie für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return die Historie
	 */
	public @NotNull List<@NotNull T> getHistorieByBezeichner(final String bezeichner) {
		final List<@NotNull T> tmp = _mapBezeichnerToHistorie.get(bezeichner);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Historien-Eintrag für den Bezeichner " + bezeichner + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Core-Type-Wert für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return der Core-Type-Wert
	 */
	public @NotNull U getWertByBezeichner(final @NotNull String bezeichner) {
		final U tmp = _mapBezeichnerToEnum.get(bezeichner);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Core-Type-Wert für den Bezeichner " + bezeichner + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   die Lister der Bezeichner
	 *
	 * @return die Liste der Core-Type-Werte
	 */
	public @NotNull List<@NotNull U> getWerteByBezeichner(final @NotNull List<@NotNull String> bezeichner) {
		final @NotNull List<@NotNull U> result = new ArrayList<>();
		for (final @NotNull String b : bezeichner)
			result.add(getWertByBezeichner(b));
		return result;
	}


	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner als Set zurück.
	 *
	 * @param bezeichner   die Lister der Bezeichner
	 *
	 * @return das Set der Core-Type-Werte
	 */
	public @NotNull Set<@NotNull U> getWerteByBezeichnerAsSet(final @NotNull List<@NotNull String> bezeichner) {
		final @NotNull Set<@NotNull U> result = new HashSet<>();
		for (final @NotNull String b : bezeichner)
			result.add(getWertByBezeichner(b));
		return result;
	}


	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner als nicht-leeres Set zurück.
	 *
	 * @param bezeichner   die Lister der Bezeichner
	 *
	 * @return das nicht-leeres Set der Core-Type-Werte
	 */
	public @NotNull Set<@NotNull U> getWerteByBezeichnerAsNonEmptySet(final @NotNull List<@NotNull String> bezeichner) {
		if (bezeichner.isEmpty())
			throw new RuntimeException(_name + ": Die Liste der Bezeichner " + bezeichner + " ist leer.");
		final @NotNull Set<@NotNull U> result = new HashSet<>();
		for (final @NotNull String b : bezeichner)
			result.add(getWertByBezeichner(b));
		return result;
	}


	/**
	 * Gibt die Historie für den angegebenen Core-Type Wert zurück.
	 *
	 * @param value   der Core-Type-Wert
	 *
	 * @return die Historie
	 */
	public @NotNull List<@NotNull T> getHistorieByWert(final @NotNull U value) {
		if (value == null)
			throw new RuntimeException("Ein Zugriff auf eine Historie ist mit null nicht möglich.");
		final List<@NotNull T> tmp = _mapEnumToHistorie.get(value);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Historien-Eintrag für den Bezeichner " + value.name() + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Historien-Eintrag für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Historien-Eintrag
	 */
	public @NotNull T getHistorieByBezeichner(final long id) {
		final T tmp = _mapIDToEintrag.get(id);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Historien-Eintrag für die ID " + id + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Core-Type-Wert für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Core-Type-Wert
	 */
	public U getWertByID(final long id) {
		final U tmp = _mapIDToEnum.get(id);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Core-Type-Wert für die ID " + id + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Core-Type-Wert für den angegebene numerischen Schlüssel zurück.
	 *
	 * @param schluessel   der numerische Schlüssel
	 *
	 * @return der Core-Type-Wert
	 */
	public U getWertBySchluessel(final @NotNull String schluessel) {
		final U tmp = _mapSchluesselToEnum.get(schluessel);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Core-Type-Wert für den Schlüssel \"" + schluessel + "\" gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Core-Type-Wert für das angegebene Kürzel zurück.
	 * Diese Methode sollte i.A. nicht mehr zur Indenfikation des Core-Types genutzt
	 * werden. Sie steht dennoch für die Kompatibilität zu alten Schuldatenbanken zur Verfügung.
	 *
	 * @param kuerzel   das Kürzel
	 *
	 * @return der Core-Type-Wert
	 */
	public U getWertByKuerzel(final @NotNull String kuerzel) {
		final U tmp = _mapKuerzelToEnum.get(kuerzel);
		if (tmp == null)
			throw new RuntimeException(_name + ": Kein Core-Type-Wert für das Kürzel " + kuerzel + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt die Daten aus der Historie zu dem Core-Type-Wert für das angegeben Schuljahr zurück.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param value       der Core-Type-Wert
	 *
	 * @return die Daten aus der Historie
	 */
	public T getEintragBySchuljahrUndWert(final int schuljahr, final @NotNull U value) {
		HashMap<@NotNull U, @NotNull T> mapEintraege = _mapWertAndSchuljahrToEintrag.get(schuljahr);
		// Prüfe, ob die Einträge im Cache sind. Wenn nicht, dann erzeuge die Daten im Cache
		if (mapEintraege == null) {
			mapEintraege = new HashMap<>();
			// Durchwandere die einzelnen Core-Types und suche den zugehörigen Historien-Eintrag des Schuljahres für die Map
			for (final @NotNull U wert: _listWerte) {
				// Bestimme zunächst die Historie des Core-Type-Wertes
				final @NotNull List<@NotNull T> historie = getHistorieByWert(wert);
				for (final @NotNull T eintrag : historie) {
					if (((eintrag.gueltigVon == null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis == null) || (schuljahr <= eintrag.gueltigBis))) {
						mapEintraege.put(wert, eintrag);
						break;
					}
				}
			}
			_mapWertAndSchuljahrToEintrag.put(schuljahr, mapEintraege);
		}
		return mapEintraege.get(value);
	}


	/**
	 * Gibt alle Schulformen dieser Aufzählung zurück, welche in dem angebenen Schuljahr gültig sind.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return eine {@link List} mit alle Schulformen
	 */
	public @NotNull List<@NotNull U> getWerteBySchuljahr(final int schuljahr) {
		List<@NotNull U> result = _mapSchuljahrToWerte.get(schuljahr);
		if (result == null) {
			result = new ArrayList<>();
			for (final @NotNull U wert: _listWerte)
				if (getEintragBySchuljahrUndWert(schuljahr, wert) != null)
					result.add(wert);
			_mapSchuljahrToWerte.put(schuljahr, result);
		}
		return Collections.unmodifiableList(result);
	}

}
