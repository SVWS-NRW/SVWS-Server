import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel45 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private aktiviert: boolean = false;

	private prioritaet: number = 0;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param aktiviert    TRUE, falls die Soll-Ist-Abweichung der Lehrkraft aktiv sein soll.
	 * @param prioritaet   Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public constructor(log: Logger, malus: Array<number>, lehrkraft: UvAlgorithmusDynDatenLehrkraft, aktiviert: boolean, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.aktiviert = aktiviert;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	/**
	 * Aktualisiert diese Regel.
	 *
	 * @param aktiviert    Die neue Aktivierung.
	 * @param prioritaet   Die neue Priorität.
	 */
	setzeAuf(aktiviert: boolean, prioritaet: number): void {
		this.changeMalus(-1);
		this.aktiviert = aktiviert;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel45: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		if (!this.aktiviert) {
			return;
		}
		const abweichung: number = this.lehrkraft.stundenSoll - this.lehrkraft.stundenAnrechnung - this.lehrkraft.istStundensummeLerngruppen;
		const gerundet: number = Math.ceil((abweichung * abweichung) - 0.5) as number;
		if (gerundet > 0) {
			this.malus[this.prioritaet] += faktor * gerundet;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel45';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel45'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel45>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel45');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel45(obj: unknown): UvAlgorithmusDynDatenRegel45 {
	return obj as UvAlgorithmusDynDatenRegel45;
}
