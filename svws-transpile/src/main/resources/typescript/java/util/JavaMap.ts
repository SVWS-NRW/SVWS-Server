import { Collection } from './Collection';
import { JavaSet } from './JavaSet';
import { JavaMapEntry } from './JavaMapEntry';


export interface JavaMap<K, V> {

    size() : number;
    isEmpty() : boolean;

    containsKey(key : K) : boolean;
    containsValue(value : V) : boolean;
    get(key : any) : V | null;

    put(key : K, value : V) : V | null;
    remove(key : any) : V | null;
    putAll(m : JavaMap<K, V>) : void;
    clear() : void;

    keySet() : JavaSet<K>;
    values() : Collection<V>;
    entrySet() : JavaSet<JavaMapEntry<K, V>>;

    equals(o : any) : boolean;
    hashCode() : number;

}


export function cast_java_util_Map<K, V>(obj : unknown) : JavaMap<K, V> {
	return obj as JavaMap<K, V>;
}
