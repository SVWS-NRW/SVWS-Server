import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostKlausurvorgabe } from '../../../core/data/gost/klausuren/GostKlausurvorgabe';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../java/lang/Class';

export class GostLaufbahnplanungGKLKlausurvorgabe extends JavaObject {

	private readonly fach: GostFach;

	private readonly halbjahr: GostHalbjahr;

	private readonly vorgabe: GostKlausurvorgabe;


	/**
	 * Erstelt eine neue Instanz mit den übergebenen Informatione zu der GKL-Klausurvorgabe
	 *
	 * @param fach       das Fach für die Klausurvorgabe
	 * @param halbjahr   das Halbjahr für die Klausurvorgabe
	 * @param vorgabe    die Klausurvorgabe
	 */
	public constructor(fach: GostFach, halbjahr: GostHalbjahr, vorgabe: GostKlausurvorgabe) {
		super();
		this.fach = fach;
		this.halbjahr = halbjahr;
		this.vorgabe = vorgabe;
	}

	/**
	 * Gibt das Fach zu der Klausurvorgabe zurück.
	 *
	 * @return das Fach
	 */
	public getFach(): GostFach {
		return this.fach;
	}

	/**
	 * Gibt das Halbjahr zu der Klausurvorgabe zurück.
	 *
	 * @return das Halbjahr
	 */
	public getHalbjahr(): GostHalbjahr {
		return this.halbjahr;
	}

	/**
	 * Gibt die Klausurvorgabe zurück.
	 *
	 * @return die Klausurvorgabe
	 */
	public getVorgabe(): GostKlausurvorgabe {
		return this.vorgabe;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostLaufbahnplanungGKLKlausurvorgabe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostLaufbahnplanungGKLKlausurvorgabe'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungGKLKlausurvorgabe>('de.svws_nrw.core.utils.gost.GostLaufbahnplanungGKLKlausurvorgabe');

}

export function cast_de_svws_nrw_core_utils_gost_GostLaufbahnplanungGKLKlausurvorgabe(obj: unknown): GostLaufbahnplanungGKLKlausurvorgabe {
	return obj as GostLaufbahnplanungGKLKlausurvorgabe;
}
