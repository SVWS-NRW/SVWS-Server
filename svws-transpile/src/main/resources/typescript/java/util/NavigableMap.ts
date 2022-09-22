import { Collection } from './Collection';
import { Comparator } from './Comparator';
import { JavaSet } from './JavaSet';
import { JavaMap } from './JavaMap';
import { JavaMapEntry } from './JavaMapEntry';
import { NavigableSet } from './NavigableSet';
import { SortedMap } from './SortedMap';

export interface NavigableMap<K, V> extends SortedMap<K, V> {

    comparator() : Comparator<K>;

    subMap(fromKey : K, toKey : K) : SortedMap<K, V>;    
    headMap(toKey : K) : SortedMap<K, V>;
    tailMap(fromKey : K) : SortedMap<K, V>;

    subMap(fromKey : K, fromInclusive : boolean, toKey : K, toInclusive : boolean) : NavigableMap<K, V>;
    headMap(toKey : K, inclusive : boolean) : NavigableMap<K, V>;
    tailMap(fromKey : K, inclusive : boolean) : NavigableMap<K, V>;

    firstKey() : K;
    lastKey() : K;

    descendingMap() : NavigableMap<K,V>;

    keySet() : JavaSet<K>;
    navigableKeySet() : NavigableSet<K>;
    descendingKeySet() : NavigableSet<K>;

    values() : Collection<V>;
    entrySet() : JavaSet<JavaMapEntry<K, V>>;

    firstEntry() : JavaMapEntry<K, V> | null;
    lastEntry() : JavaMapEntry<K, V> | null;

    pollFirstEntry() : JavaMapEntry<K, V> | null;
    pollLastEntry() : JavaMapEntry<K, V> | null;

    ceilingEntry(key : K) : JavaMapEntry<K, V> | null;
    ceilingKey(key : K) : K | null;
    
    floorEntry(key : K) : JavaMapEntry<K, V> | null;
    floorKey(key : K) : K | null;
 
    higherEntry(key : K) : JavaMapEntry<K, V> | null;
    higherKey(key : K) : K | null;

    lowerEntry(key : K) : JavaMapEntry<K, V> | null;
    lowerKey(key : K) : K | null;
}


export function cast_java_util_NavigableMap<K, V>(obj : unknown) : NavigableMap<K, V> {
	return obj as NavigableMap<K, V>;
}
