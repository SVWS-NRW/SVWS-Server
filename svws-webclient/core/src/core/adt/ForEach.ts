import { JavaObject } from '../../java/lang/JavaObject';
import { LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import type { Collection } from '../../java/util/Collection';
import { Pair } from '../../core/adt/Pair';

export class ForEach extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert eine neue Collection, die alle Paare der anderen beiden Collections beinhaltet.
	 *
	 * @param <T>  Der Typ der 1. Collection.
	 * @param <U>  Der Typ der 2. Collection.
	 * @param collection1  Die 1. Collection.
	 * @param collection2  Die 2. Collection.
	 *
	 * @return eine neue Collection, die alle Paare der anderen beiden Collections beinhaltet.
	 */
	public static pair<T, U>(collection1 : Collection<T>, collection2 : Collection<U>) : LinkedCollection<Pair<T, U>> {
		const c : LinkedCollection<Pair<T, U>> = new LinkedCollection();
		for (const t of collection1)
			for (const u of collection2)
				c.add(new Pair(t, u));
		return c;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.ForEach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.ForEach'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_ForEach(obj : unknown) : ForEach {
	return obj as ForEach;
}
