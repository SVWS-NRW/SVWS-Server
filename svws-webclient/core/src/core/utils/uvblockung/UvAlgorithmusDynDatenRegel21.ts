import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenFach } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenFach';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel21 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly fach: UvAlgorithmusDynDatenFach;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly maximum: number;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param fach         Das {@link UvAlgorithmusDynDatenFach}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param minimum      Die maximale Anzahl an Lerngruppen mit dem Fach, das die Lehrkraft unterrichten soll.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, fach: UvAlgorithmusDynDatenFach, lehrkraft: UvAlgorithmusDynDatenLehrkraft, minimum: number, prioritaet: number) {
		super();
		this.malus = malus;
		this.fach = fach;
		this.lehrkraft = lehrkraft;
		this.maximum = minimum;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		const ueberschreitung: number = this.lehrkraft.istFachZuLerngruppenAnzahl[this.fach.interneID] - this.maximum;
		if ((this.prioritaet >= 0) && (ueberschreitung > 0)) {
			this.malus[this.prioritaet] += faktor * ueberschreitung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel21';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel21'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel21>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel21');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel21(obj: unknown): UvAlgorithmusDynDatenRegel21 {
	return obj as UvAlgorithmusDynDatenRegel21;
}
