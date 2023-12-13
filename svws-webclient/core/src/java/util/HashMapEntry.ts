import { JavaObject } from '../../java/lang/JavaObject';
import { JavaMapEntry } from './JavaMapEntry';

export class HashMapEntry<K, V> extends JavaMapEntry<K, V> {

	/** Der Schlüsselwert. */
	protected _key : K;

	/** Der zugeordnete Wert. */
	protected _val : V;


	/**
	 * Erstellt einen neuen Eintrag.
	 *
	 * @param key   Der Schlüsselwert. Dieser darf nicht NULL sein.
	 * @param val   Der zugeordnete Wert. Dieser darf nicht NULL sein.
	 */
	constructor(key : K, val : V) {
		super();
		this._key = key;
		this._val = val;
	}

	public getKey() : K {
		return this._key;
	}

	public getValue() : V {
		return this._val;
	}

	public setValue(value : V) : V {
		const oldValue = this._val;
		this._val = value;
		return oldValue;
	}

	public equals(o : any) : boolean {
		if (!(o instanceof JavaMapEntry))
			return false;
		const e : JavaMapEntry<unknown, unknown> = o;
		return JavaObject.equalsTranspiler(this._key, e.getKey()) && JavaObject.equalsTranspiler(this._val, e.getValue());
	}

	public hashCode() : number {
    	return JavaObject._hashCode(JSON.stringify(this._key)) ^ JavaObject._hashCode(JSON.stringify(this._val));
	}

	public transpilerCanonicalName(): string {
		return 'java.util.HashMapEntry';
	}

	public isTranspiledInstanceOf(name : string): boolean {
    	return [
    		'java.util.HashMapEntry',
    		'java.util.Map.Entry',
    		'java.lang.Object'
    	].includes(name);
	}
}
