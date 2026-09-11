import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaMath } from '../../../java/lang/JavaMath';

export class UvAlgorithmusDynDatenRegel02 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly malus: Array<number>;

	private readonly klasse: UvAlgorithmusDynDatenKlasse;

	private sollLeitung1Anzahl: number = 0;

	private prioritaet: number = 0;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus                Das globale Malus-Array.
	 * @param klasse               Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param sollLeitung1Anzahl   Die Anzahl an gewünschten Klassenleitungen (Leitung 1).
	 * @param prioritaet           Die Priorität der Regel.
	 */
	public constructor(malus: Array<number>, klasse: UvAlgorithmusDynDatenKlasse, sollLeitung1Anzahl: number, prioritaet: number) {
		super();
		this.malus = malus;
		this.klasse = klasse;
		this.sollLeitung1Anzahl = sollLeitung1Anzahl;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	/**
	 * Setzt die neuen Werte für diese Regel und passt den Malus an.
	 *
	 * @param neueLeitung1Anzahl   Der neue Soll-Wert für die Anzahl an Klassenleitungen (Leitung 1).
	 * @param neuePrioritaet       Die neue Priorität für diese Regel.
	 */
	public setzeLeitung1AnzahlUndPrioritaet(neueLeitung1Anzahl: number, neuePrioritaet: number): void {
		this.changeMalus(-1);
		this.sollLeitung1Anzahl = neueLeitung1Anzahl;
		this.prioritaet = neuePrioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		const anzahlLeitung1: number = this.klasse.leitung1Zugeordnet.size() + this.klasse.leitung1ZugeordnetFixiert.size();
		const abweichung: number = Math.abs(this.sollLeitung1Anzahl - anzahlLeitung1);
		if ((this.prioritaet >= 0) && (abweichung > 0)) {
			this.malus[this.prioritaet] += faktor * abweichung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel02';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel02', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel02>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel02');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel02(obj: unknown): UvAlgorithmusDynDatenRegel02 {
	return obj as UvAlgorithmusDynDatenRegel02;
}
