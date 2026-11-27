import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel42 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly min: number;

	private readonly lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe>;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log                Der Logger für Debug-Zwecke.
	 * @param malus              Das globale Malus-Array.
	 * @param lehrkraft          Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param min                Die minimale Anzahl an Lerngruppen in {@code lerngruppenMenge}, in denen die Lehrkraft vertreten sein soll.
	 * @param lerngruppenMenge   Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}, auf die sich die Regel bezieht.
	 * @param prioritaet         Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public constructor(log: Logger, malus: Array<number>, lehrkraft: UvAlgorithmusDynDatenLehrkraft, min: number, lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe>, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.min = min;
		this.lerngruppenMenge = lerngruppenMenge;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel42: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		let vertreten: number = 0;
		for (const lerngruppe of this.lerngruppenMenge) {
			if (lerngruppe.gibIstLehrkraftZugeordnet(this.lehrkraft)) {
				vertreten++;
			}
		}
		const unterschreitung: number = this.min - vertreten;
		if (unterschreitung > 0) {
			this.malus[this.prioritaet] += faktor * unterschreitung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel42';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel42'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel42>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel42');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel42(obj: unknown): UvAlgorithmusDynDatenRegel42 {
	return obj as UvAlgorithmusDynDatenRegel42;
}
