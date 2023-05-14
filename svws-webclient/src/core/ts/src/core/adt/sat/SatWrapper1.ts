import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaFunction } from '../../../java/util/function/JavaFunction';
import { Random } from '../../../java/util/Random';
import { SatInput } from '../../../core/adt/sat/SatInput';
import { SatOutput } from '../../../core/adt/sat/SatOutput';

export class SatWrapper1 extends JavaObject implements JavaFunction<SatInput, SatOutput> {

	private readonly next : JavaFunction<SatInput, SatOutput>;

	private readonly rnd : Random = new Random();


	/**
	 * Konstruktor.
	 *
	 * @param next Der nächste Preprocessor oder SatSolver
	 */
	public constructor(next : JavaFunction<SatInput, SatOutput>) {
		super();
		this.next = next;
	}

	public apply(in1 : SatInput) : SatOutput {
		const nVars : number = in1.getVarCount();
		const map1to2 : Array<number> = Array(nVars + 1).fill(0);
		for (let i : number = 1; i <= nVars; i++)
			map1to2[i] = i;
		for (let i1 : number = 1; i1 <= nVars; i1++) {
			const i2 : number = this.rnd.nextInt(nVars) + 1;
			const save1 : number = map1to2[i1];
			const save2 : number = map1to2[i2];
			map1to2[i1] = save2;
			map1to2[i2] = save1;
		}
		const in2 : SatInput = new SatInput();
		for (const clause of in1.getClauses()) {
			const clause2 : Array<number> = Array(clause.length).fill(null);
			for (let i : number = 0; i < clause.length; i++) {
				const lit : number = clause[i].valueOf();
				clause2[i] = (lit >= 0) ? map1to2[lit] : -map1to2[-lit];
			}
			in2.add_clause_and_variables(clause2);
		}
		const out2 : SatOutput = this.next.apply(in2);
		const solution2 : Array<number> = out2.getSolution();
		const solution1 : Array<number> = Array(solution2.length).fill(0);
		for (let lit1 : number = 1; lit1 < solution2.length; lit1++) {
			const lit2 : number = map1to2[lit1];
			solution1[lit1] = solution2[lit2] >= 0 ? lit1 : -lit1;
		}
		return SatOutput.createCopy(out2, solution1);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatWrapper1', 'java.util.function.Function'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatWrapper1(obj : unknown) : SatWrapper1 {
	return obj as SatWrapper1;
}
