import { UvAlgorithmusImplAbstract } from '../../../core/utils/uvblockung/UvAlgorithmusImplAbstract';
import { UvAlgorithmusDynDaten } from '../../../core/utils/uvblockung/UvAlgorithmusDynDaten';
import { Random } from '../../../java/util/Random';
import { Class } from '../../../java/lang/Class';
import { Logger } from '../../../core/logger/Logger';
import { System } from '../../../java/lang/System';

export class UvAlgorithmusImplRandomWalk extends UvAlgorithmusImplAbstract {


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

	public berechneInnerhalb(zeitlimit: number): void {
		const zeitEnde: number = System.currentTimeMillis() + zeitlimit;
		while (System.currentTimeMillis() < zeitEnde) {
			this.dyn.strategieLerngruppeLehrkraftHinzufuegen();
			this.dyn.strategieLerngruppeLehrkraftEntfernen();
			this.dyn.strategieKlassenleitung1Hinzufuegen();
			this.dyn.strategieKlassenleitung1Entfernen();
			this.dyn.strategieKlassenleitung2Hinzufuegen();
			this.dyn.strategieKlassenleitung2Entfernen();
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplAbstract', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusImplRandomWalk>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplRandomWalk');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusImplRandomWalk(obj: unknown): UvAlgorithmusImplRandomWalk {
	return obj as UvAlgorithmusImplRandomWalk;
}
