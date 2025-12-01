import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { BKGymBelegpruefung, cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { Class } from '../../../../java/lang/Class';

export class BKGymBelegpruefungD6 extends BKGymBelegpruefung {


	/**
	 * Erzeugt einen neue Belegprüfung
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager: BKGymAbiturdatenManager) {
		super(manager);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD6';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung', 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD6'].includes(name);
	}

	public static readonly class = new Class<BKGymBelegpruefungD6>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD6');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefungD6(obj: unknown): BKGymBelegpruefungD6 {
	return obj as BKGymBelegpruefungD6;
}
