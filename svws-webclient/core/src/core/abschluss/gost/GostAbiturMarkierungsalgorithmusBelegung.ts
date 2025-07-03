import { JavaObject } from '../../../java/lang/JavaObject';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../java/lang/Class';

export class GostAbiturMarkierungsalgorithmusBelegung extends JavaObject {

	/**
	 * Die Fachbelegegung
	 */
	public readonly belegung : AbiturFachbelegung;

	/**
	 * Die Halbjahresbelegung
	 */
	public readonly belegungHalbjahr : AbiturFachbelegungHalbjahr;

	/**
	 * Das Halbjahr der Halbjahrebelegung
	 */
	public readonly halbjahr : GostHalbjahr;

	/**
	 * Die Notenpunkte der Halbjahresbelegung
	 */
	public readonly notenpunkte : number;


	/**
	 * Initialisiert das Objekt mit einer ggf. noch anrechenbaren und bewerteten Halbjahresbelegung
	 *
	 * @param belegung
	 * @param belegungHalbjahr
	 * @param notenpunkte
	 */
	constructor(belegung : AbiturFachbelegung, belegungHalbjahr : AbiturFachbelegungHalbjahr, notenpunkte : number) {
		super();
		this.belegung = belegung;
		this.belegungHalbjahr = belegungHalbjahr;
		const tmpHalbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
		this.halbjahr = (tmpHalbjahr === null) ? GostHalbjahr.EF1 : tmpHalbjahr;
		this.notenpunkte = notenpunkte;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusBelegung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusBelegung'].includes(name);
	}

	public static class = new Class<GostAbiturMarkierungsalgorithmusBelegung>('de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusBelegung');

}

export function cast_de_svws_nrw_core_abschluss_gost_GostAbiturMarkierungsalgorithmusBelegung(obj : unknown) : GostAbiturMarkierungsalgorithmusBelegung {
	return obj as GostAbiturMarkierungsalgorithmusBelegung;
}
