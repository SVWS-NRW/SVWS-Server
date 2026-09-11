import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenRegel45 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly lehrkraft: UvAlgorithmusDynDatenLehrkraft;

	private aktiviert: boolean = false;

	private prioritaet: number = 0;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param aktiviert    TRUE, falls die Soll-Ist-Abweichung der Lehrkraft aktiv sein soll.
	 * @param prioritaet   Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public constructor(malus: Array<number>, lehrkraft: UvAlgorithmusDynDatenLehrkraft, aktiviert: boolean, prioritaet: number) {
		super();
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
		if (!this.aktiviert) {
			return;
		}
		const abweichung: number = this.lehrkraft.stundenSoll - this.lehrkraft.stundenAnrechnung - this.lehrkraft.istStundensummeLerngruppen;
		const gerundet: number = (abweichung * abweichung) as number;
		if ((this.prioritaet >= 0) && (gerundet > 0)) {
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
