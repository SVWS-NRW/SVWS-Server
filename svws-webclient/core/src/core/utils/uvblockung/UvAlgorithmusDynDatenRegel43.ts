import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel43 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly max: number;

	private readonly lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe>;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus              Das globale Malus-Array.
	 * @param lehrkraft          Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max                Die maximale Anzahl an Lerngruppen in {@code lerngruppenMenge}, in denen die Lehrkraft vertreten sein soll.
	 * @param lerngruppenMenge   Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}, auf die sich die Regel bezieht.
	 * @param prioritaet         Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public constructor(malus: Array<number>, lehrkraft: UvAlgorithmusDynDatenLehrkraft, max: number, lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe>, prioritaet: number) {
		super();
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.max = max;
		this.lerngruppenMenge = lerngruppenMenge;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		let vertreten: number = 0;
		for (const lerngruppe of this.lerngruppenMenge) {
			if (lerngruppe.gibIstLehrkraftZugeordnet(this.lehrkraft)) {
				vertreten++;
			}
		}
		const ueberschreitung: number = vertreten - this.max;
		if ((this.prioritaet >= 0) && (ueberschreitung > 0)) {
			this.malus[this.prioritaet] += faktor * ueberschreitung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel43';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel43'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel43>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel43');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel43(obj: unknown): UvAlgorithmusDynDatenRegel43 {
	return obj as UvAlgorithmusDynDatenRegel43;
}
