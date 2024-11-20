import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { CoreTypeDataNurSchulformen, cast_de_svws_nrw_asd_data_CoreTypeDataNurSchulformen } from '../../asd/data/CoreTypeDataNurSchulformen';
import { HashMap } from '../../java/util/HashMap';
import { Schulform } from '../../asd/types/schule/Schulform';
import { ArrayList } from '../../java/util/ArrayList';
import { JavaString } from '../../java/lang/JavaString';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { SchulformSchulgliederung } from '../../asd/data/schule/SchulformSchulgliederung';
import { CoreTypeDataNurSchulformenUndSchulgliederungen, cast_de_svws_nrw_asd_data_CoreTypeDataNurSchulformenUndSchulgliederungen } from '../../asd/data/CoreTypeDataNurSchulformenUndSchulgliederungen';
import { NullPointerException } from '../../java/lang/NullPointerException';
import { Class } from '../../java/lang/Class';
import type { List } from '../../java/util/List';
import type { CoreType } from '../../asd/types/CoreType';
import { CoreTypeData, cast_de_svws_nrw_asd_data_CoreTypeData } from '../../asd/data/CoreTypeData';
import { Arrays } from '../../java/util/Arrays';
import type { JavaMap } from '../../java/util/JavaMap';
import { CoreTypeException } from '../../asd/data/CoreTypeException';
import { HashSet } from '../../java/util/HashSet';

export class CoreTypeDataManager<T extends CoreTypeData, U extends CoreType<T, U>> extends JavaObject {

	/**
	 * Eine Map mit den Daten der initisierten Core-Types
	 */
	private static _data : JavaMap<string, CoreTypeDataManager<CoreTypeData, any>> = new HashMap<string, CoreTypeDataManager<CoreTypeData, any>>();

	/**
	 * Der name des Core-Types
	 */
	private readonly _name : string;

	/**
	 * Die Version der Core-Type-Daten, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	private readonly _version : number;

	/**
	 * Die Liste aller zulässigen Werte des Core-Types, d.h. der Enumeration
	 */
	private readonly _listWerte : List<U>;

	/**
	 * Die Zuordnung der Liste mit den Historien-IDs zu den Bezeichnern des Core-Types
	 */
	private readonly _mapBezeichnerToHistorienID : JavaMap<string, number>;

	/**
	 * Die Daten des Core-Types mit der Zuordnung der Liste mit den Historien-Einträgen zu den Bezeichnern des Core-Types
	 */
	private readonly _mapBezeichnerToHistorie : JavaMap<string, List<T>>;

	/**
	 * Eine Map mit der Zuordnung der Enum-Einträge zu den Bezeichnern
	 */
	private readonly _mapBezeichnerToEnum : JavaMap<string, U> = new HashMap<string, U>();

	/**
	 * Eine Map mit der Zuordnung der Historien-IDs zu den Enum-Einträgen
	 */
	private readonly _mapEnumToHistorienID : JavaMap<U, number> = new HashMap<U, number>();

	/**
	 * Eine Map mit der Zuordnung der Historieneinträge zu den Enum-Einträgen
	 */
	private readonly _mapEnumToHistorie : JavaMap<U, List<T>> = new HashMap<U, List<T>>();

	/**
	 * Eine Map mit der Zuordnung der Historieneinträge zu deren ID
	 */
	private readonly _mapIDToEintrag : JavaMap<number, T> = new HashMap<number, T>();

	/**
	 * Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen ID
	 */
	private readonly _mapIDToEnum : JavaMap<number, U> = new HashMap<number, U>();

	/**
	 * Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen numerischen Schlüssel
	 */
	private readonly _mapSchluesselToEnum : JavaMap<string, U> = new HashMap<string, U>();

	/**
	 * Eine Map mit der Zuordnung des Core-Type-Wertes zu dessen Kürzel
	 */
	private readonly _mapKuerzelToEnum : JavaMap<string, U> = new HashMap<string, U>();

