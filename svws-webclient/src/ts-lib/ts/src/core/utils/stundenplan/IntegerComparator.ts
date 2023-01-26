import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';

export class IntegerComparator extends JavaObject implements Comparator<number | null> {


	public constructor() {
		super();
	}

	public compare(o1 : number, o2 : number) : number {
		return o1! - o2!;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplan.IntegerComparator', 'java.util.Comparator'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplan_IntegerComparator(obj : unknown) : IntegerComparator {
	return obj as IntegerComparator;
}
