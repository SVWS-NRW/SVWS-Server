import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { BKGymBelegpruefung, cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { Class } from '../../../../java/lang/Class';

export class BKGymBelegpruefungD01_10600 extends BKGymBelegpruefung {


	/**
	 * Erzeugt einen neue Belegprüfung
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager : BKGymAbiturdatenManager) {
		super(manager);
	}

	public pruefe() : void {
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD01_10600';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung', 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD01_10600'].includes(name);
	}

	public static class = new Class<BKGymBelegpruefungD01_10600>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD01_10600');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefungD01_10600(obj : unknown) : BKGymBelegpruefungD01_10600 {
	return obj as BKGymBelegpruefungD01_10600;
}
