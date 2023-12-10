import type { Collection } from './Collection';
import type { JavaMap } from './JavaMap';
import type { JavaMapEntry } from './JavaMapEntry';
import type { JavaSet } from './JavaSet';
import type { Cloneable } from '../../java/lang/Cloneable';
import type { Serializable } from '../../java/io/Serializable';

import { HashMapCollection } from './HashMapCollection';
import { JavaObject } from '../../java/lang/JavaObject';
import { IndexOutOfBoundsException } from '../lang/IndexOutOfBoundsException';
import { UnsupportedOperationException } from '../lang/UnsupportedOperationException';
import { HashMapEntrySet } from './HashMapEntrySet';
import { HashMapEntry } from './HashMapEntry';
import { HashMapKeySet } from './HashMapKeySet';
import { JavaFunction } from './function/JavaFunction';

interface HashMapIterator<E> extends Iterator<E, E> {
	iter : IterableIterator<[number, Array<E>]>;
	current: IteratorResult<[number, Array<E>]> | undefined;
	pos : number;
}

/**
 * Eine Typescript Implementierung, welche im Transpiler das Verhalten der Java-Klasse java.util.HashMap emuliert.
 */
export class HashMap<K, V> extends JavaObject implements JavaMap<K, V>, Cloneable, Serializable {

	// Die interne Javascript-Map zur Verwaltung der Kollisionslisten für die Hashwerte der Schlüsselwerte
	protected readonly _map : Map<number, Array<JavaMapEntry<K, V>>> = new Map();

	// Die Anzahl der Elemente in der Hash-Map. Ist aufgrund der Kollisionslisten ggf. größer als die Anzahl der Elemente in der internen Javascript-Map
	protected _size : number = 0;


	/**
	 * Erstellt einen neue leere Hash-Map
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt die Anzahl der Werte in der Hash-Map zurück.
	 *
	 * @returns die Anzahl der Werte in der Hash-Map
	 */
	public size() : number {
		return this._size;
	}

	/**
	 * Gibt zurück, ob die Hash-Map leer ist oder nicht
	 *
	 * @returns true, wenn die Hash-Map leer ist
	 */
	public isEmpty() : boolean {
		return this._size === 0;
	}

	/**
	 * Prüft, ob ein Wert für den Schlüsselwert key in der Hash-Map
	 * enthalten ist.
	 *
	 * @param key   der Schlüsselwert
	 *
	 * @returns true, wen ein Wert enthalten ist
	 */
	public containsKey(key : K) : boolean {
		const hash = JavaObject.getTranspilerHashCode(key);
		const koll : Array<JavaMapEntry<K, V>> | undefined = this._map.get(hash);
		if (koll === undefined)
			return false;
		for (const entry of koll)
			if (JavaObject.equalsTranspiler(entry.getKey(), key))
				return true;
		return false;
	}

	/**
	 * Prüft, ob die Hash-Map den Wert beinhaltet. Die Methode ist
	 * langsam, da die alle Einträge traversiert werden müssen.
	 *
	 * @param value   der zu prüfende Wert.
	 *
	 * @returns true, wenn der Wert enthalten ist
	 */
	public containsValue(value : V) : boolean {
		for (const [k, koll] of this._map)
			for (const e of koll)
				if (JavaObject.equalsTranspiler(e.getValue(), value))
					return true;
		return false;
	}

	/**
	 * Bestimmt den Wert in der Hash-Map, der dem übergebenen Schlüsselwert zugeordnet
	 * ist. Gibt null zurück, wenn kein Schlüsselwert zugeordnet ist. Wir für den
	 * Schlüsselwert null übergeben, so wird immer null zurückgegeben.
	 *
	 * @param key   der Schlüsselwert
	 *
	 * @returns der Wert in der Hash-Map oder null
	 */
	public get(key : any | null) : V | null {
		if (key === null)
			return null;
		const hash = JavaObject.getTranspilerHashCode(key);
		const koll : Array<JavaMapEntry<K, V>> | undefined = this._map.get(hash);
		if (koll === undefined)
			return null;
		for (const entry of koll)
			if (JavaObject.equalsTranspiler(entry.getKey(), key))
				return entry.getValue();
		return null;
	}

