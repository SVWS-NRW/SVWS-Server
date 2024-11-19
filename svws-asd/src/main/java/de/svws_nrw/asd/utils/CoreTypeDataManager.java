package de.svws_nrw.asd.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager, um die Daten eines Core-Types zu verwalten, die aus
 * einer JSON-Datei geladen wurden.
 *
 * @param <T>   der Type der geladenen Daten
 * @param <U>   der Core-Type
 */
public class CoreTypeDataManager<T extends CoreTypeData, U extends CoreType<T, U>> {

	/** Eine Map mit den Daten der initisierten Core-Types */
	private static @NotNull Map<String, CoreTypeDataManager<? extends CoreTypeData, ?>> _data = new HashMap<>();

	/**
	 * Fügt für den Core-Type einen Core-Type-Manager hinzu
	 *
	 * @param <T>       der Typ der Katalog-Einträge
	 * @param <U>       der Typ des Core-Types
	 * @param clazz     die Klassen des Core-Types
	 * @param manager   der Core-Type-Manager
	 */
	public static <T extends CoreTypeData, U extends CoreType<T, U>> void putManager(final @NotNull Class<U> clazz, final @NotNull CoreTypeDataManager<T, U> manager) {
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
	public static <T extends CoreTypeData, U extends CoreType<T, U>> @NotNull CoreTypeDataManager<T, U> getManager(final @NotNull Class<U> clazz) {
		@SuppressWarnings("unchecked")
		final CoreTypeDataManager<T, U> manager = (CoreTypeDataManager<T, U>) _data.get(clazz.getCanonicalName());
		if (manager == null)
			throw new CoreTypeException("Der Core-Type " + clazz.getSimpleName() + " wurde noch nicht initialisiert.");
		return manager;
	}


	/* ----- Die nachfolgenden Attribute werden mithilfe des Konstruktors initialisiert und der Werte/Inhalte werden anschließend nicht mehr modifiziert ----- */

	/** Der name des Core-Types */
	private final @NotNull String _name;

	/** Die Version der Core-Type-Daten, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	private final long _version;

	/** Die Liste aller zulässigen Werte des Core-Types, d.h. der Enumeration */
	private final @NotNull List<U> _listWerte;

	/** Die Zuordnung der Liste mit den Historien-IDs zu den Bezeichnern des Core-Types */
	private final @NotNull Map<String, Long> _mapBezeichnerToHistorienID;

	/** Die Daten des Core-Types mit der Zuordnung der Liste mit den Historien-Einträgen zu den Bezeichnern des Core-Types */
	private final @NotNull Map<String, List<T>> _mapBezeichnerToHistorie;

	/** Eine Map mit der Zuordnung der Enum-Einträge zu den Bezeichnern */
	private final @NotNull Map<String, U> _mapBezeichnerToEnum = new HashMap<>();

	/** Eine Map mit der Zuordnung der Historien-IDs zu den Enum-Einträgen */
	private final @NotNull Map<U, Long> _mapEnumToHistorienID = new HashMap<>();

	/** Eine Map mit der Zuordnung der Historieneinträge zu den Enum-Einträgen */
	private final @NotNull Map<U, List<T>> _mapEnumToHistorie = new HashMap<>();

	/** Eine Map mit der Zuordnung der Historieneinträge zu deren ID */
	private final @NotNull Map<Long, T> _mapIDToEintrag = new HashMap<>();

	/** Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen ID */
	private final @NotNull Map<Long, U> _mapIDToEnum = new HashMap<>();

	/** Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen numerischen Schlüssel */
	private final @NotNull Map<String, U> _mapSchluesselToEnum = new HashMap<>();

	/** Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen Kürzel */
	private final @NotNull Map<String, U> _mapKuerzelToEnum = new HashMap<>();

	/** Die Map mit der Zuordnung der zulässigen Schulformen zu der ID eines Historieneintrags. Liegt keine Einschränkung vor, so ist die Menge leer. */
	private final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** Eine Map mit der Zuordnung der Liste der Werte zu einem Schuljahr */
	private final @NotNull Map<Integer, List<U>> _mapSchuljahrToWerte = new HashMap<>();

	/** Eine geschachtelte Map mit der Zuordnung eines Historien-Eintrags zu einem Schuljahr und einem Core-Type-Wert */
	private final @NotNull HashMap<Integer, HashMap<U, T>> _mapWertAndSchuljahrToEintrag = new HashMap<>();

	/** Eine geschachtelte Map mit der Zuordnung einer Liste von Core-Type-Werten zu einem Schuljahr und einer Schulform */
	private final @NotNull Map<Integer, Map<Schulform, List<U>>> _mapBySchuljahrAndSchulform = new HashMap<>();

	/** Eine geschachtelte Map mit der Zuordnung einer von Core-Type-Werten zu einem Schuljahr, einer Schulform und dem Schlüssel */
	private final @NotNull Map<Integer, Map<Schulform, Map<String, U>>> _mapBySchuljahrAndSchulformAndSchluessel = new HashMap<>();

	/** Eine Map mit der Zuordnung der Historien-Einträge zu den jeweilgen Schuljahren */
	private final @NotNull HashMap<Integer, List<T>> _mapEintraegeBySchuljahr = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager für die übergebenen Daten
	 *
	 * @param version   die Version der Daten
	 * @param clazz     die Core-Type-Klasse
	 * @param values    ein Array mit allen Werten des Core-Types
	 * @param data      die Daten für den Core-Type
	 * @param idsHistorien   die IDs der Historien zu den einzelnen Bezeichnern
	 */
	public CoreTypeDataManager(final long version, final @NotNull Class<U> clazz,
			final @NotNull U@NotNull[] values,
			final @NotNull Map<String, List<T>> data,
			final @NotNull Map<String, Long> idsHistorien) {
		_name = clazz.getSimpleName();
		// Prüfe und setze die Version des Core-Types
		if (version <= 0)
			throw new CoreTypeException(_name + ": Der Core-Type soll mit einer ungültigen Version (kleiner oder gleich 0) initialisiert werden. Die Daten sind fehlerhaft.");
		_version = version;
		// Erstelle die Map von den Bezeichnern zu den einzelnen Werte des Core-Types
		this._listWerte = Arrays.asList(values);
		this._mapBezeichnerToHistorie = data;
		this._mapBezeichnerToHistorienID = idsHistorien;
		for (final @NotNull U coreTypeValue : values) {
			_mapBezeichnerToEnum.put(coreTypeValue.name(), coreTypeValue);
			final List<T> historie = _mapBezeichnerToHistorie.get(coreTypeValue.name());
			if (historie == null)
				throw new CoreTypeException(_name + ": Der Core-Type-Bezeichner " + coreTypeValue.name() + "hat keine Daten zugeordnet. Der Core-Type konnte nicht vollständig initialisiert werden.");
			_mapEnumToHistorie.put(coreTypeValue, historie);
			final Long idHistorie = _mapBezeichnerToHistorienID.get(coreTypeValue.name());
			if (idHistorie == null)
				throw new CoreTypeException(_name + ": Der Core-Type-Bezeichner " + coreTypeValue.name() + "hat keine Historien-ID zugeordnet. Der Core-Type konnte nicht vollständig initialisiert werden.");
			_mapEnumToHistorienID.put(coreTypeValue, idHistorie);
		}
		// Prüfe, ob alle Daten auch als Core-Type-Werte existieren
		for (final @NotNull String bezeichner : _mapBezeichnerToHistorie.keySet()) {
			final U coreTypeValue = _mapBezeichnerToEnum.get(bezeichner);
			if (coreTypeValue == null)
				throw new CoreTypeException(_name + ": Der Bezeichner " + bezeichner + " kann keinem Core-Type-Wert zugeordnet werden. Der Core-Type konnte nicht vollständig initialisiert werden.");
		}
		// Prüfe alle Historien-Einträge auf Plausibilität und erzeugen jeweils eine Zuordnung des Core-Type-Wertes bzw. des Historieneintrages zu der ID des Eintrags
		final Set<Long> setIDs = new HashSet<>();
		for (final Map.Entry<U, List<T>> entry : _mapEnumToHistorie.entrySet()) {
			Integer schuljahr = null;
			final @NotNull U coreTypeEntry = entry.getKey();
			final @NotNull List<T> historie = entry.getValue();
			for (final @NotNull T eintrag : historie) {
				// Prüfe zunächst die Historie auf plausible Einträge ...
				if ((schuljahr != null) && ((eintrag.gueltigVon == null) || (eintrag.gueltigVon < 2000) || (Integer.compare(eintrag.gueltigVon, schuljahr) <= 0) || ((eintrag.gueltigBis != null) && (eintrag.gueltigBis > 3000))))
					throw new CoreTypeException(_name + ": Die Historie ist fehlerhaft beim Eintrag für " + coreTypeEntry.name() + ". Neuere Historieneinträge müssen weiter unten in der Liste stehen.");
				schuljahr = (eintrag.gueltigBis == null) ? Integer.MAX_VALUE : eintrag.gueltigBis;
				// ... dann prüfe, ob die ID doppelt vorkommt ...
				if (setIDs.contains(eintrag.id))
					throw new CoreTypeException(_name + ": Die Historie ist fehlerhaft beim Eintrag für " + coreTypeEntry.name() + ". Die ID " + eintrag.id + " kommt mehrfach vor.");
				setIDs.add(eintrag.id);
				// ... und befülle dann die beiden Maps
				_mapIDToEintrag.put(eintrag.id, eintrag);
				_mapIDToEnum.put(eintrag.id, coreTypeEntry);
				_mapSchluesselToEnum.put(eintrag.schluessel, coreTypeEntry);
				_mapKuerzelToEnum.put(eintrag.kuerzel, coreTypeEntry);
				// Ergänze die Menge der zulässigen Schulformen, sofern eine Einschränkung vorliegt
				final Set<Schulform> setSchulformen = new HashSet<>();
				if (eintrag instanceof CoreTypeDataNurSchulformen) {
					final @NotNull List<String> listSchulformen = ((CoreTypeDataNurSchulformen) eintrag).schulformen;
					setSchulformen.addAll(Schulform.data().getWerteByBezeichnerAsSet(listSchulformen));
				}
				_mapSchulformenByID.put(eintrag.id, setSchulformen);
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
	public @NotNull List<U> getWerte() {
		return new ArrayList<>(_listWerte);
	}


	/**
	 * Gibt die Historien-ID für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return die Historien-ID
	 */
	public long getHistorienIdByBezeichner(final String bezeichner) {
		final Long tmp = _mapBezeichnerToHistorienID.get(bezeichner);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Keine Historien-ID für den Bezeichner " + bezeichner + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt die Historie für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return die Historie
	 */
	public @NotNull List<T> getHistorieByBezeichner(final String bezeichner) {
		final List<T> tmp = _mapBezeichnerToHistorie.get(bezeichner);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Kein Historien-Eintrag für den Bezeichner " + bezeichner + " gefunden.");
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
			throw new CoreTypeException(_name + ": Kein Core-Type-Wert für den Bezeichner " + bezeichner + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   die Lister der Bezeichner
	 *
	 * @return die Liste der Core-Type-Werte
	 */
	public @NotNull List<U> getWerteByBezeichner(final @NotNull List<String> bezeichner) {
		final @NotNull List<U> result = new ArrayList<>();
		for (final @NotNull String b : bezeichner)
			result.add(getWertByBezeichner(b));
		return result;
	}


	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner als Set zurück.
	 *
	 * @param bezeichner   die Liste der Bezeichner
	 *
	 * @return das Set der Core-Type-Werte
	 */
	public @NotNull Set<U> getWerteByBezeichnerAsSet(final @NotNull List<String> bezeichner) {
		final @NotNull Set<U> result = new HashSet<>();
		for (final @NotNull String b : bezeichner)
			result.add(getWertByBezeichner(b));
		return result;
	}


	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner als nicht-leeres Set zurück.
	 *
	 * @param bezeichner   die Liste der Bezeichner
	 *
	 * @return das nicht-leeres Set der Core-Type-Werte
	 */
	public @NotNull Set<U> getWerteByBezeichnerAsNonEmptySet(final @NotNull List<String> bezeichner) {
		if (bezeichner.isEmpty())
			throw new CoreTypeException(_name + ": Die Liste der Bezeichner ist leer.");
		final @NotNull Set<U> result = new HashSet<>();
		for (final @NotNull String b : bezeichner)
			result.add(getWertByBezeichner(b));
		return result;
	}


	/**
	 * Gibt die Historien-ID für den angegebenen Core-Type Wert zurück.
	 *
	 * @param value   der Core-Type-Wert
	 *
	 * @return die Historien-ID
	 */
	public long getHistorienIdByWert(final @NotNull U value) {
		if (value == null)
			throw new CoreTypeException("Ein Zugriff auf eine Historien-ID ist mit null nicht möglich.");
		final Long tmp = _mapEnumToHistorienID.get(value);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Keine Historien-ID für den Bezeichner " + value.name() + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt die Historie für den angegebenen Core-Type Wert zurück.
	 *
	 * @param value   der Core-Type-Wert
	 *
	 * @return die Historie
	 */
	public @NotNull List<T> getHistorieByWert(final @NotNull U value) {
		if (value == null)
			throw new CoreTypeException("Ein Zugriff auf eine Historie ist mit null nicht möglich.");
		final List<T> tmp = _mapEnumToHistorie.get(value);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Kein Historien-Eintrag für den Bezeichner " + value.name() + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Historien-Eintrag für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Historien-Eintrag
	 *
	 * @throws CoreTypeException   wenn kein Eintrag gefunden wird
	 */
	public @NotNull T getEintragByIDOrException(final long id) throws CoreTypeException {
		final T tmp = _mapIDToEintrag.get(id);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Kein Historien-Eintrag für die ID " + id + " gefunden.");
		return tmp;
	}


	/**
	 * Gibt den Historien-Eintrag für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Historien-Eintrag oder null, wenn die ID ungültig ist
	 */
	public T getEintragByID(final long id) {
		return _mapIDToEintrag.get(id);
	}


	/**
	 * Gibt den Core-Type-Wert für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Core-Type-Wert
	 */
	public @NotNull U getWertByID(final long id) {
		final U tmp = _mapIDToEnum.get(id);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Kein Core-Type-Wert für die ID " + id + " gefunden.");
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
		return _mapSchluesselToEnum.get(schluessel);
	}


	/**
	 * Gibt den Core-Type-Wert für den angegebene numerischen Schlüssel zurück.
	 *
	 * @param schluessel   der numerische Schlüssel
	 *
	 * @return der Core-Type-Wert
	 *
	 * @throws CoreTypeException   wenn der Schlüssel nicht existiert
	 */
	public @NotNull U getWertBySchluesselOrException(final @NotNull String schluessel) throws CoreTypeException {
		final U tmp = _mapSchluesselToEnum.get(schluessel);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Kein Core-Type-Wert für den Schlüssel \"" + schluessel + "\" gefunden.");
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
		return _mapKuerzelToEnum.get(kuerzel);
	}


	/**
	 * Gibt den Core-Type-Wert für das angegebene Kürzel zurück.
	 * Diese Methode sollte i.A. nicht mehr zur Indenfikation des Core-Types genutzt
	 * werden. Sie steht dennoch für die Kompatibilität zu alten Schuldatenbanken zur Verfügung.
	 *
	 * @param kuerzel   das Kürzel
	 *
	 * @return der Core-Type-Wert
	 *
	 * @throws CoreTypeException wenn das Kürzel nicht gültig ist
	 */
	public @NotNull U getWertByKuerzelOrException(final @NotNull String kuerzel) throws CoreTypeException {
		final U tmp = _mapKuerzelToEnum.get(kuerzel);
		if (tmp == null)
			throw new CoreTypeException(_name + ": Kein Core-Type-Wert für das Kürzel " + kuerzel + " gefunden.");
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
	public @AllowNull T getEintragBySchuljahrUndWert(final int schuljahr, final @NotNull U value) {
		// Prüfe, ob die Anfrage aus dem Cache bedient werden kann
		final HashMap<U, T> cache = _mapWertAndSchuljahrToEintrag.get(schuljahr);
		if (cache != null)
			return cache.get(value);
		// Wenn nicht, dann muss der Cache aufgebaut werden...
		final @NotNull HashMap<U, T> mapEintraege = new HashMap<>();
		// Durchwandere die einzelnen Core-Types und suche den zugehörigen Historien-Eintrag des Schuljahres für die Map
		for (final @NotNull U wert: _listWerte) {
			// Bestimme zunächst die Historie des Core-Type-Wertes
			final @NotNull List<T> historie = getHistorieByWert(wert);
			for (final @NotNull T eintrag : historie) {
				if (((eintrag.gueltigVon == null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis == null) || (schuljahr <= eintrag.gueltigBis))) {
					mapEintraege.put(wert, eintrag);
					break;
				}
			}
		}
		_mapWertAndSchuljahrToEintrag.put(schuljahr, mapEintraege);
		return mapEintraege.get(value);
	}


	/**
	 * Gibt die Menge der Historieneinträge für das angegebene Schuljahr aus der Menge aller Werte zurück.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Daten aus den Historieneinträgen für das angegebene Schuljahr aus der Menge aller Werte
	 */
	public @NotNull List<T> getEintraegeBySchuljahr(final int schuljahr) {
		// Prüfe, ob die Anfrage aus dem Cache bedient werden kann
		final List<T> cache = _mapEintraegeBySchuljahr.get(schuljahr);
		if (cache != null)
			return cache;
		// Wenn nicht, dann muss der Cache aufgebaut werden...
		final @NotNull List<T> result = new ArrayList<>();
		// Durchwandere die einzelnen Core-Types und suche den zugehörigen Historien-Eintrag des Schuljahres für die Map
		for (final @NotNull U wert: _listWerte) {
			// Bestimme zunächst die Historie des Core-Type-Wertes
			final @NotNull List<T> historie = getHistorieByWert(wert);
			for (final @NotNull T eintrag : historie) {
				if (((eintrag.gueltigVon == null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis == null) || (schuljahr <= eintrag.gueltigBis))) {
					result.add(eintrag);
					break;
				}
			}
		}
		_mapEintraegeBySchuljahr.put(schuljahr, result);
		return result;
	}


	/**
	 * Gibt alle Schulformen dieser Aufzählung zurück, welche in dem angebenen Schuljahr gültig sind.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return eine {@link List} mit alle Schulformen
	 */
	public @NotNull List<U> getWerteBySchuljahr(final int schuljahr) {
		List<U> result = _mapSchuljahrToWerte.get(schuljahr);
		if (result == null) {
			result = new ArrayList<>();
			for (final @NotNull U wert: _listWerte)
				if (getEintragBySchuljahrUndWert(schuljahr, wert) != null)
					result.add(wert);
			_mapSchuljahrToWerte.put(schuljahr, result);
		}
		return new ArrayList<>(result);
	}


	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 * @param value       der Core-Type-Wert
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform sf, final @NotNull U value) {
		return (getBySchulform(schuljahr, sf, value) != null);
	}


	/**
	 * Gibt den Katalog-Eintrag des Jahrgangs für die übergenene Schulform in dem übergebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param sf          die Schulform
	 * @param value       der Core-Type-Wert
	 *
	 * @return der Katalog-Eintrag oder null, wenn keiner gefunden wird
	 */
	public T getBySchulform(final int schuljahr, final @NotNull Schulform sf, final @NotNull U value) {
		final T eintrag = getEintragBySchuljahrUndWert(schuljahr, value);
		if (eintrag == null)
			return null;
		final Set<Schulform> result = _mapSchulformenByID.get(eintrag.id);
		if (result == null)
			throw new CoreTypeException("Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
		return (result.isEmpty() || result.contains(sf)) ? eintrag : null;
	}


	/**
	 * Liefert alle zulässigen Core-Type-Werte für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Core-Type-Werte
	 */
	public @NotNull List<U> getListBySchuljahrAndSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		final Map<Schulform, List<U>> mapBySchulform =
				_mapBySchuljahrAndSchulform.computeIfAbsent(schuljahr, k -> new HashMap<Schulform, List<U>>());
		if (mapBySchulform == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<U> result = mapBySchulform.get(schulform);
		if (result == null) {
			result = new ArrayList<>();
			final List<U> werte = getWerteBySchuljahr(schuljahr);
			for (final @NotNull U wert : werte)
				if (hatSchulform(schuljahr, schulform, wert))
					result.add(wert);
			mapBySchulform.put(schulform, result);
		}
		return result;
	}


	/**
	 * Liefert die zulässige Core-Type-Werte für die angegebene Schulform in dem angegebenen Schuljahr und dem angebenen Schlüssel oder
	 * null falls eine solcher Core-Type-Wert nicht existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 * @param schluessel  der Schlüssel für den Core-Type-Wert
	 *
	 * @return der bei der Schulform in dem angegebenen Schuljahr dem Schlüssel zugehörige Core-Type-Wert oder null falls ein solcher nicht existiert
	 */
	public U getBySchuljahrAndSchulformAndSchluessel(final int schuljahr, final @NotNull Schulform schulform, final @NotNull String schluessel) {
		final Map<Schulform, Map<String, U>> mapBySchulformAndSchluessel =
				_mapBySchuljahrAndSchulformAndSchluessel.computeIfAbsent(schuljahr, k -> new HashMap<Schulform, Map<String, U>>());
		if (mapBySchulformAndSchluessel == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		Map<String, U> mapBySchluessel = mapBySchulformAndSchluessel.get(schulform);
		if (mapBySchluessel == null) {
			mapBySchluessel = new HashMap<>();
			final List<U> werte = getWerteBySchuljahr(schuljahr);
			for (final @NotNull U wert : werte) {
				if (!hatSchulform(schuljahr, schulform, wert))
					continue;
				final T sgke = getEintragBySchuljahrUndWert(schuljahr, wert);
				if (sgke != null)
					mapBySchluessel.put(sgke.schluessel, wert);
			}
			mapBySchulformAndSchluessel.put(schulform, mapBySchluessel);
		}
		return mapBySchluessel.get(schluessel);
	}

}
