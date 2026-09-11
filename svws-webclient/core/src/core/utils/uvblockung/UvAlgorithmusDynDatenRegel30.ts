import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel30 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly lerngruppen: List<UvAlgorithmusDynDatenLerngruppe>;

	private readonly prioritaet: number;

	private readonly lehrkraftCounter: Array<number>;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus          Das globale Malus-Array.
	 * @param lerngruppen    Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}n, welche die selbe Lehrkraft haben sollen.
	 * @param nLehrkraefte   Die Anzahl aller Lehrkräfte.
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, lerngruppen: List<UvAlgorithmusDynDatenLerngruppe>, nLehrkraefte: number, prioritaet: number) {
		super();
		this.malus = malus;
		this.lerngruppen = lerngruppen;
		this.lehrkraftCounter = Array(nLehrkraefte).fill(0);
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		let verschieden: number = 0;
		for (const lerngruppe of this.lerngruppen) {
			if (lerngruppe.lehrkraefteZugeordnetAlle.isEmpty()) {
				verschieden++;
				continue;
			}
			let erstesVorkommen: boolean = false;
			for (const lehrkraft of lerngruppe.lehrkraefteZugeordnetAlle) {
				if (this.lehrkraftCounter[lehrkraft.interneID] === 0) {
					erstesVorkommen = true;
				}
				this.lehrkraftCounter[lehrkraft.interneID]++;
			}
			if (erstesVorkommen) {
				verschieden++;
			}
		}
		for (const lerngruppe of this.lerngruppen) {
			for (const lehrkraft of lerngruppe.lehrkraefteZugeordnetAlle) {
				this.lehrkraftCounter[lehrkraft.interneID] = 0;
			}
		}
		this.malus[this.prioritaet] += faktor * (this.lerngruppen.size() - verschieden);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel30';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel30'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel30>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel30');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel30(obj: unknown): UvAlgorithmusDynDatenRegel30 {
	return obj as UvAlgorithmusDynDatenRegel30;
}
