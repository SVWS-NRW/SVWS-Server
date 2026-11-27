import { JavaObject } from '../../../java/lang/JavaObject';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel30 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly lerngruppen: List<UvAlgorithmusDynDatenLerngruppe>;

	private readonly prioritaet: number;

	private readonly lehrkraftCounter: Array<number>;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log            Der Logger für Debug-Zwecke.
	 * @param malus          Das globale Malus-Array.
	 * @param lerngruppen    Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}n, welche verschiedene Lehrkräfte haben sollen.
	 * @param nLehrkraefte   Die Anzahl aller Lehrkräfte.
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public constructor(log: Logger, malus: Array<number>, lerngruppen: List<UvAlgorithmusDynDatenLerngruppe>, nLehrkraefte: number, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.lerngruppen = lerngruppen;
		this.lehrkraftCounter = Array(nLehrkraefte).fill(0);
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel30: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
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
