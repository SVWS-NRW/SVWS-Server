import { JavaObject } from '../../../java/lang/JavaObject';
import type { Comparator } from '../../../java/util/Comparator';

export class IntegerComparator extends JavaObject implements Comparator<number | null> {


	public constructor() {
		super();
	}

	public compare(o1 : number, o2 : number) : number {
		return o1! - o2!;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplan.IntegerComparator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.IntegerComparator', 'java.util.Comparator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_IntegerComparator(obj : unknown) : IntegerComparator {
	return obj as IntegerComparator;
}