	/**
	 * Die Map mit der Zuordnung der zulässigen Schulformen zu der ID eines Historieneintrags. Liegt keine Einschränkung vor, so ist die Menge leer.
	 */
	private readonly _mapSchulformenByID : HashMap<number, JavaSet<Schulform>> = new HashMap<number, JavaSet<Schulform>>();

	/**
	 * Eine Map mit der Zuordnung der Liste der Werte zu einem Schuljahr
	 */
	private readonly _mapSchuljahrToWerte : JavaMap<number, List<U>> = new HashMap<number, List<U>>();

	/**
	 * Eine geschachtelte Map mit der Zuordnung eines Historien-Eintrags zu einem Schuljahr und einem Core-Type-Wert
	 */
	private readonly _mapWertAndSchuljahrToEintrag : HashMap<number, HashMap<U, T>> = new HashMap<number, HashMap<U, T>>();

	/**
	 * Eine geschachtelte Map mit der Zuordnung einer Liste von Core-Type-Werten zu einem Schuljahr und einer Schulform
	 */
	private readonly _mapBySchuljahrAndSchulform : JavaMap<number, JavaMap<Schulform, List<U>>> = new HashMap<number, JavaMap<Schulform, List<U>>>();

	/**
	 * Eine geschachtelte Map mit der Zuordnung einer von Core-Type-Werten zu einem Schuljahr, einer Schulform und dem Schlüssel
	 */
	private readonly _mapBySchuljahrAndSchulformAndSchluessel : JavaMap<number, JavaMap<Schulform, JavaMap<string, U>>> = new HashMap<number, JavaMap<Schulform, JavaMap<string, U>>>();

	/**
	 * Eine Map mit der Zuordnung der Historien-Einträge zu den jeweilgen Schuljahren
	 */
	private readonly _mapEintraegeBySchuljahr : HashMap<number, List<T>> = new HashMap<number, List<T>>();


