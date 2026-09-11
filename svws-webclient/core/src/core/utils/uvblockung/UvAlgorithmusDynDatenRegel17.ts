import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel17 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly lerngruppe: UvAlgorithmusDynDatenLerngruppe;

	private readonly prioritaet: number;

	/**
	 * Der Wochentag dieser Regel (des Zeitslots).
	 */
	readonly wochentag: number;

	/**
	 * Die Stunde am Wochentag dieser Regel (des Zeitslots).
	 */
	readonly stunde: number;

	/**
	 * Der Wochentyp dieser Regel (des Zeitslots).
	 */
	readonly wochentyp: number;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param wochentag    Der {@link Wochentag}.
	 * @param stunde       Die Stunde am Tag.
	 * @param wochentyp    Der Wochentyp (0 = Jeder Woche, 1 = A-Woche, 2 = B-Woche, ...)
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, lerngruppe: UvAlgorithmusDynDatenLerngruppe, wochentag: number, stunde: number, wochentyp: number, prioritaet: number) {
		super();
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.wochentag = wochentag;
		this.stunde = stunde;
		this.wochentyp = wochentyp;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if (this.prioritaet <= 0) {
			return;
		}
		for (const lehrkraft of this.lerngruppe.lehrkraefteZugeordnetAlle) {
			const genau0: number = lehrkraft.istWochentagStundeWochentypCounter[this.wochentag][this.stunde][0];
			let kollision: boolean = false;
			if (this.wochentyp === 0) {
				let groesser0: number = 0;
				for (let i: number = 1; i < lehrkraft.istWochentagStundeWochentypCounter[this.wochentag][this.stunde].length; i++) {
					groesser0 += lehrkraft.istWochentagStundeWochentypCounter[this.wochentag][this.stunde][i];
				}
				kollision = (genau0 + groesser0) > 1;
			} else {
				kollision = (genau0 + lehrkraft.istWochentagStundeWochentypCounter[this.wochentag][this.stunde][this.wochentyp]) > 1;
			}
			if (kollision) {
				this.malus[this.prioritaet] += faktor;
			}
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel17';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel17', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel17>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel17');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel17(obj: unknown): UvAlgorithmusDynDatenRegel17 {
	return obj as UvAlgorithmusDynDatenRegel17;
}
