import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { Class } from '../../../java/lang/Class';
import { BeruflichesGymnasiumStundentafelFach } from '../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';
import type { Comparator } from '../../../java/util/Comparator';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturUtils extends JavaObject {

	/**
	 *  Comparator für das DTO BKGymAbiturMarkierungsalgorithmusMarkierung
	 *  Es wird nach höchster Punktzahl, FachID, Halbjahr verglichen, so dass
	 *  die Verwendung des Comparators in sort zu einer eindeutigen Sortierreihenfolge führt.
	 */
	public static readonly comparatorMarkierung: Comparator<BKGymAbiturMarkierungsalgorithmusMarkierung> = { compare: (a: BKGymAbiturMarkierungsalgorithmusMarkierung, b: BKGymAbiturMarkierungsalgorithmusMarkierung) => {
		if (b.punkte === null)
			return -1;
		if (a.punkte === null)
			return 1;
		const tmp: number = b.punkte - a.punkte;
		if (tmp !== 0)
			return tmp;
		const ltmp: number = a.fachID - b.fachID;
		if (ltmp < 0)
			return -1;
		if (ltmp > 0)
			return 1;
		return a.halbjahrID - b.halbjahrID;
	} };

	/**
	 *  Comparator für das DTO BeruflichesGymnasiumStundentafelFach
	 *  Es wird nach höchster Punktzahl, FachID, Halbjahr verglichen, so dass
	 *  die Verwendung des Comparators in sort zu einer eindeutigen Sortierreihenfolge führt.
	 */
	public static readonly comparatorStundentafelFach: Comparator<BeruflichesGymnasiumStundentafelFach> = { compare: (a: BeruflichesGymnasiumStundentafelFach, b: BeruflichesGymnasiumStundentafelFach) => {
		return a.sortierung - b.sortierung;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.bk.BKGymAbiturUtils';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.bk.BKGymAbiturUtils'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturUtils>('de.svws_nrw.core.utils.bk.BKGymAbiturUtils');

}

export function cast_de_svws_nrw_core_utils_bk_BKGymAbiturUtils(obj: unknown): BKGymAbiturUtils {
	return obj as BKGymAbiturUtils;
}
