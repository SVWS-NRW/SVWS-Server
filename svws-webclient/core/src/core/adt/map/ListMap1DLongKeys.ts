import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { MapUtils } from '../../../core/utils/MapUtils';
import type { JavaMap } from '../../../java/util/JavaMap';

export class ListMap1DLongKeys<V> extends JavaObject {

	private readonly _map: JavaMap<number, List<V>> = new HashMap<number, List<V>>();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key   Der Schlüssel.
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public add(key: number, value: V): void {
		MapUtils.getOrCreateArrayList(this._map, key).add(value);
	}

	/**
	 * Fügt das Element hinzu. Wirft eine Exception, falls es schon ein Element mit diesem Schlüssel gibt.
	 *
	 * @param key   Der Schlüssel.
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public addSingle(key: number, value: V): void {
		if (!this.get(key).isEmpty()) {
			throw new DeveloperNotificationException(JavaString.format("Es gibt schon ein Element mit (%d).", key))
		}
		this.add(key, value);
	}

	/**
	 * Erzeugt den Pfad (key) fügt aber nichts hinzu.
	 * Falls der Pfad vorher nicht existierte, verweist er dann auf eine leere Liste.
	 *
	 * @param key   Der Schlüssel.
	 */
	public addEmpty(key: number): void {
		MapUtils.getOrCreateArrayList(this._map, key);
	}

	/**
	 * Entfernt den Wert aus der zum Schlüssel (key) gehörenden Value-Liste.
	 * Falls es den Pfad (key) nicht gibt oder der Wert nicht enthalten ist, wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 * @param value Der zu entfernende Wert.
	 */
	public removeValueOrException(key: number, value: V): void {
		MapUtils.removeFromListAndTrimOrException(this._map, key, value);
	}

	/**
	 * Entfernt den Pfad (key) aus der Map.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return der bisherige Wert zu (key) oder {@code null}, falls nicht existent.
	 */
	public remove(key: number): List<V> | null {
		return this._map.remove(key);
	}

	/**
	 * Entfernt den Pfad (key) aus der Map.
	 * Falls es den Pfad (key) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return der bisherige Wert zu (key)
	 */
	public removeOrException(key: number): List<V> {
		return DeveloperNotificationException.ifNull("Pfad (" + key + ") existiert nicht!", this.remove(key));
	}

	/**
	 * Entfernt den Pfad (key) aus der Map.
	 * Wirft eine DeveloperNotificationException, falls in der gemappten Liste das Element nicht als einziges enthalten ist.
	 * Falls es den Pfad (key) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return der bisherige Wert zu (key)
	 */
	public removeSingleOrException(key: number): V {
		const values: List<V> | null = this.removeOrException(key);
		DeveloperNotificationException.ifTrue("Pfad (" + key + ") enthält nicht genau ein Element (tatsächlich " + values.size() + ")!", values.size() !== 1);
		return values.getFirst();
	}

	/**
	 * Gibt eine flache Liste aller Values in dieser 1D-ListMap zurück.
	 * Die Einfüge-Reihenfolge der einzelnen Listen bleibt erhalten.
	 *
	 * @return eine flache Liste aller enthaltenen Werte
	 */
	public getAllValues(): List<V> {
		const result: List<V> | null = new ArrayList<V>();
		for (const values of this._map.values()) {
			result.addAll(values);
		}
		return result;
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key) gibt.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key) gibt.
	 */
	public containsKey(key: number): boolean {
		return this._map.containsKey(key);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key).
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get(key: number): List<V> {
		const list: List<V> | null = this._map.get(key);
		if (list === null) {
			return new ArrayList();
		}
		return new ArrayList<V>(list);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingleOrNull(key: number): V | null {
		const list: List<V> | null = this._map.get(key);
		if (list === null) {
			return null;
		}
		if (list.size() !== 1) {
			return null;
		}
		return list.getFirst();
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingleOrException(key: number): V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingleOrNull(key));
	}

	/**
	 * Liefert das Key-Set der Map.
	 *
	 * @return das Key-Set der Map.
	 */
	public keySet(): JavaSet<number> {
		return this._map.keySet();
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key).
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public getOrException(key: number): List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey(key));
		return this.get(key);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.ListMap1DLongKeys';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.adt.map.ListMap1DLongKeys'].includes(name);
	}

	public static readonly class = new Class<ListMap1DLongKeys<any>>('de.svws_nrw.core.adt.map.ListMap1DLongKeys');

}

export function cast_de_svws_nrw_core_adt_map_ListMap1DLongKeys<V>(obj: unknown): ListMap1DLongKeys<V> {
	return obj as ListMap1DLongKeys<V>;
}