	/**
	 * Erstellt einen neuen Manager für die übergebenen Daten
	 *
	 * @param version   die Version der Daten
	 * @param clazz     die Core-Type-Klasse
	 * @param values    ein Array mit allen Werten des Core-Types
	 * @param data      die Daten für den Core-Type
	 * @param idsHistorien   die IDs der Historien zu den einzelnen Bezeichnern
	 */
	public constructor(version : number, clazz : Class<U>, values : Array<U>, data : JavaMap<string, List<T>>, idsHistorien : JavaMap<string, number>) {
		super();
		this._name = clazz.getSimpleName();
		if (version <= 0)
			throw new CoreTypeException(this._name + ": Der Core-Type soll mit einer ungültigen Version (kleiner oder gleich 0) initialisiert werden. Die Daten sind fehlerhaft.")
		this._version = version;
		this._listWerte = Arrays.asList(...values);
		this._mapBezeichnerToHistorie = data;
		this._mapBezeichnerToHistorienID = idsHistorien;
		for (const coreTypeValue of values) {
			this._mapBezeichnerToEnum.put(coreTypeValue.name(), coreTypeValue);
			const historie : List<T> | null = this._mapBezeichnerToHistorie.get(coreTypeValue.name());
			if (historie === null)
				throw new CoreTypeException(this._name + ": Der Core-Type-Bezeichner " + coreTypeValue.name() + "hat keine Daten zugeordnet. Der Core-Type konnte nicht vollständig initialisiert werden.")
			this._mapEnumToHistorie.put(coreTypeValue, historie);
			const idHistorie : number | null = this._mapBezeichnerToHistorienID.get(coreTypeValue.name());
			if (idHistorie === null)
				throw new CoreTypeException(this._name + ": Der Core-Type-Bezeichner " + coreTypeValue.name() + "hat keine Historien-ID zugeordnet. Der Core-Type konnte nicht vollständig initialisiert werden.")
			this._mapEnumToHistorienID.put(coreTypeValue, idHistorie);
		}
		for (const bezeichner of this._mapBezeichnerToHistorie.keySet()) {
			const coreTypeValue : U | null = this._mapBezeichnerToEnum.get(bezeichner);
			if (coreTypeValue === null)
				throw new CoreTypeException(this._name + ": Der Bezeichner " + bezeichner + " kann keinem Core-Type-Wert zugeordnet werden. Der Core-Type konnte nicht vollständig initialisiert werden.")
		}
		const setIDs : JavaSet<number> | null = new HashSet<number>();
		for (const entry of this._mapEnumToHistorie.entrySet()) {
			let schuljahr : number | null = null;
			const coreTypeEntry : U = entry.getKey();
			const historie : List<T> = entry.getValue();
			for (const eintrag of historie) {
				if ((schuljahr !== null) && ((eintrag.gueltigVon === null) || (eintrag.gueltigVon < 2000) || (JavaInteger.compare(eintrag.gueltigVon, schuljahr) <= 0) || ((eintrag.gueltigBis !== null) && (eintrag.gueltigBis > 3000))))
					throw new CoreTypeException(this._name + ": Die Historie ist fehlerhaft beim Eintrag für " + coreTypeEntry.name() + ". Neuere Historieneinträge müssen weiter unten in der Liste stehen.")
				schuljahr = (eintrag.gueltigBis === null) ? JavaInteger.MAX_VALUE : eintrag.gueltigBis;
				if (setIDs.contains(eintrag.id))
					throw new CoreTypeException(this._name + ": Die Historie ist fehlerhaft beim Eintrag für " + coreTypeEntry.name() + ". Die ID " + eintrag.id + " kommt mehrfach vor.")
				setIDs.add(eintrag.id);
				this._mapIDToEintrag.put(eintrag.id, eintrag);
				this._mapIDToEnum.put(eintrag.id, coreTypeEntry);
				this._mapSchluesselToEnum.put(eintrag.schluessel, coreTypeEntry);
				this._mapKuerzelToEnum.put(eintrag.kuerzel, coreTypeEntry);
				const setSchulformen : JavaSet<Schulform> | null = new HashSet<Schulform>();
				if (((eintrag instanceof JavaObject) && (eintrag.isTranspiledInstanceOf('de.svws_nrw.asd.data.CoreTypeDataNurSchulformen')))) {
					const list : List<string> = (cast_de_svws_nrw_asd_data_CoreTypeDataNurSchulformen(eintrag)).schulformen;
					setSchulformen.addAll(Schulform.data().getWerteByBezeichnerAsSet(list));
				}
				if (((eintrag instanceof JavaObject) && (eintrag.isTranspiledInstanceOf('de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen')))) {
					const list : List<SchulformSchulgliederung> = (cast_de_svws_nrw_asd_data_CoreTypeDataNurSchulformenUndSchulgliederungen(eintrag)).zulaessig;
					for (const sfsgl of list)
						setSchulformen.add(Schulform.data().getWertByBezeichner(sfsgl.schulform));
				}
				this._mapSchulformenByID.put(eintrag.id, setSchulformen);
			}
		}
	}

