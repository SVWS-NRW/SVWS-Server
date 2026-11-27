import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LogLevel } from '../../../core/logger/LogLevel';

export class UvAlgorithmusDynDatenRegel04 extends JavaObject implements UvAlgorithmusDynDatenRegel {

	private readonly log: Logger;

	private readonly malus: Array<number>;

	private readonly klasse: UvAlgorithmusDynDatenKlasse;

	private sollLeitung2Anzahl: number = 0;

	private prioritaet: number = 0;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log                  Der Logger für Debug-Zwecke.
	 * @param malus                Das globale Malus-Array.
	 * @param klasse               Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param sollLeitung2Anzahl   Die Anzahl an gewünschten stellv. Klassenleitungen (Leitung 2).
	 * @param prioritaet           Die Priorität der Regel.
	 */
	public constructor(log: Logger, malus: Array<number>, klasse: UvAlgorithmusDynDatenKlasse, sollLeitung2Anzahl: number, prioritaet: number) {
		super();
		this.log = log;
		this.malus = malus;
		this.klasse = klasse;
		this.sollLeitung2Anzahl = sollLeitung2Anzahl;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}

	/**
	 * Setzt die neuen Werte für diese Regel und passt den Malus an.
	 *
	 * @param neueLeitung2Anzahl   Der neue Soll-Wert für die Anzahl an stellv. Klassenleitungen (Leitung 2).
	 * @param neuePrioritaet       Die neue Priorität für diese Regel.
	 */
	public setzeLeitung2AnzahlUndPrioritaet(neueLeitung2Anzahl: number, neuePrioritaet: number): void {
		if ((neuePrioritaet < 0) || (neuePrioritaet > UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel04: Ungültige Priorität %d - Änderung wird ignoriert.", neuePrioritaet));
			return;
		}
		this.changeMalus(-1);
		this.sollLeitung2Anzahl = neueLeitung2Anzahl;
		this.prioritaet = neuePrioritaet;
		this.changeMalus(1);
	}

	public changeMalus(faktor: number): void {
		if ((this.prioritaet < 0) || (this.prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("UvAlgorithmusDynDatenRegel04: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.", this.prioritaet));
			return;
		}
		const anzahlLeitung2: number = this.klasse.leitung2Zugeordnet.size() + this.klasse.leitung2ZugeordnetFixiert.size();
		const abweichung: number = Math.abs(this.sollLeitung2Anzahl - anzahlLeitung2);
		if (abweichung > 0) {
			this.malus[this.prioritaet] += faktor * abweichung;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel04';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel', 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel04'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenRegel04>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel04');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel04(obj: unknown): UvAlgorithmusDynDatenRegel04 {
	return obj as UvAlgorithmusDynDatenRegel04;
}
