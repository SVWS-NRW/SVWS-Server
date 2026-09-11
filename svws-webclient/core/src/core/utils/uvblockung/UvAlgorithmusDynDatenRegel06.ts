import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel06 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly klasse: UvAlgorithmusDynDatenKlasse;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly minKlassenstundenBeiLeitung1: number;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param klasse       Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param min          Die minimale Anzahl an Klassenstunden bei Klassenleitung (Leitung 1).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, min: number, prioritaet: number) {
		super();
		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft = lehrkraft;
		this.minKlassenstundenBeiLeitung1 = min;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		const differenz: number = this.minKlassenstundenBeiLeitung1 - this.lehrkraft.istStundenProKlasse[this.klasse.interneID];
		if ((this.lehrkraft.istLeitung1InKlasse[this.klasse.interneID]) && (differenz > 0)) {
			this.malus[this.prioritaet] += faktor * differenz;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel06';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel06', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel06>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel06');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel06(obj: unknown): UvAlgorithmusDynDatenRegel06 {
	return obj as UvAlgorithmusDynDatenRegel06;
}