	/**
	 * Fügt den Wert value für den Schlüsselwert key in die Hash-Map ein.
	 * Ist für den Schlüsselwert bereits ein Wert enthalten, so wird dieser
	 * ersetzt und der ursprüngliche Wert wird zurückgegeben.
	 *
	 * @param key     der Schlüsselwert
	 * @param value   der einzufügende Wert
	 *
	 * @returns der ursprüngliche Wert oder null
	 */
	public put(key : K, value : V) : V | null {
		// ermittle die Kollisionsliste, in die der neue Map-Eintrag eingefügt werden soll...
		const hash = JavaObject.getTranspilerHashCode(key);
		let koll : Array<JavaMapEntry<K, V>> | undefined = this._map.get(hash);
		// ggf. muss eine neue Kollisionliste erzeugt werden, da bisher noch keine Liste für den Hash des Schlüsselwertes existiert...
		if (koll === undefined) {
			koll = new Array<JavaMapEntry<K, V>>();
			this._map.set(hash, koll);
		}
		let oldValue : V | null = null;   // gehe zunächst davon aus, dass kein Wert für den Schlüsselwert vorhanden ist...
		// durchwandere die Kollisionsliste und ersetze ggf. einen bereits vorhandenen Eintrag zu dem Schlüsselwert
		for (let i = 0; i < koll.length; i++) {
			// entferne ggf. einen Eintrag, der bereits in der Kollisionsliste vorhanden ist
			if (JavaObject.equalsTranspiler(koll[i].getKey(), key)) {
				oldValue = koll[i].getValue(); // der alte Wert wird gespeichert und später zurückgegeben
				koll.splice(i, 1); // entferne den alten Eintrag aus der Liste
				this._size--; // reduziere die Anzahl der Elemente in der Map, da wird später wieder erhöht, wenn der neue Eintrag hinzugefügt wird...
				break;
			}
		}
		// füge den neue Wert an das Ende der Kollisionsliste und damit in die Map ein
		const newEntry = new HashMapEntry(key, value);
		koll.push(newEntry);
		this._size++;
		return oldValue;
	}

	/**
	 * Prüft, ob dem übergebenen Schlüsselwert ein Wert zugeordnet ist.
	 * Ist dies der Fall, so wird er aus der Hash-Map entfernt und zurückgeben.
	 * Ist kein Wert zugeordnet, so wird null zurückgegeben.
	 *
	 * @param key   der Schlüsselwert
	 *
	 * @returns der entfernte Wert oder null
	 */
	public remove(key : any) : V | null {
		if (key === null)
			return null;
		// ermittle die Kollisionsliste, aus der der Wert entfernt werden soll...
		const hash = JavaObject.getTranspilerHashCode(key);
		let koll : Array<JavaMapEntry<K, V>> | undefined = this._map.get(hash);
		if (koll === undefined)
			return null;   // Keine Kollisionsliste vorhanden, also auch kein Wert...
		let oldValue : V | null = null;   // gehe zunächst davon aus, dass kein Wert für den Schlüsselwert vorhanden ist...
		// Entferne den Eintrag ggf. aus der Kollisionsliste und merke den entfernten Wert
		for (let i = 0; i < koll.length; i++) {
			// entferne ggf. einen Eintrag, der bereits in der Kollisionsliste vorhanden ist
			if (JavaObject.equalsTranspiler(koll[i].getKey(), key)) {
				oldValue = koll[i].getValue(); // der alte Wert wird gespeichert und später zurückgegeben
				koll.splice(i, 1); // entferne den alten Eintrag aus der Liste
				this._size--; // reduziere die Anzahl der Elemente in der Map, da wird später wieder erhöht, wenn der neue Eintrag hinzugefügt wird...
				break;
			}
		}
		// Entferne ggf. die Kollisions liste aus der map, wenn diese leer ist
		if (koll.length === 0)
			this._map.delete(hash);
		return oldValue;
	}

