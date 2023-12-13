import { JavaObject } from '../../java/lang/JavaObject';

export abstract class JavaMapEntry<K, V> extends JavaObject {
    abstract getKey() : K;
    abstract getValue() : V;

    abstract setValue(value : V) : V;
    abstract equals(o : any) : boolean;

    abstract hashCode() : number;

	public transpilerCanonicalName(): string {
		return 'java.util.Map.Entry';
	}

    public isTranspiledInstanceOf(name : string): boolean {
    	return [
    		'java.util.Map.Entry',
    		'java.lang.Object'
    	].includes(name);
    }
}


export function cast_java_util_Map_Entry<K, V>(obj : unknown) : JavaMapEntry<K, V> {
	return obj as JavaMapEntry<K, V>;
}
