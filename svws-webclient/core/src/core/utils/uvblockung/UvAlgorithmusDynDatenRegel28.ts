import { JavaObject } from '../../../java/lang/JavaObject';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel28 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly lerngruppe: UvAlgorithmusDynDatenLerngruppe;

	private sollAnzahlLehrkraefte: number = 0;

	private prioritaet: number = 0;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log                     Der Logger für Debug-Zwecke.
	 * @param malus                   Das globale Malus-Array.
	 * @param lerngruppe              Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param sollAnzahlLehrkraefte   Die Anzahl an Lehrkräften, die diese Lerngruppe benötigt.
	 * @param prioritaet              Die Priorität der Regel.
	 */
	public constructor(log: Logger, malus: Array<number>, lerngruppe: UvAlgorithmusDynDatenLerngruppe, sollAnzahlLehrkraefte: number, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.sollAnzahlLehrkraefte = sollAnzahlLehrkraefte;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	setzeAuf(neuesSoll: number, prioritaet: number): void {
		this.changeMalus(-1);
		this.sollAnzahlLehrkraefte = neuesSoll;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel28: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		const anzahl: number = this.lerngruppe.lehrkraefteZugeordnetAlle.size();
		const abweichung: number = Math.abs(this.sollAnzahlLehrkraefte - anzahl);
		if (abweichung > 0) {
			this.malus[this.prioritaet] += faktor * abweichung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel28';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel28', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel28>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel28');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel28(obj: unknown): UvAlgorithmusDynDatenRegel28 {
	return obj as UvAlgorithmusDynDatenRegel28;
}
