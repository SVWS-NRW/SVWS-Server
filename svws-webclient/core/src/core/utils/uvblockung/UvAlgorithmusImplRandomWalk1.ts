import { UvAlgorithmusDynDaten } from '../../../core/utils/uvblockung/UvAlgorithmusDynDaten';
import { UvAlgorithmusImplRandomWalk, cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusImplRandomWalk } from '../../../core/utils/uvblockung/UvAlgorithmusImplRandomWalk';
import { Random } from '../../../java/util/Random';
import { Class } from '../../../java/lang/Class';
import { Logger } from '../../../core/logger/Logger';

export class UvAlgorithmusImplRandomWalk1 extends UvAlgorithmusImplRandomWalk {


	/**
	 * Der Konstruktor.
	 *
	 * @param log   Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param rnd   Ein {@link Random}-Objekt zur Steuerung des Zufalls.
	 * @param dyn   Die dynamischen Daten (mit dynamischer Bewertung).
	 */
	public constructor(log: Logger, rnd: Random, dyn: UvAlgorithmusDynDaten) {
		super(log, rnd, dyn);
	}

	protected individuelleStrategie(): void {
		this.dyn.strategieMixRingZuerst(4, 3, 0, 4, 2);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk1';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk1', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplAbstract', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusImplRandomWalk1>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk1');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusImplRandomWalk1(obj: unknown): UvAlgorithmusImplRandomWalk1 {
	return obj as UvAlgorithmusImplRandomWalk1;
}
