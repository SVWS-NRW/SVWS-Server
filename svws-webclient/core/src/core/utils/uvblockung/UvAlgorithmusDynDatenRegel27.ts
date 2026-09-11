import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel27 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly klasse: UvAlgorithmusDynDatenKlasse;

	private readonly lehrkraft1: UvAlgorithmusDynDatenLehrkraft;

	private readonly lehrkraft2: UvAlgorithmusDynDatenLehrkraft;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param klasse       Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft1   Die 1. {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft2   Die 2. {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, klasse: UvAlgorithmusDynDatenKlasse, lehrkraft1: UvAlgorithmusDynDatenLehrkraft, lehrkraft2: UvAlgorithmusDynDatenLehrkraft, prioritaet: number) {
		super();
		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft1 = lehrkraft1;
		this.lehrkraft2 = lehrkraft2;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		const klasseID: number = this.klasse.interneID;
		if ((this.prioritaet >= 0) && (this.lehrkraft1.istStundenProKlasse[klasseID]) > 0 && (this.lehrkraft2.istStundenProKlasse[klasseID] > 0)) {
			this.malus[this.prioritaet] += faktor;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel27';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel27'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel27>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel27');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel27(obj: unknown): UvAlgorithmusDynDatenRegel27 {
	return obj as UvAlgorithmusDynDatenRegel27;
}
