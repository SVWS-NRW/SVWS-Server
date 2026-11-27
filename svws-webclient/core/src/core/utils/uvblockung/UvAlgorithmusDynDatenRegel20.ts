import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenFach } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenFach';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel20 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly fach: UvAlgorithmusDynDatenFach;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly minimum: number;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param fach         Das {@link UvAlgorithmusDynDatenFach}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param minimum      Die minimale Anzahl an Lerngruppen mit dem Fach, die die Lehrkraft unterrichten soll.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(log: Logger, malus: Array<number>, fach: UvAlgorithmusDynDatenFach, lehrkraft: UvAlgorithmusDynDatenLehrkraft, minimum: number, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.fach = fach;
		this.lehrkraft = lehrkraft;
		this.minimum = minimum;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel20: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		const unterschreitung: number = this.minimum - this.lehrkraft.istFachZuLerngruppenAnzahl[this.fach.interneID];
		if (unterschreitung > 0) {
			this.malus[this.prioritaet] += faktor * unterschreitung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel20';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel20'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel20>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel20');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel20(obj: unknown): UvAlgorithmusDynDatenRegel20 {
	return obj as UvAlgorithmusDynDatenRegel20;
}