	/**
	 * Fügt für den Core-Type einen Core-Type-Manager hinzu
	 *
	 * @param <T>       der Typ der Katalog-Einträge
	 * @param <U>       der Typ des Core-Types
	 * @param clazz     die Klassen des Core-Types
	 * @param manager   der Core-Type-Manager
	 */
	public static putManager<T extends CoreTypeData, U extends CoreType<T, U>>(clazz : Class<U>, manager : CoreTypeDataManager<T, U>) : void {
		CoreTypeDataManager._data.put(clazz.getCanonicalName(), manager);
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
	public static getManager<T extends CoreTypeData, U extends CoreType<T, U>>(clazz : Class<U>) : CoreTypeDataManager<T, U> {
		const manager : CoreTypeDataManager<T, U> | null = cast_de_svws_nrw_asd_utils_CoreTypeDataManager(CoreTypeDataManager._data.get(clazz.getCanonicalName()));
		if (manager === null)
			throw new CoreTypeException("Der Core-Type " + clazz.getSimpleName() + " wurde noch nicht initialisiert.")
		return manager;
	}

	/**
	 * Gibt den Namen des Core-Types zurück.
	 *
	 * @return der Name des Core-Types
	 */
	public getName() : string {
		return this._name;
	}

	/**
	 * Gibt die Version der Core-Type-Daten zurück. Diese wird z.B. genutzt,
	 * um bei einem Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 *
	 * @return die Version
	 */
	public getVersion() : number {
		return this._version;
	}

	/**
	 * Gibt die nicht veränderbare Liste aller Werte des Core-Type zurück.
	 *
	 * @return die nicht veränderbare Liste aller Werte
	 */
	public getWerte() : List<U> {
		return new ArrayList<U>(this._listWerte);
	}

	/**
	 * Gibt die Historien-ID für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return die Historien-ID
	 */
	public getHistorienIdByBezeichner(bezeichner : string | null) : number {
		const tmp : number | null = this._mapBezeichnerToHistorienID.get(bezeichner);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Keine Historien-ID für den Bezeichner " + bezeichner + " gefunden.")
		return tmp;
	}

	/**
	 * Gibt die Historie für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return die Historie
	 */
	public getHistorieByBezeichner(bezeichner : string | null) : List<T> {
		const tmp : List<T> | null = this._mapBezeichnerToHistorie.get(bezeichner);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Historien-Eintrag für den Bezeichner " + bezeichner + " gefunden.")
		return tmp;
	}

	/**
	 * Gibt den Core-Type-Wert für den angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   der Bezeichner
	 *
	 * @return der Core-Type-Wert
	 */
	public getWertByBezeichner(bezeichner : string) : U {
		const tmp : U | null = this._mapBezeichnerToEnum.get(bezeichner);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Core-Type-Wert für den Bezeichner " + bezeichner + " gefunden.")
		return tmp;
	}

	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner zurück.
	 *
	 * @param bezeichner   die Lister der Bezeichner
	 *
	 * @return die Liste der Core-Type-Werte
	 */
	public getWerteByBezeichner(bezeichner : List<string>) : List<U> {
		const result : List<U> = new ArrayList<U>();
		for (const b of bezeichner)
			result.add(this.getWertByBezeichner(b));
		return result;
	}

	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner als Set zurück.
	 *
	 * @param bezeichner   die Liste der Bezeichner
	 *
	 * @return das Set der Core-Type-Werte
	 */
	public getWerteByBezeichnerAsSet(bezeichner : List<string>) : JavaSet<U> {
		const result : JavaSet<U> = new HashSet<U>();
		for (const b of bezeichner)
			result.add(this.getWertByBezeichner(b));
		return result;
	}

	/**
	 * Gibt die Core-Type-Werte für die angegebenen Bezeichner als nicht-leeres Set zurück.
	 *
	 * @param bezeichner   die Liste der Bezeichner
	 *
	 * @return das nicht-leeres Set der Core-Type-Werte
	 */
	public getWerteByBezeichnerAsNonEmptySet(bezeichner : List<string>) : JavaSet<U> {
		if (bezeichner.isEmpty())
			throw new CoreTypeException(this._name + ": Die Liste der Bezeichner ist leer.")
		const result : JavaSet<U> = new HashSet<U>();
		for (const b of bezeichner)
			result.add(this.getWertByBezeichner(b));
		return result;
	}

	/**
	 * Gibt die Historien-ID für den angegebenen Core-Type Wert zurück.
	 *
	 * @param value   der Core-Type-Wert
	 *
	 * @return die Historien-ID
	 */
	public getHistorienIdByWert(value : U) : number {
		if (value === null)
			throw new CoreTypeException("Ein Zugriff auf eine Historien-ID ist mit null nicht möglich.")
		const tmp : number | null = this._mapEnumToHistorienID.get(value);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Keine Historien-ID für den Bezeichner " + value.name() + " gefunden.")
		return tmp;
	}

	/**
	 * Gibt die Historie für den angegebenen Core-Type Wert zurück.
	 *
	 * @param value   der Core-Type-Wert
	 *
	 * @return die Historie
	 */
	public getHistorieByWert(value : U) : List<T> {
		if (value === null)
			throw new CoreTypeException("Ein Zugriff auf eine Historie ist mit null nicht möglich.")
		const tmp : List<T> | null = this._mapEnumToHistorie.get(value);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Historien-Eintrag für den Bezeichner " + value.name() + " gefunden.")
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
	public getEintragByIDOrException(id : number) : T {
		const tmp : T | null = this._mapIDToEintrag.get(id);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Historien-Eintrag für die ID " + id + " gefunden.")
		return tmp;
	}

	/**
	 * Gibt den Historien-Eintrag für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Historien-Eintrag oder null, wenn die ID ungültig ist
	 */
	public getEintragByID(id : number) : T | null {
		return this._mapIDToEintrag.get(id);
	}

	/**
	 * Gibt den Core-Type-Wert für die angegebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Core-Type-Wert
	 */
	public getWertByID(id : number) : U {
		const tmp : U | null = this._mapIDToEnum.get(id);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Core-Type-Wert für die ID " + id + " gefunden.")
		return tmp;
	}

	/**
	 * Gibt den Core-Type-Wert für den angegebene numerischen Schlüssel zurück.
	 *
	 * @param schluessel   der numerische Schlüssel
	 *
	 * @return der Core-Type-Wert
	 */
	public getWertBySchluessel(schluessel : string) : U | null {
		return this._mapSchluesselToEnum.get(schluessel);
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
	public getWertBySchluesselOrException(schluessel : string) : U {
		const tmp : U | null = this._mapSchluesselToEnum.get(schluessel);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Core-Type-Wert für den Schlüssel \"" + schluessel + "\" gefunden.")
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
	public getWertByKuerzel(kuerzel : string) : U | null {
		return this._mapKuerzelToEnum.get(kuerzel);
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
	public getWertByKuerzelOrException(kuerzel : string) : U {
		const tmp : U | null = this._mapKuerzelToEnum.get(kuerzel);
		if (tmp === null)
			throw new CoreTypeException(this._name + ": Kein Core-Type-Wert für das Kürzel " + kuerzel + " gefunden.")
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
	public getEintragBySchuljahrUndWert(schuljahr : number, value : U) : T | null {
		const cache : HashMap<U, T> | null = this._mapWertAndSchuljahrToEintrag.get(schuljahr);
		if (cache !== null)
			return cache.get(value);
		const mapEintraege : HashMap<U, T> = new HashMap<U, T>();
		for (const wert of this._listWerte) {
			const historie : List<T> = this.getHistorieByWert(wert);
			for (const eintrag of historie) {
				if (((eintrag.gueltigVon === null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis === null) || (schuljahr <= eintrag.gueltigBis))) {
					mapEintraege.put(wert, eintrag);
					break;
				}
			}
		}
		this._mapWertAndSchuljahrToEintrag.put(schuljahr, mapEintraege);
		return mapEintraege.get(value);
	}

	/**
	 * Gibt die Menge der Historieneinträge für das angegebene Schuljahr aus der Menge aller Werte zurück.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Daten aus den Historieneinträgen für das angegebene Schuljahr aus der Menge aller Werte
	 */
	public getEintraegeBySchuljahr(schuljahr : number) : List<T> {
		const cache : List<T> | null = this._mapEintraegeBySchuljahr.get(schuljahr);
		if (cache !== null)
			return cache;
		const result : List<T> = new ArrayList<T>();
		for (const wert of this._listWerte) {
			const historie : List<T> = this.getHistorieByWert(wert);
			for (const eintrag of historie) {
				if (((eintrag.gueltigVon === null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis === null) || (schuljahr <= eintrag.gueltigBis))) {
					result.add(eintrag);
					break;
				}
			}
		}
		this._mapEintraegeBySchuljahr.put(schuljahr, result);
		return result;
	}

	/**
	 * Gibt alle Schulformen dieser Aufzählung zurück, welche in dem angebenen Schuljahr gültig sind.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return eine {@link List} mit alle Schulformen
	 */
	public getWerteBySchuljahr(schuljahr : number) : List<U> {
		let result : List<U> | null = this._mapSchuljahrToWerte.get(schuljahr);
		if (result === null) {
			result = new ArrayList();
			for (const wert of this._listWerte)
				if (this.getEintragBySchuljahrUndWert(schuljahr, wert) !== null)
					result.add(wert);
			this._mapSchuljahrToWerte.put(schuljahr, result);
		}
		return new ArrayList<U>(result);
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
	public hatSchulform(schuljahr : number, sf : Schulform, value : U) : boolean {
		return (this.getBySchulform(schuljahr, sf, value) !== null);
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
	public getBySchulform(schuljahr : number, sf : Schulform, value : U) : T | null {
		const eintrag : T | null = this.getEintragBySchuljahrUndWert(schuljahr, value);
		if (eintrag === null)
			return null;
		const result : JavaSet<Schulform> | null = this._mapSchulformenByID.get(eintrag.id);
		if (result === null)
			throw new CoreTypeException(JavaString.format("Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.", this.getClass().getSimpleName()))
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
	public getListBySchuljahrAndSchulform(schuljahr : number, schulform : Schulform) : List<U> {
		const mapBySchulform : JavaMap<Schulform, List<U>> | null = this._mapBySchuljahrAndSchulform.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<Schulform, List<U>>() });
		if (mapBySchulform === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<U> | null = mapBySchulform.get(schulform);
		if (result === null) {
			result = new ArrayList();
			const werte : List<U> | null = this.getWerteBySchuljahr(schuljahr);
			for (const wert of werte)
				if (this.hatSchulform(schuljahr, schulform, wert))
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
	public getBySchuljahrAndSchulformAndSchluessel(schuljahr : number, schulform : Schulform, schluessel : string) : U | null {
		const mapBySchulformAndSchluessel : JavaMap<Schulform, JavaMap<string, U>> | null = this._mapBySchuljahrAndSchulformAndSchluessel.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<Schulform, JavaMap<string, U>>() });
		if (mapBySchulformAndSchluessel === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let mapBySchluessel : JavaMap<string, U> | null = mapBySchulformAndSchluessel.get(schulform);
		if (mapBySchluessel === null) {
			mapBySchluessel = new HashMap();
			const werte : List<U> | null = this.getWerteBySchuljahr(schuljahr);
			for (const wert of werte) {
				if (!this.hatSchulform(schuljahr, schulform, wert))
					continue;
				const sgke : T | null = this.getEintragBySchuljahrUndWert(schuljahr, wert);
				if (sgke !== null)
					mapBySchluessel.put(sgke.schluessel, wert);
			}
			mapBySchulformAndSchluessel.put(schulform, mapBySchluessel);
		}
		return mapBySchluessel.get(schluessel);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.utils.CoreTypeDataManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.utils.CoreTypeDataManager'].includes(name);
	}

	public static class = new Class<CoreTypeDataManager<any, any>>('de.svws_nrw.asd.utils.CoreTypeDataManager');

}

export function cast_de_svws_nrw_asd_utils_CoreTypeDataManager<T extends CoreTypeData, U extends CoreType<T, U>>(obj : unknown) : CoreTypeDataManager<T, U> {
	return obj as CoreTypeDataManager<T, U>;
}
