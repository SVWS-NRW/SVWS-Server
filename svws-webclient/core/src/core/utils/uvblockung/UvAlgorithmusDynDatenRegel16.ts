import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel16 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly lerngruppe: UvAlgorithmusDynDatenLerngruppe;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, lerngruppe: UvAlgorithmusDynDatenLerngruppe, lehrkraft: UvAlgorithmusDynDatenLehrkraft, prioritaet: number) {
		super();
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.lehrkraft = lehrkraft;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet >= 0) && this.lerngruppe.gibIstLehrkraftZugeordnet(this.lehrkraft)) {
			this.malus[this.prioritaet] += faktor;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel16';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel16'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel16>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel16');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel16(obj: unknown): UvAlgorithmusDynDatenRegel16 {
	return obj as UvAlgorithmusDynDatenRegel16;
}
