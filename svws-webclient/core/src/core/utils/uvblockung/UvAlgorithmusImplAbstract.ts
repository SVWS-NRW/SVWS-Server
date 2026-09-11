import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDaten } from '../../../core/utils/uvblockung/UvAlgorithmusDynDaten';
import { Random } from '../../../java/util/Random';
import { Class } from '../../../java/lang/Class';
import { Logger } from '../../../core/logger/Logger';

export abstract class UvAlgorithmusImplAbstract extends JavaObject {

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	private readonly log: Logger;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly rnd: Random;

	/**
	 * Die dynamischen Daten (mit dynamischer Bewertung).
	 */
	protected readonly dyn: UvAlgorithmusDynDaten;


	/**
	 * Der Konstruktor.
	 *
	 * @param log   Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param rnd   Ein {@link Random}-Objekt zur Steuerung des Zufalls.
	 * @param dyn   Die dynamische Daten (mit dynamischer Bewertung).
	 */
	protected constructor(log: Logger, rnd: Random, dyn: UvAlgorithmusDynDaten) {
		super();
		this.log = log;
		this.rnd = rnd;
		this.dyn = dyn;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Lehrkräfte auf die Lerngruppen und
	 * überschreitet dabei nicht das Zeitlimit (in Millisekunden).
	 *
	 * @param zeitlimit   Das Zeitlimit (in Millisekunden).
	 */
	public abstract berechneInnerhalb(zeitlimit: number): void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplAbstract';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplAbstract'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusImplAbstract>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImplAbstract');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusImplAbstract(obj: unknown): UvAlgorithmusImplAbstract {
	return obj as UvAlgorithmusImplAbstract;
}
