import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { Class } from '../../../java/lang/Class';
import { SatInput } from '../../../core/adt/sat/SatInput';
import { SatOutput } from '../../../core/adt/sat/SatOutput';

export abstract class SatSolver extends JavaObject implements JavaFunction<SatInput, SatOutput> {

	/**
	 * Die maximale Zeit, die der Solver zum Lösen verwenden darf.
	 */
	protected maxTimeMillis : number = 1000;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Setzt die maximale Zeit, die der Solver zum Lösen verwenden darf.
	 *
	 * @param pMaxTimeMillis die maximale Zeit, die der Solver zum Lösen verwenden darf.
	 */
	public setMaxTimeMillis(pMaxTimeMillis : number) : void {
		this.maxTimeMillis = pMaxTimeMillis;
	}

	public abstract apply(t : SatInput) : SatOutput;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.sat.SatSolver';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.function.Function', 'de.svws_nrw.core.adt.sat.SatSolver'].includes(name);
	}

	public static class = new Class<SatSolver>('de.svws_nrw.core.adt.sat.SatSolver');

}

export function cast_de_svws_nrw_core_adt_sat_SatSolver(obj : unknown) : SatSolver {
	return obj as SatSolver;
}
