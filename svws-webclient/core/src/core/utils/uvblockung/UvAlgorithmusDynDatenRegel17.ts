import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel17 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

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
	 * @param log             Der Logger für Debug-Zwecke.
	 * @param malus           Das globale Malus-Array.
	 * @param lerngruppe      Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param wochentag       Der {@link Wochentag}.
	 * @param stunde          Die Stunde am Tag.
	 * @param wochentypWert   Der Wochentyp (0 = jede Woche, 1 = A-Woche, 2 = B-Woche, ...).
	 * @param prioritaet      Die Priorität der Regel.
	 */
	public constructor(log: Logger, malus: Array<number>, lerngruppe: UvAlgorithmusDynDatenLerngruppe, wochentag: number, stunde: number, wochentypWert: number, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.wochentag = wochentag;
		this.stunde = stunde;
		this.wochentyp = wochentypWert;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel17: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		let anzahlZuviel: number = 0;
		for (const lehrkraft of this.lerngruppe.lehrkraefteZugeordnetAlle) {
			const anzahl: number = (this.wochentyp === 0) ? this.gibMinimumUeberAlleWochentypen(lehrkraft) : lehrkraft.stundenplanAnzahl[this.wochentag][this.stunde][this.wochentyp];
			if (anzahl > 1) {
				anzahlZuviel += anzahl - 1;
			}
		}
		if (anzahlZuviel > 0) {
			this.malus[this.prioritaet] += faktor * anzahlZuviel;
		}
	}

	/**
	 * Liefert das Minimum des Stundenplans der Lehrkraft im aktuellen Zeitslot über alle Wochentypen > 0.
	 * <br>(Im typischen Fall entspricht das der Anzahl der Lerngruppen, die jede Woche stattfinden.)
	 *
	 * @param lehrkraft   Die Lehrkraft.
	 *
	 * @return das Minimum des Stundenplans der Lehrkraft im aktuellen Zeitslot über alle Wochentypen > 0.
	 */
	private gibMinimumUeberAlleWochentypen(lehrkraft: UvAlgorithmusDynDatenLehrkraft): number {
		let minimum: number = JavaInteger.MAX_VALUE;
		for (let wt: number = 1; wt < lehrkraft.stundenplanAnzahl[this.wochentag][this.stunde].length; wt++) {
			minimum = Math.min(minimum, lehrkraft.stundenplanAnzahl[this.wochentag][this.stunde][wt]);
		}
		return (minimum === JavaInteger.MAX_VALUE) ? 0 : minimum;
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
