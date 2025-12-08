import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { BKGymBelegpruefung, cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { Class } from '../../../../java/lang/Class';

export class BKGymBelegpruefungD15a extends BKGymBelegpruefung {


	/**
	 * Erzeugt einen neue Belegprüfung
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager: BKGymAbiturdatenManager) {
		super(manager);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD15a';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung', 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD15a'].includes(name);
	}

	public static readonly class = new Class<BKGymBelegpruefungD15a>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD15a');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefungD15a(obj: unknown): BKGymBelegpruefungD15a {
	return obj as BKGymBelegpruefungD15a;
}
