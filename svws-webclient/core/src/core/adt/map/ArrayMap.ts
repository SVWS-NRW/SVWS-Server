import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import { cast_java_lang_Enum } from '../../../java/lang/JavaEnum';
import { ArrayMapKeySet } from '../../../core/adt/map/ArrayMapKeySet';
import type { JavaSet } from '../../../java/util/JavaSet';
import { ArrayMapCollection } from '../../../core/adt/map/ArrayMapCollection';
import { ArrayMapEntrySet } from '../../../core/adt/map/ArrayMapEntrySet';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { cast_java_util_function_Function } from '../../../java/util/function/JavaFunction';
import { ArrayMapEntry } from '../../../core/adt/map/ArrayMapEntry';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { Collection } from '../../../java/util/Collection';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';
import { ArrayIndexOutOfBoundsException } from '../../../java/lang/ArrayIndexOutOfBoundsException';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class ArrayMap<K, V> extends JavaObject implements JavaMap<K, V> {

	/**
	 * Das Array mit allen gültigen Schlüsselwerten
	 */
	private readonly keyArray : Array<K>;

	/**
	 * Die Funktion, welche jedem Schlüsselwert einem Index im Array zuordnet.
	 */
	private readonly keyIndexFunction : JavaFunction<K, number>;

	/**
	 * Das Array mit den Einträgen der Map. Dieses hat die gleiche Länge wie das keyArray. Nicht vorhandene Einträge sind mit null initialisiert.
	 */
	private readonly entries : Array<ArrayMapEntry<K, V> | null>;

	/**
	 * Das Key-Set als View auf diese Map
	 */
	private readonly _keySet : JavaSet<K>;

	/**
	 * Das Entry-Set als View auf diese Map
	 */
	private readonly _collection : Collection<V>;

	/**
	 * Das Entry-Set als View auf diese Map
	 */
	private readonly _entrySet : JavaSet<JavaMapEntry<K, V>>;

	private numEntries : number = 0;

	/**
	 * Die Funktion, welche jedem Schlüsselwert einem Index im Array zuordnet - für den Fall, dass es sich um einen Enum-Type handelt.
	 */
	private readonly keyIndexFunctionEnum : JavaFunction<K, number> = { apply : (key: K) => {
		const isEnum : boolean = ((key instanceof JavaObject) && ((key as JavaObject).isTranspiledInstanceOf('java.lang.Enum')));
		if (!isEnum)
			throw new IllegalArgumentException("Der Schlüsselwerte ist keine Enum-Konstanten und somit nicht zulässig.")
		return (cast_java_lang_Enum(key)).ordinal();
	} };


	/**
	 * Erzeugt eine neue {@link ArrayMap} basierend auf dem übergebenen Array mit den Schlüsselwerten.
	 * Bei diesem Konstruktor müssen die Schlüsselwerte Elemente einer Aufzählung (Enum) sein.
	 *
	 * @param keyArray           das Array mit den Schlüsselwerten
	 */
	public constructor(keyArray : Array<K>);

	/**
	 * Erzeugt eine neue {@link ArrayMap} basierend auf dem übergebenen Array mit den Schlüsselwerten
	 * und der zugehörigen Funktion zur Bestimmung des Index eines Schlüsselwertes im Array.
	 *
	 * @param keyArray           das Array mit den Schlüsselwerten
	 * @param keyIndexFunction   die Funktion zur Bestimmung des Index eines Schlüsselwertes im Array
	 */
	public constructor(keyArray : Array<K>, keyIndexFunction : JavaFunction<K, number>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : Array<K>, __param1? : JavaFunction<K, number>) {
		super();
		if (((typeof __param0 !== "undefined") && Array.isArray(__param0)) && (typeof __param1 === "undefined")) {
			const keyArray : Array<K> = __param0;
			if (keyArray.length <= 0)
				throw new IllegalArgumentException("Das Array mit den gültigen Schlüsselwerten darf nicht leer sein.")
			const firstKey : K = keyArray[0];
			if (!(((firstKey instanceof JavaObject) && ((firstKey as JavaObject).isTranspiledInstanceOf('java.lang.Enum')))))
				throw new IllegalArgumentException("Enthält das Array der Schlüsselwerte keine Enum-Konstanten, so muss ein Funktion für die Zuordnung von Schlüsselwerten angegeben werden.")
			this.keyArray = keyArray;
			this.keyIndexFunction = this.keyIndexFunctionEnum;
			this.entries = Array(keyArray.length).fill(null);
			this._keySet = new ArrayMapKeySet(this);
			this._collection = new ArrayMapCollection(this);
			this._entrySet = new ArrayMapEntrySet(this);
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0)) && ((typeof __param1 !== "undefined") && ((typeof __param1 !== 'undefined') && (__param1 instanceof Object) && (__param1 !== null) && ('apply' in __param1) && (typeof __param1.apply === 'function')) || (__param1 === null))) {
			const keyArray : Array<K> = __param0;
			const keyIndexFunction : JavaFunction<K, number> = cast_java_util_function_Function(__param1);
			if (keyArray.length <= 0)
				throw new IllegalArgumentException("Das Array mit den gültigen Schlüsselwerten darf nicht leer sein.")
			this.keyArray = keyArray;
			this.keyIndexFunction = keyIndexFunction;
			this.entries = Array(keyArray.length).fill(null);
			this._keySet = new ArrayMapKeySet(this);
			this._collection = new ArrayMapCollection(this);
			this._entrySet = new ArrayMapEntrySet(this);
		} else throw new Error('invalid method overload');
	}

	public keySet() : JavaSet<K> {
		return this._keySet;
	}

	public values() : Collection<V> {
		return this._collection;
	}

	public entrySet() : JavaSet<JavaMapEntry<K, V>> {
		return this._entrySet;
	}

	/**
	 * Gibt die Anzahl der möglichen Schlüsselwerte für diese
	 * Map zurück. Das entspricht der Länge des Schlüsselwert-Arrays.
	 *
	 * @return die Anzahl der möglichen Schlüsselwerte
	 */
	public getNumberOfKeys() : number {
		return this.keyArray.length;
	}

	public size() : number {
		return this.numEntries;
	}

	public isEmpty() : boolean {
		return this.numEntries === 0;
	}

	private isValidIndex(index : number) : boolean {
		return ((index >= 0) && (index < this.keyArray.length));
	}

	/**
	 * Gibt den Schlüsselwert an der übergebenen Stelle index im
	 * Array der Schlüsselwerte zurück.
	 *
	 * @param index   die Stelle im Array der Schlüsselwerte
	 *
	 * @return der Schlüsselwerte oder null, falls der Index nicht gültig ist.
	 */
	public getKeyAt(index : number) : K | null {
		return this.isValidIndex(index) ? this.keyArray[index] : null;
	}

	/**
	 * Ermittelt für den übergebenen Index auf das Array der Schlüsselwerte
	 * den, dem entsprechenden Schlüsselwert, zugeordneten Eintrag.
	 *
	 * @param index  der Index in das Array der Schlüsselwerte
	 *
	 * @return der zugeordnete Eintrag oder null, wenn kein Eintrag zugeordnet ist
	 */
	getEntryByIndex(index : number) : ArrayMapEntry<K, V> | null {
		return this.isValidIndex(index) ? this.entries[index] : null;
	}

	/**
	 * Ermittelt für den übergebenen Schlüsselwert den zugeordneten Eintrag.
	 *
	 * @param key  der Schlüsselwert
	 *
	 * @return der zugeordnete Eintrag oder null, wenn kein Eintrag zugeordnet ist
	 */
	getEntry(key : unknown | null) : ArrayMapEntry<K, V> | null {
		const k : K | null = key as unknown as K;
		if (k === null)
			return null;
		const index : number = this.keyIndexFunction.apply(k).valueOf();
		return this.getEntryByIndex(index);
	}

	public containsKey(key : unknown | null) : boolean {
		return this.getEntry(key) !== null;
	}

	public containsValue(value : unknown | null) : boolean {
		if (value === null)
			return false;
		for (const entry of this.entries)
			if ((entry !== null) && (JavaObject.equalsTranspiler(value, (entry.getValue()))))
				return true;
		return false;
	}

	public get(key : unknown | null) : V | null {
		const entry : ArrayMapEntry<K, V> | null = this.getEntry(key);
		return (entry === null) ? null : entry.getValue();
	}

	/**
	 * Bestimmt den Wert aus der Map anhand des Schlüsselwertes, welcher in dem
	 * Array der Schlüsselwerte an der Stelle index steht.
	 *
	 * @param index   die Stelle im Array der Schlüsselwerte
	 *
	 * @return der Wert an der Stelle oder null, falls kein Wert zugeordnet ist
	 *
	 * @throws ArrayIndexOutOfBoundsException falls der Index ungültig ist
	 */
	public getValueAt(index : number) : V | null {
		if (!this.isValidIndex(index))
			throw new ArrayIndexOutOfBoundsException("Fehlerhafter Index für die Schlüsselwerte")
		const entry : ArrayMapEntry<K, V> | null = this.getEntryByIndex(index);
		return (entry === null) ? null : entry.getValue();
	}

	public put(key : K, value : V) : V | null {
		const index : number = this.keyIndexFunction.apply(key).valueOf();
		if (!this.isValidIndex(index))
			throw new IllegalArgumentException("Der Schlüsselwert ist ungültig und kann keinem Index zugeordnet werden.")
		const entry : ArrayMapEntry<K, V> | null = this.getEntryByIndex(index);
		if (entry === null)
			this.numEntries++;
		this.entries[index] = new ArrayMapEntry(key, value);
		return entry === null ? null : entry.getValue();
	}

	public remove(key : unknown | null) : V | null {
		if (key === null)
			throw new NullPointerException("Der Schlüsselwert darf nicht null sein.")
		const index : number = this.keyIndexFunction.apply(key as unknown as K).valueOf();
		if (!this.isValidIndex(index))
			return null;
		const entry : ArrayMapEntry<K, V> | null = this.getEntryByIndex(index);
		if (entry !== null) {
			this.entries[index] = null;
			this.numEntries--;
		}
		return entry === null ? null : entry.getValue();
	}

	public putAll(map : JavaMap<K, V> | null) : void {
		if (map === null)
			throw new NullPointerException("Der Parameter map darf nicht null sein.")
		for (const entry of map.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	public clear() : void {
		Arrays.fill(this.entries, null);
		this.numEntries = 0;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Map', 'de.svws_nrw.core.adt.map.ArrayMap'].includes(name);
	}

	public computeIfAbsent(key : K, mappingFunction: JavaFunction<K, V> ) : V | null {
		const v : V | null = this.get(key);
		if (v != null)
			return v;
		const newValue : V = mappingFunction.apply(key);
		if (newValue == null)
			return null;
		this.put(key, newValue)
		return newValue;
	}

}

export function cast_de_svws_nrw_core_adt_map_ArrayMap<K, V>(obj : unknown) : ArrayMap<K, V> {
	return obj as ArrayMap<K, V>;
}
