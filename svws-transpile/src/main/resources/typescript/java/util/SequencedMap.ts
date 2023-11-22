import type { JavaMap } from './JavaMap';

export interface SequencedMap<K, V> extends JavaMap<K, V> {

    reversed() : SequencedMap<K, V>;

}


export function cast_java_util_SequencedMap<K, V>(obj : unknown) : SequencedMap<K, V> {
	return obj as SequencedMap<K, V>;
}
