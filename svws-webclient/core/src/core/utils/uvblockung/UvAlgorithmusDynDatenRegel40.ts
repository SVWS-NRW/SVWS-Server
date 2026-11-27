import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel40 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private readonly maximum: number;

	private readonly prioritaet: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param maximum      Die maximale Anzahl an Lerngruppen.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(log: Logger, malus: Array<number>, lehrkraft: UvAlgorithmusDynDatenLehrkraft, maximum: number, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maximum = maximum;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel40: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		const ueberschreitung: number = this.lehrkraft.istAnzahlLerngruppen - this.maximum;
		if (ueberschreitung > 0) {
			this.malus[this.prioritaet] += faktor * ueberschreitung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel40';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel40'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel40>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel40');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel40(obj: unknown): UvAlgorithmusDynDatenRegel40 {
	return obj as UvAlgorithmusDynDatenRegel40;
}
