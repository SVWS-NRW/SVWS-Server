import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaFunction } from '../../../java/util/function/JavaFunction';
import { SatInput } from '../../../core/adt/sat/SatInput';
import { SatOutput } from '../../../core/adt/sat/SatOutput';

export class SatPreprocessor1 extends JavaObject implements JavaFunction<SatInput, SatOutput> {

	private readonly next : JavaFunction<SatInput, SatOutput>;


	/**
	 * Konstruktor.
	 *
	 * @param next Der nächste Preprocessor oder SatSolver
	 */
	public constructor(next : JavaFunction<SatInput, SatOutput>) {
		super();
		this.next = next;
	}

	public apply(t : SatInput) : SatOutput {
		return this.next.apply(t);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatPreprocessor1', 'java.util.function.Function'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatPreprocessor1(obj : unknown) : SatPreprocessor1 {
	return obj as SatPreprocessor1;
}
