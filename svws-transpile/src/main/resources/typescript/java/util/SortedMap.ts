import type { Collection } from './Collection';
import type { Comparator } from './Comparator';
import type { JavaSet } from './JavaSet';
import type { SequencedMap } from './SequencedMap';
import type { JavaMapEntry } from './JavaMapEntry';

export interface SortedMap<K, V> extends SequencedMap<K,V> {

    comparator() : Comparator<K>;

    subMap(fromKey : K, toKey : K) : SortedMap<K, V>;
    headMap(toKey : K) : SortedMap<K, V>;
    tailMap(fromKey : K) : SortedMap<K, V>;

    firstKey() : K;
    lastKey() : K;

    keySet() : JavaSet<K>;
    values() : Collection<V>;
    entrySet() : JavaSet<JavaMapEntry<K, V>>;

}


export function cast_java_util_SortedMap<K, V>(obj : unknown) : SortedMap<K, V> {
	return obj as SortedMap<K, V>;
}