	/**
	 * Fügt alle Werte aus der übergenen Map in diese Map ein. Bereits vorhandene
	 * Werte für Schlüsselwerte aus der übergebenen Map werden ggf. ersetzt.
	 *
	 * @param m   die Map, deren Werte eingefügt werden sollen
	 */
	public putAll(m : JavaMap<K, V>) : void {
		for (const entry of m.entrySet()) {
			if (entry === null)
				return;
			this.put(entry.getKey(), entry.getValue());
		}
	}


	/**
	 * Entfernt alle Einträge aus dieser Hash-Map.
	 */
	public clear() : void {
		this._map.clear();
		this._size = 0;
	}

	/**
	 * Gibt das Key-Set für diese Hash-Map zurück.
	 *
	 * @returns das Set mit den Schlüsselwerten.
	 */
	public keySet() : JavaSet<K> {
		return new HashMapKeySet<K, V>(this);
	}

	/**
	 * Gibt eine Collection mit den Werten dieser Hash-Map zurück.
	 *
	 * @returns die Collection mit den Werten
	 */
	public values() : Collection<V> {
		return new HashMapCollection<K, V>(this);
	}

	/**
	 * Gibt eine Menge mit allen Einträgen in dieser Map zurück.
	 *
	 * @returns das Set mit den Einträgen
	 */
	public entrySet() : JavaSet<JavaMapEntry<K, V>> {
		return new HashMapEntrySet<K, V>(this);
	}

	public equals(o : any) : boolean {
		throw new UnsupportedOperationException();
	}

	public hashCode() : number {
		throw new UnsupportedOperationException();
	}


	public computeIfAbsent(key : K, mappingFunction: JavaFunction<K, V> ) : V | null {
        const v : V | null = this.get(key);
		if (v != null)
			return v;
		const newValue : V = mappingFunction.apply(key);
		if (newValue == null)
			return null;
		this.put(key, newValue);
		return newValue;
	}


	[Symbol.iterator](): Iterator<JavaMapEntry<K, V>> {
		const result : HashMapIterator<JavaMapEntry<K, V>> = {
			iter: this._map[Symbol.iterator](),
			current: undefined,
			pos: -1,
			next() : IteratorResult<JavaMapEntry<K, V>> {
				// Wenn keine aktuelles Result des internen Iterators vorliegt, dann lade die nächste Kollisionsliste
				if (this.current === undefined)
					this.current = this.iter.next();
				if (this.current.done === true)
					return { value : null, done : true };
				// Die Kollisionsliste ist der value in der internen Map (daher value[1])
				const koll : Array<JavaMapEntry<K, V>> = this.current.value[1];
				this.pos++;
				if (this.pos >= koll.length)
					throw new IndexOutOfBoundsException("Hash-Map-Iterator: Fehlerhafter Index in die Kollisionsliste");
				const entry = koll[this.pos];
				// Die Kollisionsliste ist abgearbeitet - als nächstes muss eine neue geladen und abgearbeitet werden...
				if (this.pos == koll.length -1) {
					this.current = undefined;
					this.pos = -1;
				}
				return { value : entry, done : false };
			}
		};
		return result;
	}


	public transpilerCanonicalName(): string {
		return 'java.util.HashMap';
	}

	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.HashMap',
			'java.util.AbstractMap',
			'java.util.Map',
			'java.lang.Object',
			'java.lang.Cloneable',
			'java.io.Serializable'
		].includes(name);
	}

}


export function cast_java_util_HashMap<K, V>(obj : unknown) : HashMap<K, V> {
	return obj as HashMap<K, V>;
}
