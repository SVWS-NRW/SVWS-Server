import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel12 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly maxKlassenleitungen2: number;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max2         Die maximale Anzahl an Klassenleitungen (Leitung 2).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, lehrkraft: UvAlgorithmusDynDatenLehrkraft, max2: number, prioritaet: number) {
		super();
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maxKlassenleitungen2 = max2;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		const ueberschreitung: number = this.lehrkraft.istAnzahlLeitung2 - this.maxKlassenleitungen2;
		if ((this.prioritaet >= 0) && (ueberschreitung > 0)) {
			this.malus[this.prioritaet] += faktor * ueberschreitung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel12';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel12'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel12>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel12');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel12(obj: unknown): UvAlgorithmusDynDatenRegel12 {
	return obj as UvAlgorithmusDynDatenRegel12;
}
