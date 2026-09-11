import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel38 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly klasse: UvAlgorithmusDynDatenKlasse;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus          Das globale Malus-Array.
	 * @param klasse         Die {@link UvAlgorithmusDynDatenKlasse};
	 * @param lehrkraft      Die {@link UvAlgorithmusDynDatenLehrkraft};
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, prioritaet: number) {
		super();
		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft = lehrkraft;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		const istLeitung1: boolean = this.lehrkraft.istLeitung1InKlasse[this.klasse.interneID];
		if ((this.prioritaet >= 0) && (istLeitung1)) {
			this.malus[this.prioritaet] += faktor;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel38';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel38'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel38>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel38');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel38(obj: unknown): UvAlgorithmusDynDatenRegel38 {
	return obj as UvAlgorithmusDynDatenRegel38;
}
